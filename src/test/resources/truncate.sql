DROP TABLE FAVORITE;
DROP TABLE STATION;
create table if not exists STATION
(
    id bigint auto_increment not null,
    name varchar(255) not null unique,
    created_at datetime,
    primary key(id)
);
create table if not exists FAVORITE
(
    id bigint auto_increment not null,
    member_id bigint not null,
    source_id bigint(255),
    target_id bigint(255),
    primary key(id),
    FOREIGN KEY(source_id) REFERENCES STATION ON DELETE CASCADE ,
    FOREIGN KEY(target_id) REFERENCES STATION ON DELETE CASCADE
);
TRUNCATE TABLE LINE;
TRUNCATE TABLE LINE_STATION;
TRUNCATE TABLE MEMBER;