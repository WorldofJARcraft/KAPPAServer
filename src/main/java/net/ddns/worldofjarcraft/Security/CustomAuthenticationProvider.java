package net.ddns.worldofjarcraft.Security;

import lombok.extern.log4j.Log4j2;
import net.ddns.worldofjarcraft.DatabaseRepresentation.Benutzer;
import net.ddns.worldofjarcraft.DatabaseRepresentation.BenutzerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Log4j2
public class CustomAuthenticationProvider
        implements AuthenticationProvider {

    @Autowired
    private BenutzerRepository users;
    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        Benutzer b = Benutzer.getBenutzer(users,name);
        log.info("Log in: "+name+"found: "+(b!=null));
        if (b!=null && b.getPasswort().equals(password)) {

            // use the credentials
            // and authenticate against the third-party system
            return new UsernamePasswordAuthenticationToken(
                    name, password, new ArrayList<>());
        } else {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(
                UsernamePasswordAuthenticationToken.class);
    }
}
