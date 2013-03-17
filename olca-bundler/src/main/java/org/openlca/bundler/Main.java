package org.openlca.bundler;

import java.io.File;

public class Main {

	public static void main(String[] args) {
		System.out.println("openLCA jar - bundler: creates "
				+ "OSGi bundles from jars");
		if (args == null || args.length < 2)
			printHelp();
		try {
			File libDir = new File(args[0]);
			File outDir = new File(args[1]);
			if (!outDir.exists())
				outDir.mkdirs();
			Bundler bundler = new Bundler(libDir, outDir);
			bundler.run();
		} catch (Exception e) {
			System.err.println("Failed to run bunder");
			e.printStackTrace();
			printHelp();
		}
	}

	private static void printHelp() {
		System.out.print("Usage: java -jar bundler-<version>.jar ");
		System.out.print("<path to directory with jars>");
		System.out.println("<path to output directory for OSGi bundles>");
	}

}
