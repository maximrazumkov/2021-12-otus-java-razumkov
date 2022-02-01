package ru.calculator;

import java.time.LocalDateTime;

public class CalcDemo {

    public static void main(String[] args) {
        final long counter = 100_000_000;
        final var summator = new Summator();
        final long startTime = System.currentTimeMillis();

        for (var idx = 0; idx < counter; ++idx) {
            final var data = new Data(idx);
            summator.calc(data);

            if (idx % 10_000_000 == 0) {
                System.out.println(LocalDateTime.now() + " current idx:" + idx);
            }
        }

        final long delta = System.currentTimeMillis() - startTime;
        System.out.println(summator.getPrevValue());
        System.out.println(summator.getPrevPrevValue());
        System.out.println(summator.getSumLastThreeValues());
        System.out.println(summator.getSomeValue());
        System.out.println(summator.getSum());
        System.out.println("spend msec:" + delta + ", sec:" + (delta / 1000));
    }
}
