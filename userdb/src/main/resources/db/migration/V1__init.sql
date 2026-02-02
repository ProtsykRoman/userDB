CREATE SCHEMA IF NOT EXISTS userdb;

CREATE TABLE IF NOT EXISTS certificate_types (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    is_active BOOLEAN
);

CREATE TABLE IF NOT EXISTS databases (
	id SERIAL PRIMARY KEY,
	name VARCHAR(100) NOT NULL,
	is_active BOOLEAN
)

CREATE TABLE IF NOT EXISTS databaseroles (
	id SERIAL PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
	database_id INTEGER REFERENCES databases(id),
	is_active BOOLEAN
)

CREATE TABLE IF NOT EXISTS ranks (
	id SERIAL PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
	is_active BOOLEAN
)

CREATE TABLE IF NOT EXISTS departments (
	id SERIAL PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
	parent_id INTEGER REFERENCES departments(id),
	is_active BOOLEAN
)

CREATE TABLE IF NOT EXISTS dbusers (
	id SERIAL PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
	rank_id INTEGER REFERENCES ranks(id),
	department_id INTEGER REFERENCES departments(id),
	identification_number INTEGER,
	is_active BOOLEAN
)

CREATE TABLE IF NOT EXISTS db_users_access(
	id SERIAL PRIMARY KEY,
	dbuser_id INTEGER REFERENCES dbusers(id),
	database_id INTEGER REFERENCES databases(id),
	access_expiration_date DATE
)

CREATE TABLE IF NOT EXISTS db_users_roles(
	id SERIAL PRIMARY KEY,
	dbuser_id INTEGER REFERENCES dbusers(id),
	database_roles_id INTEGER REFERENCES databaseroles(id)
)

CREATE TABLE IF NOT EXISTS db_users_certificates(
	id SERIAL PRIMARY KEY,
	dbuser_id INTEGER REFERENCES dbusers(id),
	certificate_type_id INTEGER REFERENCES certificate_types(id),
	expiration_date DATE,
	number VARCHAR(100),
	is_blocked BOOLEAN
)

CREATE TABLE IF NOT EXISTS users (
	id SERIAL PRIMARY KEY,
	username VARCHAR(100) NOT NULL,
	role VARCHAR(50) NOT NULL,
	department_id INTEGER REFERENCES departments(id),
	password VARCHAR(100) NOT NULL,
	is_active BOOLEAN
)
