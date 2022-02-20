package ru.otus.solid.atmmachine.models;

import java.util.Arrays;
import java.util.List;

public enum Banknote {
    ONE_HUNDRED("100"), ONE_THOUSAND("1000"), FIVE_HUNDREDS("500");

    private String code;

    Banknote(String code) {
        this.code = code;
    }

    public String getCode() { return code;}

    public static List<Banknote> sortedByDesc() {
        return Arrays.stream(values())
                .sorted((o1, o2) -> Integer.compare(Integer.parseInt(o2.getCode()), Integer.parseInt(o1.getCode())))
                .toList();
    }
}
