~ Surfing the continuous delivery pipe ~
========================================
![Having fun continuously delivering microservices built by Maven](http://creativecan.com/wp-content/uploads/2012/06/wave-tube_thumb.jpg)

mvn-fluid-cd
============
[Maven](http://maven.apache.org) extension(s) to facilitate [continuous delivery](http://martinfowler.com/books/continuousDelivery.html) of [microservices](http://martinfowler.com/articles/microservices.html)

[![Build Status](https://api.travis-ci.org/rvs-fluid-it/mvn-fluid-cd.svg)](https://travis-ci.org/rvs-fluid-it/mvn-fluid-cd)

## Problem statement (Anno 2014)
![Problem statement](http://www.hydroflex-technology.com/lab/wp-content/uploads/2013/04/broken-surfboard-thumb.jpg)

__Continuously delivering [microservices](http://martinfowler.com/articles/microservices.html) built by Maven is tough and painful.__ 

* [Technology radar October 2012 - Thoughtworks](http://www.thoughtworks.com/radar/tools/maven)

  > Maven has long been a staple of build automation in the Java space. However, given its lack of flexibility and support for automation best practices, especially in the Continuous Delivery domain, the use of alternatives such as Gradle should be considered.

* [Evil (Maven) Snapshots - Halil-Cem Gürsoy](http://bed-con.org/2014/files/slides/evil_snapshots-bedcon2014.pdf)

__Programming in the CI/CD tool is hard to scale.__ 

* [Technology radar January 2015 Preview - Thoughtworks](http://www.thoughtworks.com/radar/techniques)

  > A common pre-requisite for doing continuous integration has been to have a single command that could be executed on a clone of your repository on a brand new machine to get a working environment which can build, test and package your application. This focuses the attention on automation, makes it easy for new users to get started, reduces chances of snowflake build environments and results in reliable build results that can be recreated on demand either in the CI environment or locally. However, we often see teams ignoring this and resorting to __programming in their CI/CD tool__, which not only results in build failures that are hard to debug, they also lose out on the upstream benefits of having the single point of entry to configuring and building their projects. In this version of the radar, we would like to strongly advise against this practice which sacrifices many of the benefits of CI in the interest of expediency.


## As Is (Anno 2014)
* [Continuous delivery friendly Maven versions (MNG-5576) -> Fixed - Jason van Zyl](https://issues.apache.org/jira/browse/MNG-5576)
* [Real-world strategies for continuous delivery with Maven and Jenkins - John Ferguson Smart](http://www.slideshare.net/wakaleo/continuous-deliverywithmaven)
* [Takari generations - Jason van Zyl](http://www.slideshare.net/Takari_io/takari-1)
* [Continuous Delivery for Microservices with Jenkins and the Job DSL Plugin](https://blog.codecentric.de/en/2015/01/continuous-delivery-microservices-jenkins-job-dsl-plugin/#more-25860)


## Bringing back the joy
![Bringing back the joy ~](http://creativecan.com/wp-content/uploads/2012/06/big-wave-surfing-_thumb3_thumb.jpg)
* Requirements gathering
  * Dave Dev says:
    * _Let me use my snapshots and for the rest stay out of my way ..._ 
  * Oliver Ops says:
    * _Freeze! And no adding a revision number in a manifest file is not good enough ..._
  * Jerry Jenkins
    * _I'll do it for you but Keep It Simple Stupid ..._
  * ...
* Scope
  * Java/JVM 1.7
  * Maven as build tool
    * Tested with 3.2.3 
  * git, svn as versioning tool
  * [Trunk based development using feature toggles](http://paulhammant.com/2013/04/05/what-is-trunk-based-development/)
  * ...
* Solution

  ![Solution](/mvn-ext-freeze/src/doc/Continuous delivery with Maven.png?raw=true)
  * Bundle of Maven extensions
    * General design rules for Maven extensions
      * Maximal cohesion, minimal coupling
      * Less is more
        * Minimize the transitive jar dependencies of the extension
        * Minimize the inter extension dependencies
      * Follow or extrapolate existing Maven conventions/principles 
      * Piggyback existing Maven functionality
      * Not viral
        * Local preprocessing of poms
        * Pom's stored in a Maven repository are processed poms and do not enforce installation of the extension when depending on these
      * Graceful degradation when extension is not installed
      * Does not break the Maven support in the IDE
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
  
    ![Sample](/mvn-ext-freeze/src/doc/sample-service-dependencies-graph.png?raw=true)
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
    * [40 Amazing Examples of Wave and Surf Photography - Creative Commons](http://creativecan.com/2012/06/wave-and-surf-photography/)
