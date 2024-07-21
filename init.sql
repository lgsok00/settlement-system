-- DB 생성
CREATE DATABASE IF NOT EXISTS settlement;

-- USER 생성 및 권한 부여
CREATE USER IF NOT EXISTS 'settlement'@'%' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON settlement.* TO 'settlement'@'%';
FLUSH PRIVILEGES;