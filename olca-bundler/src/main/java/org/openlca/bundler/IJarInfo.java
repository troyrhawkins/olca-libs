package org.openlca.bundler;

import java.util.List;

/**
 * Provides the general information of a jar in order to create a bundle.
 */
interface IJarInfo {

	/**
	 * The name should follow the Maven conventions:
	 * [artifact-id]-[version].jar.
	 */
	String getName();

	/** The imported packages of the jar. */
	List<ImportPackage> getImports();

	/** The export packages of the jar. */
	List<PackageNode> getExports();

	/** Indicates whether the jar provides the given package. */
	boolean provides(PackageNode packageNode);

	/** Indicates whether the jar requires the given package. */
	boolean requires(PackageNode packageNode);

}
