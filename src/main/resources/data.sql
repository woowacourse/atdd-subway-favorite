insert into station
values (1, '신당', CURRENT_TIMESTAMP()),
       (2, '상왕십리', CURRENT_TIMESTAMP()),
       (3, '왕십리', CURRENT_TIMESTAMP()),
       (4, '한양대', CURRENT_TIMESTAMP()),
       (5, '뚝섬', CURRENT_TIMESTAMP()),
       (6, '충무로', CURRENT_TIMESTAMP());

insert into line
values (1, '1호선', CURRENT_TIME(), CURRENT_TIME(), 8, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (2, '2호선', CURRENT_TIME(), CURRENT_TIME(), 8, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (3, '3호선', CURRENT_TIME(), CURRENT_TIME(), 8, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (4, '4호선', CURRENT_TIME(), CURRENT_TIME(), 18, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

insert into line_station(line, station_id, pre_station_id, distance, duration, created_at,
                         updated_at)
values (1, 1, null, 0, 0, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (1, 2, 1, 5, 3, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (1, 3, 2, 5, 2, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (1, 4, 3, 3, 4, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (2, 4, null, 0, 0, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (2, 2, 4, 9, 1, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (2, 5, 2, 3, 5, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (3, 6, null, 0, 0, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
