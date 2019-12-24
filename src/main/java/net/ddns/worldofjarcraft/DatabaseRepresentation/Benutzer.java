package net.ddns.worldofjarcraft.DatabaseRepresentation;

import net.ddns.worldofjarcraft.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;
@Entity
public class Benutzer {
    @Id
    @Column(name = "EMail")
    private String EMail;
    @Column(name= "Passwort")
    private String Passwort;

    /**
     *
     * @return Name of the user in lower case.
     */
    public String getEMail() {
        return EMail;
    }

    public String getPasswort() {
        return Passwort;
    }

    public Benutzer(){}


    public void setEMail(String EMail) {
        this.EMail = EMail;
    }

    public void setPasswort(String passwort) {
        Passwort = passwort;
    }

    public Benutzer(String EMail, String passwort) {

        this.EMail = EMail;
        Passwort = passwort;
    }
    @Override
    public String toString() {
        return String.format(
                "Customer[EMail=%s, Passwort='%s']",
                EMail, Passwort);
    }
    public static List<Benutzer> getAll(BenutzerRepository users){
        ArrayList<Benutzer> nutzer = new ArrayList<>();
        for(Benutzer user : users.findAll()){
            nutzer.add(user);
        }
        return nutzer;
    }
    public static Benutzer getBenutzer(BenutzerRepository users, String EMail){
        return users.findById(EMail).isPresent() ? users.findById(EMail).get() : null;
    }
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Benutzer)){
            return false;
        }
        Benutzer benutzer = (Benutzer) obj;
        return benutzer.getEMail().equals(this.getEMail());
    }
}
