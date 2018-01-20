package com.yuchengtech.crm.version;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.yuchengtech.crm.version.VersionInformation.Version;

/**
 * 版本控制的自定义标签父类
 * @version 2.1
 * @author km
 * @since 2014-2-25
 */
public class VersionControlTag extends SimpleTagSupport {

	public final static String JAVASCRIPT = "text/javascript";

	public final static String CSS = "text/css";

	protected String type = "";
	
	protected String src = "";

	protected String rel = "";

	protected String href = "";

	/**
	 * 代码版本的层级,子类需要在doTag()方法中赋值
	 */
	protected Version version = Version.FRAME;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	@Override
	public void doTag() throws IOException {
		if (JAVASCRIPT.equals(type)) {
			javascriptVersion();
		} else if (CSS.equals(type)) {
			cssVersion();
		}
	}

	/**
	 * 给javascript加上版本控制
	 * 
	 * @throws IOException
	 */
	protected void javascriptVersion() throws IOException {
		PageContext ctx = (PageContext) getJspContext();
		JspWriter out = ctx.getOut();
		out.println("<script type=\"" + type + "\" src=\""
				+ ctx.getServletContext().getContextPath() + src + "?ver="
				+ VersionInformation.getInstance().getVersionInfo(version) + "\"/></script>");
	}

	/**
	 * 给CSS加上版本控制
	 * 
	 * @throws IOException
	 */
	protected void cssVersion() throws IOException {
		PageContext ctx = (PageContext) getJspContext();
		JspWriter out = ctx.getOut();
		out.println("<link type=\"" + type + "\" rel=\"" + rel + "\" href=\""
				+ ctx.getServletContext().getContextPath() + href + "?ver="
				+ VersionInformation.getInstance().getVersionInfo(version) + "\"/>");
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getRel() {
		return rel;
	}

	public void setRel(String rel) {
		this.rel = rel;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

}
