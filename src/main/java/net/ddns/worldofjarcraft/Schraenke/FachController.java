package net.ddns.worldofjarcraft.Schraenke;

import net.ddns.worldofjarcraft.DatabaseRepresentation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class FachController {
    @Autowired
    private BenutzerRepository users;

    @Autowired
    KuehlschrankRepository kuehlschrankRepository;

    @Autowired
    private FachRepository faecher;
    @RequestMapping(value = "/schrank/{id}/newFach",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity addFach(HttpServletRequest request, @PathVariable int id, @RequestParam String name){
        String user = request.getRemoteUser();
        Benutzer benutzer = users.findById(user).isPresent()? users.findById(user).get() : null;
        if(benutzer != null) {
            Kuehlschrank schrank = kuehlschrankRepository.findById(id).isPresent()? kuehlschrankRepository.findById(id).get() : null;
            if(schrank!=null && schrank.getBesitzer().equals(benutzer)){
                Fach fach = new Fach(schrank,name);
                faecher.save(fach);
                return new ResponseEntity<>(fach.getlNummer(),HttpStatus.CREATED);
            }
        }
        ErrorClass error = new ErrorClass("Unauthorized!");
        return new ResponseEntity<ErrorClass>(error,HttpStatus.UNAUTHORIZED);
    }
    @RequestMapping(value = "/schrank/{id}/getAll",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getFaecher(HttpServletRequest request, @PathVariable int id){
        String user = request.getRemoteUser();
        Benutzer benutzer = users.findById(user).isPresent()? users.findById(user).get() : null;
        if(benutzer != null) {
            Kuehlschrank schrank = kuehlschrankRepository.findById(id).isPresent()? kuehlschrankRepository.findById(id).get() : null;
            if(schrank!=null && schrank.getBesitzer().equals(benutzer)) {
                List<Fach> faches = Fach.getAll(faecher,schrank);
                return new ResponseEntity<>(faches,HttpStatus.CREATED);
            }
        }
        ErrorClass error = new ErrorClass("Unauthorized!");
        return new ResponseEntity<ErrorClass>(error,HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "/schrank/{id}/{fachID}/delete",method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity deleteSchrank(HttpServletRequest request, @PathVariable int id, @PathVariable int fachID){
        String user = request.getRemoteUser();
        Benutzer benutzer = users.findById(user).isPresent()? users.findById(user).get() : null;
        if(benutzer != null) {
            Kuehlschrank schrank = kuehlschrankRepository.findById(id).isPresent()? kuehlschrankRepository.findById(id).get() : null;
            if(schrank!=null && schrank.getBesitzer().equals(benutzer)) {
                Fach fach = faecher.findById(fachID).orElse(null);
                if(fach!= null && fach.getKuehlschrank().equals(schrank)){
                    faecher.delete(fach);
                    return new ResponseEntity(HttpStatus.ACCEPTED);
                }
            }
        }
        ErrorClass error = new ErrorClass("Unauthorized!");
        return new ResponseEntity<ErrorClass>(error,HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "/schrank/{id}/{fachID}/update",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity updateFach(HttpServletRequest request, @PathVariable int id, @PathVariable int fachID, @RequestParam(name = "newName") String newName){
        String user = request.getRemoteUser();
        Benutzer benutzer = users.findById(user).isPresent()? users.findById(user).get() : null;
        if(benutzer != null) {
            Kuehlschrank schrank = kuehlschrankRepository.findById(id).isPresent()? kuehlschrankRepository.findById(id).get() : null;
            if(schrank!=null && schrank.getBesitzer().equals(benutzer)) {
                Fach fach = faecher.findById(fachID).orElse(null);
                if(fach!= null && fach.getKuehlschrank().equals(schrank)){
                    fach.setName(newName);
                    faecher.save(fach);
                    return new ResponseEntity(HttpStatus.ACCEPTED);
                }
            }
        }
        ErrorClass error = new ErrorClass("Unauthorized!");
        return new ResponseEntity<>(error,HttpStatus.UNAUTHORIZED);
    }
}
