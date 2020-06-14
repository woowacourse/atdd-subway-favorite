INSERT INTO LINE (name, start_time, end_time, interval_time)
VALUES ('1호선', '02:00:00', '13:00:00', 10),
       ('2호선', '02:00:00', '13:00:00', 10),
       ('3호선', '02:00:00', '14:00:00', 10);
INSERT INTO STATION (name)
VALUES ('삼성'),
       ('잠실'),
       ('석촌'),
       ('광나루'),
       ('지축'),
       ('삼송'),
       ('마두'),
       ('정발산'),
       ('백석');
INSERT INTO LINE_STATION (line, station_id, pre_station_id, distance, duration)
VALUES (1, 1, null, 20, 10),
       (1, 2, 1, 20, 10),
       (1, 3, 2, 20, 10),
       (1, 4, 3, 20, 10),
       (1, 5, 4, 20, 10),
       (2, 1, null, 10, 20),
       (2, 6, 1, 10, 20),
       (2, 3, 6, 10, 20),
       (2, 7, 3, 10, 20),
       (2, 5, 7, 10, 20),
       (3, 1, null, 50, 50),
       (3, 8, 1, 50, 50),
       (3, 3, 8, 50, 50),
       (3, 9, 3, 50, 50),
       (3, 5, 9, 50, 50);

INSERT INTO MEMBER (email, name, password)
VALUES ('jun@naver.com', 'tiger', '1234');