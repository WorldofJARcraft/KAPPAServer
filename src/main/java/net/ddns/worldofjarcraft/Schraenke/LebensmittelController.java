package net.ddns.worldofjarcraft.Schraenke;

import net.ddns.worldofjarcraft.DatabaseRepresentation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

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

    @RequestMapping(value = "/schrank/{id}/{fachID}/newLM", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity newLM(HttpServletRequest request, @PathVariable int id, @PathVariable int fachID, @RequestParam String name, @RequestParam String Anzahl, @RequestParam Long haltbarkeit, @RequestParam Long eingelagert) {
        String user = request.getRemoteUser();
        Benutzer benutzer = users.findById(user).isPresent() ? users.findById(user).get() : null;
        if (benutzer != null) {
            Kuehlschrank schrank = kuehlschrankRepository.findById(id).isPresent() ? kuehlschrankRepository.findById(id).get() : null;
            if (schrank != null && schrank.getBesitzer().equals(benutzer)) {
                Fach fach = faecher.findById(fachID).orElse(null);
                if (fach != null && fach.getKuehlschrank().equals(schrank)) {
                    Lebensmittel lebensmittel = new Lebensmittel(name, Anzahl, haltbarkeit, fach, benutzer, eingelagert);
                    lebensmittelRepository.save(lebensmittel);
                    return new ResponseEntity<>(lebensmittel.getNummer(), HttpStatus.CREATED);
                }
            }
        }
        ErrorClass error = new ErrorClass("Unauthorized!");
        return new ResponseEntity<ErrorClass>(error, HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "/schrank/{id}/{fachID}/getAll", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getLM(HttpServletRequest request, @PathVariable int id, @PathVariable int fachID) {
        String user = request.getRemoteUser();
        Benutzer benutzer = users.findById(user).isPresent() ? users.findById(user).get() : null;
        if (benutzer != null) {
            Kuehlschrank schrank = kuehlschrankRepository.findById(id).isPresent() ? kuehlschrankRepository.findById(id).get() : null;
            if (schrank != null && schrank.getBesitzer().equals(benutzer)) {
                Fach fach = faecher.findById(fachID).orElse(null);
                if (fach != null && fach.getKuehlschrank().equals(schrank)) {
                    List<Lebensmittel> lm = Lebensmittel.getAll(lebensmittelRepository, fach);
                    lm = lm.stream().sorted(new StringComparator()).collect(Collectors.toList());
                    return new ResponseEntity<>(lm, HttpStatus.OK);
                }
            }
        }
        ErrorClass error = new ErrorClass("Unauthorized!");
        return new ResponseEntity<ErrorClass>(error, HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "/schrank/{id}/{fachID}/{lebensmittelID}/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity deleteLM(HttpServletRequest request, @PathVariable int id, @PathVariable int fachID, @PathVariable int lebensmittelID) {
        String user = request.getRemoteUser();
        Benutzer benutzer = users.findById(user).isPresent() ? users.findById(user).get() : null;
        if (benutzer != null) {
            Kuehlschrank schrank = kuehlschrankRepository.findById(id).isPresent() ? kuehlschrankRepository.findById(id).get() : null;
            if (schrank != null && schrank.getBesitzer().equals(benutzer)) {
                Fach fach = faecher.findById(fachID).orElse(null);
                if (fach != null && fach.getKuehlschrank().equals(schrank)) {
                    Lebensmittel lm = lebensmittelRepository.findById(lebensmittelID).isPresent() ? lebensmittelRepository.findById(lebensmittelID).get() : null;
                    if (lm != null) {
                        lebensmittelRepository.delete(lm);
                    }
                    return new ResponseEntity<>("", HttpStatus.ACCEPTED);
                }
            }
        }
        ErrorClass error = new ErrorClass("Unauthorized!");
        return new ResponseEntity<ErrorClass>(error, HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "/schrank/{id}/{fachID}/{lebensmittelID}/update", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity updateLM(HttpServletRequest request, @PathVariable int id, @PathVariable int fachID, @PathVariable int lebensmittelID, @RequestParam String name, @RequestParam String Anzahl, @RequestParam Long haltbarkeit, @RequestParam Long eingelagert) {
        String user = request.getRemoteUser();
        Benutzer benutzer = users.findById(user).isPresent() ? users.findById(user).get() : null;
        if (benutzer != null) {
            Kuehlschrank schrank = kuehlschrankRepository.findById(id).isPresent() ? kuehlschrankRepository.findById(id).get() : null;
            if (schrank != null && schrank.getBesitzer().equals(benutzer)) {
                Fach fach = faecher.findById(fachID).orElse(null);
                if (fach != null && fach.getKuehlschrank().equals(schrank)) {
                    Lebensmittel lm = lebensmittelRepository.findById(lebensmittelID).isPresent() ? lebensmittelRepository.findById(lebensmittelID).get() : null;
                    if (lm != null) {
                        lm.setName(name);
                        lm.setAnzahl(Anzahl);
                        lm.setHaltbarkeitsdatum(haltbarkeit);
                        lm.setEingelagert(eingelagert);
                        lebensmittelRepository.save(lm);
                    }
                    return new ResponseEntity<>("", HttpStatus.ACCEPTED);
                }
            }
        }
        ErrorClass error = new ErrorClass("Unauthorized!");
        return new ResponseEntity<ErrorClass>(error, HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity searchLM(HttpServletRequest request, @RequestParam String query) {
        String user = request.getRemoteUser();
        Benutzer benutzer = users.findById(user).isPresent() ? users.findById(user).get() : null;
        if (benutzer != null) {
            List<Lebensmittel> results = new LinkedList<>();

            for (Lebensmittel lm : lebensmittelRepository.findAll()) {
                try {
                    String queryDecoded = query;
                    if(query.startsWith("Base64:")){
                     queryDecoded = new String(Base64.getMimeDecoder().decode(query.split("Base64:")[1]), "utf-8");
                    }
                    if (lm.getName().startsWith("Base64:")) {
                        String lm_decoded = lm.getName().split("Base64:")[1];
                        lm_decoded = new String(Base64.getMimeDecoder().decode(lm_decoded), "utf-8");
                        if (lm_decoded.contains(queryDecoded) && lm.getBesitzer().equals(benutzer)) {
                            results.add(lm);
                        }
                    } else if(lm.getName().contains(query) && lm.getBesitzer().equals(benutzer)){
                        results.add(lm);
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
            results = results.stream().sorted(new StringComparator()).collect(Collectors.toList());
            return new ResponseEntity<>(results, HttpStatus.OK);
        }
        ErrorClass error = new ErrorClass("Unauthorized!");
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "/lebensmittel/getAll", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getAllLM(HttpServletRequest request) {
        String user = request.getRemoteUser();
        Benutzer benutzer = users.findById(user).isPresent() ? users.findById(user).get() : null;
        if (benutzer != null) {
            List<Lebensmittel> results = new LinkedList<>();

            for (Lebensmittel lm : lebensmittelRepository.findAll()) {
                if(lm.getBesitzer().equals(benutzer)){
                    results.add(lm);
                }

            }
            results = results.stream().sorted(new StringComparator()).collect(Collectors.toList());
            return new ResponseEntity<>(results, HttpStatus.OK);
        }
        ErrorClass error = new ErrorClass("Unauthorized!");
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    private class StringComparator  implements Comparator<Lebensmittel> {
        public int compare(Lebensmittel obj1, Lebensmittel obj2) {
            if (obj1 == obj2) {
                return 0;
            }
            if (obj1 == null) {
                return -1;
            }
            if (obj2 == null) {
                return 1;
            }
            if (obj1.getName() == obj2.getName()) {
                return 0;
            }
            if (obj1.getName() == null) {
                return -1;
            }
            if (obj2.getName() == null) {
                return 1;
            }
            return obj1.getName().compareTo(obj2.getName());
        }
    }
}
