package UserTesting;

import net.ddns.worldofjarcraft.Application;
import net.ddns.worldofjarcraft.DatabaseRepresentation.Benutzer;
import net.ddns.worldofjarcraft.DatabaseRepresentation.BenutzerRepository;
import net.ddns.worldofjarcraft.DatabaseRepresentation.SuccessClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = Application.class)
public class CreateDeleteTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private BenutzerRepository repository;
    private String createRandomString(int length){
        StringBuilder builder = new StringBuilder();
        Random rand = new Random();
        for(int i=0; i < length;i++){
            builder.append((char) (rand.nextInt(22)+97));
        }
        return builder.toString();
    }

    private Benutzer test;
    @Before
    public void createUser(){
        Random rand = new SecureRandom();
        test = new Benutzer(createRandomString(rand.nextInt(10)+2),createRandomString(rand.nextInt(10)));
        System.out.println("Testing with User "+test.toString());
    }
    public static ResponseEntity<Boolean> createClientWithCredentials( TestRestTemplate restTemplate, String user, String pw) {

        ResponseEntity<Boolean> responseEntity =
                restTemplate.getForEntity("/user/create?EMail="+user+"&Password="+pw,Boolean.class);

        return responseEntity;

    }
    @Test
    public void createClient() {
        ResponseEntity<Boolean> responseEntity = createClientWithCredentials(restTemplate, test.getEMail(),test.getPasswort());
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        Boolean result = responseEntity.getBody();
        assertEquals(true,result);
        updateClient();
        deleteClient();
    }

    private void updateClient(){
        String old_pw = test.getPasswort();
        test.setPasswort(createRandomString((new Random()).nextInt(10)));
        ResponseEntity<Benutzer> responseEntity =
                restTemplate.withBasicAuth(test.getEMail(),old_pw).getForEntity("/user/changePassword?newPassword="+test.getPasswort(),Benutzer.class);
        Benutzer client = responseEntity.getBody();
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        assertEquals(test.getEMail(), client.getEMail());
        assertEquals(test.getPasswort(),client.getPasswort());
        assertTrue(repository.findById(test.getEMail()).isPresent());
    }

    private void deleteClient() {
        ResponseEntity<SuccessClass> responseEntity =
                restTemplate.withBasicAuth(test.getEMail(),test.getPasswort()).getForEntity("/user/delete",SuccessClass.class);
        SuccessClass client = responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Deleted!", client.getSuccess_message());
        assertFalse(repository.findById(test.getEMail()).isPresent());
    }
}