alter table users add column email varchar(255) null default null;

insert into users(id,email,name,password,username) values(101,'abc123@sdsd.com','asdsd','sdsd','sdsdssd');

INSERT INTO roles(id,name) VALUES(1,'ROLE_USER');

INSERT INTO roles(id,name) VALUES(2,'ROLE_PM');

INSERT INTO roles(id,name) VALUES(3,'ROLE_ADMIN');

insert into user_roles(user_id,role_id) values(101,1);
