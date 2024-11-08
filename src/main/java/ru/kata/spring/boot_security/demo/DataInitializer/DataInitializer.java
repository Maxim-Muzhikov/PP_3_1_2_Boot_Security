package ru.kata.spring.boot_security.demo.DataInitializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {
	
	private final UserService userService;
	private final RoleService roleService;
	
	@Autowired
	public DataInitializer(UserService userService, RoleService roleService) {
		this.userService = userService;
		this.roleService = roleService;
	}
	
	@Override
	public void run(String... args) throws Exception {
		Role roleAdmin = roleService.getByName("ADMIN");
		if (roleAdmin == null) {
			roleAdmin = new Role("ADMIN");
			roleService.save(roleAdmin);
		}
		
		Role roleUser = roleService.getByName("USER");
		if (roleUser == null) {
			roleUser = new Role("USER");
			roleService.save(roleUser);
		}
		
		if (userService.findByEmail("user@mail.ru") == null) {
			User user = new User(
					"user",
					"user",
					"user@mail.ru",
					"user");
			user.setRoles(Set.of(roleUser));
			userService.save(user);
		}
		
		if (userService.findByEmail("admin@mail.ru") == null) {
			User admin = new User(
					"admin",
					"admin",
					"admin@mail.ru",
					"admin");
			admin.setRoles(Set.of(roleAdmin, roleUser));
			userService.save(admin);
		}
	}
}