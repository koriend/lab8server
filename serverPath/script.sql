--удаление таблицы collection
drop table if exists collection;
--удаление таблицы user_date
drop table if exists user_date;



--создать таблицу user_date
create table user_date
(
    login    text not null
        constraint user_date_pk
            primary key,
    password text not null,
    mail     text,
    salt     text
);

--Оператор ALTER TABLE во время работы создает временную копию исходной таблицы.
-- Требуемое изменение выполняется на копии, затем исходная таблица удаляется,
-- а новая переименовывается.

-- Так делается для того, чтобы в новую таблицу автоматически попадали все обновления
-- кроме неудавшихся. Во время выполнения ALTER TABLE исходная таблица доступна для чтения
-- другими клиентами. Операции обновления и записи в этой таблице
-- приостанавливаются, пока не будет готова новая таблица.
alter table user_date
    owner to s265128;



--создать таблицу collection
create table collection
(
    name_object  text    not null
        constraint collection_pk
            primary key,
    value_object integer not null,
    id_object    integer not null,
    name_planet  text,
    value_planet integer,
    type_planet  text,
    id_planet    integer,
    time_create  date    not null,
    user_name    text    not null
        constraint collection_user_date_login_fk
            references user_date
            on update cascade on delete cascade
);

alter table collection
    owner to s265128;

create unique index collection_name_object_uindex
    on collection (name_object);

create unique index user_date_login_uindex
    on user_date (login);

create unique index user_date_mail_uindex
    on user_date (mail);


INSERT INTO user_date (login, password, mail, salt) VALUES ('kori', '1f728cb75539f5eb2ed9052715be54c43e037a4b', 'katrine.safina@gmail.com', 'O$f$r3M');
INSERT INTO collection (name_object, value_object, id_object, name_planet, value_planet, type_planet, id_planet, time_create, user_name) values ('Ut', 4575, 1, 'Earth', 1234, 'IS_PLANET', 2,'24.05.19', 'kori');
INSERT INTO collection (name_object, value_object, id_object, name_planet, value_planet, type_planet, id_planet, time_create, user_name) VALUES ('Iu', 654, 2, null, null, 'IS_PLANET', null, '2019-05-24', 'kori');
INSERT INTO collection (name_object, value_object, id_object, name_planet, value_planet, type_planet, id_planet, time_create, user_name) VALUES ('Re', 879, 3, 'Uiy', 583, 'IS_PLANET', 2, '2019-05-24', 'kori');
INSERT INTO collection (name_object, value_object, id_object, name_planet, value_planet, type_planet, id_planet, time_create, user_name) VALUES ('Loi', 353, 1, 'Earth', 1234, 'IS_PLANET', 2, '2019-05-24', 'kori');
INSERT INTO collection (name_object, value_object, id_object, name_planet, value_planet, type_planet, id_planet, time_create, user_name) VALUES ('Lop', 123, 4, null, null, null, null, '2019-05-24', 'kori');
INSERT INTO collection (name_object, value_object, id_object, name_planet, value_planet, type_planet, id_planet, time_create, user_name) VALUES ('Ki', 45, 4, null, null, null, null, '2019-05-24', 'kori');
