package org.openlca.bundler;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class JarPool {

	private Logger log = LoggerFactory.getLogger(getClass());
	private List<JarNode> jars = new ArrayList<>();
	private List<UberJar> uberJars = new ArrayList<>();

	/** Register a new jar to the pool. */
	public void register(JarNode jarNode) {
		if (jars.contains(jarNode))
			return;
		log.trace("register Jar {}", jarNode);
		List<JarNode> mergeCandidates = findMergeCandidates(jarNode);
		if (!mergeCandidates.isEmpty())
			buildUberJar(jarNode, mergeCandidates);
		jars.add(jarNode);
	}

	private List<JarNode> findMergeCandidates(JarNode jarNode) {
		List<JarNode> candidates = new ArrayList<>();
		for (PackageNode export : jarNode.getExports()) {
			for (JarNode candidate : jars) {
				if (candidates.contains(candidate))
					continue;
				if (candidate.provides(export))
					candidates.add(candidate);
			}
		}
		addUberJarNodes(candidates);
		return candidates;
	}

	private void addUberJarNodes(List<JarNode> candidates) {
		List<JarNode> fromUberJars = new ArrayList<>();
		for (JarNode candidate : candidates) {
			if (!candidate.hasUberJar())
				continue;
			fromUberJars.addAll(candidate.getUberJar().getJarNodes());
		}
		for (JarNode fromUberJar : fromUberJars)
			if (!candidates.contains(fromUberJar))
				candidates.add(fromUberJar);
	}

	private void buildUberJar(JarNode jarNode, List<JarNode> otherJars) {
		log.trace("Build uber-jar {}", jarNode.getName());
		UberJar uberJar = new UberJar(jarNode);
		for (JarNode otherJar : otherJars) {
			if (otherJar.hasUberJar())
				uberJars.remove(otherJar.getUberJar());
			log.trace("add {} to uber-jar", otherJar);
			uberJar.addJar(otherJar);
		}
		uberJars.add(uberJar);
	}

	public List<JarNode> getJars() {
		return new ArrayList<>(jars);
	}

	public List<UberJar> getUberJars() {
		return new ArrayList<>(uberJars);
	}

	/** Get all import packages from the jars in this pool. */
	public List<ImportPackage> getImports() {
		List<ImportPackage> imports = new ArrayList<>();
		for (JarNode jar : jars) {
			for (ImportPackage importPackage : jar.getImports()) {
				if (!imports.contains(importPackage))
					imports.add(importPackage);
			}
		}
		return imports;
	}

	/** Get the availability status of the given import package in the jar-pool. */
	public PackageAvailability getAvailability(ImportPackage importPackage) {
		PackageNode node = importPackage.getPackageNode();
		for (JarNode jar : jars) {
			if (jar.provides(node))
				return PackageAvailability.LIBRARY;
		}
		if (node.isInSystem())
			return PackageAvailability.SYSTEM;
		else
			return PackageAvailability.NOT_PROVIDED;
	}

	/** Get the export packages of all jars in this pool. */
	public List<PackageNode> getExports() {
		List<PackageNode> exports = new ArrayList<>();
		for (JarNode jar : jars) {
			for (PackageNode export : jar.getExports()) {
				if (!exports.contains(export))
					exports.add(export);
			}
		}
		return exports;
	}

	/** Get the usage status of the given package in the jar-pool. */
	public PackageUsage getUsage(PackageNode packageNode) {
		for (JarNode jar : jars) {
			if (jar.requires(packageNode))
				return PackageUsage.USED;
		}
		return PackageUsage.NOT_USED;
	}
}
