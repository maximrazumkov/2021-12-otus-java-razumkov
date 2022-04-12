package ru.otus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.executor.DataTemplate;
import ru.otus.model.Client;
import ru.otus.sessionmanager.TransactionRunner;

import java.util.List;
import java.util.Optional;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final DataTemplate<Client> dataTemplate;
    private final TransactionRunner transactionRunner;
    private final HwCache<String, Client> cache;

    public DbServiceClientImpl(DataTemplate<Client> dataTemplate, TransactionRunner transactionRunner, HwCache<String, Client> cache) {
        this.dataTemplate = dataTemplate;
        this.transactionRunner = transactionRunner;
        this.cache = cache;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionRunner.doInTransaction(connection -> {
            if (client.getId() == null) {
                var clientId = dataTemplate.insert(connection, client);
                var createdClient = new Client(clientId, client.getName());
                log.info("created client: {}", createdClient);
                cache.put(String.valueOf(clientId), client);
                return createdClient;
            }
            dataTemplate.update(connection, client);
            log.info("updated client: {}", client);
            cache.put(String.valueOf(client.getId()), client);
            return client;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        String idStr = String.valueOf(id);
        Client client = cache.get(idStr);
        if (client != null) {
            return Optional.of(client);
        }
        return transactionRunner.doInTransaction(connection -> {
            var clientOptional = dataTemplate.findById(connection, id);
            log.info("client: {}", clientOptional);
            clientOptional.ifPresent(value -> cache.put(idStr, value));
            return clientOptional;
        });
    }

    @Override
    public List<Client> findAll() {
        return transactionRunner.doInTransaction(connection -> {
            var clientList = dataTemplate.findAll(connection);
            log.info("clientList:{}", clientList);
            clientList.forEach(client -> cache.put(String.valueOf(client.getId()), client));
            return clientList;
       });
    }
}
