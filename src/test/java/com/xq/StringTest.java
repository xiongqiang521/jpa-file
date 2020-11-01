package com.xq;

/**
 * @author XQ
 * @version v1.0
 * 2020/10/31 16:54
 */
public class StringTest {
    public static void main(String[] args) {
        String name = "sss.";
        System.out.println(makeType(name));
    }

    private static String makeType(String name) {
        int indexOf = name.lastIndexOf('.');
        if (indexOf > 0) {
            return name.substring(indexOf + 1).toLowerCase();
        }
        return "";

    }
}
