package org.openlca.bundler;

import org.junit.Assert;
import org.junit.Test;

public class BundleInfoTest {

	@Test
	public void testFromNameWithSuffix() {
		String lib = "slf4j-log4j12-1.54.b2.jar";
		BundleInfo info = BundleInfo.forJarName(lib);
		Assert.assertEquals("slf4j-log4j12", info.getSmbolicName());
		Assert.assertEquals("1.54", info.getVersion());
	}

	@Test
	public void testFromNameThreeParts() {
		String lib = "slf4j-log4j12-1.54.22.jar";
		BundleInfo info = BundleInfo.forJarName(lib);
		Assert.assertEquals("slf4j-log4j12", info.getSmbolicName());
		Assert.assertEquals("1.54.22", info.getVersion());
	}

	@Test
	public void testFromNameTwoParts() {
		String lib = "slf4j-log4j12-1.54.jar";
		BundleInfo info = BundleInfo.forJarName(lib);
		Assert.assertEquals("slf4j-log4j12", info.getSmbolicName());
		Assert.assertEquals("1.54", info.getVersion());
	}

	@Test
	public void testNoVersion() {
		String lib = "slf4j-log4j12.jar";
		BundleInfo info = BundleInfo.forJarName(lib);
		Assert.assertEquals("slf4j-log4j12", info.getSmbolicName());
		Assert.assertEquals("0.0.0", info.getVersion());
	}

}
