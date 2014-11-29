package be.fluid.mvn.cd.x.freeze.replace;

import be.fluid.mvn.cd.x.freeze.model.GroupIdArtifactIdVersionPrefix;
import be.fluid.mvn.cd.x.freeze.resolve.FrozenArtifactResolver;
import org.codehaus.plexus.logging.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import static be.fluid.mvn.cd.x.freeze.model.KnownElementNames.*;
import static be.fluid.mvn.cd.x.freeze.model.MavenConventions.*;

// TODO replace snapshot versions of plugins as well

public class FreezeHandler extends DefaultHandler {

    private final Logger logger;
    private Locator documentLocator;
    private int indentLevel = 0;
    private final Writer out;
    private final FrozenArtifactResolver frozenArtifactResolver;
    private final String nl =  System.getProperty("line.separator");

    private boolean currentlyInLeafElement = false;
    private String currentElement = null;
    private int currentDepth = 0;

    private GroupIdArtifactIdVersionPrefix currentGroupIdArtifactIdVersionPrefix = new GroupIdArtifactIdVersionPrefix();

    public FreezeHandler(OutputStream outputStream, FrozenArtifactResolver frozenArtifactResolver, Logger logger) {
        this.out = new OutputStreamWriter(outputStream);
        this.frozenArtifactResolver = frozenArtifactResolver;
        this.logger = logger;
    }

    /**
     * Overrides <code>setDocumentLocator(String <i>locator</i>)</code> in
     * <code>org.xml.sax.helpers.DefaultHandler</code>,
     * which in turn implements <code>org.xml.sax.ContentHandler</code>.
     * This method is used to save the document location in case
     * it is ever needed (for example, in an error message).
     */
    public void setDocumentLocator(Locator locator) {
        documentLocator = locator;
    }

    /**
     * Overrides <code>processingInstruction(String <i>target</i>,
     * String <i>data</i>)</code> in
     * <code>org.xml.sax.helpers.DefaultHandler</code>,
     * which in turn implements <code>org.xml.sax.ContentHandler</code>.
     * Called whenever a processing instruction (PI) is encountered.
     */
    public void processingInstruction(String target, String data)
            throws SAXException {
        write(nl + "     <?" + target + " " + data + "?>");
    }

    /**
     * Overrides <code>startDocument()</code> in
     * <code>org.xml.sax.helpers.DefaultHandler</code>,
     * which in turn implements <code>org.xml.sax.ContentHandler</code>.
     * Called once at the beginning of document processing, but
     * after setDocumentLocator has been called.
     */
    public void startDocument()
            throws SAXException {
        logger.debug("[Freezehandler]: Start parsing of pom file");
    }

    /**
     * Overrides <code>endDocument()</code> in
     * <code>org.xml.sax.helpers.DefaultHandler</code>,
     * which in turn implements <code>org.xml.sax.ContentHandler</code>.
     * Called once at the end of document processing; no further
     * callbacks will occur.
     */
    public void endDocument()
            throws SAXException {
        try {
            out.flush();
            logger.debug("[Freezehandler]: End parsing of pom file");
        } catch (IOException e) {
            throw new SAXException("I/O error", e);
        }
    }

    /**
     * Overrides <code>startElement(...)</code> in
     * <code>org.xml.sax.helpers.DefaultHandler</code>,
     * which in turn implements <code>org.xml.sax.ContentHandler</code>.
     * Called for each start tag encountered.
     *
     * @param namespaceURI Required if the namespaces property is true.
     * @param attributes The specified or defaulted attributes.
     * @param localName The local name (without prefix), or the empty
     *        string if Namespace processing is not being performed.
     * @param qualifiedName The qualified name (with prefix), or the
     *        empty string if qualified names are not available.
     */

