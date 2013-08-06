/** 
 * Copyright (C), 2012-2015, kuyun Software Co.,Ltd. ALL RIGHTS RESERVED. 
 * File name:BaseAction.java
 * Version:1.0
 * Date:2011-12-4
 * Description: 公共ACTION类
 * author:zhijian.zhang
 */
package com.kuyun.common.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.kuyun.common.util.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class BaseAction extends ActionSupport {
	/**
	 * 日志
	 */
	public Logger log = Logger.getLogger(this.getClass());
	/**
	 * request session
	 */
	public HttpServletRequest request = ServletActionContext.getRequest();
	public HttpServletResponse response = ServletActionContext.getResponse();
	public String webRoot = request.getContextPath();
	
	/**
	 * 系统跟路径
	 */
	public String rootPath = ServletActionContext.getServletContext().getRealPath("/");
	// =============================零配置跳转===============================
	/**
	 * 默认跳转URL
	 */
	public final static String DEFAULT = "default";

	private String viewpath;

	// 开始参数
	private Integer start;
	// 记录数
	private Integer limit;

	private String sort;

	private String dir;

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	/**
	 * 功能：Redirect方式跳转URL
	 * 
	 * @param vpath
	 * @return
	 */
	public String redirect(String vpath) {
		viewpath = vpath;
		return "redirect";
	}

	/**
	 * 
	 * 功能：Dispatcher跳转
	 * 
	 * @author: zhijian.zhang
	 * @Date: 2011-6-15
	 * 
	 * @param vpath
	 *            跳转路径
	 * @return
	 */
	public String dispatcher(String vpath) {
		viewpath = vpath;
		return "dispatcher";
	}

	public String getViewpath() {
		return viewpath;
	}

	/**
	 * 获取排序参数
	 * 
	 * <pre>
	 * 
	 * </pre>
	 * 
	 * @author zhangzhijian
	 * @date 2012-9-19 下午03:01:25
	 * 
	 * @return
	 */
	public String getOrderType() {
		String orderType = null;
		if (StringUtil.isNotNull(getSort()) && StringUtil.isNotNull(getDir())) {
			orderType = getSort() + " " + getDir();
		}
		return orderType;
	}
}
