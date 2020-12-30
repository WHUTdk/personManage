package com.dingkai.personManage.common.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author dingkai1
 * @desc 汉字拼音工具类
 * @date 2020/12/30 10:09
 */
public class Pinyin4jUtil {

    private static final Logger logger = LoggerFactory.getLogger(Pinyin4jUtil.class);

    private static HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
    private static final Pattern pattern = Pattern.compile("[^\u4e00-\u9fa5A-Za-z0-9]");

    static {
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
    }

    /**
     * 获取简拼
     */
    public static String converterToSimplePinyin(String chines, String separator) {
        String[] ss = converterToSimplePinyin(chines).split(",");
        if (StringUtils.isEmpty(separator)) {
            separator = "|";
        }
        return StringUtils.join(ss, separator);
    }

    /**
     * 获取全拼
     */
    public static String converterToFullPinyin(String chines, String separator) {
        String[] ss = converterToFullPinyin(chines).split(",");
        if (StringUtils.isEmpty(separator)) {
            separator = "|";
        }
        return StringUtils.join(ss, separator);
    }

    /**
     * 汉字转换位汉语拼音首字母，英文字符不变，特殊字符丢失 支持多音字，生成方式如（长沙市长:cssc,zssz,zssc,cssz）
     *
     * @param chines 汉字
     * @return 拼音简称
     */
    public static String converterToSimplePinyin(String chines) {
        chines = replaceAll(chines);
        StringBuffer pinyinName = new StringBuffer();
        List<String> list = new ArrayList<>();
        char[] nameChars = chines.toCharArray();
        for (char nameChar : nameChars) {
            if (nameChar <= 128) {
                // 英文字母或者特殊字符
                list.add(String.valueOf(nameChar));
                continue;
            }
            list.add("%");
            try {
                // 取得当前汉字的所有全拼
                String[] strs = PinyinHelper.toHanyuPinyinStringArray(
                        nameChar, defaultFormat);
                if (strs != null && strs.length > 0) {
                    for (String str : strs) {
                        // 取首字母
                        pinyinName.append(str.charAt(0)).append(",");
                    }
                    pinyinName.deleteCharAt(pinyinName.length() - 1);
                }
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                e.printStackTrace();
            }
            pinyinName.append(" ");
        }
        return parseTheChineseByObject(distinctTheChinese(pinyinName.toString()), list);
    }

    /**
     * 汉字转换位汉语全拼，英文字符不变，特殊字符丢失
     * 支持多音字，生成方式如（重当参:zhongdangcen,zhongdangcan,chongdangcen
     * ,chongdangshen,zhongdangshen,chongdangcan）
     *
     * @param chines 汉字
     * @return 拼音
     */
    public static String converterToFullPinyin(String chines) {
        chines = replaceAll(chines);
        char[] nameChar = chines.toCharArray();
        List<Map<String, Integer>> mapList = new ArrayList<Map<String, Integer>>();
        Map<String, Integer> onlyOne;
        for (int i = 0; i < nameChar.length; i++) {
            if (nameChar[i] > 128) {
                try {
                    onlyOne = new Hashtable<String, Integer>();
                    // 取得当前汉字的所有全拼
                    String[] strs = PinyinHelper.toHanyuPinyinStringArray(
                            nameChar[i], defaultFormat);
                    if (strs != null && strs.length > 0) {
                        for (int j = 0; j < strs.length; j++) {
                            onlyOne.put(convertInitialToUpperCase(strs[j]), new Integer(1));
                        }
                        mapList.add(onlyOne);
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            }
        }
        return parseTheChineseByObject(mapList, null);
    }

    /**
     * 替换字符串内特殊符号
     */
    public static String replaceAll(String content) {
        try {
            if (StringUtils.isBlank(content)) {
                return "";
            }
            Matcher m = pattern.matcher(content);
            return m.replaceAll("").trim();
        } catch (Exception e) {
            logger.error("替换字符串内特殊符号异常  content:{}, 异常:{}", content, e);
            return "";
        }
    }

    /**
     * 解析并组合拼音，对象合并方案(推荐使用)
     */
    private static String parseTheChineseByObject(List<Map<String, Integer>> list, List<String> sumList) {
        // 用于统计每一次,集合组合数据
        Map<String, Integer> first = null;
        // 遍历每一组集合
        for (int i = 0; i < list.size(); i++) {
            // 每一组集合与上一次组合的Map
            Map<String, Integer> temp = new Hashtable<String, Integer>();
            // 第一次循环，first为空
            if (first != null) {
                // 取出上次组合与此次集合的字符，并保存
                for (String s : first.keySet()) {
                    for (String s1 : list.get(i).keySet()) {
                        String str = s + s1;
                        temp.put(str, 1);
                    }
                }
                // 清理上一次组合数据
                if (temp.size() > 0) {
                    first.clear();
                }
            } else {
                for (String s : list.get(i).keySet()) {
                    temp.put(s, 1);
                }
            }
            // 保存组合数据以便下次循环使用
            if (temp.size() > 0) {
                first = temp;
            }
        }
        StringBuffer sb = new StringBuffer();
        if (first != null) {
            // 遍历取出组合字符串
            for (String str : first.keySet()) {
                if (null == sumList) {
                    sb.append(str).append(",");
                } else {
                    List<String> arr = new ArrayList<String>();
                    Collections.addAll(arr, str.split(""));
                    arr.remove("");
                    StringBuffer sf = new StringBuffer();
                    for (int i = 0; i < sumList.size(); i++) {
                        if (!"%".equals(sumList.get(i))) {
                            sf.append(sumList.get(i));
                        } else if (arr.size() != 0) {
                            sf.append(arr.get(0));
                            arr.remove(0);
                        }
                    }
                    sb.append(sf).append(",");
                }
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * 去除多音字重复数据
     */
    private static List<Map<String, Integer>> distinctTheChinese(String pinyin) {
        String[] firsts = pinyin.split(" ");
        // 去除重复拼音后的拼音列表
        List<Map<String, Integer>> mapList = Arrays.stream(firsts).map(a -> {
            Map<String, Integer> onlyOne = new Hashtable<String, Integer>();
            String[] china = a.split(",");
            // 多音字处理
            // 读出每个汉字的拼音
            for (String s : china) {
                Integer count = onlyOne.get(s);
                if (count == null) {
                    onlyOne.put(convertInitialToUpperCase(s), new Integer(1));
                }
            }
            return onlyOne;
        }).collect(Collectors.toList());
        return mapList;
    }

    private static String convertInitialToUpperCase(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * 获取拼音首字母
     */
    public static String getFirstLetter(String content) {
        content = replaceAll(content);
        char[] chars = content.trim().toCharArray();
        if (Objects.isNull(chars) || chars.length <= 0) {
            return "";
        }
        if (chars[0] > 128) {
            try {
                // 取得当前汉字的所有全拼
                String[] strs = PinyinHelper.toHanyuPinyinStringArray(
                        chars[0], defaultFormat);
                if (strs != null && strs.length > 0) {
                    // 取首字母
                    return String.valueOf(strs[0].charAt(0)).toUpperCase();
                }
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                e.printStackTrace();
            }
        } else {
            return String.valueOf(chars[0]).toUpperCase();
        }
        return "";
    }

    public static void main(String[] args) {
        System.out.println(converterToSimplePinyin("市长"));
        System.out.println(converterToFullPinyin("市长"));
        System.out.println(converterToSimplePinyin("市长", "|"));
        System.out.println(converterToFullPinyin("市长", "|"));
        System.out.println(getFirstLetter("丁凯"));
    }

}
