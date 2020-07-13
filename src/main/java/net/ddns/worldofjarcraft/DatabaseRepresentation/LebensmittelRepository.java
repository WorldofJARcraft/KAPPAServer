package net.ddns.worldofjarcraft.DatabaseRepresentation;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LebensmittelRepository extends CrudRepository<Lebensmittel, Integer> {
    @Query("select e from Lebensmittel e where name like CONCAT('%',:name,'%') and Besitzer = :userID ORDER BY Name")
    public List<Lebensmittel> searchByName(@Param("name") String name, @Param("userID") Benutzer user);
    @Query("select e from Lebensmittel e where e.Besitzer=:besitzer and e.Fach=:fach order by name")
    public List<Lebensmittel> getAllByBesitzerIsAndFachIsOrderByName(Benutzer besitzer, Fach fach);

    @Query("select e from Lebensmittel e where e.Besitzer=:besitzer order by name")
    public List<Lebensmittel> getAllByBesitzerIsOrderByName(Benutzer besitzer);
}
