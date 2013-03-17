olca-bundler
============
The olca-bundler is a tool for generating OSGi bundles from a set of Java libraries (jars).
It uses the [Bnd tool](http://www.aqute.biz/Code/Bnd) for the analysis of the jars and the
generation of the OSGi bundles. The usage is as follows:

`java -jar <bundler-jar> 
	<path to directory with jars> 
	<path to output directory for OSGi bundles>`

In the process of generating the OSGi bundles the bundler does the following steps:

1. The provided and required packages of every jar are analyzed
2. If a jar is already an OSGi bundle it is directly copied to the output folder
3. For the other jars the `Import-Package` directive is calculated where packages are set
 as ignored if they are not provided by the other jars in the input directory or the 
 system runtime 
4. If there are packages provided by multiple jars an single bundle is generated for these
 libraries.
5. The calculated manifests are written under the 'resource' folder of the output directory
 and the Bnd tool is executed for each library with the respective manifest as input
 
Additionally, a dependency graph is written to the 'resource' folder of the specified
output directory: `dependencies.gv`. It contains the jars and their package dependencies 
in the [Graphviz](http://www.graphviz.org/) graph-format which can be used for the
visualization of the dependencies.


 