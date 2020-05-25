INSERT INTO STATION(name) VALUES('신정역');
INSERT INTO STATION(name) VALUES('여의도역');
INSERT INTO STATION(name) VALUES('천호역');
INSERT INTO STATION(name) VALUES('잠실역');
INSERT INTO STATION(name) VALUES('석촌역');
INSERT INTO STATION(name) VALUES('호돌역');

INSERT INTO LINE(name, start_time, end_time, interval_time) VALUES ('5호선', '06:00:00', '22:00:00', 10);
INSERT INTO LINE(name, start_time, end_time, interval_time) VALUES ('8호선', '06:00:00', '22:00:00', 10);
INSERT INTO LINE(name, start_time, end_time, interval_time) VALUES ('9호선', '06:00:00', '22:00:00', 10);

-- 5호선
INSERT INTO LINE_STATION(line, station_id, distance, duration) VALUES (1, 1,1,2); -- 출발 - 신정역
INSERT INTO LINE_STATION(line, station_id, pre_station_id, distance, duration) VALUES (1, 2, 1, 1, 10); -- 신정 - 여의도
INSERT INTO LINE_STATION(line, station_id, pre_station_id, distance, duration) VALUES (1, 3, 2, 1, 10); -- 여의도 - 천호

-- 8호선
INSERT INTO LINE_STATION(line, station_id, distance, duration) VALUES (2, 3, 1, 2); -- 출발 - 천호
INSERT INTO LINE_STATION(line, station_id, pre_station_id, distance, duration) VALUES (2, 4, 3,1, 10); -- 천호 - 잠실
INSERT INTO LINE_STATION(line, station_id, pre_station_id, distance, duration) VALUES (2, 5, 4,1, 10); -- 잠실 - 석촌

-- 9호선
INSERT INTO LINE_STATION(line, station_id, distance, duration) VALUES (3, 2, 1, 1); -- 출발 - 여의도
INSERT INTO LINE_STATION(line, station_id, pre_station_id, distance, duration) VALUES (3, 5, 2, 1, 5); -- 여의도 - 석촌