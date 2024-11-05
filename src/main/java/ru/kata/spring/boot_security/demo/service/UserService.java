package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {
	public List<User> getAll();
	public List<User> getByRole(Role role);
	public User getById(Long id);
	public void save(User user);
	public void delete(User user);
	public void deleteById(Long id);
	public User findByEmail(String email);
}