create table if not exists STATION
(
    id bigint auto_increment not null,
    name varchar(255) not null unique,
    created_at datetime,
    primary key(id)
);

create table if not exists LINE
(
    id bigint auto_increment not null,
    name varchar(255) not null,
    start_time time not null,
    end_time time not null,
    interval_time int not null,
    created_at datetime,
    updated_at datetime,
    primary key(id)
);

create table if not exists LINE_STATION
(
    line bigint not null,
    station_id bigint not null,
    pre_station_id bigint,
    distance int,
    duration int,
    created_at datetime,
    updated_at datetime
);

create table if not exists MEMBER
(
    id bigint auto_increment not null,
    email varchar(255) not null unique,
    name varchar(255) not null,
    password varchar(255) not null,
    primary key(id)
);

create table if not exists FAVORITE
(
    id bigint auto_increment not null,
    member bigint not null,
    departure_id bigint not null,
    destination_id bigint not null,
    primary key(id)
);


INSERT INTO LINE(NAME, START_TIME, END_TIME, INTERVAL_TIME, CREATED_AT, UPDATED_AT) VALUES ('1호선', '11:00:00', '11:00:00', 3, '2020-05-26 09:22:35.388', '2020-05-26 09:22:35.388');
INSERT INTO STATION(NAME, CREATED_AT) VALUES ('a', '2020-05-26 09:22:35.388');
INSERT INTO STATION(NAME, CREATED_AT) VALUES ('b', '2020-05-26 09:22:35.388');
INSERT INTO STATION(NAME, CREATED_AT) VALUES ('c', '2020-05-26 09:22:35.388');
INSERT INTO STATION(NAME, CREATED_AT) VALUES ('d', '2020-05-26 09:22:35.388');
INSERT INTO STATION(NAME, CREATED_AT) VALUES ('e', '2020-05-26 09:22:35.388');
INSERT INTO STATION(NAME, CREATED_AT) VALUES ('f', '2020-05-26 09:22:35.388');
INSERT INTO LINE_STATION(LINE, STATION_ID, PRE_STATION_ID, DISTANCE, DURATION, CREATED_AT) VALUES ('1', '1', null, '10', '10', '2020-05-26 09:22:35.388');
INSERT INTO LINE_STATION(LINE, STATION_ID, PRE_STATION_ID, DISTANCE, DURATION, CREATED_AT) VALUES ('1', '2', '1', '10', '10', '2020-05-26 09:22:35.388');
INSERT INTO LINE_STATION(LINE, STATION_ID, PRE_STATION_ID, DISTANCE, DURATION, CREATED_AT) VALUES ('1', '3', '2', '10', '10', '2020-05-26 09:22:35.388');

INSERT INTO MEMBER(EMAIL, NAME, PASSWORD) VALUES ('aa@aa.com', 'a', 'a');
INSERT INTO MEMBER(EMAIL, NAME, PASSWORD) VALUES ('bb@bb.com', 'b', 'b');
