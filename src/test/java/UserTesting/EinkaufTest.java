package UserTesting;

import net.ddns.worldofjarcraft.Application;
import net.ddns.worldofjarcraft.DatabaseRepresentation.Benutzer;
import net.ddns.worldofjarcraft.DatabaseRepresentation.Einkauf;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = Application.class)
public class EinkaufTest {
    @Autowired
    private TestRestTemplate restTemplate;

    private String createRandomString(int length){
        StringBuilder builder = new StringBuilder();
        Random random = new SecureRandom();
        for(int i=0;i<length;i++){
            builder.append((char)(random.nextInt(61)+65));
        }
        return builder.toString();
    }

    private Benutzer test;
    @Before
    public void createUser(){
        test = new Benutzer("UndNochEinTestUser","Test");
        System.out.println("Testing with User "+test.toString());
        BasicAuthorizationInterceptor bai = new BasicAuthorizationInterceptor(test.getEMail(), test.getPasswort());
        restTemplate.getRestTemplate().getInterceptors().add(bai);
    }

    @Test
    public void showEinkaeufe() {

        ResponseEntity<Einkauf[]> responseEntity =
                restTemplate.withBasicAuth(test.getEMail(),test.getPasswort()).getForEntity("/einkauf",Einkauf[].class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Einkauf[] einkaufs = responseEntity.getBody();
    }

    @Test
    public void addEinkauf(){
        ResponseEntity<Integer> responseEntity =
                restTemplate.withBasicAuth(test.getEMail(),test.getPasswort()).postForEntity("/einkauf/create",null,Integer.class,new HashMap<String, String>().put("Einkauf","Kaufe ein"));
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

}
