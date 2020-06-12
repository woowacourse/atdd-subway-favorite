INSERT INTO STATION (name) VALUES ('핀역');
INSERT INTO STATION (name) VALUES ('유안역');
INSERT INTO STATION (name) VALUES ('시카역');
INSERT INTO STATION (name) VALUES ('디디역');
INSERT INTO STATION (name) VALUES ('포비역');

INSERT INTO LINE (name, start_time, end_time, interval_time) VALUES ('1호선', '10:00', '23:00', 10);
INSERT INTO LINE (name, start_time, end_time, interval_time) VALUES ('2호선', '09:00', '22:00', 20);
INSERT INTO LINE (name, start_time, end_time, interval_time) VALUES ('3호선', '09:00', '22:00', 20);

INSERT INTO LINE_STATION (line, station_id, pre_station_id, distance, duration) VALUES (1, 1, null, 0, 0);
INSERT INTO LINE_STATION (line, station_id, pre_station_id, distance, duration) VALUES (1, 2, 1, 1, 10);
INSERT INTO LINE_STATION (line, station_id, pre_station_id, distance, duration) VALUES (2, 2, null, 0, 0);
INSERT INTO LINE_STATION (line, station_id, pre_station_id, distance, duration) VALUES (2, 3, 2, 10, 1);
INSERT INTO LINE_STATION (line, station_id, pre_station_id, distance, duration) VALUES (2, 4, 3, 10, 1);
INSERT INTO LINE_STATION (line, station_id, pre_station_id, distance, duration) VALUES (3, 1, null, 0, 0);
INSERT INTO LINE_STATION (line, station_id, pre_station_id, distance, duration) VALUES (3, 4, 1, 10, 1);
