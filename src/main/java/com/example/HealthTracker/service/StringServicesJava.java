package com.example.HealthTracker.service;

import java.util.Arrays;

public class StringServicesJava {
    public boolean checkIsogram(String inputString)
    {
        // always it is good to convert the string in either
        // lower/upper case because for isogram irrespective
        // of the case we need to check that no alphabet
        // should be occurred more than once.
        inputString = inputString.toLowerCase();
        int stringLength = inputString.length();

        char characterArray[] = inputString.toCharArray();
        // This will help to sort the alphabets
        Arrays.sort(characterArray);
        for (int i = 0; i < stringLength - 1; i++) {
            if (characterArray[i] == characterArray[i + 1])
                return false;
        }
        return true;
    }
    // If the given string contains all the letters from a
    // to z (before that let us convert the given string into
    // lowercase) then it is a panagram
    public boolean checkPanagram(String inputString)
    {
        // always it is good to convert the string in either
        // lower/upper case because for panagram
        // irrespective of the case we need to check that
        // all alphabets should be present.
        inputString = inputString.toLowerCase();
        boolean isAllAlphabetPresent = true;
        // Loop over each character itself
        for (char ch = 'a'; ch <= 'z'; ch++) {
            // Using contains we can check whether the
            // string contains the given alphabet. if it
            // does not contain make the boolean variable
            // false
            if (!inputString.contains(String.valueOf(ch))) {
                isAllAlphabetPresent = false;
                break;
            }
        }

        return isAllAlphabetPresent;
    }

    // While comparing two strings, if a string contains the
    // same characters of another string but the order of the
    // characters are different
    public boolean checkAnagram(String inputString1,
                                String inputString2)
    {
        // always it is good to convert the string in either
        // lower/upper case because for anagram
        // irrespective of the case we need to check that
        // alphabets are available in both the string.
        inputString1 = inputString1.toLowerCase();
        inputString2 = inputString2.toLowerCase();
        int str1Length = inputString1.length();
        int str2Length = inputString2.length();
        // For anagram, only ordering is different but
        // string lengthwise they should be equal
        if (str1Length != str2Length) {
            return false;
        }

        char[] str1 = inputString1.toCharArray();
        char[] str2 = inputString2.toCharArray();

        // Sort both strings
        Arrays.sort(str1);
        Arrays.sort(str2);

        // Compare sorted strings
        for (int index = 0; index < str1Length; index++) {
            if (str1[index] != str2[index])
                return false;
        }

        return true;
    }
}
