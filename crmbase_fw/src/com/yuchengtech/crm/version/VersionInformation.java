package com.yuchengtech.crm.version;

import org.apache.log4j.Logger;

/**
 * 获取工程版本信息
 * 
 * @version 2.1
 * @author km
 * @since 2014-2-24
 * 
 */
public class VersionInformation {

	private static VersionInformation instance;

	private String frontVersion = "1.0.201402261745";

	/*** 主版本号 */
	private String major;

	/*** 子版本号 */
	private String minor;

	/*** 补丁版本号 */
	private String patch;

	public final static String OUT_INFO = "读取前台代码版本:";

	public final static String ERROR_INFO = "读取前台代码版本异常!版本号:";

	private static Logger log = Logger.getLogger(VersionInformation.class);

	public enum Version {
		FRAME, SUB
	}

	public static VersionInformation getInstance() {
		if (null == instance) {
			instance = new VersionInformation();
		}
		return instance;
	}

	public void initialize() {
		try {
			major = frontVersion.split("\\.")[0];
			minor = frontVersion.split("\\.")[1];
			patch = frontVersion.split("\\.")[2];
			log.info(OUT_INFO + frontVersion);
		} catch (Exception e) {
			log.error(ERROR_INFO + frontVersion);
			e.printStackTrace();
		}
	}

	public String getVersionInfo(Version version) {
		if (version.equals(Version.FRAME)) {
			return major + "." + minor;
		} else {
			return major + "." + minor + "." + patch;
		}
	}

	public String getFrontVersion() {
		return frontVersion;
	}

	public void setFrontVersion(String frontVersion) {
		this.frontVersion = frontVersion;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getMinor() {
		return minor;
	}

	public void setMinor(String minor) {
		this.minor = minor;
	}

	public String getPatch() {
		return patch;
	}

	public void setPatch(String patch) {
		this.patch = patch;
	}
}
