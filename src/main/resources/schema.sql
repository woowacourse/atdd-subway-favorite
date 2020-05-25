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
)
;
-- INSERT INTO MEMBER(EMAIL, NAME, PASSWORD) VALUES ('aa@aa.com', 'a', 'a');
-- INSERT INTO MEMBER(EMAIL, NAME, PASSWORD) VALUES ('bb@bb.com', 'b', 'b');

-- // TODO 즐겨찾기 테이블 스키마 추가
