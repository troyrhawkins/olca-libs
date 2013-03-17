olca-bundler
============
The olca-bundler is a tool for generating OSGi bundles from a set of Java libraries (jars).
It uses the [Bnd tool](http://www.aqute.biz/Code/Bnd) for the analysis of the jars and the
generation of the OSGi bundles. The usage is as follows:

`java -jar <bundler-jar> 
	<path to directory with jars> 
	<path to output directory for OSGi bundles>`


 