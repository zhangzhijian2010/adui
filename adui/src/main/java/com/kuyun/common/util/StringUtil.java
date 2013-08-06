/**
 * tenfen.com Inc.
 * Copyright (c) 2012-2015 All Rights Reserved.
 */
package com.kuyun.common.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 字符串工具类
 * 
 * <pre>
 * 
 * </pre>
 * 
 * @author zzj
 * @version $Id: StringUtil.java, v 0.1 2012-10-30 下午05:15:54 zhangzhijian Exp $
 */
public class StringUtil {
    /**
     * 判断给定的字符串不为空包括空串
     * 
     * <pre>
     * 
     * </pre>
     * 
     * @author zzj
     * @date 2012-10-30 下午05:16:52
     * 
     * @param str
     * @return
     */
    public static boolean isNotNull(String str) {
        if (str == null || "".equals(str.trim())) {
            return false;
        }
        return true;
    }

    /**
     * 判断字符为空或空串
     * 
     * <pre>
     * 
     * </pre>
     * 
     * @author zzj
     * @date 2012-11-1 上午11:15:42
     * 
     * @param str
     * @return
     */
    public static boolean isNull(String str) {
        return !isNotNull(str);
    }

    /**
     * 
     * 返回hash记录
     * 
     * <pre>
     * 
     * </pre>
     * 
     * @author zzj
     * @date 2012-11-2 上午10:52:46
     * 
     * @param str
     * @return
     */
    public static long hashcode(String str) {
        long h = 0;
        int len = str.length();
        if (h == 0 && len > 0) {
            int off = 0;
            char val[] = str.toCharArray();
            for (int i = 0; i < len; i++) {
                h = 31 * h + val[off++];
            }
        }
        return h;
    }

    /**
     * 去掉html标签
     * 
     * <pre>
     * 
     * </pre>
     * 
     * @author zhangzhijian
     * @date 2012-8-31 下午03:26:00
     * 
     * @param html
     * @return
     */
    public static String replaceHtml(String html) {
        if (isNull(html)) {
            return "";
        }
        html = html.replace("<br>", "。");
        String regEx = "<.+?>"; // 表示标签
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(html);
        String s = m.replaceAll("");
        return s;
    }

