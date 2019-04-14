package net.ddns.worldofjarcraft.Security;

import java.util.Arrays;
import java.util.List;

import net.ddns.worldofjarcraft.DatabaseRepresentation.Benutzer;
import net.ddns.worldofjarcraft.DatabaseRepresentation.BenutzerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@Controller
public class UserController {

    @Autowired
    private  BenutzerRepository users;
    @RequestMapping(path = "/user", method = RequestMethod.GET)
    public ResponseEntity listUser(HttpServletRequest request) {
        System.out.println("Request by "+request.getUserPrincipal().getName());
        return new ResponseEntity  (Benutzer.getAll(users),HttpStatus.OK);
    }

    @RequestMapping(path = "/user/{mail}", method = RequestMethod.GET)
    public ResponseEntity listUser(@PathVariable(value = "mail") String mail) {
        Benutzer b = Benutzer.getAll(users).stream().filter(user -> user.getEMail().equals(mail)).findFirst().orElse(null);
        return new ResponseEntity(b != null, HttpStatus.OK);

    }

    @RequestMapping(path = "/user", method = RequestMethod.POST)
    public ResponseEntity listUser(@RequestBody Benutzer user) {
        return new ResponseEntity("18", HttpStatus.OK);
    }

    @RequestMapping(path="/logmeout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        HttpSession session= request.getSession(false);
        if(session != null) {
            session.invalidate();
        }
        for(Cookie cookie : request.getCookies()) {
            cookie.setMaxAge(0);
        }
        return "redirect:/greeting";
    }
}