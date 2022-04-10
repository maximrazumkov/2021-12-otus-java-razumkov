package ru.otus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.executor.DataTemplate;
import ru.otus.model.Client;
import ru.otus.model.Manager;
import ru.otus.sessionmanager.TransactionRunner;

import java.util.List;
import java.util.Optional;

public class DbServiceManagerImpl implements DBServiceManager {
    private static final Logger log = LoggerFactory.getLogger(DbServiceManagerImpl.class);

    private final DataTemplate<Manager> managerDataTemplate;
    private final TransactionRunner transactionRunner;
    private final HwCache<String, Manager> cache;

    public DbServiceManagerImpl(DataTemplate<Manager> managerDataTemplate, TransactionRunner transactionRunner, HwCache<String, Manager> cache) {
        this.managerDataTemplate = managerDataTemplate;
        this.transactionRunner = transactionRunner;
        this.cache = cache;
    }

    @Override
    public Manager saveManager(Manager manager) {
        return transactionRunner.doInTransaction(connection -> {
            if (manager.getNo() == null) {
                var managerNo = managerDataTemplate.insert(connection, manager);
                var createdManager = new Manager(managerNo, manager.getLabel(), manager.getParam1());
                log.info("created manager: {}", createdManager);
                cache.put(String.valueOf(managerNo), manager);
                return createdManager;
            }
            managerDataTemplate.update(connection, manager);
            log.info("updated manager: {}", manager);
            cache.put(String.valueOf(manager.getNo()), manager);
            return manager;
        });
    }

    @Override
    public Optional<Manager> getManager(long no) {
        String idStr = String.valueOf(no);
        Manager manager = cache.get(idStr);
        if (manager != null) {
            return Optional.of(manager);
        }
        return transactionRunner.doInTransaction(connection -> {
            var managerOptional = managerDataTemplate.findById(connection, no);
            log.info("manager: {}", managerOptional);
            managerOptional.ifPresent(value -> cache.put(idStr, value));
            return managerOptional;
        });
    }

    @Override
    public List<Manager> findAll() {
        return transactionRunner.doInTransaction(connection -> {
            var managerList = managerDataTemplate.findAll(connection);
            log.info("managerList:{}", managerList);
            managerList.forEach(manager -> cache.put(String.valueOf(manager.getNo()), manager));
            return managerList;
       });
    }
}
