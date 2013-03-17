package org.openlca.bundler;

import java.util.ArrayList;
import java.util.List;

class PackageNode {

	private String name;
	private List<PackageNode> usedPackages = new ArrayList<>();
	private Boolean inSystem = null;

	public PackageNode(String name) {
		this.name = name;
	}

	/** Returns true if the package is available in the system. */
	public boolean isInSystem() {
		if (inSystem != null)
			return inSystem;
		inSystem = findInSystem();
		return inSystem;
	}

	private boolean findInSystem() {
		Package p = Package.getPackage(name);
		if (p != null)
			return true; // loaded
		return new SystemPackageSearch().find(name);
	}

	public String getName() {
		return name;
	}

	public List<PackageNode> getUsedPackages() {
		return usedPackages;
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
		PackageNode other = (PackageNode) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PackageNode [name=" + name + "]";
	}

}
