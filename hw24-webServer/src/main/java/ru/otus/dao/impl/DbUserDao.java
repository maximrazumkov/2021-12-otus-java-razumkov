package ru.otus.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.dao.UserDao;
import ru.otus.model.User;

import java.util.Optional;

public class DbUserDao implements UserDao {

    private static final Logger log = LoggerFactory.getLogger(DbUserDao.class);

    private final DataTemplate<User> clientDataTemplate;
    private final TransactionManager transactionManager;

    public DbUserDao(DataTemplate<User> clientDataTemplate, TransactionManager transactionManager) {
        this.clientDataTemplate = clientDataTemplate;
        this.transactionManager = transactionManager;
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var users = clientDataTemplate.findByEntityField(session, "login", login);
            log.info("users: {}", users);
            return users.isEmpty() ? Optional.empty(): Optional.ofNullable(users.get(0));
        });
    }
}
