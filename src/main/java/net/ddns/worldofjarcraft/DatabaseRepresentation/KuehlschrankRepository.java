package net.ddns.worldofjarcraft.DatabaseRepresentation;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KuehlschrankRepository extends CrudRepository<Kuehlschrank, Integer> {
}
