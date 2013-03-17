olca-deps
============
This project directly generates the OSGi bundles of some of the third party libraries
required for openLCA directly from a Maven POM. Therefore it uses the olca-bundler
tool which you can also find in this repository. 

To run the build copy the bundler (with dependencies) into the 'tool' directory of this project
(check the POM if the name of the bundler jar is still correct).

When you run the POM with the Maven package command the libraries will be copied
into the 'lib' folder of the build-directory and than the bundler will generate
the OSGi bundles of these libraries into the 'bundles' folder of the build directory.

All libraries should be available in the central Maven repository except ojalgo 33 (?).
Thus, just [download ojalgo](http://sourceforge.net/projects/ojalgo/files/ojAlgo/) 
and install it into your local repository via:

`mvn install:install-file -Dfile=<path-to-jar> -DgroupId=org.ojalgo \
    -DartifactId=ojalgo -Dversion=33.0 -Dpackaging=jar`

 