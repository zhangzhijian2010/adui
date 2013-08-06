/** 
 * Copyright (C), 2010-2012, TIETIAO Software Co.,Ltd. ALL RIGHTS RESERVED. 
 * File name:IgnoreNullPropertyFilter.java
 * Version:1.0
 * Date:2011-6-16
 * Description:JSON时间处理器
 * author:zhijian.zhang
 */
package com.kuyun.common.json;

import java.text.SimpleDateFormat;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class TimestampToJsonProcessor implements JsonValueProcessor {
	private SimpleDateFormat sf;

	public TimestampToJsonProcessor(SimpleDateFormat sf) {
		this.sf = sf;
	}

	public Object processArrayValue(Object value, JsonConfig jsonConfig) {
		return null;
	}

	public Object processObjectValue(String key, Object value,
			JsonConfig jsonConfig) {
		return sf.format(value);
	}

}
