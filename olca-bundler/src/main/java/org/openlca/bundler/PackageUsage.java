package org.openlca.bundler;

/**
 * Indicates the usage of a package in a set of libraries.
 */
enum PackageUsage {

	/** The package is used in library as import package. */
	USED,

	/** The package is not used by a library or bundle. */
	NOT_USED

}
