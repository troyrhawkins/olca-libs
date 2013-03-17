package org.openlca.bundler;

public class ImportPackage {

	private PackageNode packageNode;
	private boolean optional;
	private String version;

	public PackageNode getPackageNode() {
		return packageNode;
	}

	public void setPackageNode(PackageNode packageNode) {
		this.packageNode = packageNode;
	}

	public boolean isOptional() {
		return optional;
	}

	public void setOptional(boolean optional) {
		this.optional = optional;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((packageNode == null) ? 0 : packageNode.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
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
		ImportPackage other = (ImportPackage) obj;
		if (packageNode == null) {
			if (other.packageNode != null)
				return false;
		} else if (!packageNode.equals(other.packageNode))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}

}
