package net.ddns.worldofjarcraft.UserManagement;

import lombok.extern.log4j.Log4j2;
import lombok.val;
import net.ddns.worldofjarcraft.DatabaseRepresentation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
@Log4j2
public class UserManagementController {
    private final String INSERT_SQL = "INSERT INTO benutzer(EMail, Passwort) values (?,?);";
    private final String DELETE_SQL = "DELETE FROM benutzer WHERE EMail=?;";

    @Autowired
    private BenutzerRepository users;

    @Autowired
    private PasswordResetRepository resetRepository;
    @Autowired
    JdbcTemplate template;
    @RequestMapping("/user/create")
    public ResponseEntity createUser(@RequestParam(value = "EMail", required = true) String EMail, @RequestParam(value = "Password", required = true) String Password){
        if(EMail.startsWith("Base64:")){
            EMail = new String(Base64.getMimeDecoder().decode(EMail.split("Base64:")[1]), StandardCharsets.UTF_8);
        }
        if(Password.startsWith("Base64:")){
            Password = new String(Base64.getMimeDecoder().decode(Password.split("Base64:")[1]), StandardCharsets.UTF_8);
        }
        Benutzer b = new Benutzer(EMail,Password);
        if(Benutzer.getBenutzer(users,b.getEMail())==null){
            users.save(b);
            //System.out.println("Inserted user "+holder.getKey().intValue());
            return new ResponseEntity(true,HttpStatus.CREATED);
        }
        else {
            ErrorClass error = new ErrorClass("User exists!");
            return new ResponseEntity(false,HttpStatus.CONFLICT);
        }
    }
    @RequestMapping("/user/delete")
    public ResponseEntity deleteUser(HttpServletRequest request){
        Benutzer b = Benutzer.getBenutzer(users, request.getUserPrincipal().getName());
        if(b!=null){
            users.delete(b);
            return new ResponseEntity(new SuccessClass("Deleted!"),HttpStatus.OK);
        }
        else {
            return new ResponseEntity(new ErrorClass("User does not exist!"),HttpStatus.NOT_FOUND);
        }
    }
    @RequestMapping("/user/changePassword")
    public ResponseEntity changePassword(HttpServletRequest request, @RequestParam(name = "newPassword") String newPassword){
        Benutzer b = Benutzer.getBenutzer(users,request.getUserPrincipal().getName());
        if(b!=null){
            b.setPasswort(newPassword);
            users.save(b);
            return new ResponseEntity(Benutzer.getBenutzer(users,b.getEMail()),HttpStatus.ACCEPTED);
        }
        else {
            return new ResponseEntity(new ErrorClass("User does not exist!"),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/user/requestReset")
    public ResponseEntity<Object> requestPasswordChange(@RequestParam String email, @RequestParam String newPassword){
        val respectiveUser = Benutzer.getBenutzer(users, email);
        if(respectiveUser != null){
            try {
                new PasswordResetRequest().createAndNotifyUser(respectiveUser, newPassword, resetRepository);
            } catch (Exception e){
                log.error(e);
                return new ResponseEntity<>(new ErrorClass("Internal error"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>("", HttpStatus.OK);
        }
        return new ResponseEntity<>(new ErrorClass("User unknown"), HttpStatus.NOT_FOUND);
    }
    @GetMapping("/user/doReset")
    public ResponseEntity<Object> resetPassword(@RequestParam int nonce, @RequestParam String email){
        val respectiveUser = Benutzer.getBenutzer(users, email);
        if(respectiveUser != null){
            try {
                val resetRequest = resetRepository.getAllByBesitzerAndNonce(respectiveUser, nonce);
                PasswordResetRequest newest = null;
                for(val request : resetRequest){
                    if(newest == null || newest.getTime() < request.getTime()){
                        newest = request;
                    }
                }
                if(newest == null){
                    return new ResponseEntity<>(new ErrorClass("No reset request found..."), HttpStatus.NOT_FOUND);
                }
                else{
                    respectiveUser.setPasswort(newest.getPassword());
                    users.save(respectiveUser);

                    return new ResponseEntity<>("OK", HttpStatus.OK);
                }
            } catch (Exception e){
                log.error(e);
                return new ResponseEntity<>(new ErrorClass("Internal error"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(new ErrorClass("User unknown"), HttpStatus.NOT_FOUND);
    }
}
