package ua.com.userdb.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.userdb.model.Database;

public interface DatabaseRepository extends JpaRepository<Database, Integer>{
	Optional<Database> findByName(String name);
}
