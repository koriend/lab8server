package serverPath;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

public class Message {
    private Socket socket;
    private Logger log = Logger.getLogger(this.getClass().getName());

    public Message(Socket socket){
        this.socket = socket;
    }

    public void sendMessage(String string) throws
            IOException {
        log.info("Отправляется сообщение: " + string + '\n');
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(string);
        oos.flush();
    }

    public String recirveMessage() throws
            IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        String string = (String) ois.readObject();
        log.info("Принято сообщение: " + string + '\n');
        ois.close();
        return string;
    }


}
