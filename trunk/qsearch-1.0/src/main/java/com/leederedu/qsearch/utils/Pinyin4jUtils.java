package com.leederedu.qsearch.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by lenovo on 2016/6/21.
 */
public class Pinyin4jUtils {

    public static String getPinyinOne(String chinese) {
        Set<String> strings = makeStringByStringSet(chinese, false);
        if (strings == null || strings.isEmpty()) return "";
        return strings.iterator().next();
    }

    public static String getPinyinOneToLowerCase(String chinese) {
        Set<String> strings = makeStringByStringSet(chinese, false);
        if (strings == null || strings.isEmpty()) return "";
        return strings.iterator().next().toLowerCase();
    }


    public static String getOneJianPinLowerCase(String chinese) {
       String pinYinJianXie = getPinyinJianPin(chinese);
       if(pinYinJianXie == null || pinYinJianXie.trim().equals("")) return "";
       return pinYinJianXie.toLowerCase().split(",")[0].trim();
    }


    /***************************************************************************
     * 获取中文汉字拼音 默认输出
     *
     * @return
     */
    public static String getPinyin(String chinese) {
        return getPinyinZh_CN(makeStringByStringSet(chinese));
    }

    /***************************************************************************
     * 拼音大写输出
     *
     * @Name: Pinyin4jUtil.java
     */
    public static String getPinyinToUpperCase(String chinese) {
        return getPinyinZh_CN(makeStringByStringSet(chinese)).toUpperCase();
    }

    /***************************************************************************
     * 拼音小写输出
     *
     * @return
     * @Name: Pinyin4jUtil.java
     */
    public static String getPinyinToLowerCase(String chinese) {
        return getPinyinZh_CN(makeStringByStringSet(chinese)).toLowerCase();
    }

    /***************************************************************************
     * 首字母大写输出
     *
     * @return
     * @Name: Pinyin4jUtil.java
     */
    public static String getPinyinFirstToUpperCase(String chinese) {
        return getPinyin(chinese);
    }

    /***************************************************************************
     * 拼音简拼输出
     *
     * @return
     * @Name: Pinyin4jUtil.java
     */
    public static String getPinyinJianPin(String chinese) {
        return getPinyinConvertJianPin(getPinyin(chinese));
    }

    /***************************************************************************
     * 字符集转换
     *
     * @param chinese 中文汉字
     * @throws BadHanyuPinyinOutputFormatCombination
     * @Name: Pinyin4jUtil.java
     */
    public static Set<String> makeStringByStringSet(String chinese) {
        return makeStringByStringSet(chinese, true);
    }

    public static Set<String> makeStringByStringSet(String chinese, boolean firstUp) {
        char[] chars = chinese.toCharArray();
        if (chinese != null && !chinese.trim().equalsIgnoreCase("")) {
            char[] srcChar = chinese.toCharArray();
            String[][] temp = new String[chinese.length()][];
            for (int i = 0; i < srcChar.length; i++) {
                char c = srcChar[i];

                // 是中文或者a-z或者A-Z转换拼音
                if (String.valueOf(c).matches("[\\u4E00-\\u9FA5]+")) {

                    try {
                        temp[i] = PinyinHelper.toHanyuPinyinStringArray(chars[i], getDefaultOutputFormat());

                    } catch (BadHanyuPinyinOutputFormatCombination e) {
                        e.printStackTrace();
                    }
                } else if (((int) c >= 65 && (int) c <= 90)
                        || ((int) c >= 97 && (int) c <= 122)) {
                    temp[i] = new String[]{String.valueOf(srcChar[i])};
                } else {
                    temp[i] = new String[]{String.valueOf(srcChar[i])};
                }
            }
            String[] pingyinArray = Exchange(temp, firstUp);
            Set<String> zhongWenPinYin = new HashSet<String>();
            for (int i = 0; i < pingyinArray.length; i++) {
                zhongWenPinYin.add(pingyinArray[i]);
            }
            return zhongWenPinYin;
        }
        return null;
    }

