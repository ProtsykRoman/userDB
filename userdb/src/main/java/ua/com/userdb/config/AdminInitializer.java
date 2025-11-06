package ua.com.userdb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import ua.com.userdb.dao.UserRepository;
import ua.com.userdb.model.Role;
import ua.com.userdb.model.User;

@Component
public class AdminInitializer implements ApplicationRunner{
	private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    @Value("${admin.password}")
    private String adminPassword;

    public AdminInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode(adminPassword)); 
            admin.setRole(Role.ADMIN);

            userRepository.save(admin);
            System.out.println("✔️ Admin user created");
        }
    }
}
