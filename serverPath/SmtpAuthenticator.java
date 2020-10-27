package serverPath;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class SmtpAuthenticator extends Authenticator {

    public SmtpAuthenticator(){
        super();
    }

    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        final String username = "white.crolick";
        final String password = "rfnz2000";
        if ((username != null) && (username.length() > 0) && (password != null)
                && (password.length   () > 0)) {

            return new PasswordAuthentication(username, password);
        }

        return null;
    }
}
