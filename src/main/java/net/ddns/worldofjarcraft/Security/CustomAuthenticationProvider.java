package net.ddns.worldofjarcraft.Security;

import net.ddns.worldofjarcraft.DatabaseRepresentation.Benutzer;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CustomAuthenticationProvider
        implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        boolean authenticated = false;
        for(Benutzer benutzer : Benutzer.getAll()){
            if(benutzer.getEMail().equals(name)&&benutzer.getPasswort().equals(password)){
                authenticated = true;
                break;
            }
        }
        if (authenticated) {

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
