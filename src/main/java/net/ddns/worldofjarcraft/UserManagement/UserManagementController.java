package net.ddns.worldofjarcraft.UserManagement;

import net.ddns.worldofjarcraft.DatabaseRepresentation.Benutzer;
import net.ddns.worldofjarcraft.DatabaseRepresentation.BenutzerRepository;
import net.ddns.worldofjarcraft.DatabaseRepresentation.ErrorClass;
import net.ddns.worldofjarcraft.DatabaseRepresentation.SuccessClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class UserManagementController {
    private final String INSERT_SQL = "INSERT INTO benutzer(EMail, Passwort) values (?,?);";
    private final String DELETE_SQL = "DELETE FROM benutzer WHERE EMail=?;";

    @Autowired
    private BenutzerRepository users;
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
}
