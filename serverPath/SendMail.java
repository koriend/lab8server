package serverPath;

import com.sun.mail.util.MailConnectException;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

public class SendMail {
    private final String username = "white.crolick";

    public SendMail(final String email, String password) throws MailConnectException {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "25");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.socketFactory.port", "25");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.starttls.enable","true");
        SmtpAuthenticator authenticator = new SmtpAuthenticator();
        Session session = Session.getDefaultInstance(properties, authenticator);
        session.setDebug(false);

        try{
            InternetAddress email_from = new InternetAddress(username, "Password_lab");
            InternetAddress email_to = new InternetAddress(email);

            javax.mail.Message message = new MimeMessage(session);
            message.setFrom(email_from);
            message.setRecipient(javax.mail.Message.RecipientType.TO, email_to);

            message.setSubject("Password");
            message.setText(password);

            Transport.send(message);
        } catch (MessagingException | UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }
}
