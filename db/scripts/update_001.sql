CREATE TABLE item (
                      id SERIAL PRIMARY KEY,
                      description TEXT,
                      created TIMESTAMP,
                      user_id integer,
                      done BOOLEAN
);

create table j_role (
                        id serial primary key,
                        name varchar(2000)
);

create table j_user (
                        id serial primary key,
                        name varchar(2000),
                        email varchar(2000),
                        password varchar(255),
                        role_id int not null references j_role(id)
);

insert into j_role values (1, 'ADMIN');
insert into j_role values (2, 'USER');
