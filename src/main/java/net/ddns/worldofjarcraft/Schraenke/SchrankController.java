package net.ddns.worldofjarcraft.Schraenke;

import net.ddns.worldofjarcraft.DatabaseRepresentation.BenutzerRepository;
import net.ddns.worldofjarcraft.DatabaseRepresentation.Kuehlschrank;
import net.ddns.worldofjarcraft.DatabaseRepresentation.KuehlschrankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class SchrankController {
    @Autowired
    private BenutzerRepository users;

    @Autowired
    private KuehlschrankRepository schraenke;
    @RequestMapping(value = "/schrank/new",method = RequestMethod.GET)
    public ResponseEntity addKuehlschrank(HttpServletRequest request, @Param(value = "name") String name, @Param(value = "faecher") int faecher){

        return null;
    }
}
