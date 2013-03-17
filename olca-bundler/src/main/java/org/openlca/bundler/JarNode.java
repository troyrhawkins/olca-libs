package org.openlca.bundler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class JarNode implements IJarInfo {

	private boolean bundle;
	private UberJar uberJar;
	private BundleInfo bundleInfo;
	private String name;
	private File file;
	private List<PackageNode> exports = new ArrayList<>();
	private List<ImportPackage> imports = new ArrayList<>();

	public JarNode(File file) {
		this.name = file.getName();
		this.file = file;
	}

	public File getFile() {
		return file;
	}

	@Override
	public boolean provides(PackageNode node) {
		for (PackageNode export : exports)
			if (export.equals(node))
				return true;
		return false;
	}

	@Override
	public boolean requires(PackageNode node) {
		for (ImportPackage importPackage : imports)
			if (importPackage.getPackageNode().equals(node))
				return true;
		return false;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<PackageNode> getExports() {
		return exports;
	}

	@Override
	public List<ImportPackage> getImports() {
		return imports;
	}

	/** Returns true if the Jar is already an OSGi bundle. */
	public boolean isBundle() {
		return bundle;
	}

	/** Set this to true if the Jar is already an OSGi bundle. */
	public void setBundle(boolean bundle) {
		this.bundle = bundle;
	}

	public UberJar getUberJar() {
		return uberJar;
	}

	public void setUberJar(UberJar uberJar) {
		this.uberJar = uberJar;
	}

	/** Returns true if this Jar is part of an uber-jar. */
	public boolean hasUberJar() {
		return uberJar != null;
	}

	@Override
	public String toString() {
		return "JarNode [name=" + name + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JarNode other = (JarNode) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public BundleInfo getBundleInfo() {
		return bundleInfo;
	}

	public void setBundleInfo(BundleInfo bundleInfo) {
		this.bundleInfo = bundleInfo;
	}

}
