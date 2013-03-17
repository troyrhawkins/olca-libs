package org.openlca.bundler;

import java.io.BufferedWriter;
import java.io.Writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DotExport implements Runnable {

	private Logger log = LoggerFactory.getLogger(getClass());
	private Writer writer;
	private JarPool jarPool;

	public DotExport(Writer writer, JarPool jarPool) {
		this.writer = writer;
		this.jarPool = jarPool;
	}

	@Override
	public void run() {
		try (BufferedWriter buffer = new BufferedWriter(writer)) {
			buffer.write("digraph g {");
			buffer.newLine();
			writeJarNodes(buffer);
			writeImportNodes(buffer);
			writeImportLinks(buffer);
			writeExportLinks(buffer);
			buffer.write("}");
		} catch (Exception e) {
			log.error("Failed to write graph");
		}
	}

	private void writeJarNodes(BufferedWriter writer) throws Exception {
		for (JarNode jarNode : jarPool.getJars()) {
			String line = q(jarNode.getName());
			line += " [shape=component,style=filled,fillcolor=";
			line += jarNode.isBundle() ? q("#8ACC9E") : q("#6BCADE");
			line += "]";
			writer.write(line);
			writer.newLine();
		}
		writer.newLine();
	}

	private void writeImportNodes(BufferedWriter writer) throws Exception {
		for (ImportPackage importPackage : jarPool.getImports()) {
			String line = q(importPackage.getPackageNode().getName());
			line += " [shape=ellipse,style=filled,fillcolor=";
			line += q(getPackageColor(importPackage)) + "]";
			writer.write(line);
			writer.newLine();
		}
		writer.newLine();
	}

	private String getPackageColor(ImportPackage importPackage) {
		switch (jarPool.getAvailability(importPackage)) {
		case LIBRARY:
			return "#FCCF7C";
		case SYSTEM:
			return "#FB93AC";
		case NOT_PROVIDED:
			return "#867CBB";
		default:
			return "#000000";
		}
	}

	private void writeImportLinks(BufferedWriter writer) throws Exception {
		for (JarNode jar : jarPool.getJars()) {
			for (ImportPackage importPackage : jar.getImports()) {
				String line = q(importPackage.getPackageNode().getName());
				line += " -> " + q(jar.getName());
				writer.write(line);
				writer.newLine();
			}
		}
		writer.newLine();
	}

	private void writeExportLinks(BufferedWriter writer) throws Exception {
		for (JarNode jar : jarPool.getJars()) {
			for (PackageNode export : jar.getExports()) {
				if (jarPool.getUsage(export) == PackageUsage.NOT_USED)
					continue;
				String line = q(jar.getName()) + " -> " + q(export.getName());
				writer.write(line);
				writer.newLine();
			}
		}
		writer.newLine();
	}

	private String q(String val) {
		return "\"" + val + "\"";
	}

}
