openLCA target platform
=======================
The ANT build of this project creates the openLCA target platform.
It mainly copies the OSGi bundles from the various Eclipse sources into
the respective plug-in and feature directories. The main purpose of
this is to keep track of the libraries that we use in openLCA. With this
we are also able to change single libraries without searching for the old
one and its dependencies.

The non-Eclipse dependencies are managed via the olca-deps project 
(https://github.com/GreenDelta/olca-libs/tree/master/olca-deps) which 
generates the respective OSGi bundles. 



 