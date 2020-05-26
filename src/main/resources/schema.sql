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

-- // TODO 즐겨찾기 테이블 스키마 추가


-- Test Data
INSERT INTO LINE (name, start_time, end_time, interval_time)
VALUES
('2호선', '12:00', '00:00', 10),
('분당선', '12:00', '00:00', 10),
('신분당선', '12:00', '00:00', 10);
INSERT INTO STATION (name)
VALUES
('강남역'),
('역삼역'),
('선릉역'),
('삼성역'),
('종합운동장역'),
('잠실새내역'),
('잠실역'),
('잠실나루역'),
('청계산입구역'),
('양재시민의숲역'),
('양재역'),
('한티역'),
('도곡역'),
('구룡역'),
('개포동역'),
('대모산입구역');
INSERT INTO LINE_STATION (line, pre_station_id, station_id, distance, duration)
VALUES
(1, null, 1, 0, 0),
(1, 1, 2, 9, 3),
(1, 2, 3, 11, 2),
(1, 3, 4, 10, 2),
(1, 4, 5, 8, 2),
(1, 5, 6, 9, 2),
(1, 6, 7, 7, 2),
(1, 7, 8, 8, 1),
(2, null, 9, 0, 0),
(2, 9, 10, 9, 3),
(2, 10, 11, 8, 2),
(2, 11, 1, 10, 2),
(3, null, 3, 0, 0),
(3, 3, 12, 10, 2),
(3, 12, 13, 8, 2),
(3, 13, 14, 7, 1),
(3, 14, 15, 8, 2),
(3, 15, 16, 9, 1);