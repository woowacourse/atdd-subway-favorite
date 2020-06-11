insert into MEMBER (id, email, name, password)
VALUES (1, 'turtle@woowa.dev', 'turtle', '1111'),
       (2, 'hodol@woowa.dev', 'hodol', '2222');

insert into STATION (id, name)
values (1, 'wangsimni'),
       (2, 'seoulforest'),
       (3, 'apgujeong'),
       (4, 'seollueng'),
       (5, 'samsung'),
       (6, 'jamsilsaenae'),
       (7, 'jamsil'),
       (8, 'sukchon'),
       (9, 'mongchoncastle');

insert into LINE (id, name, start_time, end_time, interval_time)
values (1, 'bundang', '05:30', '23:30', 10),
       (2, 'line2', '05:30', '23:30', 10),
       (3, 'line8', '05:30', '23:30', 10);

insert into LINE_STATION (line, pre_station_id, station_id, distance, duration)
values (1, null, 1, 0, 0),
       (1, 1, 2, 4, 2),
       (1, 2, 3, 12, 3),
       (1, 3, 4, 5, 6),
       (2, null, 4, 10, 2),
       (2, 4, 5, 5, 2),
       (2, 5, 6, 8, 8),
       (2, 6, 7, 3, 10),
       (3, null, 7, 2, 4),
       (3, 7, 8, 8, 3),
       (3, 8, 9, 9, 2);
