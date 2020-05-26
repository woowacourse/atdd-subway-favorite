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
    duration int
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
    source_station_id bigint not null,
    target_station_id bigint not null,
    primary key(id)
);

insert into STATION values (1, '가', '2020-05-25 16:54:44.065');
insert into STATION values (2, '나', '2020-05-25 16:54:44.065');
insert into STATION values (3, '다', '2020-05-25 16:54:44.065');
insert into STATION values (4, '라', '2020-05-25 16:54:44.065');
insert into STATION values (5, '마', '2020-05-25 16:54:44.065');
insert into STATION values (6, '바', '2020-05-25 16:54:44.065');
insert into STATION values (7, '사', '2020-05-25 16:54:44.065');

insert into LINE values (1, '1호선', '05:30:00', '23:30:00', 10, '2020-05-25 16:54:44.065', '2020-05-25 16:54:44.065');
insert into LINE values (2, '2호선', '05:35:00', '23:35:00', 15, '2020-05-25 16:54:44.065', '2020-05-25 16:54:44.065');

insert into LINE_STATION values (1, 1, null, 0, 0);
insert into LINE_STATION values (1, 2, 1, 0, 0);
insert into LINE_STATION values (1, 3, 2, 0, 0);
insert into LINE_STATION values (1, 4, 3, 0, 0);
insert into LINE_STATION values (2, 5, null, 0, 0);
insert into LINE_STATION values (2, 6, 5, 0, 0);
insert into LINE_STATION values (2, 7, 6, 0, 0);

insert into MEMBER values (1, 'hwanghe159@naver.com', '황준호', 1234);