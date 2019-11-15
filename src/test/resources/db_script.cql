

CREATE KEYSPACE IF NOT EXISTS mykeyspace WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};

use mykeyspace;

drop type if exists Credential;
CREATE type IF NOT EXISTS Credential
(
    id       uuid,
    username TEXT,
    password TEXT,
    domain   TEXT
);

-- insert into Credential (id, username, password, domain) values(now(), 'sam', 'verysecret', 'divine');
-- select * from Credential;

--------------------------------

drop table if exists Volume;
CREATE table IF NOT EXISTS Volume
(
    id          uuid primary key,
    mountPoint  TEXT,
    size        INT
);

insert into Volume (id, mountPoint, size) VALUES (now(), 'c:/', 12345);

select * from Volume;

--------------------------------

-- https://docs.datastax.com/en/dse/5.1/cql/cql/cql_using/refCollectionType.html
drop table if exists Workload;
create table if not exists Workload
(
    id          uuid,
    ip          TEXT,
    credential  uuid,
    volumes      list <uuid>,
    PRIMARY KEY (id, ip),
);

-- insert into Workload(id, ip, credential) VALUES (now(), '127.0.0.1', ea685197-3f56-499d-9e4e-6603601a1022);

-------------------------------
drop table if exists TargetCloud;
create table if not exists TargetCloud (
    id      uuid primary key,
    cloudType    TEXT,
    credentials list <uuid>,
    targetWorkload uuid,
);

-------------------------------
drop table if exists Migration;
create table if not exists Migration (
   id      uuid primary key,
   mountPoints list<TEXT>,
   sourceWorkload uuid,
   targetCloud uuid,
   migrationState TEXT
);
