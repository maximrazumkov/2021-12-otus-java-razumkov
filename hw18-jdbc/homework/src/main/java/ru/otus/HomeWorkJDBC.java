package ru.otus;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.datasource.DriverManagerDataSource;
import ru.otus.executor.DbExecutorImpl;
import ru.otus.jdbc.mapper.DataTemplateJdbc;
import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.EntitySQLMetaData;
import ru.otus.jdbc.mapper.EntitySqlParams;
import ru.otus.jdbc.mapper.impl.EntityClassMetaDataImpl;
import ru.otus.jdbc.mapper.impl.EntitySQLMetaDataImpl;
import ru.otus.jdbc.mapper.impl.EntitySqlParamsImpl;
import ru.otus.model.Client;
import ru.otus.model.Manager;
import ru.otus.service.DbServiceClientImpl;
import ru.otus.service.DbServiceManagerImpl;
import ru.otus.sessionmanager.TransactionRunnerJdbc;

import javax.sql.DataSource;

public class HomeWorkJDBC {
    private static final String URL = "jdbc:postgresql://localhost:5432/demoDB";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123";

    private static final Logger log = LoggerFactory.getLogger(HomeWorkJDBC.class);

    public static void main(String[] args) throws Exception {
// Общая часть
        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        flywayMigrations(dataSource);
        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();

// Работа с клиентом
        EntityClassMetaData<Client> entityClassMetaDataClient = EntityClassMetaDataImpl.initEntityClassMetaData(Client.class);
        EntitySQLMetaData entitySQLMetaDataClient = new EntitySQLMetaDataImpl(entityClassMetaDataClient);
        EntitySqlParams<Client> entitySqlParamsClient = new EntitySqlParamsImpl<>(entityClassMetaDataClient);
//        System.out.println(entitySQLMetaDataClient.getInsertSql());
//        System.out.println(entitySQLMetaDataClient.getUpdateSql());
//        System.out.println(entitySQLMetaDataClient.getSelectByIdSql());
//        System.out.println(entitySQLMetaDataClient.getSelectAllSql());
//        System.out.println(entitySqlParamsClient.getFiledValues(new Client("dbServiceFirst")));
        var dataTemplateClient = new DataTemplateJdbc<Client>(dbExecutor, entitySQLMetaDataClient, entitySqlParamsClient); //реализация DataTemplate, универсальная

// Код дальше должен остаться
        var dbServiceClient = new DbServiceClientImpl(transactionRunner, dataTemplateClient);
        dbServiceClient.saveClient(new Client("dbServiceFirst"));

        var clientSecond = dbServiceClient.saveClient(new Client("dbServiceSecond"));
        var clientSecondSelected = dbServiceClient.getClient(clientSecond.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getId()));
        log.info("clientSecondSelected:{}", clientSecondSelected);

// Сделайте тоже самое с классом Manager (для него надо сделать свою таблицу)

        EntityClassMetaData<Manager> entityClassMetaDataManager = EntityClassMetaDataImpl.initEntityClassMetaData(Manager.class);
        EntitySQLMetaData entitySQLMetaDataManager = new EntitySQLMetaDataImpl(entityClassMetaDataManager);
        EntitySqlParams<Manager> entitySqlParamsManager = new EntitySqlParamsImpl<>(entityClassMetaDataManager);
//        System.out.println(entitySQLMetaDataManager.getInsertSql());
//        System.out.println(entitySQLMetaDataManager.getUpdateSql());
//        System.out.println(entitySQLMetaDataManager.getSelectByIdSql());
//        System.out.println(entitySQLMetaDataManager.getSelectAllSql());
//        System.out.println(entitySqlParamsManager.getFiledValues(new Manager("ManagerSecond")));
        var dataTemplateManager = new DataTemplateJdbc<Manager>(dbExecutor, entitySQLMetaDataManager, entitySqlParamsManager);

        var dbServiceManager = new DbServiceManagerImpl(transactionRunner, dataTemplateManager);
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
