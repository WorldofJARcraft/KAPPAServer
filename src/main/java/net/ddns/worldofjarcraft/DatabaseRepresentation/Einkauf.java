package net.ddns.worldofjarcraft.DatabaseRepresentation;


import javax.persistence.*;

@Entity
@Table(name="einkauf")
public class Einkauf {
    @Column(name = "Lebensmittel")
    private String Lebensmittel;
    @ManyToOne
    @JoinColumn(name = "Benutzer")
    private Benutzer nutzer;
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
        return nutzer;
    }

    public void setNutzer(Benutzer nutzer) {
        this.nutzer = nutzer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public Einkauf(){}

    public Einkauf(String lebensmittel, Benutzer nutzer) {
        Lebensmittel = lebensmittel;
        this.nutzer = nutzer;
    }

    public Einkauf(String lebensmittel, Benutzer nutzer, int id) {
        Lebensmittel = lebensmittel;
        this.nutzer = nutzer;
        this.id = id;
    }
}
