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
    source varchar(255),
    target varchar(255) not null,
    primary key(id)
);

insert into station (name)
VALUES ('잠실');
insert into station (name)
VALUES ('잠실새내');
insert into station (name)
VALUES ('종합운동장');
insert into station (name)
VALUES ('삼전');
insert into station (name)
VALUES ('석촌고분');
insert into station (name)
VALUES ('석촌');
insert into station (name)
VALUES ('부산');
insert into station (name)
VALUES ('대구');
insert into line (name, start_time, end_time, interval_time)
VALUES ('2호선', current_time, current_time, 3);
insert into line (name, start_time, end_time, interval_time)
VALUES ('9호선', current_time, current_time, 3);
insert into line (name, start_time, end_time, interval_time)
VALUES ('8호선', current_time, current_time, 3);
insert into line (name, start_time, end_time, interval_time)
VALUES ('ktx', current_time, current_time, 3);
insert into line_station (line, station_id, pre_station_id, distance, duration)
VALUES (1, 1, null, 0, 0);
insert into line_station (line, station_id, pre_station_id, distance, duration)
VALUES (1, 2, 1, 10, 1);
insert into line_station (line, station_id, pre_station_id, distance, duration)
VALUES (1, 3, 2, 10, 1);
insert into line_station (line, station_id, pre_station_id, distance, duration)
VALUES (2, 3, null, 0, 0);
insert into line_station (line, station_id, pre_station_id, distance, duration)
VALUES (2, 4, 3, 10, 1);
insert into line_station (line, station_id, pre_station_id, distance, duration)
VALUES (2, 5, 4, 1, 10);
insert into line_station (line, station_id, pre_station_id, distance, duration)
VALUES (2, 6, 5, 1, 10);
insert into line_station (line, station_id, pre_station_id, distance, duration)
VALUES (3, 1, null, 0, 0);
insert into line_station (line, station_id, pre_station_id, distance, duration)
VALUES (3, 6, 1, 1, 10);
insert into line_station (line, station_id, pre_station_id, distance, duration)
VALUES (4, 7, null, 10, 10);
insert into line_station (line, station_id, pre_station_id, distance, duration)
VALUES (4, 8, 7, 10, 10);
