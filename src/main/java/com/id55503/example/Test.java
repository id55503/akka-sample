package com.id55503.example;

public class Test {

    private static void change(String[] str){
        str[0] = "aaa";
    }

    public static void main(String[] args) {
        String[] string = new String[]{"111","222"};
        change(string);
        System.out.println(string[0]);
        System.out.println(Math.log(10)/Math.log(2));

    }

}
