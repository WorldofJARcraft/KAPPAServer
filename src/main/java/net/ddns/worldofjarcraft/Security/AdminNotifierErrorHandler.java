package net.ddns.worldofjarcraft.Security;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import net.ddns.worldofjarcraft.DatabaseRepresentation.ErrorClass;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

@ControllerAdvice
@Log4j2
public class AdminNotifierErrorHandler {
    @Value("${admin.email}")
    private static String adminEmail;
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ErrorClass> resourceNotFoundException(Exception ex, WebRequest req) {
        handleException(ex, req.getUserPrincipal() == null ? "unknown" : req.getUserPrincipal().getName());
        val error = new ErrorClass(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static void handleException(Exception ex, String user){
        val emailToAdmin = new StringBuilder();
        emailToAdmin.append("KAPPA encountered the following error(s) for user ");
        emailToAdmin.append(user);
        emailToAdmin.append(" :\n");
        val sw = new StringWriter();
        val pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        emailToAdmin.append(sw.toString());

        Email from = new Email("noreply@kappa-server-eu.herokuapp.com");
        String subject = "[FIXME] KAPPA: "+ex.getMessage();
        Email to = new Email(adminEmail);
        Content content = new Content("text/plain", emailToAdmin.toString());
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
        log.info("Using sendgrid host "+sg.getHost());
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            log.info("Sendgrid response code: "+response.getStatusCode());
            log.info("Sendgrid response: "+response.getBody());
            log.info("Sendgrid headers: "+response.getHeaders());
        } catch (IOException exception) {
            log.error(exception);
        }
    }
}
