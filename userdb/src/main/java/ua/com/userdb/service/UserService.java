package ua.com.userdb.service;

import java.util.List;
import java.util.Optional;

import ua.com.userdb.model.User;

public interface UserService {
	List<User> findAll();
	Optional<User> findUserById(Integer id);
	Optional<User> findUserByUsername(String username);
	User createUser(User user);
	Optional<User> updateUser(Integer id, User user);
	boolean deleteUser(Integer id);
}
