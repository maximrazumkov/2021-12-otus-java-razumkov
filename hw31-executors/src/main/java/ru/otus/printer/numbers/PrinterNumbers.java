package ru.otus.printer.numbers;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PrinterNumbers {

    public static void main(String... args) {
        PrinterNumbers pingPong = new PrinterNumbers();
        Thread thread1 = new Printer(2, 1, 10, pingPong);
        Thread thread2 = new Printer(1, 1, 10, pingPong);
        thread1.start();
        thread2.start();
    }
}
