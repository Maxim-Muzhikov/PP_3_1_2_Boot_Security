package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
	
	private final RoleRepository roleRepository;
	
	@Autowired
	public RoleServiceImpl(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}
	
	@Override
	public List<Role> getAll() {
		return roleRepository.findAll();
	}
	
	@Override
	public Role getById(Long id) {
		return roleRepository.findById(id).orElse(null);
	}
	
	@Override
	public Role getByName(String name) {
		return roleRepository.findByName(name);
	}
	
	@Override
	public void set(Role role) {
		roleRepository.save(role);
	}
	
	@Override
	public void delete(Role role) {
		roleRepository.delete(role);
	}
	
	@Override
	public void deleteById(Long id) {
		roleRepository.deleteById(id);
	}
	
}
