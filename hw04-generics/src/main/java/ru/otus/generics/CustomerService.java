package ru.otus.generics;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class CustomerService {

    private final TreeMap<Customer, String> treeMap = new TreeMap<>(Comparator.comparingLong(Customer::getScores));

    //todo: 3. надо реализовать методы этого класса
    //важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны

    public Map.Entry<Customer, String> getSmallest(){
        //Возможно, чтобы реализовать этот метод, потребуется посмотреть как Map.Entry сделан в jdkd
        return getCloneMapEntry(treeMap.firstEntry());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return getCloneMapEntry(treeMap.higherEntry(customer)); // это "заглушка, чтобы скомилировать"
    }

    private final Map.Entry<Customer, String> getCloneMapEntry(Map.Entry<Customer, String> mapEntry) {
        if (mapEntry == null) {
            return null;
        }
        Customer customer = new Customer(mapEntry.getKey());
        return new AbstractMap.SimpleEntry<>(customer, mapEntry.getValue());
    }

    public void add(Customer customer, String data) {
        treeMap.put(customer, data);
    }
}
