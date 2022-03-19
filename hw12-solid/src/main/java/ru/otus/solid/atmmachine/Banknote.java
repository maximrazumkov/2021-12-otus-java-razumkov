package ru.otus.solid.atmmachine;

import java.util.Arrays;
import java.util.List;

public enum Banknote {
    ONE_HUNDRED(100), ONE_THOUSAND(1000), FIVE_HUNDREDS(500);

    private int code;

    Banknote(int code) {
        this.code = code;
    }

    public int getCode() { return code;}

    public static List<Banknote> sortedByDesc() {
        return Arrays.stream(values())
                .sorted((o1, o2) -> Integer.compare(o2.getCode(), o1.getCode()))
                .toList();
    }
}
