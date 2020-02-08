package com.myfirebaseproject.utils;

public class DoubleUtilities {
    public static boolean isValidDouble(String doubleValue) {
        try{
            Double.parseDouble(doubleValue);
          return true;
        } catch(Exception e) {
            /*if(doubleValue.toCharArray().length == 1) {
                return false;
            }else {
                return false;
            }*/
            return false;
        }
    }
}
