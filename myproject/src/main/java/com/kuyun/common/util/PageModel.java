/** 
 * Copyright (C), 2010-2012, TIETIAO Software Co.,Ltd. ALL RIGHTS RESERVED. 
 * File name:PageModel.java
 * Version:1.0
 * Date:2011-11-18
 * Description: 分页对象
 * author:zhijian.zhang
 */
package com.kuyun.common.util;

import java.util.List;

public class PageModel<T extends Object> {

	// 存储数据记录
	private List<T> dataList;

	// 当前页号
	private int pageNo;

	// 从第几条开始。
	private int startIndex;

	// 页面大小
	private int pageSize;

	// 总记录数
	private int totalCount;

	// 总页数
	private int totalPage;

	// 前一页
	private int prevPage;

	// 后一页
	private int nextPage;

	// 最后一页
	private int lastPage;

	// 首页
	private int firstPage;

	/**
	 * 构造分页对象
	 * 
	 * @param datas
	 *            列表数据
	 * @param currentpage
	 *            当前页
	 * @param pageSize
	 *            每页记录数
	 * @param totalCount
	 *            总记录数
	 */
	public PageModel(List<T> datas, int currentpage, int pageSize,
			int totalCount) {
		this.dataList = datas;
		this.totalCount = totalCount;
		this.pageSize = pageSize;
		this.pageNo = currentpage;
	}

	/**
	 * 后台构造分页对象函数
	 * 
	 * @param datas
	 *            列表数据
	 * @param totalCount
	 *            总记录数
	 */
	public PageModel(List<T> datas, int totalCount) {
		this.dataList = datas;
		this.totalCount = totalCount;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public List<T> getDataList() {
		return dataList;
	}

	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}

	public int getPrevPage() {
		prevPage = pageNo - 1;
		if (prevPage < 1) {
			prevPage = 1;
		}
		return prevPage;
	}

	public int getNextPage() {
		nextPage = pageNo + 1;
		if (nextPage > getTotalPage()) {
			nextPage = getTotalPage();
		}
		return nextPage;
	}

	public int getLastPage() {
		lastPage = getTotalPage();
		return lastPage;
	}

	public int getFirstPage() {
		firstPage = 1;
		return firstPage;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getTotalPage() {
		if (pageSize == 0) {
			return 0;
		}
		totalPage = (totalCount + pageSize - 1) / pageSize;
		return totalPage;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
}
