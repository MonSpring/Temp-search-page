package com.example.testsearch.util;

public class WhiteSpaceRemover {

    public static String doubleWhiteSpaceChangeTheonlyOneWhiteSpace(String word) {
        word = word.replaceAll("\\s+", " ");
        return word;
    }

    public static String allWhiteSpaceRemove(String word) {
        word = word.replaceAll("\\s", "");
        return word;
    }
}
