package net.ddns.worldofjarcraft;

import net.ddns.worldofjarcraft.DatabaseRepresentation.*;
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

    @Autowired
    private BenutzerRepository users;
    @Autowired
    private KuehlschrankRepository schraenke;
    @Autowired
    private FachRepository faecher;

    @Autowired
    private LebensmittelRepository lebensmittelRepository;
    @Override
    public void run(String... args) {
        /*for(Einkauf einkauf : repo.findAll()){
            if(einkauf.getNutzer()!=null)
            System.out.println(einkauf.getNutzer().getEMail());
        }
        System.out.println("Und jetzt alle...");
        for(Benutzer benutzer : users.findAll()){
            System.out.println(benutzer.getEMail());
        }
        System.out.println("Und nun zum Kuehlschrank-Test...");
        Kuehlschrank schrank = schraenke.findById(18).get();
        for(Fach fach : Fach.getAll(faecher,schrank)){
            System.out.println("Fach: "+fach.getlNummer());
            for(Lebensmittel lebensmittel : Lebensmittel.getAll(lebensmittelRepository,fach)){
                System.out.println(lebensmittel.getName());
            }
        }*/
    }
}
