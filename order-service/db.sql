CREATE TABLE address (
    id VARCHAR(255) PRIMARY KEY,
    detail VARCHAR(255),
    ward VARCHAR(255),
    district VARCHAR(255),
    city VARCHAR(255),
    country VARCHAR(255),
    updated_by VARCHAR(255),
    created_by VARCHAR(255),
    created_date DATE,
    last_modified_date DATE
);
CREATE TABLE receiver (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255),
    phone_number VARCHAR(15),
    account_id VARCHAR(255),
    status BIT(1),
    address_id VARCHAR(255),
    updated_by VARCHAR(255),
    created_by VARCHAR(255),
    created_date DATE,
    last_modified_date DATE,
    FOREIGN KEY (address_id) REFERENCES address(id)
);
CREATE TABLE purchase_order (
    id VARCHAR(255) PRIMARY KEY,
    code VARCHAR(255),
    status VARCHAR(255),
    receiver_id VARCHAR(255),
    updated_by VARCHAR(255),
    created_by VARCHAR(255),
    created_date DATE,
    last_modified_date DATE,
    FOREIGN KEY (receiver_id) REFERENCES receiver(id)
);
CREATE TABLE order_detail (
    id VARCHAR(255) PRIMARY KEY,
    quantity INT,
    unit_price DOUBLE,
    product_id VARCHAR(255),
    comment_status BIT(1),
    status VARCHAR(255),
    customer_id VARCHAR(255),
    order_id VARCHAR(255),
    updated_by VARCHAR(255),
    created_by VARCHAR(255),
    created_date DATE,
    last_modified_date DATE,
    FOREIGN KEY (order_id) REFERENCES purchase_order(id)
);
