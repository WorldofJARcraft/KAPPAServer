package net.ddns.worldofjarcraft.DatabaseRepresentation;


import javax.persistence.*;

@Entity
@Table(name="einkauf")
public class Einkauf {
    @Column(name = "Lebensmittel")
    private String Lebensmittel;
    @Column(name = "Benutzer")
    private String nutzer;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Num")
    private int id;

    public String getLebensmittel() {
        return Lebensmittel;
    }

    public void setLebensmittel(String lebensmittel) {
        Lebensmittel = lebensmittel;
    }

    public Benutzer getNutzer() {
        return null;
    }

    public void setNutzer(Benutzer nutzer) {
        this.nutzer = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public Einkauf(){}
    public Einkauf(String lebensmittel, Benutzer nutzer, int id) {
        Lebensmittel = lebensmittel;
        this.nutzer = null;
        this.id = id;
    }
}
