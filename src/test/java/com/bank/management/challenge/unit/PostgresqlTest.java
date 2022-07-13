package com.bank.management.challenge.unit;

import com.bank.management.challenge.infrastructure.entities.Person;
import liquibase.Liquibase;
import liquibase.Scope;
import liquibase.database.Database;
import liquibase.database.core.PostgresDatabase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.diff.DiffResult;
import liquibase.diff.compare.CompareControl;
import liquibase.diff.output.DiffOutputControl;
import liquibase.diff.output.changelog.DiffToChangeLog;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.ext.hibernate.database.HibernateSpringPackageDatabase;
import liquibase.ext.hibernate.database.connection.HibernateConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.FileSystemResourceAccessor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.dialect.PostgreSQL10Dialect;
import org.junit.jupiter.api.Test;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.testcontainers.containers.ContainerLaunchException;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
@Testcontainers
class PostgresqlTest {
    private static final String PACKAGES = Person.class.getPackageName();

    private static Network network = Network.newNetwork();

    @Container
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:latest")
            .withDatabaseName("postgresql")
            .withUsername("postgres")
            .withPassword("postgres");

    @Test
    void testContainer() throws ClassNotFoundException, SQLException, LiquibaseException, IOException, ParserConfigurationException {
        log.info("Database is running: {}",postgreSQLContainer.isRunning());
        log.info(postgreSQLContainer.getJdbcUrl());
        log.info(postgreSQLContainer.getDatabaseName());

        Class.forName(postgreSQLContainer.getDriverClassName());
        try(Connection connection = DriverManager.getConnection(postgreSQLContainer.getJdbcUrl(), postgreSQLContainer.getUsername(), postgreSQLContainer.getPassword())) {
            Database database = new PostgresDatabase();
            database.setConnection(new JdbcConnection(connection));

            Liquibase liquibase = new Liquibase((String) null, new ClassLoaderResourceAccessor(), database);

            Database hibernateDatabase = new HibernateSpringPackageDatabase();
            hibernateDatabase.setDefaultSchemaName("public");
            hibernateDatabase.setDefaultCatalogName("postgres");
            String urlConn = "hibernate:spring:" + PACKAGES + "?dialect=" + PostgreSQL10Dialect.class.getName() +
                    "&hibernate.physical_naming_strategy=" + CamelCaseToUnderscoresNamingStrategy.class.getName() + "&hibernate.implicit_naming_strategy=" + SpringImplicitNamingStrategy.class.getName();
            hibernateDatabase.setConnection(new JdbcConnection(new HibernateConnection(urlConn, new ClassLoaderResourceAccessor())));
            System.out.println(urlConn);
            DiffResult diffResult = liquibase.diff(hibernateDatabase, database, CompareControl.STANDARD);

            if(!diffResult.areEqual()){
                long executionTime = System.currentTimeMillis();

                File outFile = new File("src/main/resources/db/changelog/changelog-main.xml");
                try(OutputStream outChangeLog = new FileOutputStream(outFile)) {
                    String changeLogString = toChangeLog(diffResult);
                    outChangeLog.write(changeLogString.getBytes(StandardCharsets.UTF_8));
                    Scope.getCurrentScope().getLog(getClass()).info("Changelog:\n" + changeLogString);
                }

                liquibase = new Liquibase(outFile.getAbsolutePath(), new FileSystemResourceAccessor(File.listRoots()), database);
                StringWriter stringWriter = new StringWriter();
                liquibase.update((String) null, stringWriter);

                String sql = stringWriter.toString();
                File sqlOutFile = new File("src/main/resources/db/changelog/changelog-main.sql");
                try(OutputStream sqlOutChangeLog = new FileOutputStream(sqlOutFile)) {
                    sqlOutChangeLog.write(sql.getBytes(StandardCharsets.UTF_8));
                }
            }
        } catch(ContainerLaunchException e){
            log.error("Error initializing container",e);
        }
    }

    private String toChangeLog(DiffResult diffResult) throws IOException, ParserConfigurationException, DatabaseException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(out, true, StandardCharsets.UTF_8);
        DiffToChangeLog diffToChangeLog = new DiffToChangeLog(diffResult,
                new DiffOutputControl().setIncludeCatalog(false).setIncludeSchema(false));
        diffToChangeLog.print(printStream);
        printStream.close();
        return out.toString(StandardCharsets.UTF_8);
    }

}
