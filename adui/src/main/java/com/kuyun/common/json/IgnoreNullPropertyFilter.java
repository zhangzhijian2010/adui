/** 
 * Copyright (C), 2012-2015, kuyun Software Co.,Ltd. ALL RIGHTS RESERVED. 
 * File name:IgnoreNullPropertyFilter.java
 * Version:1.0
 * Date:2011-6-16
 * Description: JSON空属性过滤器
 * author:zhijian.zhang
 */
package com.kuyun.common.json;

import net.sf.json.util.PropertyFilter;

public class IgnoreNullPropertyFilter implements PropertyFilter {
    public boolean apply(Object object, String name, Object value) {
        if (value == null)
            return true;
        else
            return false;
    }
}
