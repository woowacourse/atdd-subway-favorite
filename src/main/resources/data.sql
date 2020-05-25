insert into station (name) values ('강남역'), ('선릉역'), ('삼성역');

insert into LINE (id, name, start_time, end_time, interval_time)
values (1, '2호선', '05:30', '05:30', 10);

insert into line_station (line, pre_station_id, station_id, distance, duration)
values (1, null, 1, 0, 0),
        (1, 1, 2, 10, 10),
        (1, 2, 3, 10, 5);

insert into member (email, name, password) values ('test@test.com', 'tester', 'q123');


