insert into member (email, name, password) values ('test@test.com', 'test', 'q123');

insert into line (id, name, start_time, end_time, interval_time) values (1, '2호선', CURRENT_TIME, CURRENT_TIME, 10);

insert into station (id, name) values (1, '강남역');
insert into station (id, name) values (2, '선릉역');
insert into station (id, name) values (3, '삼성역');

insert into line_station (line, pre_station_id, station_id, distance, duration) values (1, null, 1, 10, 10);
insert into line_station (line, pre_station_id, station_id, distance, duration) values (1, 1, 2, 10, 10);
insert into line_station (line, pre_station_id, station_id, distance, duration) values (1, 2, 3, 10, 10);



