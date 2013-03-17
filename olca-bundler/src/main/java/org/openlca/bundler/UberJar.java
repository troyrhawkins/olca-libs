package org.openlca.bundler;

import java.util.ArrayList;
import java.util.List;

/**
 * An "uber"-jar is required if there are multiple jars (with the same version)
 * that export the same package. When creating the output-bundles we first
 * generate a single jar of these jars.
 */
class UberJar implements IJarInfo {

	private String name;
	private List<JarNode> jarNodes = new ArrayList<>();
	private List<ImportPackage> imports = new ArrayList<>();
	private List<PackageNode> exports = new ArrayList<>();

	public UberJar(JarNode firstJar) {
		this.name = firstJar.getName();
		firstJar.setUberJar(this);
		jarNodes.add(firstJar);
		imports.addAll(firstJar.getImports());
		exports.addAll(firstJar.getExports());
	}

	public void addJar(JarNode jarNode) {
		if (jarNodes.contains(jarNode))
			return;
		jarNode.setUberJar(this);
		jarNodes.add(jarNode);
		for (PackageNode packageNode : jarNode.getExports())
			if (!exports.contains(packageNode))
				exports.add(packageNode);
		for (ImportPackage importPackage : jarNode.getImports())
			if (!imports.contains(importPackage))
				imports.add(importPackage);
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

	public List<JarNode> getJarNodes() {
		return new ArrayList<>(jarNodes);
	}

	@Override
	public boolean provides(PackageNode packageNode) {
		return exports.contains(packageNode);
	}

	@Override
	public boolean requires(PackageNode packageNode) {
		for (ImportPackage importPackage : imports)
			if (importPackage.getPackageNode().equals(packageNode))
				return true;
		return false;
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
		UberJar other = (UberJar) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UberJar [name=" + name + "]";
	}

}
