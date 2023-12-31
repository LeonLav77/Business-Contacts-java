-- Filename: 10_V1__Create_Company_Table.sql
DROP TABLE IF EXISTS contacts;
DROP TABLE IF EXISTS companies;

CREATE TABLE IF NOT EXISTS companies (
       id BIGINT PRIMARY KEY AUTO_INCREMENT,
       name VARCHAR(255) NOT NULL,
       industry VARCHAR(255),
       headquarters VARCHAR(255),
       website VARCHAR(255),
       created_at DATE
);