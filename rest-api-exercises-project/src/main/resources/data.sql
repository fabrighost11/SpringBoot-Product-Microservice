CREATE TABLE products
(
    id bigInt AUTO_INCREMENT PRIMARY KEY,
    name varchar(250),
	price double(250),
	stock int,
	type varchar(250)
);

insert into products (id,name,price,stock,type) values (1,'Washing machine',250.99,22,'HOME_APPLIANCE');
insert into products (id,name,price,stock,type) values (2,'television',480.75,38,'TECHNOLOGICAL');
insert into products (id,name,price,stock,type) values (3,'Mobile phone',184,75,'TECHNOLOGICAL');
insert into products (id,name,price,stock,type) values (4,'Table',74.99,15,'FURNITURE');
insert into products (id,name,price,stock,type) values (5,'Wood chair',24.74,50,'FURNITURE');
insert into products (id,name,price,stock,type) values (6,'Oven',125.99,23,'HOME_APPLIANCE');
insert into products (id,name,price,stock,type) values (7,'Sofa',56.79,19,'FURNITURE');
insert into products (id,name,price,stock,type) values (8,'Shower',535.99,14,'HOME_APPLIANCE');
insert into products (id,name,price,stock,type) values (9,'Tablet',264.00,45,'TECHNOLOGICAL');
insert into products (id,name,price,stock,type) values (10,'PlayStation 5',900.00,12,'TECHNOLOGICAL');