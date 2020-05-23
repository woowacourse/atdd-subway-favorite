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
    member_id bigint not null,
    start_station_name varchar(255) not null,
    end_station_name varchar(255) not null,
    primary key(id),
    foreign key(member_id) REFERENCES MEMBER (id) ON DELETE CASCADE ON UPDATE CASCADE,
    foreign key(start_station_name) REFERENCES STATION (name) ON DELETE CASCADE ON UPDATE CASCADE,
    foreign key(end_station_name) REFERENCES STATION (name) ON DELETE CASCADE ON UPDATE CASCADE
);
