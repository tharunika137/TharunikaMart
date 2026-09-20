package com.tharunika.tharunikamart.listener;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.servlet.*;
import java.io.InputStream;
import java.sql.*;

public class AppContextListener implements ServletContextListener {

    private HikariDataSource ds;

    public void contextInitialized(ServletContextEvent e) {
        try {
            String db = System.getenv().getOrDefault(
                "THARUNIKA_DB_URL",
                System.getProperty(
                    "tharunikamart.db",
                    "jdbc:h2:./data/tharunikamart;MODE=PostgreSQL;DB_CLOSE_DELAY=-1"
                )
            );
            String user = System.getenv().getOrDefault("THARUNIKA_DB_USER", "sa");
            String pass = System.getenv().getOrDefault("THARUNIKA_DB_PASSWORD", "");

            HikariConfig c = new HikariConfig();
            c.setJdbcUrl(db);
            c.setDriverClassName(resolveDriverClass(db));
            c.setUsername(user);
            c.setPassword(pass);

            c.setMaximumPoolSize(10);
            c.setMinimumIdle(2);
            c.setPoolName("TharunikaMartPool");

            ds = new HikariDataSource(c);
            e.getServletContext().setAttribute("ds", ds);

            try (Connection con = ds.getConnection()) {
                runScript(con, "schema.sql");
                runScript(con, "seed.sql");
            }

        } catch (Exception ex) {
            throw new RuntimeException("Database initialization failed", ex);
        }
    }

    private String resolveDriverClass(String jdbcUrl) {
        if (jdbcUrl != null && jdbcUrl.startsWith("jdbc:postgresql:")) {
            return "org.postgresql.Driver";
        }
        return "org.h2.Driver";
    }

    private void runScript(Connection con, String resource) throws Exception {
        try (InputStream in =
                getClass().getClassLoader().getResourceAsStream(resource)) {

            if (in == null) {
                throw new IllegalStateException(resource + " not found");
            }

            String sql = new String(
                in.readAllBytes(),
                java.nio.charset.StandardCharsets.UTF_8
            );

            for (String statement : sql.split(";")) {
                if (!statement.trim().isBlank()) {
                    try (PreparedStatement ps =
                            con.prepareStatement(statement)) {
                        ps.execute();
                    }
                }
            }
        }
    }

    public void contextDestroyed(ServletContextEvent e) {
        if (ds != null) {
            ds.close();
        }
    }
}