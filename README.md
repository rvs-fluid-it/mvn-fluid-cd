mvn-fluid-cd
============

Maven extension(s) to facilitate continuously delivering microservices

## Problem statement

> Continuously delivering [microservices](http://martinfowler.com/articles/microservices.html) built by Maven is tough and painfull. 

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
  * Stay tuned ...
