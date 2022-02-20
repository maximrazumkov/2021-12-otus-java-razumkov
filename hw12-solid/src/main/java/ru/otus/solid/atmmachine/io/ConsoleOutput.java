package ru.otus.solid.atmmachine.io;

public class ConsoleOutput implements Output {
    @Override
    public void print(String strLine) {
        System.out.println(strLine);
        System.out.println();
    }
}
