CREATE TABLE todo (
                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                      text VARCHAR(255),
                      done TINYINT DEFAULT 0
);