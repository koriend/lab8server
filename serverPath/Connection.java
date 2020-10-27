package serverPath;

import collectionPath.CommandManager;
import collectionPath.ThingManager;
//import org.postgresql.util.PSQLException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;


class Connection extends Thread {
    private int host;
    private ServerSocket server = null;
    private Socket socket = null;


    Connection(int host){
        this.host = host;

    }

 void connect(){
            try {
                this.server = new ServerSocket(host);
                System.out.println("Ждет клиента...");
            } catch (IOException e) {
                System.out.println("Не удается подключить");
            }
        while (true) {
            try {
                this.socket = server.accept();
                try {
                    DbAdapter.connect();
                } catch (SQLException | ClassNotFoundException e){
                    e.printStackTrace();
                }
            } catch (IOException e) {
                System.out.println("Can't accept");
                System.exit(0);
            }
            Thread mCommandManager = new Thread(new CommandManager(new ThingManager(new File("/home/katrin/IdeaProjects/lab6/lab6_thread/lab6_thread_Server/src/BaseCollection.json")), socket));
            mCommandManager.start();
        }
    }


    void close(){
        try{
            socket.close();
            server.close();
        } catch (IOException e){
            System.out.println("Error [close]");
            System.exit(0);
        }
    }


}
