package ua.com.userdb.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.userdb.model.DBUser;

public interface DBUserRepository extends JpaRepository<DBUser, Integer>{
	Optional<DBUser> findByName(String name);
}
