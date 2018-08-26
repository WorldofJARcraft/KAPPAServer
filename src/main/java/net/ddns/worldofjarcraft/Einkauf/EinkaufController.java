package net.ddns.worldofjarcraft.Einkauf;

import net.ddns.worldofjarcraft.Application;
import net.ddns.worldofjarcraft.DatabaseRepresentation.Benutzer;
import net.ddns.worldofjarcraft.DatabaseRepresentation.Einkauf;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

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
            return new ResponseEntity(new Einkauf[1],HttpStatus.NO_CONTENT);
    }
    @RequestMapping(value = "/einkauf/create",method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity addEinkauf(HttpServletRequest request, @RequestParam(value = "Einkauf") String einkauf){
        String user = request.getRemoteUser();
        System.out.println("Request by "+user);
        int num = Application.getTemplate().update(
                "INSERT INTO `einkauf` (`Num`, `Lebensmittel`, `Benutzer`) VALUES (NULL, ?, ?);", einkauf,Benutzer.getBenutzer(request.getRemoteUser()).getEMail());
            return new ResponseEntity(num,HttpStatus.CREATED);
    }
}
