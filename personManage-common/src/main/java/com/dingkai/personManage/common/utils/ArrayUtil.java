package com.dingkai.personManage.common.utils;

import org.apache.commons.collections4.CollectionUtils;

/**
 * @author dingkai1
 * @desc
 * @date 2021/3/10 16:41
 */
public class ArrayUtil {

    public static String arrayToStr(String[] strings, String split) {
        if (strings == null || strings.length == 0) {
            return "";
        }
        if (split == null) {
            split = "";
        }
        StringBuilder sb = new StringBuilder();
        for (String string : strings) {
            sb.append(string).append(split);
        }
        if (!"".equals(split)) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

}
