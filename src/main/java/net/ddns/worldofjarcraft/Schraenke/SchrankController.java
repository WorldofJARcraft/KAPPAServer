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
public class SchrankController {
    @Autowired
    private BenutzerRepository users;

    @Autowired
    private KuehlschrankRepository schraenke;
    @RequestMapping(value = "/schrank/getAll",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getSchraenke(HttpServletRequest request){
        String user = request.getRemoteUser();
        Benutzer benutzer = users.findById(user).isPresent()? users.findById(user).get() : null;
        if(benutzer != null) {
            List<Kuehlschrank> kuehlschranks = Kuehlschrank.getAll(schraenke,benutzer);
            return new ResponseEntity<List<Kuehlschrank>>(kuehlschranks,HttpStatus.OK);
        }
        ErrorClass error = new ErrorClass("Unauthorized!");
        return new ResponseEntity<ErrorClass>(error,HttpStatus.UNAUTHORIZED);
    }
    @RequestMapping(value = "/schrank/new",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity addKuehlschrank(HttpServletRequest request, @Param(value = "name") String name, @Param(value = "faecher") int faecher){
        String user = request.getRemoteUser();
        Benutzer benutzer = users.findById(user).isPresent()? users.findById(user).get() : null;
        if(benutzer != null) {
            Kuehlschrank schrank = new Kuehlschrank(name, faecher,benutzer);
            schraenke.save(schrank);
            return new ResponseEntity<Number>(schrank.getLaufNummer(),HttpStatus.CREATED);
        }
        ErrorClass error = new ErrorClass("Unauthorized!");
        return new ResponseEntity<ErrorClass>(error,HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "/schrank/{id}/delete",method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity deleteSchrank(HttpServletRequest request, @PathVariable int id){
        String user = request.getRemoteUser();
        Benutzer benutzer = users.findById(user).isPresent()? users.findById(user).get() : null;
        if(benutzer != null) {
            Kuehlschrank schrank = schraenke.findById(id).isPresent()? schraenke.findById(id).get() : null;
            if(schrank != null){
                schraenke.delete(schrank);
            }
            return new ResponseEntity<String>("",HttpStatus.ACCEPTED);
        }
        ErrorClass error = new ErrorClass("Unauthorized!");
        return new ResponseEntity<>(error,HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "/schrank/{id}/update",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity updateSchrank(HttpServletRequest request, @PathVariable int id, @Param(value = "name") String name, @Param(value = "faecher") int faecher){
        String user = request.getRemoteUser();
        Benutzer benutzer = users.findById(user).isPresent()? users.findById(user).get() : null;
        if(benutzer != null) {
            Kuehlschrank schrank = schraenke.findById(id).isPresent()? schraenke.findById(id).get() : null;
            if(schrank!=null) {
                schrank.setName(name);
                schrank.setZahl_Faecher(faecher);
                schraenke.save(schrank);
            }
            return new ResponseEntity<>("",HttpStatus.ACCEPTED);
        }
        ErrorClass error = new ErrorClass("Unauthorized!");
        return new ResponseEntity<ErrorClass>(error,HttpStatus.UNAUTHORIZED);
    }
    @RequestMapping(value = "/schrank/{id}/get",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getSchrank(HttpServletRequest request, @PathVariable int id, @Param(value = "name") String name, @Param(value = "faecher") int faecher){
        String user = request.getRemoteUser();
        Benutzer benutzer = users.findById(user).isPresent()? users.findById(user).get() : null;
        if(benutzer != null) {
            Kuehlschrank schrank = schraenke.findById(id).isPresent()? schraenke.findById(id).get() : null;
            if(schrank!=null) {
                return new ResponseEntity<>(schrank,HttpStatus.ACCEPTED);
            }
        }
        ErrorClass error = new ErrorClass("Unauthorized!");
        return new ResponseEntity<ErrorClass>(error,HttpStatus.UNAUTHORIZED);
    }
}
