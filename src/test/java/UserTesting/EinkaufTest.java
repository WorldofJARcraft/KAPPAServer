package UserTesting;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.ddns.worldofjarcraft.Application;
import net.ddns.worldofjarcraft.DatabaseRepresentation.Benutzer;
import net.ddns.worldofjarcraft.DatabaseRepresentation.Einkauf;
import net.ddns.worldofjarcraft.DatabaseRepresentation.EinkaufRepository;
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = Application.class)
public class EinkaufTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private EinkaufRepository repository;
    private String createRandomString(int length){
        StringBuilder builder = new StringBuilder();
        Random random = new SecureRandom();
        for(int i=0;i<length;i++){
            builder.append((char)(random.nextInt(61)+65));
        }
        return builder.toString();
    }

    private Benutzer test;
    private int id;

    /**
     * Generates random String.
     * @param length lengtrh of String
     * @return String
     */
    private String randomString(int length){
        StringBuilder builder = new StringBuilder();
        Random rand = new Random();
            for(int i=0; i < length;i++){
                builder.append((char) (rand.nextInt(22)+97));
            }
        return builder.toString();
    }
    @Before
    public void createUser(){
        test = new Benutzer(randomString(20),randomString(10));
        System.out.println("Testing with User "+test.toString());
        CreateDeleteTest.createClientWithCredentials(restTemplate, test.getEMail(), test.getPasswort());
    }


    private void showEinkaeufe() {

        ResponseEntity<Einkauf[]> responseEntity =
                restTemplate.withBasicAuth(test.getEMail(),test.getPasswort()).getForEntity("/einkauf",Einkauf[].class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Einkauf[] einkaufs = responseEntity.getBody();
    }

    private void addEinkauf(){
        ResponseEntity<Integer> responseEntity =
                restTemplate.withBasicAuth(test.getEMail(),test.getPasswort()).getForEntity("/einkauf/create?Einkauf=EinEinkauf",Integer.class);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        //save ID for later tests
        id = responseEntity.getBody();
    }

    private void updateEinkauf(){
        ResponseEntity<Integer> responseEntity =
                restTemplate.withBasicAuth(test.getEMail(),test.getPasswort()).getForEntity("/einkauf/"+id+"/update?newValue=Hallo",Integer.class);
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        assertNotEquals(responseEntity.getBody(),new Integer(0));
        assertEquals(repository.findById(id).get().getLebensmittel(),"Hallo");
    }
    private void deleteEinkauf(){
        ResponseEntity<Integer> responseEntity =
                restTemplate.withBasicAuth(test.getEMail(),test.getPasswort()).getForEntity("/einkauf/"+id+"/delete",Integer.class);
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        assertFalse(repository.findById(id).isPresent());
    }

    @Test
    public void runTest(){
        addEinkauf();
        showEinkaeufe();
        updateEinkauf();
        deleteEinkauf();
    }
}
