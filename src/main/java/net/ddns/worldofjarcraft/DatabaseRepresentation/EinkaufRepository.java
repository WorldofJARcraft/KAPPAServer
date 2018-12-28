package net.ddns.worldofjarcraft.DatabaseRepresentation;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EinkaufRepository extends CrudRepository<Einkauf, Integer> {
}
