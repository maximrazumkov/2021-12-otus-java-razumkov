package ru.otus.solid.atmmachine.io;

import java.util.Scanner;

public class ScannerInput implements Input {

    private final Scanner scanner;

    public ScannerInput(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public String readLine() {
        String line = scanner.nextLine();
        System.out.println();
        return line;
    }
}
