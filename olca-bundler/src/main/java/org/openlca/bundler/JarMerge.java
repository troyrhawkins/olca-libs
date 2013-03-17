package org.openlca.bundler;

import java.io.File;
import java.util.List;
import java.util.UUID;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Merge several jars into a single one. */
class JarMerge {

	private Logger log = LoggerFactory.getLogger(getClass());
	private File workDir;
	private List<JarNode> jarNodes;

	public JarMerge(List<JarNode> jarNodes, File workDir) {
		this.jarNodes = jarNodes;
		this.workDir = workDir;
	}

	public File run(String fileName) {
		File targetFile = new File(workDir, fileName);
		log.trace("create uber-jar {}", targetFile);
		File tmpDir = new File(workDir, UUID.randomUUID().toString());
		tmpDir.mkdirs();
		File[] files = unzip(tmpDir);
		zip(files, targetFile);
		// TODO: tmpDir.delete()
		return targetFile;
	}

	private void zip(File[] files, File targetFile) {
		try {
			ZipFile target = new ZipFile(targetFile);
			ZipParameters parameters = new ZipParameters();
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
			for (File file : files) {
				if (file.isDirectory())
					target.addFolder(file, parameters);
				else
					target.addFile(file, parameters);
			}
		} catch (Exception e) {
			log.error("Failed to zip files", e);
		}
	}

	private File[] unzip(File tmpDir) {
		try {
			for (JarNode node : jarNodes) {
				log.trace("add jar {}", node);
				ZipFile zipFile = new ZipFile(node.getFile());
				zipFile.extractAll(tmpDir.getAbsolutePath());
			}
			return tmpDir.listFiles();
		} catch (Exception e) {
			log.error("Failed to create zip", e);
			return new File[0];
		}
	}

}
