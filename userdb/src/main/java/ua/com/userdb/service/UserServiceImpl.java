package ua.com.userdb.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ua.com.userdb.dao.UserRepository;
import ua.com.userdb.model.User;

@Service
public class UserServiceImpl implements UserService{
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	
	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public Optional<User> findUserById(Integer id) {
		return userRepository.findById(id);
	}

	@Override
	public Optional<User> findUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public User createUser(User user) {
		if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
		return userRepository.save(user);
	}

	@Override
	public Optional<User> updateUser(Integer id, User user) {
		Optional<User> exitingUser = userRepository.findById(id);
		if(exitingUser.isPresent()) {
			User updated = exitingUser.get();
			updated.setDepartment(user.getDepartment());
			updated.setIsActive(user.getIsActive());
			updated.setPassword(user.getPassword());
			updated.setRole(user.getRole());
			updated.setUsername(user.getUsername());
			userRepository.save(updated);
			return Optional.of(updated);
		}else {
			return Optional.empty();
		}
	}

	@Override
	public boolean deleteUser(Integer id) {
		if(userRepository.existsById(id)) {
			userRepository.deleteById(id);
			return true;
		}else {
			return false;
		}
	}

}
