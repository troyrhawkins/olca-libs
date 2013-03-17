package org.openlca.bundler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Directive {

	private static Logger log = LoggerFactory.getLogger(Directive.class);

	/** Get the package information of an OSGi import directive. */
	public static List<ImportPackage> parseImports(String directive) {
		String input = directive != null ? directive.trim() : null;
		if (input == null || input.isEmpty())
			return Collections.emptyList();
		log.trace("parse import directive: {}", input);
		List<PackageInfo> infos = getPackageInfos(input);
		List<ImportPackage> imports = new ArrayList<>();
		for (PackageInfo info : infos) {
			log.trace("found: package {}, properties {}", info.name,
					info.properties);
			ImportPackage importPackage = new ImportPackage();
			importPackage.setPackageNode(new PackageNode(info.name));
			String resolution = info.properties.get("resolution");
			importPackage.setOptional("optional".equals(resolution));
			importPackage.setVersion(info.properties.get("version"));
			imports.add(importPackage);
		}
		return imports;
	}

	/** Get the package information of an OSGi export directive. */
	public static List<PackageNode> parseExports(String directive) {
		String input = directive != null ? directive.trim() : null;
		if (input == null || input.isEmpty())
			return Collections.emptyList();
		log.trace("parse export directive: {}", input);
		List<PackageNode> nodes = new ArrayList<>();
		List<PackageInfo> infos = getPackageInfos(input);
		for (PackageInfo info : infos) {
			log.trace("found: package {}, properties {}", info.name,
					info.properties);
			PackageNode node = new PackageNode(info.name);
			nodes.add(node);
			addUsedPackages(info, node);
		}
		return nodes;
	}

	private static void addUsedPackages(PackageInfo info, PackageNode node) {
		if (!info.properties.containsKey("uses"))
			return;
		List<PackageInfo> uses = getPackageInfos(info.properties.get("uses"));
		for (PackageInfo useInfo : uses) {
			PackageNode usedNode = new PackageNode(useInfo.name);
			node.getUsedPackages().add(usedNode);
		}
	}

	/** Get the package info of a (non-empty) directive */
	private static List<PackageInfo> getPackageInfos(String directive) {
		List<PackageInfo> infos = new ArrayList<>();
		List<String> statements = getStatements(directive);
		for (String statement : statements) {
			PackageInfo p = new PackageInfo(statement);
			infos.add(p);
		}
		return infos;
	}

	/** Get the comma separated statements of a (non-empty) directive */
	private static List<String> getStatements(String directive) {
		List<String> statements = new ArrayList<>();
		StringBuilder statement = new StringBuilder();
		boolean inQuote = false;
		for (char c : directive.toCharArray()) {
			if (c == '"') {
				inQuote = !inQuote;
				continue;
			}
			if (inQuote) {
				statement.append(c);
				continue;
			}
			if (c != ',')
				statement.append(c);
			else if (statement.length() > 0) {
				statements.add(statement.toString());
				statement = new StringBuilder();
			}
		}
		if (statement.length() > 0) {
			statements.add(statement.toString());
		}
		return statements;
	}

	/**
	 * The package info of a single package statement in an import or export
	 * directive.
	 */
	private static class PackageInfo {

		String name;
		Map<String, String> properties = new HashMap<>();

		PackageInfo(String statement) {
			String[] parts = statement.split(";");
			name = parts[0].trim();
			addProperties(parts);
		}

		private void addProperties(String[] parts) {
			if (parts.length < 2)
				return;
			for (int i = 1; i < parts.length; i++) {
				String[] keyVal = parts[i].split("=|:=", 2);
				if (keyVal.length < 2)
					continue;
				properties.put(keyVal[0].trim(), keyVal[1].trim());
			}
		}
	}

}
