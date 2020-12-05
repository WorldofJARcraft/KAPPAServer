package net.ddns.worldofjarcraft.DatabaseRepresentation;

import lombok.extern.log4j.Log4j2;
import lombok.val;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

@Entity
@Log4j2
public class Lebensmittel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column
    private int Nummer;

    @Column
    private String Name;

    @Column
    private String Anzahl;

    @Column
    private long Haltbarkeitsdatum;

    @ManyToOne
    @JoinColumn(name = "Fach")
    private Fach Fach;

    @ManyToOne
    @JoinColumn(name = "Besitzer")
    private Benutzer Besitzer;

    @Column
    private long eingelagert;

    public Lebensmittel() {
    }

    public Lebensmittel(String name, String anzahl, long haltbarkeitsdatum, net.ddns.worldofjarcraft.DatabaseRepresentation.Fach fach, Benutzer besitzer, long eingelagert) {
        Name = name;
        Anzahl = anzahl;
        Haltbarkeitsdatum = haltbarkeitsdatum;
        Fach = fach;
        Besitzer = besitzer;
        this.eingelagert = eingelagert;
    }

    public int getNummer() {
        return Nummer;
    }

    public void setNummer(int nummer) {
        Nummer = nummer;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAnzahl() {
        return Anzahl;
    }

    public void setAnzahl(String anzahl) {
        Anzahl = anzahl;
    }

    public long getHaltbarkeitsdatum() {
        return Haltbarkeitsdatum;
    }

    public void setHaltbarkeitsdatum(long haltbarkeitsdatum) {
        Haltbarkeitsdatum = haltbarkeitsdatum;
    }

    public net.ddns.worldofjarcraft.DatabaseRepresentation.Fach getFach() {
        return Fach;
    }

    public void setFach(net.ddns.worldofjarcraft.DatabaseRepresentation.Fach fach) {
        Fach = fach;
    }

    public Benutzer getBesitzer() {
        return Besitzer;
    }

    public void setBesitzer(Benutzer besitzer) {
        Besitzer = besitzer;
    }

    public long getEingelagert() {
        return eingelagert;
    }

    public void setEingelagert(long eingelagert) {
        this.eingelagert = eingelagert;
    }

    public static List<Lebensmittel> getAll(LebensmittelRepository repository, Fach fach){
        List<Lebensmittel> lebensmittel = new ArrayList<>();
        for(Lebensmittel mittel : repository.findAll()){
            if(mittel.getFach().equals(fach)){
                lebensmittel.add(mittel);
            }
        }
        return lebensmittel;
    }
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Lebensmittel)){
            return false;
        }
        Lebensmittel mittel = (Lebensmittel) obj;
        return mittel.getNummer() == this.getNummer();
    }

    public static boolean migrated = false;

    public static String decodeLebensmittelAttribute(String attr){
        if(attr == null){
            return null;
        }
        if(attr.equals("Base64:")){
            return "";
        }
        if(attr.startsWith("Base64:")){
            attr = new String(Base64.getMimeDecoder().decode(attr.split("Base64:")[1]), StandardCharsets.UTF_8);
        }
        return attr;
    }

    @Transactional
    public void migrateAll(LebensmittelRepository repository){
        List<Lebensmittel> updatedLebensmittel = new LinkedList<>();
        for(Lebensmittel lebensmittel : repository.findAll()){
            String lebensmittelName = lebensmittel.getName();
            String lebensmittelAnzahl = lebensmittel.getAnzahl();
            lebensmittel.setName(decodeLebensmittelAttribute(lebensmittelName));
            lebensmittel.setAnzahl(decodeLebensmittelAttribute(lebensmittelAnzahl));
            updatedLebensmittel.add(lebensmittel);
        }
        repository.saveAll(updatedLebensmittel);
        migrated = true;
    }

    public void checkMigrated(LebensmittelRepository repository){
        migrated = repository.getUnmigrated() == 0;
        log.info("Migrated: "+migrated);
    }
}
