package collectionPath;

import com.sun.mail.util.MailConnectException;
import graphic.Thing;
import org.postgresql.util.PSQLException;
import serverPath.DbAdapter;
import serverPath.PasswordGenerator;
import serverPath.SendMail;
import textPath.*;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class DataBaseCommand {
    private Statement statement;
    private ResultSet resultSet;
    private Connection connection;
    private  String string = "";
    private String pepper = "$F2/~oIh:VN)9O>?7ch4p";
    private CopyOnWriteArrayList<ConcurrentSkipListMap<String, String>> array;
private ConcurrentSkipListMap<String, String[][]> mapu;

    DataBaseCommand(){
        statement = DbAdapter.getStatement();
        resultSet = DbAdapter.getResultSet();
        connection = DbAdapter.getConnection();
    }


    synchronized void insertDB(String key, Thing thing, String login){
        string="";
        try {
            PreparedStatement preparedStatement = null;
            Clock clock = Clock.system(ZoneId.of("Europe/Moscow"));
            LocalDateTime ldt = LocalDateTime.ofInstant(clock.instant(), ZoneId.of("UTC"));
            try {
                switch (thing.getId()) {
                    case 1:
                    case 3:
                        preparedStatement = connection.prepareStatement("insert into collection (name_object, value_object, id_object, name_planet, value_planet, type_planet, id_planet, time_create, user_name) VALUES (?,?,?,?,?,?,?,?,?)");
                        preparedStatement.setString(1, key);
                        preparedStatement.setInt(2, thing.getValueThing());
                        preparedStatement.setInt(3, thing.getId());
                        preparedStatement.setString(4, thing.getNamePlan());
                        preparedStatement.setInt(5, thing.getPlanet().getDiametrPlan());
                        preparedStatement.setString(6, thing.getTypePlan().getTypeOfPlan());
                        preparedStatement.setInt(7, 2);
                        preparedStatement.setDate(8, Date.valueOf(LocalDate.now()));
                        preparedStatement.setString(9, login);

                        break;
                    case 2:
                        preparedStatement = connection.prepareStatement("insert into collection (name_object,value_object,id_object,type_planet,time_create,user_name) values (?,?,?,?,?,?)");
                        preparedStatement.setString(1, key);
                        preparedStatement.setInt(2, thing.getValueThing());
                        preparedStatement.setInt(3, thing.getId());
                        preparedStatement.setString(4, thing.getTypePlan().getTypeOfPlan());
                        preparedStatement.setDate(5, Date.valueOf(LocalDate.now()));
                        preparedStatement.setString(6, login);
                        break;
                    case 4:
                        preparedStatement = connection.prepareStatement("insert into collection (name_object, value_object, id_object, time_create, user_name) values (?,?,?,?,?)");
                        preparedStatement.setString(1, key);
                        preparedStatement.setInt(2, thing.getValueThing());
                        preparedStatement.setInt(3, thing.getId());
                        preparedStatement.setDate(4, Date.valueOf(LocalDate.now()));
                        preparedStatement.setString(5, login);
                }
                preparedStatement.executeUpdate();
                string+="Элемент добавлен";

            } catch (PSQLException e){
                string+="Ключ с данным названием уже существует";
            }
            setString(string);

        } catch (SQLException e){
            System.out.println("sql exc");
            e.printStackTrace();
        }
       show_colection();
    }

    void addDB(Thing thing, String login){
        string="";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select min(value_object) from collection group by id_object having id_object=?");
            preparedStatement.setInt(1,thing.getId());
            resultSet = preparedStatement.executeQuery();

            resultSet.next();
            if (thing.getValueThing() < resultSet.getInt(1)){
                switch (thing.getId()) {
                    case 1:
                    case 3:
                        preparedStatement = connection.prepareStatement("insert into collection (name_object, value_object, id_object, name_planet, value_planet, type_planet, id_planet, time_create, user_name) VALUES (?,?,?,?,?,?,?,?,?)");
                        preparedStatement.setString(1, thing.getNamePlan());
                        preparedStatement.setInt(2, thing.getValueThing());
                        preparedStatement.setInt(3, thing.getId());
                        preparedStatement.setString(4, thing.getNamePlan());
                        preparedStatement.setInt(5, thing.getPlanet().getDiametrPlan());
                        preparedStatement.setString(6, thing.getTypePlan().getTypeOfPlan());
                        preparedStatement.setInt(7, 2);
                        preparedStatement.setDate(8, Date.valueOf(LocalDate.now()));
                        preparedStatement.setString(9, login);
                        break;
                    case 2:
                        preparedStatement = connection.prepareStatement("insert into collection (name_object,value_object,id_object,type_planet,time_create,user_name) values (?,?,?,?,?,?)");
                        preparedStatement.setString(1,thing.getNamePlan());
                        preparedStatement.setInt(2, thing.getValueThing());
                        preparedStatement.setInt(3, thing.getId());
                        preparedStatement.setString(4, thing.getTypePlan().getTypeOfPlan());
                        preparedStatement.setDate(5, Date.valueOf(LocalDate.now()));
                        preparedStatement.setString(6, login);
                        break;
                    case 4:
                        preparedStatement = connection.prepareStatement("insert into collection (name_object, value_object, id_object, time_create, user_name) values (?,?,?,?,?)");
                        preparedStatement.setString(1, thing.getNamePlan());
                        preparedStatement.setInt(2, thing.getValueThing());
                        preparedStatement.setInt(3, thing.getId());
                        preparedStatement.setDate(4, Date.valueOf(LocalDate.now()));
                        preparedStatement.setString(5, login);
                }
                preparedStatement.executeUpdate();
                string+="Элемент добавлен";
            } else {
                string+="Элемент не является наименьшим в коллекции";
            }
        } catch (SQLException e){

        }
    }

    void removeDB(String key, String login){
        string="";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("delete from collection where name_object=? and user_name=?");
            preparedStatement.setString(1, key);
            preparedStatement.setString(2, login);
            int k = preparedStatement.executeUpdate();
            if(k==0){
                string="Ключа с таким именем не найдено или вы не имееть права его удалять";
            } else {
                string="Элемент удален";
            }
        setString(string);
        } catch (SQLException e){
            e.printStackTrace();
        }

        show_colection();

    }

    void removeGreaterKey(String key, String login){
        try {
            resultSet = statement.executeQuery("select value_object from collection group by id_object having min(value_object)");
            resultSet.next();
            int k =  resultSet.getInt("value_thing");
            PreparedStatement preparedStatement = connection.prepareStatement("delete from collection where value_object<? and user_name=?");
            preparedStatement.setInt(1,k);
            preparedStatement.setString(2, login);
            int l = preparedStatement.executeUpdate();
            if(k==0){
                string="Ключа с таким именем не найдено или вы не имееть права его удалять";
            } else {
                string="Элемент удален";
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

     synchronized void showDB(){
       // String[] qw = new String[];
        string="";
        try {
            resultSet = statement.executeQuery("select * from collection where id_object='1'");
            while (resultSet.next()){
                Machine machine = new Machine(resultSet.getString("name_object"), new Planet(resultSet.getString("name_planet"), resultSet.getInt("value_planet"), Planet.TypeOfPlanet.valueOf(resultSet.getString("type_planet")), 2, resultSet.getString("user_name")), resultSet.getInt("value_object"), resultSet.getString("user_name"));
                string+=new Thing(machine).toString()+'\n';
            }
            resultSet = statement.executeQuery("select * from collection where id_object='2'");
            while (resultSet.next()){
                Planet planet = new Planet(resultSet.getString("name_object"), resultSet.getInt("value_object"), Planet.TypeOfPlanet.valueOf(resultSet.getString("type_planet")), 2, resultSet.getString("user_name"));
                string+=new Thing(planet).toString()+'\n';
            }
            resultSet = statement.executeQuery("select * from collection where id_object='3'");
            while (resultSet.next()){
                Planet planet = new Planet(resultSet.getString("name_planet"), resultSet.getInt("value_planet"), Planet.TypeOfPlanet.valueOf(resultSet.getString("type_planet")), 2 , resultSet.getString("user_name"));
                Relief relief = new Relief(resultSet.getString("name_object"), planet, resultSet.getInt("value_object"), resultSet.getString("user_name"));
                string+=new Thing(relief).toString()+'\n';
            }
            resultSet = statement.executeQuery("select * from collection where id_object='4'");
            while (resultSet.next()){
                Star star = new Star(resultSet.getString("name_object"), resultSet.getInt("value_object"), resultSet.getString("user_name"));
                string+=new Thing(star).toString()+'\n';
            }
            setString(string);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    synchronized void show_col(){
        try {

            resultSet = statement.executeQuery("select count(name_object) as count from collection");
            resultSet.next();
            int i = Integer.parseInt(resultSet.getString("count"));
            String[] str = new String[i];
            resultSet = statement.executeQuery("select * from collection where id_object='1'");
            resultSet.next();
            int j = 0;
            ConcurrentSkipListMap<String, String> mapo = new ConcurrentSkipListMap<>();
            CopyOnWriteArrayList<ConcurrentSkipListMap<String, String>> collection = new CopyOnWriteArrayList<>();
            while (resultSet.next()){
                //str[j] = resultSet.getString("name_object") +resultSet.getString("name_planet")+resultSet.getInt("value_planet")+Planet.TypeOfPlanet.valueOf(resultSet.getString("type_planet")) +  2+resultSet.getString("user_name")+ resultSet.getInt("value_object") +  resultSet.getString("user_name");
                //string+=new Thing(machine).toString()+'\n';
                mapo.put("name_object", resultSet.getString("name_object"));
                mapo.put("name_planet", resultSet.getString("name_planet"));
                mapo.put("value_planet", resultSet.getString("value_planet"));
                mapo.put("type_planet", resultSet.getString("type_planet"));
                mapo.put("value_object", resultSet.getString("value_object"));
                mapo.put("id_object", resultSet.getString("1"));
                mapo.put("user_name", resultSet.getString("user_name"));

                collection.add(mapo);
                mapo = new ConcurrentSkipListMap<>();

            }
            resultSet = statement.executeQuery("select * from collection where id_object='2'");
            while (resultSet.next()){
                mapo.put("name_object", resultSet.getString("name_object"));
                mapo.put("type_planet", resultSet.getString("type_planet"));
                mapo.put("value_object", resultSet.getString("value_object"));
                mapo.put("id_object", resultSet.getString("2"));
                mapo.put("user_name", resultSet.getString("user_name"));

                collection.add(mapo);
                mapo = new ConcurrentSkipListMap<>();
            }
            resultSet = statement.executeQuery("select * from collection where id_object='3'");
            while (resultSet.next()){
                Planet planet = new Planet(resultSet.getString("name_planet"), resultSet.getInt("value_planet"), Planet.TypeOfPlanet.valueOf(resultSet.getString("type_planet")), 2 , resultSet.getString("user_name"));
                Relief relief = new Relief(resultSet.getString("name_object"), planet, resultSet.getInt("value_object"), resultSet.getString("user_name"));
                string+=new Thing(relief).toString()+'\n';
                mapo.put("name_object", resultSet.getString("name_object"));
                mapo.put("name_planet", resultSet.getString("name_planet"));
                mapo.put("value_planet", resultSet.getString("value_planet"));
                mapo.put("type_planet", resultSet.getString("type_planet"));
                mapo.put("value_object", resultSet.getString("value_object"));
                mapo.put("id_object", resultSet.getString("3"));
                mapo.put("user_name", resultSet.getString("user_name"));

                collection.add(mapo);
                mapo = new ConcurrentSkipListMap<>();
            }
            resultSet = statement.executeQuery("select * from collection where id_object='4'");
            while (resultSet.next()){
                Star star = new Star(resultSet.getString("name_object"), resultSet.getInt("value_object"), resultSet.getString("user_name"));
                string+=new Thing(star).toString()+'\n';
                mapo.put("name_object", resultSet.getString("name_object"));
                mapo.put("value_object", resultSet.getString("value_object"));
                mapo.put("id_object", resultSet.getString("4"));
                mapo.put("user_name", resultSet.getString("user_name"));

                collection.add(mapo);
                mapo = new ConcurrentSkipListMap<>();
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    void addUser(String login, String mail){
        try {
            PasswordGenerator passwordGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                    .useDigits(true)
                    .useLower(true)
                    .useUpper(true)
                    .build();
            String randPass = passwordGenerator.generate(6);
            new SendMail(mail, randPass) ;
            String salt = new PasswordGenerator.PasswordGeneratorBuilder()
                    .useDigits(true)
                    .useLower(true)
                    .useUpper(true)
                    .usePunctuation(true)
                    .build()
                    .generate(7);
            randPass = pepper.concat(randPass).concat(salt);
            System.out.println(randPass);
            string = "";
            PreparedStatement p = connection.prepareStatement("insert into user_date (login, password, mail, salt) values (?, ?, ?, ?)");
            p.setString(1, login);
            try {
                p.setString(2, HashClass.hash(randPass));
            } catch (NoSuchAlgorithmException e){
                p.setString(2, randPass);
            }
            p.setString(3, mail);
            p.setString(4, salt);
            p.executeUpdate();

            string += "Зарегестрирован новый пользователь\n";

            setString(string);

        } catch (SQLException | MailConnectException e){
            string="";
            string="Нет соединения с сетью";
            setString(string);
        }
    }

    void showUsers(){
        try {
            resultSet = statement.executeQuery("select * from user_date");
            while (resultSet.next()) {
                User user = new User();
                user.setLogin(resultSet.getString(1));
                user.setPassword(resultSet.getString(2));
                user.setMail(resultSet.getString(3));

                String str = user.toString();

                setString(str);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    void checkUser(String login, String password){
        string="";
        try{
            PreparedStatement pre = connection.prepareStatement("select * from user_date where login=?");
            pre.setString(1, login);
            resultSet = pre.executeQuery();
            System.out.println(login);
            String str ="";
            try {
                resultSet.next();
                String hash="";
                try {
                    password = pepper.concat(password).concat(resultSet.getString(4));
                    System.out.println(password);
                    hash = HashClass.hash(password);
                    System.out.println(hash);
                } catch (NoSuchAlgorithmException e){
                    e.printStackTrace();
                }
                if (resultSet.getString(2).equals(hash)) {
                    str = "1";
                } else {
                    str = "0";
                }
            } catch (PSQLException e){
                e.printStackTrace();
                str = "-1";
            }
            setString(str);

        } catch (SQLException e){
            e.printStackTrace();
        }

    }

    public void setPassword(String login, String pass){
        string="";
        try{
          PreparedStatement preparedStatement = connection.prepareStatement("update user_date set password = ?, salt = ? where login=?");
          String salt = new PasswordGenerator.PasswordGeneratorBuilder()
                    .useDigits(true)
                    .useLower(true)
                    .useUpper(true)
                    .usePunctuation(true)
                    .build()
                    .generate(7);
          pass= pepper.concat(pass).concat(salt);
            System.out.println(pass);
            System.out.println(HashClass.hash(pass));
          preparedStatement.setString(1, HashClass.hash(pass));
          preparedStatement.setString(2, salt);
          preparedStatement.setString(3, login);
          int k = preparedStatement.executeUpdate();
            System.out.println(k);

          string+="Пароль обновлен";

          setString(string);
        } catch (SQLException | NoSuchAlgorithmException e){
            e.printStackTrace();
        }
    }

    public void show_colection(){
        ConcurrentSkipListMap<String, String[][]> map = new ConcurrentSkipListMap<>();
        String[][] arr = new String[8][2];
        try{
            resultSet = statement.executeQuery("select * from collection where id_object='1'");

            while (resultSet.next()){
                Timestamp ts = resultSet.getTimestamp("time_create");
                java.util.Date date = new java.util.Date();
                date.setTime(ts.getTime());
                String formattedDate = new SimpleDateFormat("yyyy.MM.dd").format(date);
                String[][] arr1 = {
                        {"name_object", resultSet.getString("name_object") },
                        {"value_object", String.valueOf(resultSet.getInt("value_object"))},
                        {"id_project", "1"},
                        {"name_planet", resultSet.getString("name_planet")},
                        {"value_planet", String.valueOf(resultSet.getInt("value_planet"))},
                        {"type_planet",resultSet.getString("type_planet")},
                        {"id_planet", "2"},
                        {"time", formattedDate},
                        {"user_name", resultSet.getString("user_name")}

                };
                map.put(resultSet.getString("name_object"), arr1);
            }
            resultSet = statement.executeQuery("select * from collection where id_object='2'");
            while (resultSet.next()){
                Timestamp ts = resultSet.getTimestamp("time_create");
                java.util.Date date = new java.util.Date();
                date.setTime(ts.getTime());
                String formattedDate = new SimpleDateFormat("yyyy.MM.dd").format(date);
                String[][] arr1 = {
                        {"name_object", resultSet.getString("name_object") },
                        {"value_object", String.valueOf(resultSet.getInt("value_object"))},
                        {"id_project", "2"},
                        {"name_planet", ""},
                        {"value_planet",""},
                        {"type_planet",resultSet.getString("type_planet")},
                        {"id_planet", ""},
                        {"time", formattedDate},
                        {"user_name", resultSet.getString("user_name")}
                };
                map.put(resultSet.getString("name_object"), arr1);
            }
            resultSet = statement.executeQuery("select * from collection where id_object='3'");
            while (resultSet.next()){
                Timestamp ts = resultSet.getTimestamp("time_create");
                java.util.Date date = new java.util.Date();
                date.setTime(ts.getTime());
                String formattedDate = new SimpleDateFormat("yyyy.MM.dd").format(date);
                String[][] arr1 = {
                        {"name_object", resultSet.getString("name_object") },
                        {"value_object", String.valueOf(resultSet.getInt("value_object"))},
                        {"id_project", "3"},
                        {"name_planet", resultSet.getString("name_planet")},
                        {"value_planet", String.valueOf(resultSet.getInt("value_planet"))},
                        {"type_planet",resultSet.getString("type_planet")},
                        {"id_planet", "2"},
                        {"time", formattedDate},
                        {"user_name", resultSet.getString("user_name")}
                };
                map.put(resultSet.getString("name_object"), arr1);
            }
            resultSet = statement.executeQuery("select * from collection where id_object='4'");
            while (resultSet.next()){
                Timestamp ts = resultSet.getTimestamp("time_create");
                java.util.Date date = new java.util.Date();
                date.setTime(ts.getTime());
                String formattedDate = new SimpleDateFormat("yyyy.MM.dd").format(date);
                String[][] arr1 = {
                        {"name_object", resultSet.getString("name_object") },
                        {"value_object", String.valueOf(resultSet.getInt("value_object"))},
                        {"id_project", "4"},
                        {"name_planet", ""},
                        {"value_planet", ""},
                        {"type_planet",""},
                        {"id_planet", ""},
                        {"time", formattedDate},
                        {"user_name", resultSet.getString("user_name")}
                };
                map.put(resultSet.getString("name_object"), arr1);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        setMapu(map);
    }

    ConcurrentSkipListMap<String, Thing> importCollection(){
        ConcurrentSkipListMap<String, Thing> map = new ConcurrentSkipListMap<>();
        try {
            resultSet = statement.executeQuery("select * from collection where id_object='1'");
            while (resultSet.next()){
                System.out.println(resultSet);
                Planet planet = new Planet(resultSet.getString("name_planet"), resultSet.getInt("value_planet"), Planet.TypeOfPlanet.valueOf(resultSet.getString("type_planet")), 2, resultSet.getString("user_name"));
                Machine machine = new Machine(resultSet.getString("name_object"), planet, resultSet.getInt("value_object"), resultSet.getString("user_name"));
                map.put(resultSet.getString("name_object"), new Thing(machine));
            }
            resultSet = statement.executeQuery("select * from collection where id_object='2'");
            while (resultSet.next()){
                Planet planet = new Planet(resultSet.getString("name_object"), resultSet.getInt("value_object"), Planet.TypeOfPlanet.valueOf(resultSet.getString("type_planet")), 2, resultSet.getString("user_name"));
                map.put(resultSet.getString("name_object"), new Thing(planet));
            }
            resultSet = statement.executeQuery("select * from collection where id_object='3'");
            while (resultSet.next()){
                Planet planet = new Planet(resultSet.getString("name_planet"), resultSet.getInt("value_planet"), Planet.TypeOfPlanet.valueOf(resultSet.getString("type_planet")), 2 , resultSet.getString("user_name"));
                Relief relief = new Relief(resultSet.getString("name_object"), planet, resultSet.getInt("value_object"), resultSet.getString("user_name"));
                map.put(resultSet.getString("name_object"), new Thing(relief));
            }
            resultSet = statement.executeQuery("select * from collection where id_object='4'");
            while (resultSet.next()){
                Star star = new Star(resultSet.getString("name_object"), resultSet.getInt("value_object"), resultSet.getString("user_name"));
                map.put(resultSet.getString("name_object"), new Thing(star));
            }

            System.out.println("Коллекция импортирована из базы");

        } catch (SQLException e){
            System.out.println("Коллекция не импортирована");
            int k = (int)((Math.random()+1)*10000);
            PreparedStatement pre = null;
            try{
                pre = connection.prepareStatement("create table ? (name_object  text    not null\n" +
                        "        constraint collection_pk\n" +
                        "            primary key,\n" +
                        "    value_object integer not null,\n" +
                        "    id_object    integer not null,\n" +
                        "    name_planet  text,\n" +
                        "    value_planet integer,\n" +
                        "    type_planet  text,\n" +
                        "    id_planet    integer,\n" +
                        "    time_create  date    not null,\n" +
                        "    user_name    text    not null\n" +
                        "        constraint collection_user_date_login_fk\n" +
                        "            references user_date\n" +
                        "            on update cascade on delete cascade);" +
                        "alter table collection\n" +
                        "    owner to katrin;" +
                        "create unique index collection_name_object_uindex\n" +
                        "    on collection (name_object);");

                pre.setString(1, "user_date" + k);
                pre.executeUpdate();
                System.out.println("Создана новая база данных");
            }catch (SQLException e1){
                System.out.println("Не удалось создать базу данных. Выход из системы");
                close();
                System.exit(0);
            }

        }
        return map;
    }

    public void saveBD(ConcurrentSkipListMap<String, Thing> map){

    }
    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public void setArray(CopyOnWriteArrayList<ConcurrentSkipListMap<String, String>> array) {
        this.array = array;
    }

    public CopyOnWriteArrayList<ConcurrentSkipListMap<String, String>> getArray() {
        return array;
    }

    public void setMapu(ConcurrentSkipListMap<String,String[][]> mapu) {
        this.mapu = mapu;
    }

    public ConcurrentSkipListMap<String, String[][]> getMapu() {
        return mapu;
    }

    private void close(){
        try {
            connection.close();
            statement.close();
            resultSet.close();
        } catch (SQLException e){
            e.printStackTrace();
            System.exit(0);
        }
    }
}
