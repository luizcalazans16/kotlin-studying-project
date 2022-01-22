CREATE TABLE purchase (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    nfe VARCHAR(255),
    created_at DATETIME NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customer(id)
);

CREATE TABLE purchase_book (
    purchase_id INT NOT NULL,
    book_id INT NOT NULL,
    FOREIGN KEY (purchase_id) REFERENCES purchase(id),
    FOREIGN KEY (book_id) REFERENCES book(id),
    PRIMARY KEY (purchase_id, book_id)
);