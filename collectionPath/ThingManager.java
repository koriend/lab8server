package collectionPath;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
//import org.jetbrains.annotations.Contract;
import graphic.Thing;

import java.io.*;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class ThingManager {
    private ConcurrentSkipListMap<String, Thing> thingConcurrentSkipListMap;
    private Date date;
    private File fileEntry;
    private boolean emptFlag = true;
    private TreeMap<String, Thing> tempMap = new TreeMap<>();
    private ConcurrentSkipListMap<String, Thing> tempConcMap = new ConcurrentSkipListMap<>();
    private String s = "";
    private DataBaseCommand dataCommand = new DataBaseCommand();



    ThingManager() {
        this.date = new Date();
        this.thingConcurrentSkipListMap = new ConcurrentSkipListMap<>();
        this.fileEntry = null;

    }

    /**
     * конструктор для инициализации коллекции
     * @param fileCollection входящая коллекция
     */
    public ThingManager(File fileCollection) {
        //this.socket = socket;
        //this.thingConcurrentSkipListMap = this.importMap(fileCollection);

            this.thingConcurrentSkipListMap = this.dataCommand.importCollection();
        //System.out.println("kikikiki");
        //System.out.println(this.thingConcurrentSkipListMap);
        this.date = new Date();
        setEmptFlag(false);
        this.fileEntry = fileCollection;
    }

    /**
     * вывод информации о коллекции
     *
     * непотокобезопасный метод size
     */
    void info() {
        s = "";
            s = s + "\u001b[33mТип элементов в коллекции: Machine, Planet, Relief, Star\n";
            //out.flush();
            s = s + "Последняя инициализация: " + this.date + '\n';
            //out.flush();
            if (!isEmptFlag()) {
                s = s + "Размер: " + this.thingConcurrentSkipListMap.size() + "\u001b[0m" + '\n';
            } else {
                s = s + "Размер = 0" + "\u001b[0m" + '\n';
            }
    }

    synchronized void add_if_min(Thing thingElement) {
            if (!isEmptFlag()) {
                int minThing = thingConcurrentSkipListMap.values().stream().filter(value -> value.getId() == thingElement.getId()).min(Comparator.comparing(Thing::getValueThing)).get().getValueThing();
                if ((minThing > thingElement.getValueThing())) {

                    this.thingConcurrentSkipListMap.put(thingElement.getNamePlan(), thingElement);
                    s = s + "\u001b[33mЭлемент " + thingElement.getNamePlan() + " добавлен" + "\u001b[0m";
                } else {
                   s = s + "\u001b[31mЭтот элемент больше текущего минимального\u001b[0m";
                }
            } else {
                this.thingConcurrentSkipListMap.put(thingElement.getNamePlan(), thingElement);
                s = s + "\u001b[33mЭлемент " + thingElement.getNamePlan() + " добавлен" + "\u001b[0m";
            }

    }


    void remove(String name, String login) {
        s = "";
            try {
                if (isEmptFlag()) {
                    throw new ArgumentFormatException("File is empty\n");
                }

               // if (this.thingConcurrentSkipListMap.remove(name) == null) {
                if(!this.thingConcurrentSkipListMap.containsKey(name)){
                    s = s + "\u001b[31mКлюч не найден\u001b[0m\n";
                } else if(this.thingConcurrentSkipListMap.get(name).getUser().equals(login)){
                    this.thingConcurrentSkipListMap.remove(name);
                    s = s + "\u001b[33mЭлемент " + name + " удален" + "\u001b[0m\n";
                    s = s + "\u001b[33mТеккущее состояние коллекции: \u001b[0m\n";
                    this.show();
                } else{
                    s = s + "Вы не можете удалить это элемент\n";
                }
            } catch (ArgumentFormatException var3) {
                s = s + (var3.toString());
            }

    }


    void remove_greater_key(String name, String login) {
        s = "";
            try {
                if (isEmptFlag()) {
                    throw new ArgumentFormatException("File is empty\n");
                }
                if(this.thingConcurrentSkipListMap.get(name).getUser().equals(login)){
                    tempMap = new TreeMap<>();
                    thingConcurrentSkipListMap.keySet().stream().filter(key -> key.compareToIgnoreCase(name) < 0).forEach(key -> tempConcMap.put(key, thingConcurrentSkipListMap.get(key)));

                    thingConcurrentSkipListMap = new ConcurrentSkipListMap<>(tempConcMap);
                    s = s + "\u001b[33mТеккущее состояние коллекции: \u001b[0m\n";
                    this.show();
                } else {
                    s = s + "Вы не можете удалить это элемент";
                }
            } catch (ArgumentFormatException var3) {
                s = s + var3.toString();
            }
    }

    void show() {
        s = "";
        try {
                if (isEmptFlag()) {
                    throw new ArgumentFormatException("Файл пуст\n");
                } else {

                    thingConcurrentSkipListMap.entrySet().stream().forEach(x ->
                            s = s + x.toString() + "\n"
                    );
                }
            } catch (ArgumentFormatException var3) {
                s = s + var3.toString();
            }
            //out.writeUTF('\n');
        //System.out.println(s);


    }

    synchronized ConcurrentSkipListMap<String, Thing> importMap(File file) {
        s = "";
        try {
            if (!file.isFile()) {
                setEmptFlag(true);
                System.exit(0);
                throw new FileNotFoundException("Input incorrect path\n");
            }

            if (file.length() == 0L) {
                setEmptFlag(true);
                System.out.println("&&&");
                throw new FileNotFoundException("File is empty\n");
            } else {
                setEmptFlag(false);
            }

            if (!file.canRead()) {
                setEmptFlag(true);
                System.exit(0);
                throw new SecurityException("You need a root\n");
            }

            String jsonToRead = Thing.pullStingFromJson(file);
            if (!this.mapFromJson(jsonToRead).isEmpty()) {
                try {
                    this.tempMap = this.mapFromJson(jsonToRead);
                    thingConcurrentSkipListMap = new ConcurrentSkipListMap<>(tempMap);
                } catch (NullPointerException var4) {
                    s = s + "\u001b[31mError: check your file\u001b[0m\n";
                    System.exit(0);
                }

                s = s + "\u001b[32mCollection was imported\u001b[0m\n";
            } else {
                s = s + "\u001b[31mError: Collection isn't imported\u001b[0m\n";
                System.exit(0);
            }
        } catch (IOException | JsonSyntaxException | SecurityException var5) {
            s = s + "\u001b[31m" + var5.getMessage() + "\u001b[0m\n";
        }
        return thingConcurrentSkipListMap;

    }

    private TreeMap<String, Thing> mapFromJson(String jsonStr) {
        TreeMap<String, Thing> map;
        Type type = (new TypeToken<TreeMap<String, Thing>>() {
        }).getType();
        map = (new Gson()).fromJson(jsonStr, type);

        return map;
    }

    synchronized void saveFile(File fileCollection) {
        s = "";
        Gson gson = new Gson();
        File saveFile = fileCollection;

        try {
            BufferedOutputStream buff = new BufferedOutputStream(new FileOutputStream(saveFile));
                buff.write(gson.toJson(this.thingConcurrentSkipListMap).getBytes());
                buff.flush();
                s = s + "\u001b[33mКоллекция сохранена в файл: \u001b[0m" + saveFile.getAbsolutePath() + "\n";

        } catch (JsonSyntaxException | NullPointerException | IOException var37) {
            System.out.println(var37.getMessage());
            SimpleDateFormat form = new SimpleDateFormat("yyyy.MM.dd.hh.mm.ss");
            saveFile = new File("save_" + form + ".json");

            try {
                BufferedOutputStream buff = new BufferedOutputStream(new FileOutputStream(saveFile));
                    buff.flush();
                    s = s + "\u001b[33mКоллекция сохранена в файл: \u001b[0m" + saveFile + "\n";

            } catch (IOException var35) {
                s = s + "\u001b[31mКолекцию не удалось сохранить\u001b[0m\n";
            }
        }

    }

    void help() {
        //System.out.println("opoi");
        s = "";
            s = s + "\u001b[33mФормат элемента: \n           {\"name\":Pop, \"value\":879, \"planet\"{\"name\":Earth, \"value\":878, \"type\":IS_PLANET, \"idOfPlanet\":2}, \"id\":1}\u001b\n";
            s = s + "\u001b[33madd_if_min {element}\u001b[0m: добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции\n";
            s = s + "\u001b[33minsert {String key} {element}\u001b[0m: добавить новый элемент с заданным ключом\n";
            s = s + "\u001b[33mremove {String key}\u001b[0m: удалить элемент из коллекции по его ключу\n";
            s = s + "\u001b[33mremove_greater_key {String key}\u001b[0m: удалить из коллекции все элементы, ключ которых превышает заданный\n";
            s = s + "\u001b[33mshow:\u001b[0m вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n";
            s = s + "\u001b[33mimport {String path}\u001b[0m: добавить в коллекцию все данные из файла\n";
            s = s + "\u001b[33minfo\u001b[0m: вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n";
            s = s + "\u001b[33mexit\u001b[0m: выйти из программу, сохранив коллекцию в файл\n";
            setS(this.s);
        //System.out.println(getS());
        //return s;
    }

    private boolean isEmptFlag() {
        return emptFlag;
    }

    private void setEmptFlag(boolean emptFlag) {
        this.emptFlag = emptFlag;
    }

    File getFileEntry() {
        return fileEntry;
    }

    void infoAboutId() {
            s = s + "Machine.class: 1\n";
            s = s + "Planet.class: 2\n";
            s = s + "Relief.class: 3\n";
            s = s + "Star.class: 4\n";
    }


    String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }


}