    public void startElement(String namespaceURI,
                             String localName,
                             String qualifiedName,
                             Attributes attributes)
            throws SAXException {
        currentlyInLeafElement = false;
        currentDepth ++;
        currentElement = qualifiedName;
        // Emit element name
        write(nl + indent(+1) + "<" +
                getName(localName, qualifiedName));

        // Emit each attribute name/value pair in this element
        if (attributes != null) {
            for (int i = 0; i < attributes.getLength(); i++) {
                String attributeName = getName(attributes.getLocalName(i),
                        attributes.getQName(i));
                write(" " + attributeName + "=\"" +
                        attributes.getValue(i) + "\"");
            }
        }
        write(">");
    }

    /**
     * Overrides <code>endElement()</code> in
     * <code>org.xml.sax.helpers.DefaultHandler</code>,
     * which in turn implements <code>org.xml.sax.ContentHandler</code>.
     * Called for each start tag encountered.
     */
    public void endElement(String namespaceUri,
                           String localName,
                           String qualifiedName)
            throws SAXException {
        currentElement = null;
        currentDepth--;
        String indentation = indent(-1);
        String closingElement = "</" + getName(localName, qualifiedName) + ">";
        write(currentlyInLeafElement ? closingElement : nl + indentation + closingElement);
        currentlyInLeafElement = false;
        this.currentElement = null;
        if (frozenArtifactResolver.artifactInheritsVersionFromParent() &&
            ARTIFACT_ID.equals(qualifiedName) &&
            currentDepth == 1) {
            write(nl + indentation + "<" + VERSION + ">" + frozenArtifactResolver.artifactFrozenVersion() + "</" + VERSION + ">");
        }
        switch (qualifiedName) {
            case GROUP_ID:
            case ARTIFACT_ID:
            case PACKAGING:
            case RELATIVE_PATH:
            case "":
                break;
            case VERSION:
            default:
                this.currentGroupIdArtifactIdVersionPrefix = new GroupIdArtifactIdVersionPrefix();
        }
    }

    /**
     * Overrides <code>characters(char[] <i>ch</i>,
     * int <i>start</i>, int <i>length</i>)</code> in
     * <code>org.xml.sax.helpers.DefaultHandler</code>,
     * which in turn implements <code>org.xml.sax.ContentHandler</code>.
     * Called one <i>or more</i> times for the content characters
     * of each element. The particular characters may be accessed
     * with <code>new String(<i>buf</i>, <i>offset</i>, <i>len</i>)</code>.
     */
    public void characters(char buf[], int offset, int len)
            throws SAXException {
        String s = new String(buf, offset, len).trim();
        if (!s.equals("")) {
            currentlyInLeafElement = true;
            if (currentElement != null) {
                switch (currentElement) {
                    case GROUP_ID:
                        this.currentGroupIdArtifactIdVersionPrefix = this.currentGroupIdArtifactIdVersionPrefix.addGroupId(s);
                        break;
                    case ARTIFACT_ID:
                        this.currentGroupIdArtifactIdVersionPrefix = this.currentGroupIdArtifactIdVersionPrefix.addArtifactId(s);
                        break;
                    case VERSION:
                        if (this.currentGroupIdArtifactIdVersionPrefix.groupId() != null && this.currentGroupIdArtifactIdVersionPrefix.artifactId() != null) {
                            if (s.endsWith(SNAPSHOT_POSTFIX)) {
                                String versionPrefix = s.split(SNAPSHOT_POSTFIX)[0];
                                this.currentGroupIdArtifactIdVersionPrefix = this.currentGroupIdArtifactIdVersionPrefix.addVersionPrefix(versionPrefix);
                                s = this.frozenArtifactResolver.getLatestFrozenVersion(this.currentGroupIdArtifactIdVersionPrefix).version();
                                logger.info("[Freezehandler]: " + this.currentGroupIdArtifactIdVersionPrefix + SNAPSHOT_POSTFIX +
                                        " in pom is frozen as version " + s);
                                // TODO Add to freeze mapping for caching purposes
                            }
                        }
                    case RELATIVE_PATH:
                        s = s.replace(POM_FILE, FROZEN_POM_FILE);
                    default:
                        break;
                }
            }
        }
        write(s);
    }

