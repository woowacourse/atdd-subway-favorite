INSERT INTO MEMBER VALUES ( 1, 'a@a.com', 'a', '1');

insert into LINE values (1, '1호선', CURRENT_TIME(), CURRENT_TIME(), 8, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
insert into LINE values (2, '2호선', CURRENT_TIME(), CURRENT_TIME(), 8, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

insert into STATION values (1, '신당', CURRENT_TIMESTAMP());
insert into STATION values (2, '상왕십리', CURRENT_TIMESTAMP());
insert into STATION values (3, '왕십리', CURRENT_TIMESTAMP());
insert into STATION values (4, '한양대', CURRENT_TIMESTAMP());
insert into STATION values (5, '뚝섬', CURRENT_TIMESTAMP());

insert into LINE_STATION values (1, 1, null, 0, 10, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP() );
insert into LINE_STATION values (1, 2, 1, 5, 10, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP() );
insert into LINE_STATION values (1, 3, 2, 5, 10, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP() );
insert into LINE_STATION values (1, 4, 3, 3, 10, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP() );

insert into LINE_STATION values (2, 4, null, 0, 10, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP() );
insert into LINE_STATION values (2, 2, 4, 9, 10, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP() );
insert into LINE_STATION values (2, 5, 2, 3, 10, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP() );

insert into FAVORITE values (1, 1, 1, 5);