    /**
     * 清除所有特殊字符
     * 
     * <pre>
     * 
     * </pre>
     * 
     * @author zhangzhijian
     * @date 2012-9-7 下午03:53:02
     * 
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static String replaceSpecialStr(String str) throws PatternSyntaxException {
        if (isNull(str)) {
            return "";
        }
        // 清除掉所有特殊字符
        String regEx = "[！@#￥%……&*（）——+|{}【】]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        String result = m.replaceAll("").trim();
        p = Pattern.compile(" {1,}");// 去除多余空格
        m = p.matcher(result);
        String second = m.replaceAll("");
        return second;
    }

    /**
     * 判断给定的字符串是否为数字
     * 
     * <pre>
     * 
     * </pre>
     * 
     * @author zzj
     * @date 2012-11-28 下午01:59:52
     * 
     * @param str
     * @return
     */
    public static boolean isNumeric(CharSequence str) {
        Pattern pattern = Pattern.compile("^[0-9]+$");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断是否是汉字
     * 
     * <pre>
     * 
     * </pre>
     * 
     * @author zhangzhijian
     * @date 2013-3-22 上午10:59:21
     * 
     * @param chars
     * @return
     */
    public static boolean isChineseChar(CharSequence chars) {
        Pattern pattern = Pattern.compile("^[\\u4E00-\\u9FA5]+$");
        return pattern.matcher(chars).matches();
    }

    /**
     * 是否英文
     * 
     * <pre>
     * 
     * </pre>
     * 
     * @author zhangzhijian
     * @date 2013-3-22 上午11:01:08
     * 
     * @param chars
     * @return
     */
    public static boolean isEnglish(CharSequence chars) {
        Pattern pattern = Pattern.compile("^[a-zA-Z]+$");
        return pattern.matcher(chars).matches();
    }

    // 全角转半角的 转换函数
    public static final String full2HalfChange(String QJstr) {
        StringBuffer outStrBuf = new StringBuffer("");
        String Tstr = "";
        byte[] b = null;
        for (int i = 0; i < QJstr.length(); i++) {
            Tstr = QJstr.substring(i, i + 1);
            // 全角空格转换成半角空格
            if (Tstr.equals("　")) {
                outStrBuf.append(" ");
                continue;
            }
            try {
                b = Tstr.getBytes("unicode");
                // 得到 unicode 字节数据
                if (b[2] == -1) {
                    // 表示全角？
                    b[3] = (byte) (b[3] + 32);
                    b[2] = 0;
                    outStrBuf.append(new String(b, "unicode"));
                } else {
                    outStrBuf.append(Tstr);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } // end for.
        return outStrBuf.toString();
    }

    // 半角转全角
    public static final String half2Fullchange(String QJstr) throws UnsupportedEncodingException {
        StringBuffer outStrBuf = new StringBuffer("");
        String Tstr = "";
        byte[] b = null;
        for (int i = 0; i < QJstr.length(); i++) {
            Tstr = QJstr.substring(i, i + 1);
            if (Tstr.equals(" ")) {
                // 半角空格
                outStrBuf.append(Tstr);
                continue;
            }
            b = Tstr.getBytes("unicode");
            if (b[2] == 0) {
                // 半角?
                b[3] = (byte) (b[3] - 32);
                b[2] = -1;
                outStrBuf.append(new String(b, "unicode"));
            } else {
                outStrBuf.append(Tstr);
            }
        }
        return outStrBuf.toString();
    }

    /**
     * 封装IN的SQL条件语句
     * 
     * <pre>
     * 
     * </pre>
     * 
     * @author zhangzhijian
     * @date 2013-1-10 下午03:24:12
     * 
     * @param inLists
     * @param quotationFlag
     *            是否加入单引号 如：'1','2','3'
     * @return
     */
    public static String wrap2InSqlStat(Collection<String> colls, boolean quotationFlag) {
        StringBuilder inSb = new StringBuilder();
        int size = colls.size();
        int count = 0;
        for (String str : colls) {
            ++count;
            if (quotationFlag) {
                inSb.append("'").append(str).append("'");
            } else {
                inSb.append(str);
            }
            if (count < size) {
                inSb.append(",");
            }
        }
        return inSb.toString();
    }

    /**
     * 替换content内的超链接中的文字
     * 
     * <pre>
     * 
     * </pre>
     * 
     * @author zhangzhijian
     * @date 2013-2-20 下午12:01:39
     * 
     * @param content
     * @param replaceStr
     * @return
     */
    public static String replaceAHrefContent(String content, String replaceStr) {
        String regEx = "<a[^<]*>[^a]*</a>";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(content);
        return m.replaceAll(replaceStr);
    }

    /**
     * 判断给定str在给定的内容中出现的次数
     * 
     * <pre>
     * 
     * </pre>
     * 
     * @author zhangzhijian
     * @date 2013-2-21 下午6:00:02
     * 
     * @param content
     * @param str
     * @return
     */
    public static int countStrAppearTimes(String content, String str) {
        int indexOf = content.indexOf(str);
        int count = 0;
        while (indexOf >= 0) {
            ++count;
            indexOf = content.indexOf(str, indexOf + str.length());
        }
        return count;
    }

    /**
     * 判断给出的字符串包含给定的正则匹配的字符
     * 
     * <pre>
     * 
     * </pre>
     * 
     * @author zhangzhijian
     * @date 2013-3-22 下午4:08:22
     * 
     * @return
     */
    public static boolean containsRegexStr(String str, String regexStr) {
        Pattern p = Pattern.compile(regexStr);
        return p.matcher(str).find();
    }

    public static Set<String> matcherRegexStr(String str, String regex) {
        if (StringUtil.isNotNull(str)) {
            Set<String> matchSet = new HashSet<String>();
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(str);
            while (m.find()) {
                String matchStr = m.group();
                if (StringUtil.isNotNull(matchStr)) {
                    matchStr = matchStr.replaceAll("[<>《》]", "");
                    if (StringUtil.isNotNull(matchStr)) {
                        matchSet.add(matchStr);
                    }
                }
            }
            return matchSet;
        }
        return null;
    }

    /**
     * MD5
     * 
     * <pre>
     * 
     * </pre>
     * 
     * @author zhangzhijian
     * @date 2013-2-5 下午4:25:41
     * 
     * @param string
     * @return
     */
    public static String md5(String string) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
                            'e', 'f' };
        try {
            byte[] bytes = string.getBytes();
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(bytes);
            byte[] updateBytes = messageDigest.digest();
            int len = updateBytes.length;
            char myChar[] = new char[len * 2];
            int k = 0;
            for (int i = 0; i < len; i++) {
                byte byte0 = updateBytes[i];
                myChar[k++] = hexDigits[byte0 >>> 4 & 0x0f];
                myChar[k++] = hexDigits[byte0 & 0x0f];
            }
            return new String(myChar);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 统计给定字符串匹配正则字符的个数
     * 
     * <pre>
     * 
     * </pre>
     * 
     * @author zhangzhijian
     * @date 2013-4-27 上午10:31:12
     * 
     * @param str
     * @param reg
     * @return
     */
    public static int countMatchRegNum(String str, String reg) {
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(str);
        int reuslt = 0;
        while (m.find()) {
            ++reuslt;
        }
        return reuslt;
    }

    /**
     * 过滤乱码....只保留中文、英文、数字、一些特殊符号...
     * 
     * <pre>
     * 
     * </pre>
     * 
     * @author zhangzhijian
     * @date 2013-5-7 下午1:00:56
     * 
     * @param str
     * @return
     */
    public static String filterMessyCode(String str) {
        if (str == null || "".equals(str.trim())) {
            return str;
        }
        Pattern p = Pattern.compile("[\\u4E00-\\u9FA5\\p{Punct}0-9a-zA-Z【】《》”“：？—！、·℃％－（）〖〗Ⅰ]");
        Matcher m = p.matcher(str);
        StringBuilder result = new StringBuilder();
        while (m.find()) {
            result.append(m.group());
        }
        if (result.length() == 0) {
            return str;
        }
        return result.toString();
    }

    /**
     * 获取两个串的最长公共子串列表
     * 
     * <pre>
     * 
     * </pre>
     * 
     * @author zhangzhijian
     * @date 2013-6-5 下午1:14:20
     * 
     * @param one
     * @param two
     * @return
     */
    public static List<String> getLCString(String one, String two) {
        char[] str1 = one.toCharArray();
        char[] str2 = two.toCharArray();
        int len1, len2;
        len1 = str1.length;
        len2 = str2.length;
        int maxLen = len1 > len2 ? len1 : len2;
        int[] max = new int[maxLen];// 保存最长子串长度的数组
        int[] maxIndex = new int[maxLen];// 保存最长子串长度最大索引的数组
        int[] c = new int[maxLen];
        int i, j;
        for (i = 0; i < len2; i++) {
            for (j = len1 - 1; j >= 0; j--) {
                if (str2[i] == str1[j]) {
                    if ((i == 0) || (j == 0))
                        c[j] = 1;
                    else
                        c[j] = c[j - 1] + 1;// 此时C[j-1]还是上次循环中的值，因为还没被重新赋值
                } else {
                    c[j] = 0;
                }

                // 如果是大于那暂时只有一个是最长的,而且要把后面的清0;
                if (c[j] > max[0]) {
                    max[0] = c[j];
                    maxIndex[0] = j;

                    for (int k = 1; k < maxLen; k++) {
                        max[k] = 0;
                        maxIndex[k] = 0;
                    }
                }
                // 有多个是相同长度的子串
                else if (c[j] == max[0]) {
                    for (int k = 1; k < maxLen; k++) {
                        if (max[k] == 0) {
                            max[k] = c[j];
                            maxIndex[k] = j;
                            break; // 在后面加一个就要退出循环了
                        }
                    }
                }
            }
            // for (int temp : c) {
            // System.out.print(temp);
            // }
            // System.out.println();
        }
        List<String> maxSubStrs = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        // 打印最长子字符串
        for (j = 0; j < maxLen; j++) {
            if (max[j] > 0) {
                // System.out.println("第" + (j + 1) + "个公共子串:");
                for (i = maxIndex[j] - max[j] + 1; i <= maxIndex[j]; i++)
                    sb.append(str1[i]);
                maxSubStrs.add(sb.toString());
                sb.setLength(0);
                // System.out.println(" ");
            }
        }
        return maxSubStrs;
    }

    public static void main(String[] args) {
        String content = "<html></html>李孝《》”“利“      ：自���丰%？！—、·-℃％.－-！<（）〖〗Ⅰ!1.2--——一九二一——一九四九——“七一”“7·23”[产业倒逼新机遇]４５．６８】胸秘��?妱���丰胸�Ƿ广�͊商�ǒ睐贯彻十八��·开局新举措";
        content = StringUtil.full2HalfChange(content);
        content = StringUtil.replaceHtml(content);
        content = content.replaceAll(" {1,}", "");
        System.out.println(content);
    }
}
