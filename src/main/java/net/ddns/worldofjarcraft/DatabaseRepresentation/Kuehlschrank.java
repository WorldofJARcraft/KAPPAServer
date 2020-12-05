package net.ddns.worldofjarcraft.DatabaseRepresentation;

import javax.annotation.Generated;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Kuehlschrank {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "laufnummer")
    private int laufNummer;

    @Column
    private String Name;

    @Column
    private int Zahl_Faecher;

    @ManyToOne
    @JoinColumn(name = "Besitzer")
    private Benutzer Besitzer;

    public int getLaufNummer() {
        return laufNummer;
    }

    public void setLaufNummer(int laufNummer) {
        this.laufNummer = laufNummer;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getZahl_Faecher() {
        return Zahl_Faecher;
    }

    public void setZahl_Faecher(int zahl_Faecher) {
        Zahl_Faecher = zahl_Faecher;
    }

    public Benutzer getBesitzer() {
        return Besitzer;
    }

    public void setBesitzer(Benutzer besitzer) {
        Besitzer = besitzer;
    }

    public Kuehlschrank(String name, int zahl_Faecher, Benutzer besitzer) {

        Name = name;
        Zahl_Faecher = zahl_Faecher;
        Besitzer = besitzer;
    }

    public Kuehlschrank() {

    }

    public static List<Kuehlschrank> getAll(KuehlschrankRepository repository, Benutzer benutzer){
        ArrayList<Kuehlschrank> kuehlschranks = new ArrayList<>();
        for(Kuehlschrank schrank : repository.findAll()){
            if(schrank.getBesitzer().equals(benutzer)){
                kuehlschranks.add(schrank);
            }
        }
        return kuehlschranks;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Kuehlschrank)){
            return false;
        }
        Kuehlschrank schrank = (Kuehlschrank) obj;
        return schrank.getLaufNummer() == this.getLaufNummer();
    }
}
