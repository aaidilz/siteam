CREATE DATABASE gamestore;

USE gamestore;

-- Drop tables (run this if you want to reset schema)
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE tr_transaction;
DROP TABLE mst_game;
DROP TABLE ref_genre;
DROP TABLE ref_user;

SET FOREIGN_KEY_CHECKS = 1;

-- Users Table
CREATE TABLE ref_user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    Saldo INT DEFAULT 0,
    role VARCHAR(50) NOT NULL
);

-- Genres Table
CREATE TABLE ref_genre (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Games Table
CREATE TABLE mst_game (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    genre_id INT,
    developer_id INT,
    price INT,
    FOREIGN KEY (genre_id) REFERENCES ref_genre (id),
    FOREIGN KEY (developer_id) REFERENCES ref_user (id)
);

-- Transactions Table
CREATE TABLE tr_transaction (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    item_id VARCHAR(100) NOT NULL,
    amount INT,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES ref_user (id)
);