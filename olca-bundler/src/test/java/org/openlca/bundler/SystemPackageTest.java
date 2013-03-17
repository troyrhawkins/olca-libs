package org.openlca.bundler;

import org.junit.Assert;
import org.junit.Test;

public class SystemPackageTest {

	@Test
	public void testPackageExists() throws Exception {
		PackageNode node = new PackageNode("javax.management");
		Assert.assertTrue(node.isInSystem());
		node = new PackageNode("java.util");
		Assert.assertTrue(node.isInSystem());
	}

	@Test
	public void testPackageNotExists() throws Exception {
		PackageNode node = new PackageNode("org.openlca");
		Assert.assertFalse(node.isInSystem());
		node = new PackageNode("java.util2");
		Assert.assertFalse(node.isInSystem());
	}
}
