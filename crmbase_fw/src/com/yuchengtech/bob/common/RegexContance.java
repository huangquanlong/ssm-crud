package com.yuchengtech.bob.common;

import java.util.regex.Pattern;

/**
 * @describe Of course, there are some regex string, and some method to check the format of string.
 * @author WillJoe
 *
 */
public class RegexContance {
    
    private static final String numberReg = "^([-]|[.]|[-.]|[0-9])[0-9]*[.]*[0-9]+$";
    
    private static final String intNum = "^[0-9]+$";
    
    private static final String fullYearDate = "[0-9]{2}(/|-)[0-9]{2}(/|-)[0-9]{4}";
    
    private static final String simpleYearDate = "[0-9]{2}(/|-)[0-9]{2}(/|-)[0-9]{2}";
    
    /**
     * @describe Check a string is a number, include int,long,float,double...
     * @param The number string.
     * @return
     */
    public static final boolean CheckNumric(String number){
        return Pattern.compile(numberReg).matcher(number).matches()||Pattern.compile(intNum).matcher(number).matches();
    }
    
    /**
     * @describe Check out that a string is a date format:(dd/mm/yyyy dd-mm-yyyy dd/mm/yy dd-mm-yy).
     * @param date
     * @return
     */
    public static final boolean CheckDate(String date){
        return Pattern.compile(fullYearDate).matcher(date).matches()||Pattern.compile(simpleYearDate).matcher(date).matches();
    }
    
    /**
     * @describe Check the a string format is regular by the regix.
     * @param regix
     * @param number
     * @return
     */
    public static final boolean ExcuteReg(String regix,String number){
        return Pattern.compile(regix).matcher(number).matches();
    }
    
}
