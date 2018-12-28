package net.ddns.worldofjarcraft.DatabaseRepresentation;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BenutzerRepository extends CrudRepository<Benutzer, String> {
}
