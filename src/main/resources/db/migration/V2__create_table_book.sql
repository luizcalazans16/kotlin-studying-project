CREATE TABLE book(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    status VARCHAR(255) NOT NULL,
    customer_id INT NOT NULL,

    FOREIGN KEY (customer_id) REFERENCES customer(id)
);