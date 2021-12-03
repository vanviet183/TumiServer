package com.example.tumiweb;

import java.util.ArrayList;
import java.util.Scanner;

public class test {
    public static void main(String[] args) {
        Scanner sc =  new Scanner(System.in);
        System.out.println("Nhập chuỗi: ");
        String s = sc.nextLine();

        int sum = 0;
        int count = 0;
        for(int i=0;i<s.length();i++){
            if(Character.isDigit(s.charAt(i))) {
                count++;
                sum+=  s.charAt(i) - 48;
            }
        }
        System.out.println("Có " + count + " ký tự số" );

        int a = 1;
        for(int i=0;i<s.length();i++){
            if(Character.isDigit(s.charAt(i))) {
                if(sum % ( s.charAt(i) - 48) ==0 )
                    a *= s.charAt(i)  - 48  ;
            }
        }
        System.out.println("Tích: " + a);
    }
}
