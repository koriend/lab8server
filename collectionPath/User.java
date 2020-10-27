package collectionPath;

public class User {
    private String login;
    private String password;
    private String mail;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
        return;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    @Override
    public String toString() {
        return "User { login: " + getLogin() + ", password: " + getPassword() + ", mail: " + getMail() + " }";
    }
}
