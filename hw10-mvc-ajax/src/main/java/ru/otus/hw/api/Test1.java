package ru.otus.hw.api;

import jakarta.persistence.criteria.CriteriaBuilder;

import java.io.Console;

public class Test1 {
    // вывести в консоль цифры от 0 до 1000, кратные 3 и НЕ кратные 5,
    // сумма цифр в которых меньше 10

    public void printNumbers() {
        int startNumber = 0;
        int endNumber = 1000;

        Integer currentNumber = startNumber;
        while (currentNumber <= endNumber) {
            int mod1 = currentNumber % 3;
            int mod2 = currentNumber % 5;
            if (mod1 == 0 && mod2 != 0) {
                String strCurrentNumber = currentNumber.toString();
                int sumNubers = 0;
                while (strCurrentNumber.length() >= 1) {
                    sumNubers = sumNubers + Integer.parseInt(strCurrentNumber);
                    strCurrentNumber = strCurrentNumber.substring(1, strCurrentNumber.length()-1);
                }
                if (sumNubers < 10) {
                    System.out.println(currentNumber);
                }
            }
            currentNumber++;
        }
    }
}


