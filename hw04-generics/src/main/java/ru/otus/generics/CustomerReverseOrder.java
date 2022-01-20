package ru.otus.generics;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;

public class CustomerReverseOrder {

    private final Stack<Customer> customerQueue = new Stack<>();

    //todo: 2. надо реализовать методы этого класса
    //надо подобрать подходящую структуру данных, тогда решение будет в "две строчки"

    public void add(Customer customer) {
        customerQueue.push(customer);
    }

    public Customer take() {
        return customerQueue.pop();
    }
}
