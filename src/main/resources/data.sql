INSERT INTO LINE(name, start_time, end_time, interval_time, bg_color) VALUES ('1호선', '15:00', '16:00', 10, 'bg-black-400');
INSERT INTO LINE(name, start_time, end_time, interval_time, bg_color) VALUES ('2호선', '07:00', '23:00', 10, 'bg-red-400');
INSERT INTO LINE(name, start_time, end_time, interval_time, bg_color) VALUES ('부산 2호선', '07:00', '23:00', 10, 'bg-blue-400');
INSERT INTO LINE(name, start_time, end_time, interval_time, bg_color) VALUES ('3호선', '07:00', '23:00', 10, 'bg-gray-400');

INSERT INTO STATION(name) VALUES ('서울');
INSERT INTO STATION(name) VALUES ('용산');
INSERT INTO STATION(name) VALUES ('신촌');
INSERT INTO STATION(name) VALUES ('잠실');
INSERT INTO STATION(name) VALUES ('잠실나루');
INSERT INTO STATION(name) VALUES ('해운대');
INSERT INTO STATION(name) VALUES ('수영');
INSERT INTO STATION(name) VALUES ('광안');
INSERT INTO STATION(name) VALUES ('강남');

INSERT INTO LINE_STATION(line, station_id, pre_station_id, distance, duration)
VALUES (1, 1, null, 0, 0);
INSERT INTO LINE_STATION(line, station_id, pre_station_id, distance, duration)
VALUES (1, 2, 1, 700, 1);
INSERT INTO LINE_STATION(line, station_id, pre_station_id, distance, duration)
VALUES (1, 3, 2, 1100, 4);
INSERT INTO LINE_STATION(line, station_id, pre_station_id, distance, duration)
VALUES (2, 2, null, 0, 0);
INSERT INTO LINE_STATION(line, station_id, pre_station_id, distance, duration)
VALUES (2, 4, 2, 1500, 6);
INSERT INTO LINE_STATION(line, station_id, pre_station_id, distance, duration)
VALUES (2, 5, 4, 900, 2);
INSERT INTO LINE_STATION(line, station_id, pre_station_id, distance, duration)
VALUES (3, 6, null, 0, 0);
INSERT INTO LINE_STATION(line, station_id, pre_station_id, distance, duration)
VALUES (3, 7, 6, 400, 3);
INSERT INTO LINE_STATION(line, station_id, pre_station_id, distance, duration)
VALUES (3, 8, 7, 600, 5);
INSERT INTO LINE_STATION(line, station_id, pre_station_id, distance, duration)
VALUES (4, 1, null, 0, 0);
INSERT INTO LINE_STATION(line, station_id, pre_station_id, distance, duration)
VALUES (4, 9, 1, 600, 10);
INSERT INTO LINE_STATION(line, station_id, pre_station_id, distance, duration)
VALUES (4, 5, 9, 100, 11);

INSERT INTO MEMBER(email, name, password)
VALUES ('test@test.com', 'test', '1q2w3e');
