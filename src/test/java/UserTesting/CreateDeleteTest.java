package UserTesting;

import net.ddns.worldofjarcraft.Application;
import net.ddns.worldofjarcraft.DatabaseRepresentation.Benutzer;
import net.ddns.worldofjarcraft.DatabaseRepresentation.SuccessClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Random;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = Application.class)
public class CreateDeleteTest {
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
        Random rand = new SecureRandom();
        test = new Benutzer(createRandomString(rand.nextInt(10)+2),createRandomString(rand.nextInt(10)));
        System.out.println("Testing with User "+test.toString());
    }

    @Test
    public void createClient() {

        ResponseEntity<Benutzer> responseEntity =
                restTemplate.getForEntity("/user/create?EMail="+test.getEMail()+"&Password="+test.getPasswort(),Benutzer.class);
        Benutzer client = responseEntity.getBody();
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(test.getEMail(), client.getEMail());
        assertEquals(test.getPasswort(),client.getPasswort());
        deleteClient();
    }
    private void deleteClient() {
        ResponseEntity<SuccessClass> responseEntity =
                restTemplate.withBasicAuth(test.getEMail(),test.getPasswort()).getForEntity("/user/delete",SuccessClass.class);
        SuccessClass client = responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Deleted!", client.getSuccess_message());
    }
}