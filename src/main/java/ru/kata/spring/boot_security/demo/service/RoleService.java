package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface RoleService {
	public List<Role> getAll();
	
	public Role getById(Long id);
	
	public Role getByName(String name);
	
	public void save(Role role);
	
	public void delete(Role role);
	
	public void deleteById(Long id);
}
