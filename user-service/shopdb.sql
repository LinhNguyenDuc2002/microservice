CREATE DATABASE shop;

CREATE TABLE address(
	id bigint primary key not null,
    specific_address varchar(200),
    ward varchar(100),
    district varchar(100),
    city varchar(100) not null,
    country varchar(100) not null
);

CREATE TABLE shop(
	id bigint primary key not null,
    shop_name varchar(100) not null,
    phone varchar(15) not null,
    email varchar(100) not null,
    join_date Date not null,
    id_address bigint,
    FOREIGN KEY (id_address) REFERENCES address(id)
);

CREATE TABLE user(
	id bigint primary key not null,
	username varchar(100) not null,
    password varchar(200) not null,
    fullname varchar(100) not null,
    dob Date not null,
    email varchar(100) not null,
    phone varchar(12) not null,
    note varchar(100),
    join_date Date not null,
    status boolean not null,
    address_id bigint,
    FOREIGN KEY (address_id) REFERENCES address(id)
);

CREATE TABLE category(
	id bigint primary key not null,
    name varchar(100) not null,
    note text
);

CREATE TABLE product(
	id bigint primary key not null,
    product_name varchar(100) not null,
    category_id bigint,
    price double not null,
    quantity bigint,
    sold bigint,
    update_day Date not null,
    note text,
    FOREIGN KEY (category_id) REFERENCES category(id)
);

CREATE TABLE comment(
	id bigint PRIMARY KEY not null,
    user_id bigint not null,
    product_id bigint not null,
    comment_date Date not null,
    message text not null,
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (product_id) REFERENCES product(id)
);

CREATE TABLE role(
	id int primary key not null,
    role_name varchar(100) not null
);

CREATE TABLE bill(
	id bigint primary key not null,
	user_id bigint not null,
    address_id bigint,
    purchase_date Date not null,
    active boolean not null,
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (address_id) REFERENCES address(id)
);

CREATE TABLE details(
	id bigint primary key not null,
    user_id bigint not null,
    product_id bigint not null,
    bill_id bigint,
    quantity bigint not null,
    status boolean not null,
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (product_id) REFERENCES product(id),
    FOREIGN KEY (bill_id) REFERENCES bill(id)
);

CREATE TABLE image(
	id bigint primary key not null,
    format varchar(255),
    resource_type varchar(255),
    secure_url varchar(255),
    created_at Date,
    url varchar(255),
    public_id varchar(255),
    product_id bigint,
    comment_id bigint,
    user_id bigint,
    FOREIGN KEY (product_id) REFERENCES product(id),
    FOREIGN KEY (comment_id) REFERENCES comment(id),
    FOREIGN KEY (user_id) REFERENCES user(id)
);

INSERT INTO category values(1,"Phone","...");
INSERT INTO category values(2,"Ipad","...");
INSERT INTO category values(3,"Laptop","...");
INSERT INTO category values(4,"TV","...");
INSERT INTO category values(5,"Tủ lạnh","...");

INSERT into role values(1,"USER");
INSERT into role values(2,"EMPLOYEE");
INSERT into role values(3,"ADMIN");