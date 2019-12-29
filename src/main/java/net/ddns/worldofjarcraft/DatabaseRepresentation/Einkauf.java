package net.ddns.worldofjarcraft.DatabaseRepresentation;


import javax.persistence.*;

@Entity
@Table(name="einkauf")
public class Einkauf {
    @Column(name = "Lebensmittel")
    private String lebensmittel;
    @ManyToOne
    @JoinColumn(name = "Benutzer")
    private Benutzer nutzer;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Num")
    private int id;

    public String getLebensmittel() {
        return lebensmittel;
    }

    public void setLebensmittel(String lebensmittel) {
        lebensmittel = lebensmittel;
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
        lebensmittel = lebensmittel;
        this.nutzer = nutzer;
    }

    public Einkauf(String lebensmittel, Benutzer nutzer, int id) {
        lebensmittel = lebensmittel;
        this.nutzer = nutzer;
        this.id = id;
    }
}
