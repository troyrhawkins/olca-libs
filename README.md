olca-libs
=========
This repository contains tools and scripts to build the runtime dependencies of openLCA as 
OSGi bundles.

olca-bundler
------------
The olca-bundler is a tool for generating OSGi bundles from a set of Java libraries (jars).

olca-deps
---------
This project directly generates the OSGi bundles of some of the third party libraries 
required for openLCA directly from a Maven POM. Therefore it uses the olca-bundler tool.

olca-poi
--------
This project is a Maven configuration to build an OSGi bundle for 
[Apache POI](http://poi.apache.org/).

