package org.openlca.bundler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class DirectiveTest {

	@Test
	public void parseSimpleExport() {
		String directive = "org.slf4j, org.slf4j.spi";
		List<PackageNode> nodes = Directive.parseExports(directive);
		assertTrue(nodes.size() == 2);
		assertTrue(nodes.contains(new PackageNode("org.slf4j")));
		assertTrue(nodes.contains(new PackageNode("org.slf4j.spi")));
	}

	@Test
	public void parseExport() {
		String directive = "org.slf4j;version=1.2.3; uses:=\"a.b.c, b.c.d, e.f.g\", org.slf4j.spi;version=2.3";
		List<PackageNode> nodes = Directive.parseExports(directive);
		assertTrue(nodes.size() == 2);
		assertTrue(nodes.contains(new PackageNode("org.slf4j")));
		assertTrue(nodes.contains(new PackageNode("org.slf4j.spi")));
	}

	@Test
	public void parseExportWithUse() {
		String directive = "org.slf4j;version=1.2.3; uses:=\"a.b.c, b.c.d, e.f.g\", org.slf4j.spi;version=2.3";
		List<PackageNode> nodes = Directive.parseExports(directive);
		PackageNode nodeWithUse = null;
		for (PackageNode node : nodes)
			if (node.getName().equals("org.slf4j"))
				nodeWithUse = node;
		List<PackageNode> used = nodeWithUse.getUsedPackages();
		assertTrue(used.size() == 3);
		assertTrue(used.contains(new PackageNode("a.b.c")));
		assertTrue(used.contains(new PackageNode("b.c.d")));
		assertTrue(used.contains(new PackageNode("e.f.g")));
	}

	@Test
	public void parseSimpleImport() {
		String directive = "com.sun.msv.datatype, com.sun.msv.datatype.xsd";
		List<ImportPackage> imports = Directive.parseImports(directive);
		assertTrue(imports.size() == 2);
		for (ImportPackage pack : imports) {
			assertTrue(pack.getPackageNode().getName()
					.equals("com.sun.msv.datatype")
					|| pack.getPackageNode().getName()
							.equals("com.sun.msv.datatype.xsd"));
			assertFalse(pack.isOptional());
		}
	}

	@Test
	public void parseImports() {
		String directive = "com.sun.msv.datatype;resolution:=optional;version=\"[3.8,4)\","
				+ "com.sun.msv.datatype.xsd;resolution:=optional;version=\"[3.8,4)\"";
		List<ImportPackage> imports = Directive.parseImports(directive);
		assertTrue(imports.size() == 2);
		for (ImportPackage pack : imports) {
			assertTrue(pack.getPackageNode().getName()
					.equals("com.sun.msv.datatype")
					|| pack.getPackageNode().getName()
							.equals("com.sun.msv.datatype.xsd"));
			assertTrue(pack.isOptional());
			assertTrue(pack.getVersion().equals("[3.8,4)"));
		}
	}
}
