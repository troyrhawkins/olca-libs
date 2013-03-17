olca-poi
============
This project is just a Maven configuration to build an OSGi bundle for 
[Apache POI](http://poi.apache.org/) including the HSSF and XSSF packages.

As there are some issues when we try to build a POI OSGi package including 
HSSF and XSSF the right way (same package names in different jars, class
loader issues due to the use of reflection, etc.) the simplest way is
to create a single OSGi bundle including all required resources. This is 
what we are doing here.
 

 