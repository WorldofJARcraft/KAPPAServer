package net.ddns.worldofjarcraft.DatabaseRepresentation;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.io.IOException;
import java.security.SecureRandom;

@Entity
@NoArgsConstructor
@Getter
@Setter(value = AccessLevel.PRIVATE)
@Log4j2
public class PasswordResetRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String password;

    @Column
    private int nonce;

    @Column
    private long time;

    private static SecureRandom random = new SecureRandom();

    @ManyToOne
    @JoinColumn(name = "Besitzer")
    private Benutzer Besitzer;
    @Transactional
    public void createAndNotifyUser(Benutzer target, String newPassword, PasswordResetRepository repository){
        final int nonce = random.nextInt();
        this.setNonce(nonce);
        this.setBesitzer(target);
        this.setPassword(newPassword);
        this.setTime(System.currentTimeMillis());
        //send E-Mail to user...

        Email from = new Email("noreply@kappa-server-eu.herokuapp.com");
        String subject = "KAPPA Passwort-Reset";
        Email to = new Email(target.getEMail());
        Content content = new Content("text/HTML", "Um das Passwort zur&uuml;ckzusetzen, bitte folgenden Link in einem Browser &ouml;ffnen:  \n<a href=\"https://kappa-server-eu.herokuapp.com/user/doReset?email="+target.getEMail()+"&nonce="+nonce+"\">https://kappa-server-eu.herokuapp.com/user/doReset?email="+target.getEMail()+"&nonce="+nonce+"</a>");
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            log.info("Sendgrid response code: "+response.getStatusCode());
            log.info("Sendgrid response: "+response.getBody());
            log.info("Sendgrid headers: "+response.getHeaders());
        } catch (IOException ex) {
            log.error(ex);
        }
        repository.save(this);
    }
}
