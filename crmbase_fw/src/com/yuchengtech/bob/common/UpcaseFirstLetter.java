package com.yuchengtech.bob.common;

/**
 * @describe a simple class to make the first letter of a String to uppercase.
 * E.g used to make the getter and setter method.
 */
public class UpcaseFirstLetter {
    /**
     * @param methodName source String.
     * @return the String after translate.
     */
    public static String CheckAndSetFirst(String methodName){
        char first = methodName.charAt(0);
        String mName = methodName;
        if(first>=97 && first<=122){
            first -= 32;
            mName = first+mName.substring(1);
        }
        return mName;
    }
}
