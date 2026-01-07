CREATE DATABASE IF NOT EXISTS gamestore;

USE gamestore;

-- Drop tables (run this if you want to reset schema)
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS tr_transaction;
DROP TABLE IF EXISTS mst_game;
DROP TABLE IF EXISTS ref_genre;
DROP TABLE IF EXISTS ref_user;

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
    FOREIGN KEY (genre_id) REFERENCES ref_genre (id) ON DELETE CASCADE,
    FOREIGN KEY (developer_id) REFERENCES ref_user (id) ON DELETE CASCADE
);

-- Transactions Table
CREATE TABLE tr_transaction (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    item_id VARCHAR(100) NOT NULL,
    amount INT,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_played BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES ref_user (id) ON DELETE CASCADE
);

-- =====================================================
-- DUMMY DATA - 10 Users, 10 Genres, 10 Games
-- =====================================================

-- Insert Users (3 Admins, 3 Developers, 4 Regular Users)
INSERT INTO ref_user (username, password, Saldo, role) VALUES
('admin1', 'admin123', 0, 'ADMIN'),
('admin2', 'admin123', 0, 'ADMIN'),
('superadmin', 'super123', 0, 'ADMIN'),
('devstudio1', 'dev123', 500000, 'DEVELOPER'),
('indiegames', 'dev123', 750000, 'DEVELOPER'),
('megacorp', 'dev123', 1200000, 'DEVELOPER'),
('player1', 'user123', 150000, 'USER'),
('gamer99', 'user123', 200000, 'USER'),
('proplayer', 'user123', 500000, 'USER'),
('casualgamer', 'user123', 75000, 'USER');

-- Insert Genres (10 Genres)
INSERT INTO ref_genre (name) VALUES
('Action'),
('Adventure'),
('RPG'),
('Strategy'),
('Simulation'),
('Sports'),
('Racing'),
('Horror'),
('Puzzle'),
('Fighting');

-- Insert Games (10 Games with different developers and genres)
INSERT INTO mst_game (name, genre_id, developer_id, price) VALUES
('Cyber Warrior 2077', 1, 4, 450000),      -- Action by devstudio1
('Lost Kingdom', 2, 5, 299000),             -- Adventure by indiegames
('Dragon Quest Ultimate', 3, 6, 550000),    -- RPG by megacorp
('Empire Builder', 4, 4, 350000),           -- Strategy by devstudio1
('City Life Simulator', 5, 5, 199000),      -- Simulation by indiegames
('Football Champions 2026', 6, 6, 400000),  -- Sports by megacorp
('Turbo Racing X', 7, 4, 299000),           -- Racing by devstudio1
('Nightmare Mansion', 8, 5, 249000),        -- Horror by indiegames
('Mind Bender Puzzles', 9, 6, 99000),       -- Puzzle by megacorp
('Street Fighter Ultimate', 10, 4, 350000); -- Fighting by devstudio1

-- Insert Sample Transactions (Games owned by users)
-- Player1 owns: Cyber Warrior 2077, Mind Bender Puzzles
INSERT INTO tr_transaction (user_id, item_id, amount, is_played) VALUES
(7, 'GM_1', 450000, TRUE),   -- player1 bought Cyber Warrior 2077, played
(7, 'GM_9', 99000, FALSE);   -- player1 bought Mind Bender Puzzles, not played

-- Gamer99 owns: Lost Kingdom, Nightmare Mansion, Turbo Racing X
INSERT INTO tr_transaction (user_id, item_id, amount, is_played) VALUES
(8, 'GM_2', 299000, TRUE),   -- gamer99 bought Lost Kingdom, played
(8, 'GM_8', 249000, TRUE),   -- gamer99 bought Nightmare Mansion, played
(8, 'GM_7', 299000, FALSE);  -- gamer99 bought Turbo Racing X, not played

-- Proplayer owns: Dragon Quest Ultimate, Empire Builder, Football Champions
INSERT INTO tr_transaction (user_id, item_id, amount, is_played) VALUES
(9, 'GM_3', 550000, TRUE),   -- proplayer bought Dragon Quest Ultimate, played
(9, 'GM_4', 350000, TRUE),   -- proplayer bought Empire Builder, played
(9, 'GM_10', 350000, FALSE); -- proplayer bought Street Fighter Ultimate, not played

-- Casualgamer owns: City Life Simulator
INSERT INTO tr_transaction (user_id, item_id, amount, is_played) VALUES
(10, 'GM_5', 199000, FALSE); -- casualgamer bought City Life Simulator, not played

-- Sample Top-Up Transactions
INSERT INTO tr_transaction (user_id, item_id, amount, is_played) VALUES
(7, 'TP_1001', 150000, FALSE),   -- player1 top-up
(8, 'TP_1002', 200000, FALSE),   -- gamer99 top-up
(9, 'TP_1003', 500000, FALSE),   -- proplayer top-up
(10, 'TP_1004', 75000, FALSE);   -- casualgamer top-up

-- =====================================================
-- Quick Login Credentials Reference:
-- =====================================================
-- ADMIN:
--   username: admin1      password: admin123
--   username: admin2      password: admin123
--   username: superadmin  password: super123
--
-- DEVELOPER:
--   username: devstudio1  password: dev123
--   username: indiegames  password: dev123
--   username: megacorp    password: dev123
--
-- USER:
--   username: player1     password: user123 (Saldo: 150000)
--   username: gamer99     password: user123 (Saldo: 200000)
--   username: proplayer   password: user123 (Saldo: 500000)
--   username: casualgamer password: user123 (Saldo: 75000)
-- =====================================================