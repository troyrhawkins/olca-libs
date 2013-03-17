package org.openlca.bundler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class BundleInfo {

	private String smbolicName;
	private String version;

	/**
	 * Extracts the name and version from a Jar name. The name must follow the
	 * Maven naming convention in order to extract the information correctly,
	 * see http://maven.apache.org/guides/mini/guide-naming-conventions.html.
	 */
	public static BundleInfo forJarName(String jarName) {
		Logger log = LoggerFactory.getLogger(BundleInfo.class);
		log.trace("Extract bundle info from name: {}", jarName);
		Pattern suffixPattern = Pattern
				.compile("-[0-9]+\\.[0-9]+(\\.[0-9]+)?.*\\.jar");
		Matcher matcher = suffixPattern.matcher(jarName);
		BundleInfo info = new BundleInfo();
		if (!matcher.find()) {
			info.smbolicName = jarName.replace(".jar", "");
			info.version = "0.0.0";
			log.trace("Version pattern not found, return {}", info);
			return info;
		}
		info.smbolicName = jarName.substring(0, matcher.start());
		String versionPart = jarName.substring(matcher.start(), matcher.end());
		extractVersion(info, versionPart);
		log.trace("return {}", info);
		return info;
	}

	private static void extractVersion(BundleInfo info, String versionPart) {
		Pattern versionPattern = Pattern.compile("[0-9]+\\.[0-9]+(\\.[0-9]+)?");
		Matcher matcher = versionPattern.matcher(versionPart);
		if (matcher.find())
			info.version = versionPart
					.substring(matcher.start(), matcher.end());
		else
			info.version = "0.0.0";
	}

	public String getSmbolicName() {
		return smbolicName;
	}

	public void setSmbolicName(String smbolicName) {
		this.smbolicName = smbolicName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "BundleInfo [smbolicName=" + smbolicName + ", version="
				+ version + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((smbolicName == null) ? 0 : smbolicName.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BundleInfo other = (BundleInfo) obj;
		if (smbolicName == null) {
			if (other.smbolicName != null)
				return false;
		} else if (!smbolicName.equals(other.smbolicName))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}

}
