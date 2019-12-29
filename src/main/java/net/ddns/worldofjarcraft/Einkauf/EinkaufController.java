package net.ddns.worldofjarcraft.Einkauf;

import net.ddns.worldofjarcraft.Application;
import net.ddns.worldofjarcraft.DatabaseRepresentation.Benutzer;
import net.ddns.worldofjarcraft.DatabaseRepresentation.BenutzerRepository;
import net.ddns.worldofjarcraft.DatabaseRepresentation.Einkauf;
import net.ddns.worldofjarcraft.DatabaseRepresentation.EinkaufRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class EinkaufController {
    @Autowired
    private EinkaufRepository repo;

    @Autowired
    private BenutzerRepository users;

    @GetMapping("/einkauf")
    @ResponseBody
    ResponseEntity showEinkaeufe(HttpServletRequest request){
        String user = request.getRemoteUser();
        System.out.println("Request by "+user);
        List<Einkauf> einkaufs = new ArrayList<>();
        for(Einkauf einkauf : repo.findAllByOrderByLebensmittel()){
            //remove orphans
            if(einkauf.getNutzer()==null){
                repo.delete(einkauf);
            }
            else if(einkauf.getNutzer().getEMail().equals(user)){
                einkaufs.add(einkauf);
            }
        }
        if(!einkaufs.isEmpty()) return new ResponseEntity<>(einkaufs.toArray(new Einkauf[1]),HttpStatus.OK);
        else
            return new ResponseEntity<>(new Einkauf[1],HttpStatus.NO_CONTENT);
    }
    @RequestMapping(value = "/einkauf/create",method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity addEinkauf(HttpServletRequest request, @RequestParam(value = "Einkauf") String einkauf){
        String user = request.getRemoteUser();
        Benutzer benutzer = users.findById(user).isPresent() ? users.findById(user).get() : null;
        if(benutzer != null) {
            System.out.println("Request by " + user);
            Einkauf newEinkauf = new Einkauf(einkauf,benutzer);
            repo.save(newEinkauf);
            Number key = newEinkauf.getId();
            // convert Number to Int using ((Number) key).intValue()
            return new ResponseEntity<String>("" + key, HttpStatus.CREATED);
        }
        return new ResponseEntity<String>("", HttpStatus.UNAUTHORIZED);
    }
    @RequestMapping(value = "/einkauf/{id}/delete",method = RequestMethod.DELETE)
    @ResponseBody
    ResponseEntity delete(HttpServletRequest request, @PathVariable int id){
        String user = request.getRemoteUser();
        Einkauf einkauf = repo.findById(id).isPresent() ?  repo.findById(id).get() : null;
        if(einkauf!=null){
            if(einkauf.getNutzer().getEMail().equals(user)){
                repo.delete(einkauf);
                return new ResponseEntity<>(true,HttpStatus.ACCEPTED);
            }
        }
        return new ResponseEntity<>(false,HttpStatus.NOT_FOUND);
    }
    @RequestMapping(value = "/einkauf/{id}/update",method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity update(HttpServletRequest request, @PathVariable int id, @RequestParam(name="newValue", required = true) String newValue){
        String user = request.getRemoteUser();
        System.out.println("Request by "+user);
        Einkauf einkauf = repo.findById(id).isPresent() ?  repo.findById(id).get() : null;
        if(einkauf!=null){
            if(einkauf.getNutzer().getEMail().equals(user)){
                einkauf.setLebensmittel(newValue);
                repo.save(einkauf);
                return new ResponseEntity<Number>(einkauf.getId(),HttpStatus.ACCEPTED);
            }
        }
        return new ResponseEntity<String>("",HttpStatus.NOT_FOUND);
    }
    @RequestMapping(value = "/einkauf/{id}/get",method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity getEinkauf(HttpServletRequest request, @PathVariable int id){
        String user = request.getRemoteUser();
        System.out.println("Request by "+user);
        Einkauf einkauf = repo.findById(id).isPresent() ?  repo.findById(id).get() : null;
        if(einkauf!=null){
            if(einkauf.getNutzer().getEMail().equals(user)){
                return new ResponseEntity<>(einkauf,HttpStatus.ACCEPTED);
            }
        }
        return new ResponseEntity<String>("",HttpStatus.NOT_FOUND);
    }
}
