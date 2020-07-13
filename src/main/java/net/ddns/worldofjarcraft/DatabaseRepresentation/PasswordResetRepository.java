package net.ddns.worldofjarcraft.DatabaseRepresentation;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PasswordResetRepository extends CrudRepository<PasswordResetRequest, Integer> {
    @Query("select p from PasswordResetRequest p where p.Besitzer=:besitzer and p.nonce=:nonce order by time")
    public List<PasswordResetRequest> getAllByBesitzerAndNonce(Benutzer besitzer, Integer nonce);
}
