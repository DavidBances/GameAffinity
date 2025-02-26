CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password CHAR(255) NOT NULL,
    role ENUM('ADMINISTRATOR', 'MODERATOR', 'REGULAR_USER') NOT NULL DEFAULT 'REGULAR_USER'
);

CREATE TABLE libraries (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE games (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    genre VARCHAR(255),
    avg_score DOUBLE DEFAULT 0,
    description TEXT NULL,
    image_url VARCHAR(255),
    developer VARCHAR(255),
    release_year INT NOT NULL,
);

CREATE TABLE library_games (
    id INT AUTO_INCREMENT PRIMARY KEY,
    library_id INT NOT NULL,
    user_id INT NOT NULL,
    game_id INT NOT NULL,
    state ENUM('AVAILABLE', 'PLAYING', 'PAUSED', 'COMPLETED', 'DROPPED', 'WISHLIST') DEFAULT 'AVAILABLE',
    game_score INT DEFAULT NULL,
    review TEXT NULL,
    time_played DECIMAL(5,1) DEFAULT 0,
    FOREIGN KEY (library_id) REFERENCES libraries(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (game_id) REFERENCES games(id) ON DELETE CASCADE
);

CREATE TABLE friendships (
    id INT AUTO_INCREMENT PRIMARY KEY,
    requester_id INT NOT NULL,
    receiver_id INT NOT NULL,
    status ENUM('PENDING', 'ACCEPTED', 'REJECTED') NOT NULL,
    FOREIGN KEY (requester_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (receiver_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE platforms (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE game_platforms (
    game_id INT NOT NULL,
    platform_id INT NOT NULL,
    PRIMARY KEY (game_id, platform_id),
    FOREIGN KEY (game_id) REFERENCES games(id) ON DELETE CASCADE,
    FOREIGN KEY (platform_id) REFERENCES platforms(id) ON DELETE CASCADE
);
