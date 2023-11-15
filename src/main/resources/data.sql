insert into product values (1, 'aCode', 'EUR', 'productDescription', 12.54, 10, true);

insert into shop_user (username, password, firstname, surname, city, number, street, zipcode)
values ('sallyanne', 'password', 'Sally', 'Anne', 'Bucuresti', '1', 'Aviatorilor', '123456'),
('johndoe', 'password2', 'John', 'Doe', 'Timisoara', '22', 'Calea Victoriei', '76549');

insert into user_roles values (1, 'ADMIN'), (1, 'EXPEDITOR');
insert into user_roles values (2, 'CLIENT');