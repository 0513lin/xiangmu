package com;

import java.math.BigDecimal;

public class TestMain {

    public static void main(String[] args) {
        Double d =1.62208488433E12;

        System.out.println("d = " + d);

        BigDecimal b = new BigDecimal(d);
        System.out.println("b=="+b.toPlainString());
    }
}
