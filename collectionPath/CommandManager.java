package collectionPath;

import graphic.Thing;

import serverPath.Message;
import serverPath.ObjectMessanger;
import textPath.*;
import textPath.Planet.TypeOfPlanet;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentSkipListMap;

public class CommandManager implements Runnable {
    private ThingManager collectionManager = new ThingManager();
    private DataBaseCommand dataManager = new DataBaseCommand();
    private Thing thingAct = null;
    private boolean needExit = false;
    private Command invoke;
    private ConcurrentSkipListMap<String, String> commandMap = null;
    private Socket socket;

    public CommandManager(ThingManager collectionManager, Socket socket) {
        if (collectionManager != null) {
            this.collectionManager = collectionManager;
        }
        this.socket = socket;
    }

    public CommandManager(ThingManager collectionManager) {
        if (collectionManager != null) {
            this.collectionManager = collectionManager;
        }
    }

    private ConcurrentSkipListMap<String, String> readingObject() {
        ConcurrentSkipListMap<String, String> map = new ConcurrentSkipListMap<>();
        try {
            map = (ConcurrentSkipListMap<String, String>) new ObjectMessanger(this.socket).recieveObject();
            if (map != null) {
                System.out.println("Выполняется запрос от клиента:" + socket + ": " + map.get("command"));
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            needExit = true;
            System.exit(-1);
        }
        return map;
    }

    private Thing inObject( ConcurrentSkipListMap<String, String> map) {
        Thing temp = new Thing();
        switch (Integer.parseInt(map.get("idOfObject"))) {
            case 1:
                temp = new Thing(new Machine(map.get("nameOfObject"), new Planet(map.get("nameOfPlanet"), Integer.parseInt(map.get("valueOfPlanet")), TypeOfPlanet.valueOf(map.get("typeOfPlanet")), Integer.parseInt(map.get("idOfPlanet")), map.get("login")), Integer.parseInt(map.get("valueOfObject")), map.get("login")));
                break;
            case 2:
                //System.out.println();
                temp = new Thing(new Planet(map.get("nameOfPlanet"), Integer.parseInt(map.get("valueOfPlanet")), TypeOfPlanet.valueOf(map.get("typeOfPlanet")), Integer.parseInt(map.get("idOfPlanet")), map.get("login")));
                break;
            case 3:
                temp = new Thing(new Relief(map.get("nameOfObject"), new Planet(map.get("nameOfPlanet"), Integer.parseInt(map.get("valueOfPlanet")), TypeOfPlanet.valueOf(map.get("typeOfPlanet")), Integer.parseInt(map.get("idOfPlanet")), map.get("login")), Integer.parseInt(map.get("valueOfObject")), map.get("login")));
                break;
            case 4:
                temp = new Thing(new Star(map.get("nameOfObject"), Integer.parseInt(map.get("valueOfObject")), map.get("login")));
        }
        return temp;
    }

    @Override
    public void run() {
        System.out.println("Новый клиент");
        while (!socket.isClosed()) {
            commandMap = readingObject();
            if (!needExit && (commandMap != null)) {
                String log = commandMap.get("login");
                switch (commandMap.get("command")) {
                    case "insert":
                        thingAct = inObject(commandMap);
                        invoke = () -> dataManager.insertDB(commandMap.get("key"), thingAct, log);
                        break;
                    case "add_if_min":
                        thingAct = inObject(commandMap);
                        invoke = () -> dataManager.addDB(thingAct, log);
                        break;
                    case "remove_greater_key":
                        invoke = () -> dataManager.removeGreaterKey(commandMap.get("key"), log);
                        break;
                    case "import":
                        invoke = () -> collectionManager.importMap(new File(commandMap.get("path")));
                        break;
                    case "remove":
                        invoke = () ->dataManager.removeDB(commandMap.get("key"), log);
                        break;
                    case "exit":
                        invoke = () -> needExit = true;
                        break;
                    case "help":
                        //String temp = collectionManager.help();
                        invoke = () -> collectionManager.help();
                        break;
                    case "info":
                        invoke = () -> collectionManager.info();
                        break;
                    case "show":
                        invoke = () -> dataManager.show_colection();
                        break;
                    case "info_about_id":
                        invoke = () -> collectionManager.infoAboutId();
                        break;
                    case "save":
                        invoke = () -> collectionManager.saveFile(collectionManager.getFileEntry());
                        break;
                    case "load":

                        invoke = () -> dataManager.importCollection();
                        break;
                    case "register":
                        invoke = () -> {
                            String login = commandMap.get("login");
                            String mail = commandMap.get("mail");
                            dataManager.addUser(login, mail);
                        };
                        break;
                    case "sign_in":
                        invoke = () -> {
                            String login = commandMap.get("login");
                            String pass = commandMap.get("password");
                            dataManager.checkUser(login, pass);
                        };
                        break;
                    case "change_pass":
                        invoke = () -> dataManager.setPassword(commandMap.get("login"), commandMap.get("pass"));
                        break;
                }
            }
            if(commandMap != null) {

                //System.out.println(commandMap);
                if (commandMap.get("command").equals("info_about_id")  || commandMap.get("command").equals("info")  ||  commandMap.get("command").equals("exit") || commandMap.get("command").equals("help") || commandMap.get("command").equals("import") || commandMap.get("command").equals("save") || commandMap.get("command").equals("load")) {
                    //commandMap.get("command").equals("show") ||
                    invoke.execute();
                    String com = collectionManager.getS();

                    try {
                        if (commandMap.get("command").equals("exit")) {

                            new Message(socket).sendMessage("Выход из клиента");
                            this.socket.close();
                        } else {
                            new Message(socket).sendMessage(com);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Не может оправить ответ");
                    }
                } else if(commandMap.get("command").equals("register")  || commandMap.get("command").equals("remove_greater_key") || commandMap.get("command").equals("add_if_min")  || commandMap.get("command").equals("remove_greater_key")  || commandMap.get("command").equals("change_pass")||  commandMap.get("command").equals("sign_in") || commandMap.get("command").equals("change_pass")){
                    invoke.execute();
                    String com = dataManager.getString();

                    try {
                        new Message(socket).sendMessage(com);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }else if(commandMap.get("command").equals("show") || commandMap.get("command").equals("remove") || commandMap.get("command").equals("insert")){
                    invoke.execute();
                    ConcurrentSkipListMap<String, String[][]> temp_map = dataManager.getMapu();
                    try {
                        new ObjectMessanger(socket).sendObject(temp_map);
                        System.out.println("t78");
                    } catch (IOException e){

                        e.printStackTrace();

                    }
                }else {
                    System.out.println("Ошибка при вводе комманды");
                    try {
                        new Message(socket).sendMessage("Введите комманду снова\n");
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public boolean isNeedExit() {
        return needExit;
    }
}
