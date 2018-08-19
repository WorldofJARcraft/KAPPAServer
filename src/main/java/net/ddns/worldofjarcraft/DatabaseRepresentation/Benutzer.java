package net.ddns.worldofjarcraft.DatabaseRepresentation;

import net.ddns.worldofjarcraft.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class Benutzer {
    private String EMail;
    private String Passwort;

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
    public static List<Benutzer> getAll(){
        return new ArrayList<>(Application.getTemplate().query(
                "SELECT * FROM Benutzer WHERE ?;", new Object[]{"1"},
                (rs, RowNum) -> new Benutzer(rs.getString("EMail"), rs.getString("Passwort"))
        ));
    }

    public static Benutzer getBenutzer(String EMail){
        List<Benutzer> benutzers = new ArrayList<Benutzer>(Application.getTemplate().query("SELECT * FROM Benutzer WHERE EMail=?;", new Object[] {EMail }, (rs, RowNum) -> new Benutzer(rs.getString("EMail"), rs.getString("Passwort"))));
        return benutzers.isEmpty()?null:benutzers.get(0);
    }
}
