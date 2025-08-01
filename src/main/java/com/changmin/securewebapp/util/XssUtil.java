package com.changmin.securewebapp.util;

public class XssUtil {
    public static String cleanXss(String input) {
        if (input == null) return null;

        return input
                .replaceAll("<","&lt;")
                .replaceAll(">","&gt;")
                .replaceAll("\\(","&#40;")
                .replaceAll("\\)","&#41;")
                .replaceAll("`","&#39;")
                .replaceAll("\"","&quot;");
    }
}