    /**
     * Overrides <code>ignorableWhitespace(char[] <i>ch</i>,
     * int <i>start</i>, int <i>length</i>)</code> in
     * <code>org.xml.sax.helpers.DefaultHandler</code>,
     * which in turn implements <code>org.xml.sax.ContentHandler</code>.
     * Can only be called when validation is being performed.
     * The particular characters may be accessed
     * with <code>new String(<i>buf</i>, <i>offset</i>, <i>len</i>)</code>.
     */
    public void ignorableWhitespace(char buf[], int offset, int len)
            throws SAXException {
        String s = new String(buf, offset, len);
        write(nl + "ignorableW..." + indent(0));
    }


    /**
     * Overrides <code>error(SAXParseException <i>exception</i>)</code> in
     * <code>org.xml.sax.helpers.DefaultHandler</code>,
     * which in turn implements <code>org.xml.sax.ContentHandler</code>.
     * Called when a recoverable parser error occurs.
     */
    public void error(SAXParseException exception)
            throws SAXException {
        printSaxParseException("caught in 'error' method", exception);
    }

    /**
     * Overrides <code>warning(SAXParseException <i>exception</i>)</code> in
     * <code>org.xml.sax.helpers.DefaultHandler</code>,
     * which in turn implements <code>org.xml.sax.ContentHandler</code>.
     * Called when a recoverable parser warning occurs.
     */
    public void warning(SAXParseException exception)
            throws SAXException {
        printSaxParseException("caught in 'warning' method", exception);
    }


    // Util
    /**
     * If the first String parameter is nonempty, return it,
     * else return the second string parameter.
     *
     * @param s1 The string to be tested.
     * @param s2 The alternate String.
     * @return s1 if it isn't empty, else s2.
     */
    private String getName(String s1, String s2) {
        if (s1 == null || "".equals(s1)) return s2;
        else return s1;
    }

    /**
     * Returns a String to use for indentation and changes the static
     * variable <code>indentLevel</code> according to the input parameter
     * <code>change</code> as follows:
     * <ul>
     *   <li>If <code>change</code> < 0, reduce the indentation
     *       level and return a string at the new level</li>
     *   <li>If <code>change</code> = 0, return a string at the
     *       current level</li>
     *   <li>If <code>change</code> > 0, increase the indentation,
     *       but return a string at previous level</li>
     * <ul>
     *
     * @param change Whether to decrease, leave alone, or increase
     *      the indentation level.
     * @return A string to use for indentation.
     */
    private String indent(int change) {
        int returnLevel;

        if (change > 0) {
            returnLevel = indentLevel;
            indentLevel++;
        } else if (change == 0) {
            returnLevel = indentLevel;
        } else {
            assert change < 0;
            indentLevel--;
            returnLevel = indentLevel;
        }

        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < returnLevel; i++) {
            buffer.append("  ");
        }

        return buffer.toString();
    }

    // Wrap I/O exceptions in SAX exceptions, to
    // suit handler signature requirements
    private void write(String s)
            throws SAXException {
        try {
            out.write(s);
            out.flush();
        } catch (IOException ioException) {
            throw new SAXParseException("I/O error",
                    documentLocator,
                    ioException);
        }
    }

    /**
     * Utility method to print information about a SAXException.
     *
     * @param message A message to be included in the error output.
     * @param e The exception to be printed.
     */
    void printSaxException(String message, SAXException e) {
        System.err.println();
        System.err.println("*** SAX Exception -- " + message);
        System.err.println("      SystemId = \"" +
                documentLocator.getSystemId() + "\"");
        e.printStackTrace(System.err);
    }

    /**
     * Utility method to print information about a SAXParseException.
     *
     * @param message A message to be included in the error output.
     * @param e The exception to be printed.
     */
    void printSaxParseException(String message,
                                SAXParseException e) {
        System.err.println();
        System.err.println("*** SAX Parse Exception -- " + message);
        System.err.println("      SystemId = \"" + e.getSystemId() + "\"");
        System.err.println("      PublicId = \"" + e.getPublicId() + "\"");
        System.err.println("      line number " + e.getLineNumber());
        e.printStackTrace(System.err);
    }
}