    /***************************************************************************
     * Default Format 默认输出格式
     *
     * @return
     * @Name: Pinyin4jUtil.java
     */
    public static HanyuPinyinOutputFormat getDefaultOutputFormat() {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);// 小写
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 没有音调数字
        format.setVCharType(HanyuPinyinVCharType.WITH_U_AND_COLON);// u显示
        return format;
    }

    /***************************************************************************
     * @param strJaggedArray
     * @param firstUp
     * @return
     */
    public static String[] Exchange(String[][] strJaggedArray, boolean firstUp) {
        String[][] temp = DoExchange(strJaggedArray, firstUp);
        return temp[0];
    }

    /***************************************************************************
     * @param strJaggedArray
     * @return
     */
    private static String[][] DoExchange(String[][] strJaggedArray, boolean firstUp) {
        int len = strJaggedArray.length;
        if (len >= 2) {
            int len1 = strJaggedArray[0].length;
            int len2 = strJaggedArray[1].length;
            int newlen = len1 * len2;
            String[] temp = new String[newlen];
            int Index = 0;
            for (int i = 0; i < len1; i++) {
                for (int j = 0; j < len2; j++) {
                    if (firstUp) {
                        temp[Index] = capitalize(strJaggedArray[0][i]) + capitalize(strJaggedArray[1][j]);
                    } else {
                        temp[Index] = strJaggedArray[0][i] + strJaggedArray[1][j];
                    }
                    Index++;
                }
            }
            String[][] newArray = new String[len - 1][];
            for (int i = 2; i < len; i++) {
                newArray[i - 1] = strJaggedArray[i];
            }
            newArray[0] = temp;
            return DoExchange(newArray, firstUp);
        } else {
            return strJaggedArray;
        }
    }

    /***************************************************************************
     * 首字母大写
     *
     * @param s
     * @return
     */
    public static String capitalize(String s) {
        char ch[];
        ch = s.toCharArray();
        if (ch != null && ch.length > 0) {
            if (ch[0] >= 'a' && ch[0] <= 'z') {
                ch[0] = (char) (ch[0] - 32);
            }
        }
        String newString = new String(ch);
        return newString;
    }

    /***************************************************************************
     * 字符串集合转换字符串(逗号分隔)
     *
     * @param stringSet
     * @return
     */
    public static String getPinyinZh_CN(Set<String> stringSet) {
        StringBuilder str = new StringBuilder();
        int i = 0;
        for (String s : stringSet) {
            if (i == stringSet.size() - 1) {
                str.append(s);
            } else {
                str.append(s + ",");
            }
            i++;
        }
        return str.toString();
    }

    /***************************************************************************
     * 获取每个拼音的简称
     *
     * @param chinese
     * @return
     * @Name: Pinyin4jUtil.java
     */
    public static String getPinyinConvertJianPin(String chinese) {
        String[] strArray = chinese.split(",");
        String strChar = "";
        for (String str : strArray) {
            char arr[] = str.toCharArray(); // 将字符串转化成char型数组
            for (int i = 0; i < arr.length; i++) {
                if (arr[i] >= 65 && arr[i] < 91 || (arr[i] >= 48 && arr[i] <= 57)) { // 判断是否是大写字母
                    strChar += new String(arr[i] + "");
                }
            }
            strChar += ",";
        }
        return strChar;
    }

    /***************************************************************************
     * Test
     *
     * @param args
     * @Name: Pinyin4jUtil.java
     */
    public static void main(String[] args) {
        String str = "1张A三a曾123薄车";
        System.out.println("非中文不转换：" + getPinyin(str));
        System.out.println("小写输出：" + getPinyinToLowerCase(str));
        System.out.println("大写输出：" + getPinyinToUpperCase(str));
        System.out.println("首字母大写输出：" + getPinyinFirstToUpperCase(str));
        System.out.println("简拼输出：" + getPinyinJianPin(str));
        System.out.println("简拼输出1：" + getOneJianPinLowerCase("优能力"));
    }
}




