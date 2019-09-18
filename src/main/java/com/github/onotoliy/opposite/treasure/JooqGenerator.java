package com.github.onotoliy.opposite.treasure;

import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.FileSystemResourceAccessor;
import org.jooq.codegen.GenerationTool;
import org.jooq.meta.jaxb.Configuration;
import org.jooq.meta.jaxb.Generator;
import org.jooq.meta.jaxb.Jdbc;
import org.jooq.meta.jaxb.Schema;
import org.jooq.meta.jaxb.Target;
import org.testcontainers.containers.PostgreSQLContainer;

import java.net.URL;
import java.nio.file.Paths;
import java.sql.Connection;
import java.time.Duration;

public class JooqGenerator {

    public static void main(String[] args) throws Exception {
        URL changelog = JooqGenerator.class.getResource(args[0]);
        String directory = args[1];

        /*
          Удалить файлы из указанной директории.
         */
        Paths.get(System.getProperty("user.dir"), directory)
             .toFile()
             .deleteOnExit();

        PostgreSQLContainer container = (PostgreSQLContainer)
            new PostgreSQLContainer("postgres:10.4")
                .withDatabaseName("service")
                .withUsername("service")
                .withPassword("service")
                .withStartupTimeout(Duration.ofSeconds(600));

        container.start();

        /*
          Выполнение liquibase.
         */
        try (Connection connection = container.createConnection("")) {
            new Liquibase(
                changelog.getPath(),
                new FileSystemResourceAccessor(),
                DatabaseFactory.getInstance()
                               .findCorrectDatabaseImplementation(new JdbcConnection(connection))
            ).update("");
        }

        /*
          Генерация схемы.
         */
        Configuration configuration = new Configuration()
            .withJdbc(new Jdbc()
                .withDriver("org.postgresql.Driver")
                .withUrl(container.getJdbcUrl())
                .withUser(container.getUsername())
                .withPassword(container.getPassword()))
            .withGenerator(new Generator()
                .withName("org.jooq.codegen.DefaultGenerator")
                .withDatabase(new org.jooq.meta.jaxb.Database()
                    .withName("org.jooq.meta.postgres.PostgresDatabase")
                    .withExcludes("DATABASECHANGELOGLOCK*|DATABASECHANGELOG*")
                    .withSchemata(new Schema().withInputSchema("public")))
                .withTarget(new Target()
                    .withPackageName("com.github.onotoliy.opposite.treasure.jooq")
                    .withDirectory(directory)));

        GenerationTool.generate(configuration);

        container.stop();
    }
}
