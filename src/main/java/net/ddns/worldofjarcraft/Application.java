package net.ddns.worldofjarcraft;

import net.ddns.worldofjarcraft.DatabaseRepresentation.Benutzer;
import net.ddns.worldofjarcraft.DatabaseRepresentation.Einkauf;
import net.ddns.worldofjarcraft.DatabaseRepresentation.EinkaufRepository;
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
    private EinkaufRepository repo;

    public static JdbcTemplate getTemplate() {
        return null;
    }

    @Override
    public void run(String... args) {
        for(Einkauf einkauf : repo.findAll()){
            System.out.println(einkauf.getLebensmittel());
        }

    }
}
