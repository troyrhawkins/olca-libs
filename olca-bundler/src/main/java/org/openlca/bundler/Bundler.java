package org.openlca.bundler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Files;
import com.google.common.io.InputSupplier;

public class Bundler {

	private Logger log = LoggerFactory.getLogger(getClass());

	private final String BND_TOOL = "bnd-1.50.0.jar";
	private File rawDir;
	private File outDir;
	private File resourceDir;

	public Bundler(File rawDir, File outDir) {
		this.rawDir = rawDir;
		this.outDir = outDir;
	}

	public void run() {
		resourceDir = new File(outDir, "resources");
		if (!resourceDir.exists())
			resourceDir.mkdirs();
		copyBndTool();
		JarPool jarPool = createJarPool();
		writeGraph(jarPool);
		handleJars(jarPool);
		handleUberJars(jarPool);
		log.info("DONE");
	}

	private void copyBndTool() {
		try {
			File bndFile = new File(resourceDir, BND_TOOL);
			Files.copy(new InputSupplier<InputStream>() {
				@Override
				public InputStream getInput() throws IOException {
					return Bundler.class.getResourceAsStream(BND_TOOL);
				}
			}, bndFile);
		} catch (Exception e) {
			log.error("Failed to copy BND tool", e);
		}
	}

	private JarPool createJarPool() {
		JarPool pool = new JarPool();
		try {
			for (File file : rawDir.listFiles()) {
				if (!file.getName().endsWith(".jar"))
					continue;
				JarAnalyser jarAnalyser = new JarAnalyser();
				JarNode node = jarAnalyser.analyse(file);
				pool.register(node);
			}
		} catch (Exception e) {
			log.error("Faile to parse jar-file", e);
		}
		return pool;
	}

	private void writeGraph(JarPool jarPool) {
		File graphFile = new File(resourceDir, "dependencies.gv");
		log.info("write dependency graph to {}", graphFile);
		try (FileWriter writer = new FileWriter(graphFile)) {
			DotExport export = new DotExport(writer, jarPool);
			export.run();
		} catch (Exception e) {
			log.error("Failed to write dependency graph", e);
		}
	}

	private void handleJars(JarPool jarPool) {
		for (JarNode jarNode : jarPool.getJars()) {
			if (jarNode.hasUberJar())
				continue;
			if (jarNode.isBundle())
				copyBundle(jarNode);
			else
				convertJar(jarNode, jarPool);
		}
	}

	private void handleUberJars(JarPool jarPool) {
		for (UberJar uberJar : jarPool.getUberJars()) {
			List<JarNode> jars = uberJar.getJarNodes();
			if (jars.isEmpty())
				continue;
			if (jars.size() == 1)
				convertJar(jars.get(0), jarPool);
			else
				convertUberJar(uberJar, jarPool);
		}
	}

	private void copyBundle(JarNode jarNode) {
		log.info("{} is already a bundle -> copied", jarNode.getName());
		File targetFile = new File(outDir, jarNode.getFile().getName());
		try {
			Files.copy(jarNode.getFile(), targetFile);
		} catch (Exception e) {
			log.error("Failed to copy bundle " + jarNode.getName(), e);
		}
	}

	private void convertJar(JarNode jarNode, JarPool jarPool) {
		File manifest = createManifest(jarNode, jarPool);
		try {
			runBnd(jarNode, jarNode.getFile(), manifest);
		} catch (Exception e) {
			log.error("Faile to create jar " + jarNode, e);
		}
	}

	private void convertUberJar(UberJar uberJar, JarPool jarPool) {
		try {
			List<JarNode> jars = uberJar.getJarNodes();
			String uberName = "uber-" + uberJar.getName();
			File workDir = new File(resourceDir, uberName.replace(".jar", ""));
			workDir.mkdirs();
			JarMerge merge = new JarMerge(jars, workDir);
			File jarFile = merge.run(uberName);
			File manifest = createManifest(uberJar, jarPool);
			runBnd(uberJar, jarFile, manifest);
		} catch (Exception e) {
			log.trace("failed to build uber-jar", e);
		}
	}

	private File createManifest(IJarInfo jarInfo, JarPool jarPool) {
		String manifestName = jarInfo.getName().replaceAll(".jar", ".mf");
		File manifest = new File(resourceDir, manifestName);
		try (FileWriter writer = new FileWriter(manifest)) {
			new ManifestWriter(jarInfo, jarPool).write(writer);
		} catch (Exception e) {
			log.error("Failed to write manifest " + manifest, e);
		}
		return manifest;
	}

	private void runBnd(IJarInfo jarInfo, File srcJar, File manifest)
			throws Exception {
		BundleInfo info = BundleInfo.forJarName(jarInfo.getName());
		String outName = info.getSmbolicName() + "-" + info.getVersion()
				+ "-gen-bundle.jar";
		log.info("Run bnd tool for {}", outName);
		File outFile = new File(outDir, outName);
		File bndFile = new File(resourceDir, BND_TOOL);
		List<String> args = Arrays.asList("java", "-jar",
				bndFile.getAbsolutePath(), "wrap", "-o",
				outFile.getAbsolutePath(), "-p", manifest.getAbsolutePath(),
				srcJar.getAbsolutePath());
		ProcessBuilder builder = new ProcessBuilder(args);
		builder.start();
	}

	public static void main(String[] args) {
		File libDir = new File("C:\\Users\\Dell\\Downloads\\build_temp\\lib");
		File outDir = new File("C:\\Users\\Dell\\Downloads\\build_temp\\out");
		Bundler bundler = new Bundler(libDir, outDir);
		bundler.run();
	}
}
