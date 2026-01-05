CREATE SCHEMA IF NOT EXISTS userdb;


CREATE TABLE IF NOT EXISTS userdb.certificate_types (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE
);


CREATE TABLE IF NOT EXISTS userdb.databases (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE
);


CREATE TABLE IF NOT EXISTS userdb.databaseroles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    database_id INT NOT NULL REFERENCES userdb.databases(id)
);


CREATE TABLE IF NOT EXISTS userdb.ranks (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE
);


CREATE TABLE IF NOT EXISTS userdb.departments (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    parent_id INT REFERENCES userdb.departments(id),
    is_active BOOLEAN DEFAULT TRUE
);


CREATE TABLE IF NOT EXISTS userdb.dbusers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    rank_id INT REFERENCES userdb.ranks(id),
    department_id INT NOT NULL REFERENCES userdb.departments(id),
    identification_number INT,
    is_active BOOLEAN DEFAULT TRUE
);


CREATE TABLE IF NOT EXISTS userdb.db_users_access (
    id SERIAL PRIMARY KEY,
    dbuser_id INT NOT NULL REFERENCES userdb.dbusers(id),
    database_id INT NOT NULL REFERENCES userdb.databases(id),
    access_expiration_date DATE,
    is_blocked BOOLEAN
);


CREATE TABLE IF NOT EXISTS userdb.db_users_certificates (
    id SERIAL PRIMARY KEY,
    dbuser_id INT NOT NULL REFERENCES userdb.dbusers(id),
    certificate_type_id INT NOT NULL REFERENCES userdb.certificate_types(id),
    expiration_date DATE,
    number VARCHAR(255),
    is_blocked BOOLEAN
);


CREATE TABLE IF NOT EXISTS userdb.db_users_roles (
    id SERIAL PRIMARY KEY,
    dbuser_id INT NOT NULL REFERENCES userdb.dbusers(id),
    database_roles_id INT NOT NULL REFERENCES userdb.databaseroles(id)
);


CREATE TABLE IF NOT EXISTS userdb.users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    role VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    department_id INT REFERENCES userdb.departments(id)
);
