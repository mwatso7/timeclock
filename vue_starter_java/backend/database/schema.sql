BEGIN TRANSACTION;

DROP TABLE IF EXISTS users;

CREATE TABLE users (
  id serial PRIMARY KEY,
  username varchar(255) NOT NULL UNIQUE,     -- Username
  email varchar(255) NOT NULL UNIQUE,
  password varchar(32) NOT NULL,      -- Password
  salt varchar(256) NOT NULL,          -- Password Salt
  role varchar(255) NOT NULL default('user'),
  isDisabled boolean default('false')
);

drop table if exists timestamps cascade;


create table timestamps (

id serial PRIMARY KEY,
stamp timestamp not null DEFAULT NOW(),
username varchar(255) not null,
isIn varchar(255),

constraint fk_timestamps_username foreign key(username) references users(username)
);

drop table if exists recipients cascade;

create table recipients (

id serial PRIMARY KEY,
email varchar(255) NOT NULL,
username varchar(255) not null,

constraint fk_recipients_username foreign key(username) references users(username)
);

COMMIT TRANSACTION;

