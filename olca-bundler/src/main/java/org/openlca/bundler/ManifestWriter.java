package org.openlca.bundler;

import java.io.BufferedWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;

/**
 * See also http://www.aqute.biz/Bnd/Format for the BND directives.
 */
class ManifestWriter {

	private IJarInfo jarNode;
	private JarPool jarPool;

	public ManifestWriter(IJarInfo jarNode, JarPool jarPool) {
		this.jarNode = jarNode;
		this.jarPool = jarPool;
	}

	public void write(Writer writer) throws Exception {
		try (BufferedWriter buffer = new BufferedWriter(writer)) {
			writeGeneralInfos(buffer);
			writeExports(buffer);
			writeImports(buffer);
		}
	}

	private void writeGeneralInfos(BufferedWriter buffer) throws Exception {
		BundleInfo info = BundleInfo.forJarName(jarNode.getName());
		line(buffer, "Bundle-Name: " + info.getSmbolicName());
		line(buffer, "Bundle-SymbolicName: " + info.getSmbolicName()
				+ ";singleton:=true");
		line(buffer, "Bundle-Version: " + info.getVersion());
		line(buffer, "Bundle-RequiredExecutionEnvironment: JavaSE-1.6");
		line(buffer, "Bundle-ActivationPolicy: lazy");
		line(buffer, "-nouses: true");
		line(buffer, "-nodefaultversion: true");
	}

	private void writeExports(BufferedWriter buffer) throws Exception {
		List<String> exports = new ArrayList<>();
		for (PackageNode export : jarNode.getExports()) {
			exports.add(export.getName() + ";-noimport:=true");
		}
		buffer.write("Export-Package: " + Joiner.on(",").join(exports));
		buffer.newLine();
	}

	private void writeImports(BufferedWriter buffer) throws Exception {
		List<String> imports = new ArrayList<>();
		for (ImportPackage importPackage : jarNode.getImports()) {
			if (jarNode.provides(importPackage.getPackageNode()))
				continue;
			String str = importPackage.getPackageNode().getName();
			if (jarPool.getAvailability(importPackage) == PackageAvailability.NOT_PROVIDED)
				str = "!" + str;
			// if (jarPool.getAvailability(importPackage) ==
			// PackageAvailability.NOT_PROVIDED)
			// str += ";resolution:=optional";
			imports.add(str);
		}
		buffer.write("Import-Package: " + Joiner.on(",").join(imports));
		buffer.newLine();
	}

	/** Writer a line with carriage return. */
	private void line(BufferedWriter writer, String line) throws Exception {
		writer.write(line);
		writer.newLine();
	}

}
