package net.ddns.worldofjarcraft.Einkauf;

import net.ddns.worldofjarcraft.Application;
import net.ddns.worldofjarcraft.DatabaseRepresentation.Benutzer;
import net.ddns.worldofjarcraft.DatabaseRepresentation.Einkauf;
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
    @GetMapping("/einkauf")
    @ResponseBody
    ResponseEntity showEinkaeufe(HttpServletRequest request){
        String user = request.getRemoteUser();
        System.out.println("Request by "+user);
        List<Einkauf> einkaufs = new ArrayList<>(Application.getTemplate().query(
                "SELECT * FROM einkauf WHERE Benutzer = ?;", new Object[]{user},
                (rs, RowNum) -> new Einkauf(rs.getString("Lebensmittel"),Benutzer.getBenutzer(rs.getString("Benutzer")),rs.getInt("Num"))
        ));
        if(!einkaufs.isEmpty()) return new ResponseEntity<>(einkaufs.toArray(new Einkauf[1]),HttpStatus.OK);
        else
            return new ResponseEntity<>(new Einkauf[1],HttpStatus.NO_CONTENT);
    }
    @RequestMapping(value = "/einkauf/create",method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity addEinkauf(HttpServletRequest request, @RequestParam(value = "Einkauf") String einkauf){
        String user = request.getRemoteUser();
        System.out.println("Request by "+user);
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(Application.getTemplate());
        jdbcInsert.withTableName("einkauf").usingGeneratedKeyColumns(
                "Num");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("Lebensmittel", einkauf);
        parameters.put("Benutzer", user);
        // execute insert
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(
                parameters));
        // convert Number to Int using ((Number) key).intValue()
            return new ResponseEntity<String>(""+key,HttpStatus.CREATED);
    }
    @RequestMapping(value = "/einkauf/{id}/delete",method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity delete(HttpServletRequest request, @PathVariable int id){
        String user = request.getRemoteUser();
        System.out.println("Request by "+user);
        int num = Application.getTemplate().update(
                "DELETE FROM `Einkauf` WHERE `Einkauf`.`Num` = ?", id);
        return new ResponseEntity<Number>(num,HttpStatus.ACCEPTED);
    }
    @RequestMapping(value = "/einkauf/{id}/update",method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity update(HttpServletRequest request, @PathVariable int id, @RequestParam(name="newValue", required = true) String newValue){
        String user = request.getRemoteUser();
        System.out.println("Request by "+user);
        int num = Application.getTemplate().update(
                "UPDATE `Einkauf` SET `Lebensmittel` = ? WHERE `Einkauf`.`Num` = ?;", newValue, id);
        return new ResponseEntity<Number>(num,HttpStatus.ACCEPTED);
    }
}
