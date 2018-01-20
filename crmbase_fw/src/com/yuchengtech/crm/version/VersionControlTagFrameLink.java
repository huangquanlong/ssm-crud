package com.yuchengtech.crm.version;

import java.io.IOException;

import com.yuchengtech.crm.version.VersionInformation.Version;

/**
 * 框架版本控制的自定义标签类 用于获取版本信息,并拼接到jsp中引用的css文件的版本信息中
 * @version 2.0
 * @author km
 * @since 2014-2-18,2014-2-24
 * 
 */
public class VersionControlTagFrameLink extends VersionControlTag {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	@Override
	public void doTag() throws IOException {
		version = Version.FRAME;
		if (CSS.equals(type)) {
			cssVersion();
		}
	}

}
