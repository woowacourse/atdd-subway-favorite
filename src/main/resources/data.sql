insert into member (email, name, password) values ('test@test.com', 'test', 'q123');

insert into line (name, start_time, end_time, interval_time) values ('2호선', CURRENT_TIME, CURRENT_TIME, 10);

insert into station (name) values ('강남역');
insert into station (name) values ('선릉역');
insert into station (name) values ('삼성역');

insert into line_station (line, pre_station_id, station_id) values (1, 1, 3);



