/** 
 *Copyright (C), 2012-2015, kuyun Software Co.,Ltd. ALL RIGHTS RESERVED. 
 * File name:IgnoreNullPropertyFilter.java
 * Version:1.0
 * Date:2011-6-16
 * Description: 系统中所用到的JSON工具类
 * author:zhijian.zhang
 */
package com.kuyun.common.json;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONBuilder;
import net.sf.json.xml.XMLSerializer;

import com.kuyun.common.util.PageModel;

public class JsonControllerHelper {
	/**
	 * 加入了空属性去除过滤器和时间格式化处理器的配置对象。
	 */
	public static final JsonConfig COMMON_JSON_CONFIG = new JsonConfig();

	static {
		// 加入空属性去除过滤器。
		COMMON_JSON_CONFIG
				.setJsonPropertyFilter(new IgnoreNullPropertyFilter());

		// 加入时间格式化处理器。
		COMMON_JSON_CONFIG.registerJsonValueProcessor(Date.class,
				new TimestampToJsonProcessor(new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss")));

	}

	public static void main(String[] args) {
		List<Map<String, Integer>> z = new ArrayList<Map<String, Integer>>();
		Map<String, Integer> x = new HashMap<String, Integer>();
		x.put("ss", 1);
		x.put("dd", 2);
		z.add(x);
		Map<String, Integer> y = new HashMap<String, Integer>();
		y.put("zz", 3);
		y.put("ee", 4);
		z.add(y);
		JSONArray dataArray = JSONArray.fromObject(z, COMMON_JSON_CONFIG);
		System.out.println(dataArray.toString());
	}

	/**
	 * 功能：简化参数的产生Grid的JSON数据操作，默认的sumPropertyName为“sum”，rootPropertyName为“root”。
	 * 
	 * @author: zhijian.zhang
	 * @Date: 2011-6-16
	 * 
	 * @param response
	 *            HTTP response对象
	 * @param sum
	 *            记录总数
	 * @param data
	 *            数据对象，只接受数组对象，比如List。
	 * @throws Exception
	 */
	public static void writeGridJsonToResponse(HttpServletResponse response,
			long sum, Object data) throws Exception {
		response.setContentType("text/html");
		writeGridJsonToResponse(response, "sum", sum, "root", data,
				COMMON_JSON_CONFIG);
	}

	/**
	 * 功能：简化参数的产生Grid的JSON数据操作，默认的rootPropertyName为“root”。
	 * 
	 * @author: zhijian.zhang
	 * @Date: 2011-6-16
	 * 
	 * @param response
	 *            HTTP response对象
	 * @param data
	 *            数据对象，只接受数组对象，比如List。
	 * @param config
	 *            处理器的配置对象
	 * @throws Exception
	 */
	public static void writeGridJsonTorespons(HttpServletResponse response,
			Object data, JsonConfig config) throws Exception {
		response.setContentType("text/html");
		writeGridJsonToResponse(response, "sum", 0l, "root", data, config);
	}

	/**
	 * 功能： 产生Grid的JSON数据，并将其写入response对象中。
	 * 
	 * @author: zhijian.zhang
	 * @Date: 2011-6-16
	 * 
	 * @param response
	 *            HTTP response对象
	 * @param sumPropertyName
	 *            JSON数据中标识记录总数的属性名称
	 * @param sum
	 *            记录总数
	 * @param rootPropertyName
	 *            JSON数据中标识记录数据根节点的属性名称
	 * @param data
	 *            数据对象，只接受数组对象，比如List。
	 * @throws Exception
	 */
	public static void writeGridJsonToResponse(HttpServletResponse response,
			String sumPropertyName, long sum, String rootPropertyName,
			Object data, JsonConfig config) throws Exception {
		response.setContentType("text/html");
		JSONArray dataArray = JSONArray.fromObject(data, config);
		new JSONBuilder(response.getWriter()).object().key(sumPropertyName)
				.value(sum).key(rootPropertyName).value(dataArray).endObject();
	}

	/**
	 * 功能： 将领域对象或领域对象集转换成JSON，并写入response中。
	 * 
	 * @author: zhijian.zhang
	 * @Date: 2011-6-16
	 * 
	 * @param response
	 *            HTTP response对象
	 * @param data
	 *            领域对象或领域对象集
	 * @throws Exception
	 */
	public static void writeJsonToResponse(HttpServletResponse response,
			Object data) throws Exception {
		response.setContentType("text/html");
		String json = null;
		if (data.getClass().isArray() || data instanceof Collection<?>)
			json = JSONArray.fromObject(data, COMMON_JSON_CONFIG).toString();
		else
			json = JSONObject.fromObject(data, COMMON_JSON_CONFIG).toString();

		response.getWriter().append(json);
	}

	/**
	 * 功能： 将领域对象或领域对象集转换成JSON，并写入response中。
	 * 
	 * @author: zhijian.zhang
	 * @Date: 2011-6-16
	 * 
	 * @param response
	 *            HTTP response对象
	 * @param data
	 *            领域对象或领域对象集
	 * @param config
	 *            处理器的配置对象
	 * @throws Exception
	 */
	public static void writeJsonToResponse(HttpServletResponse response,
			Object data, JsonConfig config) throws Exception {
		response.setContentType("text/html");
		String json = null;

		if (data.getClass().isArray() || data instanceof Collection<?>)
			json = JSONArray.fromObject(data, config).toString();
		else
			json = JSONObject.fromObject(data, config).toString();

		response.getWriter().append(json);
	}

	/**
	 * 功能： 将一个对象集转换成可以支持EXT Tree显示的JSON串，并写入response中。
	 * 
	 * @author: zhijian.zhang
	 * @Date: 2011-6-16
	 * 
	 * @param response
	 *            HTTP response对象
	 * @param nodes
	 *            要转换的对象集
	 * @param idFieldName
	 *            在领域对象中存储了node的id属性的域名
	 * @param textFieldName
	 *            在领域对象中存储了node的text属性的域名
	 * @param leafFieldName
	 *            在领域对象中存储了node的leaf属性的域名
	 * @param isLeaf
	 *            可选，指定节点的类型是否是叶子节点。
	 * @throws Exception
	 */
	public static void writeTreeJsonToResponse(HttpServletResponse response,
			List<?> nodes, String idFieldName, String textFieldName,
			String leafFieldName, Boolean isLeaf) throws Exception {
		response.setContentType("text/html");
		JSONArray nodesJson = JSONArray.fromObject(nodes, COMMON_JSON_CONFIG);

		@SuppressWarnings("unchecked")
		ListIterator<JSONObject> i = nodesJson.listIterator();

		while (i.hasNext()) {
			JSONObject nodeJson = i.next();

			if (idFieldName != null)
				nodeJson.put("id", nodeJson.get(idFieldName));

			nodeJson.put("text", nodeJson.get(textFieldName));

			if (isLeaf != null)
				nodeJson.put("leaf", isLeaf);
			else if (leafFieldName != null)
				nodeJson.put("leaf", nodeJson.get(leafFieldName));
		}
		/** start modify by wzh 2011-8-11 **/
		String str = nodesJson.toString();
		str = nodesJson.toString().replace("\"checked\":false,", "");
		response.getWriter().append(str);
	}

	/**
	 * 
	 * 功能：多个list集合的对象
	 * 
	 * @author: Sophie
	 * @Date: 2011-7-21
	 * 
	 * @param response
	 * @param map
	 * @param check
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static void writeTreeJsonToMapCheckResponse(
			HttpServletResponse response, Map<Integer, Object> map)
			throws Exception {
		response.setContentType("text/html");
		List lst;
		List temp;
		String idFieldName;
		String textFieldName;
		String leafFieldName = null;
		JSONArray nodesJson = null;
		String str = null;
		if (map != null && map.size() > 0) {
			for (int i = 0; i < map.size(); i++) {
				lst = (List) map.get(i + 1);
				if (lst != null && lst.size() > 0) {
					temp = (List) lst.get(0);
					if (temp != null && temp.size() > 0) {
						idFieldName = (String) lst.get(1);
						textFieldName = (String) lst.get(2);
						if (lst.size() == 4) {
							leafFieldName = (String) lst.get(3);
						}
						nodesJson = JSONArray.fromObject(temp,
								COMMON_JSON_CONFIG);

						@SuppressWarnings("unchecked")
						ListIterator<JSONObject> node = nodesJson
								.listIterator();

						while (node.hasNext()) {
							JSONObject nodeJson = node.next();

							if (idFieldName != null) {
								nodeJson.put("id", nodeJson.get(idFieldName));
							}
							nodeJson.put("text", nodeJson.get(textFieldName));

							if (leafFieldName != null) {
								nodeJson.put("leaf",
										nodeJson.get(leafFieldName));
							} else {
								nodeJson.put("leaf", 1);
							}
						}
						if (str != null) {
							str = str.substring(0, str.length() - 1) + ","
									+ nodesJson.toString().substring(1);
						} else {
							str = nodesJson.toString();
						}

					}
				}
			}
		}

		response.getWriter().append(str);
	}

	/**
	 * 功能： 将一个对象集转换成可以支持EXT Tree显示的JSON串，并写入response中。
	 * 
	 * @author: zhijian.zhang
	 * @Date: 2011-6-16
	 * 
	 * @param response
	 *            HTTP response对象
	 * @param nodes
	 *            要转换的对象集
	 * @param idFieldName
	 *            在领域对象中存储了node的id属性的域名
	 * @param textFieldName
	 *            在领域对象中存储了node的text属性的域名
	 * @param leafFieldName
	 *            在领域对象中存储了node的leaf属性的域名
	 * @param isLeaf
	 *            可选，指定节点的类型是否是叶子节点。
	 * @throws Exception
	 */
	public static void writeTreeWithNumJsonToResponse(
			HttpServletResponse response, List<?> nodes, String idFieldName,
			String textFieldName, String numberFieldName, String leafFieldName,
			Boolean isLeaf) throws Exception {
		response.setContentType("text/html");
		JSONArray nodesJson = JSONArray.fromObject(nodes, COMMON_JSON_CONFIG);

		@SuppressWarnings("unchecked")
		ListIterator<JSONObject> i = nodesJson.listIterator();

		while (i.hasNext()) {
			JSONObject nodeJson = i.next();

			if (idFieldName != null)
				nodeJson.put("id", nodeJson.get(idFieldName));

			nodeJson.put(
					"text",
					nodeJson.get(textFieldName) + "("
							+ nodeJson.get(numberFieldName) + ")");

			if (isLeaf != null)
				nodeJson.put("leaf", isLeaf);
			else if (leafFieldName != null)
				nodeJson.put("leaf", nodeJson.get(leafFieldName));
		}

		response.getWriter().append(nodesJson.toString());
	}

	/**
	 * 功能：简化参数的产生Grid的JSON数据操作，默认的sumPropertyName为“sum”，rootPropertyName为“root”。
	 * 
	 * @author: zhijian.zhang
	 * @Date: 2011-6-16
	 * 
	 * @param response
	 *            HTTP response对象
	 * @param sum
	 *            记录总数
	 * @param data
	 *            数据对象，只接受数组对象，比如List。
	 * @param config
	 *            处理器的配置对象
	 * @throws Exception
	 */
	public static void writeGridJsonToResponseWithCofig(
			HttpServletResponse response, long sum, Object data,
			JsonConfig config) throws Exception {
		response.setContentType("text/html");
		writeGridJsonToResponse(response, "sum", sum, "root", data, config);
	}

	/**
	 * 功能：将Json格式数据转换成xml格式数据
	 * 
	 * @author: zyxun
	 * @Date: 2011-9-1
	 * 
	 * @param json
	 *            需要转换成XML的JSON数据
	 * @return 转换后的xml字符串
	 */
	public static String jsonToXml(JSONObject json) {
		String xml = new XMLSerializer().write(json);
		return xml;
	}

	/**
	 * 
	 * 功能：将分页对象转成Json数据发送至Response中
	 * 
	 * @author: zhijian.zhang
	 * @Date: 2012-1-2
	 * 
	 * @param response
	 * @param pageModel
	 */
	public static void writePageModelToResponse(HttpServletResponse response,
			@SuppressWarnings("rawtypes") PageModel pageModel) throws Exception {
		writeGridJsonToResponse(response, "sum", pageModel.getTotalCount(),
				"root", pageModel.getDataList(), COMMON_JSON_CONFIG);
	}

}
