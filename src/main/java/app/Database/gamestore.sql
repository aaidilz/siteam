CREATE DATABASE IF NOT EXISTS gamestore;

USE gamestore;

-- Users Table
CREATE TABLE IF NOT EXISTS user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

-- Genres Table
CREATE TABLE IF NOT EXISTS genre (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Games Table
CREATE TABLE IF NOT EXISTS game (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    genre_id INT,
    developer_id INT,
    price INT,
    FOREIGN KEY (genre_id) REFERENCES genre (id)
);

-- Transactions Table
CREATE TABLE IF NOT EXISTS transaction (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    game_id INT,
    amount INT,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user (id),
    FOREIGN KEY (game_id) REFERENCES game (id)
);