package ru.otus;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.HwListener;
import ru.otus.cachehw.MyCache;
import ru.otus.datasource.DriverManagerDataSource;
import ru.otus.executor.DbExecutorImpl;
import ru.otus.mapper.*;
import ru.otus.mapper.impl.EntitySQLMetaDataImpl;
import ru.otus.mapper.impl.EntitySqlParamsImpl;
import ru.otus.mapper.impl.InitializerEntityClassMetaDataImpl;
import ru.otus.model.Client;
import ru.otus.model.Manager;
import ru.otus.service.DbServiceClientImpl;
import ru.otus.service.DbServiceManagerImpl;
import ru.otus.sessionmanager.TransactionRunnerJdbc;

import javax.sql.DataSource;

public class HomeWorkJDBC {
    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    private static final Logger log = LoggerFactory.getLogger(HomeWorkJDBC.class);

    public static void main(String[] args) throws Exception {
// Общая часть
        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        flywayMigrations(dataSource);
        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();

// Работа с клиентом
        InitializerEntityClassMetaData initializer = new InitializerEntityClassMetaDataImpl();
        EntityClassMetaData<Client> entityClassMetaDataClient = initializer.init(Client.class);
        EntitySQLMetaData entitySQLMetaDataClient = new EntitySQLMetaDataImpl(entityClassMetaDataClient);
        EntitySqlParams<Client> entitySqlParamsClient = new EntitySqlParamsImpl<>(entityClassMetaDataClient);
        var dataTemplateClient = new DataTemplateJdbc<>(dbExecutor, entitySQLMetaDataClient, entitySqlParamsClient); //реализация DataTemplate, универсальная


        HwCache<String, Client> clientCache = new MyCache<>();
        HwListener<String, Client> clientCacheListener = new HwListener<String, Client>() {
            @Override
            public void notify(String key, Client value, String action) {
                log.info("key:{}, value:{}, action: {}", key, value, action);
            }
        };
        clientCache.addListener(clientCacheListener);
// Код дальше должен остаться
        var dbServiceClient = new DbServiceClientImpl(dataTemplateClient, transactionRunner, clientCache);
        dbServiceClient.saveClient(new Client("dbServiceFirst"));

        var clientSecond = dbServiceClient.saveClient(new Client("dbServiceSecond"));
        var clientSecondSelected = dbServiceClient.getClient(clientSecond.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getId()));
        log.info("clientSecondSelected:{}", clientSecondSelected);

// Сделайте тоже самое с классом Manager (для него надо сделать свою таблицу)

        EntityClassMetaData<Manager> entityClassMetaDataManager = initializer.init(Manager.class);
        EntitySQLMetaData entitySQLMetaDataManager = new EntitySQLMetaDataImpl(entityClassMetaDataManager);
        EntitySqlParams<Manager> entitySqlParamsManager = new EntitySqlParamsImpl<>(entityClassMetaDataManager);
        var dataTemplateManager = new DataTemplateJdbc<Manager>(dbExecutor, entitySQLMetaDataManager, entitySqlParamsManager);

        HwCache<String, Manager> managerCache = new MyCache<>();
        HwListener<String, Manager> managerCacheListener = new HwListener<String, Manager>() {
            @Override
            public void notify(String key, Manager value, String action) {
                log.info("key:{}, value:{}, action: {}", key, value, action);
            }
        };
        managerCache.addListener(managerCacheListener);

        var dbServiceManager = new DbServiceManagerImpl(dataTemplateManager, transactionRunner, managerCache);
        dbServiceManager.saveManager(new Manager("ManagerFirst"));

        var managerSecond = dbServiceManager.saveManager(new Manager("ManagerSecond"));
        var managerSecondSelected = dbServiceManager.getManager(managerSecond.getNo())
                .orElseThrow(() -> new RuntimeException("Manager not found, id:" + managerSecond.getNo()));
        log.info("managerSecondSelected:{}", managerSecondSelected);
    }

    private static void flywayMigrations(DataSource dataSource) {
        log.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        log.info("db migration finished.");
        log.info("***");
    }
}
