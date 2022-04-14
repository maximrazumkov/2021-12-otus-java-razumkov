package ru.otus.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.model.Client;
import ru.otus.dao.ClientDao;

import java.util.List;

public class DbClientDao implements ClientDao {
    private static final Logger log = LoggerFactory.getLogger(DbClientDao.class);

    private final DataTemplate<Client> clientDataTemplate;
    private final TransactionManager transactionManager;

    public DbClientDao(DataTemplate<Client> clientDataTemplate, TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
        this.clientDataTemplate = clientDataTemplate;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(session -> {
            var clientCloned = client.clone();
            clientDataTemplate.insert(session, clientCloned);
            session.flush();
            log.info("created client: {}", clientCloned);
            return clientCloned;
        });
    }

    @Override
    public List<Client> findAll() {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientList = clientDataTemplate.findAll(session);
            log.info("clientList:{}", clientList);
            return clientList;
        });
    }
}