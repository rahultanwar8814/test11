package com.example.test11;

public class RunTimeSave {

    private static String Number="8059126255",imgnumber=null;

    private static int l=0;

    public static void setImgnumber(String str1) {
        imgnumber = str1;
    }

    public static String getImgnumber() {
        return imgnumber;
    }

    public static int setLike(int like) {
      l = like;
      return l;
    }

    public static int getLike() {
        return l;
    }

    public static String getNumber() {
        return Number;
    }

    public static void setNumber(String number) {
        Number = number;
    }
}
