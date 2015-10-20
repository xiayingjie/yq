package com.common.manage.tool;

import java.util.Random;

/**
 * @classDescription:
 * @author:xiayingjie
 * @createTime:15/8/28
 */

public class RandomUtil {
    public static int random(int n) {
        if (n < 1 || n > 10) {
            throw new IllegalArgumentException("cannot random " + n + " bit number");
        }
        Random ran = new Random();
        if (n == 1) {
            return ran.nextInt(10);
        }
        int bitField = 0;
        char[] chs = new char[n];
        for (int i = 0; i < n; i++) {
            while (true) {
                int k = ran.nextInt(10);
                if ((bitField & (1 << k)) == 0) {
                    bitField |= 1 << k;
                    chs[i] = (char) (k + '0');
                    break;
                }
            }
        }
        return Integer.parseInt(new String(chs));
    }

    public static void main(String[] args) {
        System.out.println(random(4));
    }

}
