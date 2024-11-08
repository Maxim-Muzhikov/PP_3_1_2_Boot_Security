package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	public List<User> getAll() {
		return userRepository.findAll();
	}
	
	@Override
	public List<User> getByRole(Role role) {
		return userRepository.findByRole(role);
	}
	
	@Override
	public User getById(Long id) {
		return userRepository.findById(id).orElse(null);
	}
	
	@Override
	public void save(User user) {
		if (user.getPassword() != null && user.getPassword().isEmpty()) {
			User userFromDB = getById(user.getId());
			user.setPassword(userFromDB.getPassword());
		} else if (user.getPassword() != null) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		}
		
		userRepository.save(user);
	}
	
	@Override
	public void delete(User user) {
		userRepository.delete(user);
	}
	
	@Override
	public void deleteById(Long id) {
		userRepository.deleteById(id);
	}
	
	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	@Override
	public User getUserForProfile(Long userId, UserDetails currentUser) {
		Collection<? extends GrantedAuthority> currentUserAuthorities = currentUser.getAuthorities();
		boolean isCurrentUserAdmin = currentUserAuthorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
		boolean isCurrentUserUser = currentUserAuthorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"));
		
		User user;
		
		if (userId != null) {
			boolean isAllowedToSee = isCurrentUserAdmin || (isCurrentUserUser &&
					Objects.equals(findByEmail(currentUser.getUsername()).getId(), userId));
			
			if (!isAllowedToSee) {
				return null; // Indicate unauthorized access
			}
			
			user = getById(userId);
		} else {
			user = findByEmail(currentUser.getUsername());
		}
		
		return user;
	}
	
	@Override
	public boolean isCurrentUser(User user, UserDetails currentUser) {
		if (user == null || currentUser == null) return false;
		return Objects.equals(user.getId(), findByEmail(currentUser.getUsername()).getId());
	}
//
//	private static List<UserDTO> getUserDTOList(List<User> users) {
//		return users.stream().map(UserDTO::new).collect(Collectors.toList());
//	}
}
