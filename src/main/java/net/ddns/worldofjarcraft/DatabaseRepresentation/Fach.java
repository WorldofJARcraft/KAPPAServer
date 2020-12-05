package net.ddns.worldofjarcraft.DatabaseRepresentation;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Fach {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "lnummer")
    private int lNummer;

    @ManyToOne
    @JoinColumn(name = "Kuehlschrank")
    private Kuehlschrank Kuehlschrank;

    @Column
    private String Name;

    public Fach() {
    }

    public Fach(Kuehlschrank kuehlschrank, String name) {
        Kuehlschrank = kuehlschrank;
        Name = name;
    }

    public int getlNummer() {
        return lNummer;
    }

    public void setlNummer(int lNummer) {
        this.lNummer = lNummer;
    }

    public net.ddns.worldofjarcraft.DatabaseRepresentation.Kuehlschrank getKuehlschrank() {
        return Kuehlschrank;
    }

    public void setKuehlschrank(net.ddns.worldofjarcraft.DatabaseRepresentation.Kuehlschrank kuehlschrank) {
        Kuehlschrank = kuehlschrank;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public static List<Fach> getAll(FachRepository repository, Kuehlschrank kuehlschrank){
        List<Fach> faches = new ArrayList<>();
        for(Fach fach : repository.findAll()){
            if(fach.getKuehlschrank().equals(kuehlschrank)){
                faches.add(fach);
            }
        }
        return faches;
    }
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Fach)){
            return false;
        }
        Fach fach = (Fach) obj;
        return fach.getlNummer() == this.getlNummer();
    }
}
