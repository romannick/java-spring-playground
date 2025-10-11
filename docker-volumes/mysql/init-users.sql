CREATE DATABASE IF NOT EXISTS playground;

CREATE USER IF NOT EXISTS 'userService'@'%' IDENTIFIED BY 'password';
CREATE USER IF NOT EXISTS 'productService'@'%' IDENTIFIED BY 'password';

GRANT ALL PRIVILEGES ON playground.* TO 'userService'@'%';
GRANT ALL PRIVILEGES ON playground.* TO 'productService'@'%';

FLUSH PRIVILEGES;
