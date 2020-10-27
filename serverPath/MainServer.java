package serverPath;

public class MainServer {
    public static void main(String[] args) {

        Connection connection = new Connection(8989);
        connection.connect();
        connection.close();

    }

}
