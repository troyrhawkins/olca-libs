package org.openlca.bundler;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aQute.lib.osgi.Jar;

/**
 * Search for a package in the JRE.
 */
class SystemPackageSearch {

	private Logger log = LoggerFactory.getLogger(getClass());

	public boolean find(String packageName) {
		String libDirPath = System.getProperty("java.home") + "/lib";
		log.trace("search for package {} in system libraries under {}",
				packageName, libDirPath);
		File libDir = new File(libDirPath);
		if (!libDir.exists())
			return false;
		for (File file : libDir.listFiles()) {
			if (!file.getName().endsWith(".jar"))
				continue;
			if (findInJar(file, packageName))
				return true;
		}
		return false;
	}

	public boolean findInJar(File file, String packageName) {
		try (Jar jar = new Jar(file)) {
			for (String other : jar.getPackages()) {
				if (packageName.equals(other))
					return true;
			}
			return false;
		} catch (Exception e) {
			log.error("Failed to search for package name in " + file, e);
			return false;
		}
	}

}
