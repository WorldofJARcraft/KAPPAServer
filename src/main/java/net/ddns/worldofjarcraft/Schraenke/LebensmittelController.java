package net.ddns.worldofjarcraft.Schraenke;

import net.ddns.worldofjarcraft.DatabaseRepresentation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
public class LebensmittelController {
    @Autowired
    private BenutzerRepository users;

    @Autowired
    KuehlschrankRepository kuehlschrankRepository;

    @Autowired
    private FachRepository faecher;

    @Autowired
    private LebensmittelRepository lebensmittelRepository;

    @RequestMapping(value = "/schrank/{id}/{fachID}/newLM",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity newLM(HttpServletRequest request, @PathVariable int id, @PathVariable int fachID, @RequestParam String name, @RequestParam String Anzahl, @RequestParam Long haltbarkeit, @RequestParam Long eingelagert){
        String user = request.getRemoteUser();
        Benutzer benutzer = users.findById(user).isPresent()? users.findById(user).get() : null;
        if(benutzer != null) {
            Kuehlschrank schrank = kuehlschrankRepository.findById(id).isPresent()? kuehlschrankRepository.findById(id).get() : null;
            if(schrank!=null && schrank.getBesitzer().equals(benutzer)){
                Fach fach = faecher.findById(fachID).orElse(null);
                if(fach!=null && fach.getKuehlschrank().equals(schrank)) {
                    Lebensmittel lebensmittel = new Lebensmittel(name, Anzahl, haltbarkeit, fach, benutzer, eingelagert);
                    lebensmittelRepository.save(lebensmittel);
                    return new ResponseEntity<>(lebensmittel.getNummer(),HttpStatus.CREATED);
                }
            }
        }
        ErrorClass error = new ErrorClass("Unauthorized!");
        return new ResponseEntity<ErrorClass>(error,HttpStatus.UNAUTHORIZED);
    }
    @RequestMapping(value = "/schrank/{id}/{fachID}/getAll",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getLM(HttpServletRequest request, @PathVariable int id, @PathVariable int fachID){
        String user = request.getRemoteUser();
        Benutzer benutzer = users.findById(user).isPresent()? users.findById(user).get() : null;
        if(benutzer != null) {
            Kuehlschrank schrank = kuehlschrankRepository.findById(id).isPresent()? kuehlschrankRepository.findById(id).get() : null;
            if(schrank!=null && schrank.getBesitzer().equals(benutzer)){
                Fach fach = faecher.findById(fachID).orElse(null);
                if(fach!=null && fach.getKuehlschrank().equals(schrank)) {
                    List<Lebensmittel> lm = Lebensmittel.getAll(lebensmittelRepository, fach);
                    return new ResponseEntity<>(lm,HttpStatus.OK);
                }
            }
        }
        ErrorClass error = new ErrorClass("Unauthorized!");
        return new ResponseEntity<ErrorClass>(error,HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "/schrank/{id}/{fachID}/{lebensmittelID}/delete",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity deleteLM(HttpServletRequest request, @PathVariable int id, @PathVariable int fachID, @PathVariable int lebensmittelID){
        String user = request.getRemoteUser();
        Benutzer benutzer = users.findById(user).isPresent()? users.findById(user).get() : null;
        if(benutzer != null) {
            Kuehlschrank schrank = kuehlschrankRepository.findById(id).isPresent()? kuehlschrankRepository.findById(id).get() : null;
            if(schrank!=null && schrank.getBesitzer().equals(benutzer)){
                Fach fach = faecher.findById(fachID).orElse(null);
                if(fach!=null && fach.getKuehlschrank().equals(schrank)) {
                    Lebensmittel lm = lebensmittelRepository.findById(lebensmittelID).isPresent() ? lebensmittelRepository.findById(lebensmittelID).get() : null;
                    if(lm != null){
                        lebensmittelRepository.delete(lm);
                    }
                    return new ResponseEntity<>("",HttpStatus.ACCEPTED);
                }
            }
        }
        ErrorClass error = new ErrorClass("Unauthorized!");
        return new ResponseEntity<ErrorClass>(error,HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "/schrank/{id}/{fachID}/{lebensmittelID}/update",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity updateLM(HttpServletRequest request, @PathVariable int id, @PathVariable int fachID, @PathVariable int lebensmittelID, @RequestParam String name, @RequestParam String Anzahl, @RequestParam Long haltbarkeit, @RequestParam Long eingelagert){
        String user = request.getRemoteUser();
        Benutzer benutzer = users.findById(user).isPresent()? users.findById(user).get() : null;
        if(benutzer != null) {
            Kuehlschrank schrank = kuehlschrankRepository.findById(id).isPresent()? kuehlschrankRepository.findById(id).get() : null;
            if(schrank!=null && schrank.getBesitzer().equals(benutzer)){
                Fach fach = faecher.findById(fachID).orElse(null);
                if(fach!=null && fach.getKuehlschrank().equals(schrank)) {
                    Lebensmittel lm = lebensmittelRepository.findById(lebensmittelID).isPresent() ? lebensmittelRepository.findById(lebensmittelID).get() : null;
                    if(lm != null){
                        lm.setName(name);
                        lm.setAnzahl(Anzahl);
                        lm.setHaltbarkeitsdatum(haltbarkeit);
                        lm.setEingelagert(eingelagert);
                        lebensmittelRepository.save(lm);
                    }
                    return new ResponseEntity<>("",HttpStatus.ACCEPTED);
                }
            }
        }
        ErrorClass error = new ErrorClass("Unauthorized!");
        return new ResponseEntity<ErrorClass>(error,HttpStatus.UNAUTHORIZED);
    }
}
