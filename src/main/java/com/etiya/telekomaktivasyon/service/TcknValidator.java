package com.etiya.telekomaktivasyon.service;

public class TcknValidator {

    public static boolean isValid(String tckn) {
        if (tckn == null || !tckn.matches("\\d{11}")) {
            return false;
        }
        if (tckn.charAt(0) == '0') {
            return false;
        }

        int[] d = new int[11];
        for (int i = 0; i < 11; i++) {
            d[i] = tckn.charAt(i) - '0';
        }

        int oddSum = d[0] + d[2] + d[4] + d[6] + d[8];
        int evenSum = d[1] + d[3] + d[5] + d[7];

        int digit10 = ((oddSum * 7) - evenSum) % 10;
        if (digit10 < 0) digit10 += 10;
        if (digit10 != d[9]) {
            return false;
        }

        int totalFirst10 = 0;
        for (int i = 0; i < 10; i++) {
            totalFirst10 += d[i];
        }
        int digit11 = totalFirst10 % 10;

        return digit11 == d[10];
    }
}