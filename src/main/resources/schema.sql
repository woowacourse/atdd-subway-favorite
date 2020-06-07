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

create table  if not exists FAVORITE_PATH
(
    id bigint auto_increment,
    member_id bigint not null,
    start_station_id bigint not null,
    start_station_name varchar(255) not null,
    end_station_id bigint not null,
    end_station_name varchar(255) not null ,
    primary key(id),
    unique member_start_end(member_id, start_station_id, end_station_id)
)
