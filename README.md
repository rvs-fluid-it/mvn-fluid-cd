mvn-fluid-cd
============

[Maven](http://maven.apache.org) extension(s) to facilitate [continuous delivery](http://martinfowler.com/books/continuousDelivery.html) of [microservices](http://martinfowler.com/articles/microservices.html)

## Problem statement

> Continuously delivering [microservices](http://martinfowler.com/articles/microservices.html) built by Maven is tough and painful. 

(Anno 2014)

* [Technology radar Oktober 2012 - Thoughtworks](http://www.thoughtworks.com/radar/tools/maven)

> Maven has long been a staple of build automation in the Java space. However, given its lack of flexibility and support for automation best practices, especially in the Continuous Delivery domain, the use of alternatives such as Gradle should be considered.

* [Evil (Maven) Snapshots - Halil-Cem Gürsoy](http://bed-con.org/2014/files/slides/evil_snapshots-bedcon2014.pdf) 

## As Is (Anno 2014)
* [Continuous delivery friendly Maven versions (MNG-5576) -> Fixed - Jason van Zyl](https://jira.codehaus.org/browse/MNG-5576)
* [Real-world strategies for continuous delivery with Maven and Jenkins - John Ferguson Smart](http://www.slideshare.net/wakaleo/continuous-deliverywithmaven)
* [Takari generations - Jason van Zyl](http://www.slideshare.net/Takari_io/takari-1)


## Bringing back the joy
* Requirements gathering
  * Dave Dev says:
    * _Let me use my snapshots and for the rest stay out of my way ..._ 
  * Oliver Ops says:
    * _Freeze! And no adding a revision number in a manifest file is not good enough ..._
  * Jerry Jenkins
    * _I'll do it for you but Keep It Simple Stupid ..._
  * ...
* Scope
  * Java/JVM
  * Maven as build tool
  * git, svn as versioning tool
  * ...
* Solution
  * Bunch of Maven extensions
  * Algorithm
    * Activate freezing when a revision is provided (-Drevision=xyz)
    * Start parsing pom.xml file and stream it to frozen.pom.xml 
    * Extract the properties (groupId, artifactId, version) of the pom's project artifact
    * Replace the snapshot ending of the pom's project artifact version with the given revision number (1.2-SNAPSHOT -> 1.2-xyz)
    * For each snapshot dependency version encountered while parsing the pom
      * Lookup the latest frozen version of the artifact in Maven's local repository
      * Replace the version of the dependency whith the latest frozen version
    * Let the Maven build use the frozen poms instead of the regular poms for all subsequent steps (especially install and deploy)
  * See it in action
    * Clone the _mvn-fluid-it_ github project
    * 'cd mvn-ext-s-modules'
    * Run _'mvn clean install'_ (First run will build the Maven extensions and will copy these to the $M2_HOME/lib/ext folder)
    * Run _'mvn clean install'_ again (The magic (= the freezing of the snapshots :-)) will now happen because the extensions were installed in the previous step.) 
    * Have a look at the integration tests in _'mvn-ext-freeze/src/it'_
    * (To uninstall the extensions just remove the four jars from the $M2_HOME/lib/ext)
  * Links
    * [Creating a Custom Build Extension for Maven 3.0 - Bret Porter](https://brettporter.wordpress.com/2010/10/05/creating-a-custom-build-extension-for-maven-3-0/)
    * [Plexus Container Five Minute Tutorial](http://blog.sonatype.com/2009/05/plexus-container-five-minute-tutorial)
    * [Maven's event spy mechanism](http://maven.apache.org/ref/3.2.3/apidocs/org/apache/maven/eventspy/EventSpy.html)
    * [__S__ uper __H__ elpful __I__ ntegration __T__ est __T__ hingy  , better known as the Shitty plugin](http://mojo.codehaus.org/shitty-maven-plugin/index.html)
