package net.ddns.worldofjarcraft;

import net.ddns.worldofjarcraft.DatabaseRepresentation.Benutzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class Application implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    private static final Logger log = LoggerFactory.getLogger(Application.class);


    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static JdbcTemplate template=null;

    public static JdbcTemplate getTemplate() {
        return template;
    }

    @Override
    public void run(String... args) {
        template = jdbcTemplate;

        jdbcTemplate.query(
                "SELECT * FROM Benutzer WHERE ?;", new Object[] { "1" },
                (rs, RowNum) -> new Benutzer(rs.getString("EMail"), rs.getString("Passwort"))
        ).forEach(customer -> System.out.println(customer.toString()));
    }
}
