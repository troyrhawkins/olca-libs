package org.openlca.bundler;

import java.io.File;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aQute.lib.osgi.Analyzer;
import aQute.lib.osgi.Jar;

class JarAnalyser {

	private Logger log = LoggerFactory.getLogger(getClass());

	public JarNode analyse(File file) throws Exception {
		log.trace("analyse jar-file {}", file.getName());
		JarNode node = new JarNode(file);
		Jar jar = new Jar(file);
		BundleInfo bundleInfo = getBundleInfo(jar);
		if (bundleInfo == null) {
			node.setBundle(false);
			handlePlainJar(jar, node);
		} else {
			node.setBundle(true);
			node.setBundleInfo(bundleInfo);
			handleBundle(jar, node);
		}
		return node;
	}

	/** Returns the bundle info if exists, otherwise null */
	private BundleInfo getBundleInfo(Jar jar) throws Exception {
		log.trace("bundle info for {}", jar.getName());
		Manifest manifest = jar.getManifest();
		if (manifest == null)
			return null;
		Attributes attributes = manifest.getMainAttributes();
		String symbolicName = attributes.getValue("Bundle-SymbolicName");
		String version = attributes.getValue("Bundle-Version");
		if (symbolicName == null || version == null)
			return null;
		BundleInfo info = new BundleInfo();
		info.setSmbolicName(symbolicName);
		info.setVersion(version);
		log.trace("bundle {} {} found", symbolicName, version);
		return info;
	}

	private void handleBundle(Jar jar, JarNode node) throws Exception {
		log.trace("Handle bundle {}", node.getBundleInfo());
		addPackageInfo(jar.getManifest(), node);
	}

	private void handlePlainJar(Jar jar, JarNode node) throws Exception {
		log.trace("Handle plain jar {}", jar.getName());
		Analyzer analyzer = new Analyzer();
		analyzer.setJar(jar);
		analyzer.setProperty("Import-Package", "*");
		analyzer.setProperty("Export-Package", "*");
		Manifest manifest = analyzer.calcManifest();
		addPackageInfo(manifest, node);
		analyzer.close();
	}

	private void addPackageInfo(Manifest manifest, JarNode node)
			throws Exception {
		log.trace("Add package infos to {}", node);
		Attributes attributes = manifest.getMainAttributes();
		String exports = attributes.getValue("Export-Package");
		String imports = attributes.getValue("Import-Package");
		List<PackageNode> exportPackages = Directive.parseExports(exports);
		node.getExports().addAll(exportPackages);
		List<ImportPackage> importPackages = Directive.parseImports(imports);
		node.getImports().addAll(importPackages);
	}
}
