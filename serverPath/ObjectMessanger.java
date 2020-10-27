package serverPath;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

public class ObjectMessanger {
    private Socket socket;
    private Logger log = Logger.getLogger(this.getClass().getName());

    public ObjectMessanger(Socket socket){
        this.socket = socket;
    }

    public void sendObject(Object object) throws
            IOException {
        log.info("Отправляется объект: " + object);
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(object);
        oos.flush();
        //oos.close();
        // log.info("Отправляется сообщение: " + string + '\n');
        //        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        //        oos.writeObject(string);
        //        oos.flush();
    }

    public Object recieveObject() throws
            IOException, ClassNotFoundException {
        Object object = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            object = ois.readObject();
        } catch (EOFException e){
            System.out.println("Конец передачи");
            System.exit(0);

        }
        if(object!=null) {
            log.info("Принят объект: " + object + '\n');
        }
        return object;
    }
}
