package net.ddns.worldofjarcraft.DatabaseRepresentation;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Lebensmittel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}
