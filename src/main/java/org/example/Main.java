package org.example;

import java.time.LocalTime;

public class Main {
    public static void main(String[] args) {

        String time1 = "10:30";
        String time2 = "15:45";

        LocalTime localTime1 = LocalTime.parse(time1);
        LocalTime localTime2 = LocalTime.parse(time2);

        int comparisonResult = localTime1.compareTo(localTime2);

        if (comparisonResult < 0) {
            System.out.println(time1 + " küçüktür " + time2);
        }
        else if (comparisonResult > 0) {
            System.out.println(time1 + " büyüktür " + time2);
        } else {
            System.out.println(time1 + " ile " + time2 + " eşittir.");
        }
    }
}