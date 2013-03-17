package org.openlca.bundler;

/**
 * Indicates the availability of a package.
 */
enum PackageAvailability {

	/** The package is available in the standard library; means JRE. */
	SYSTEM,

	/** The package is provided by another library or bundle. */
	LIBRARY,

	/**
	 * The package is not provided via the standard library or another library
	 * or bundle.
	 */
	NOT_PROVIDED

}
