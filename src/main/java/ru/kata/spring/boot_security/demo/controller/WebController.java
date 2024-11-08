package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class WebController {
	
	private final UserService userService;
	private final RoleService roleService;
	
	public WebController(UserService userService, RoleService roleService) {
		this.userService = userService;
		this.roleService = roleService;
	}
	
	
	@GetMapping({"/", "/index"})
	public String index() {
		return "index";
	}
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	
	@GetMapping("/admin")
	public String admin(Model model) {
		List<UserDTO> users = userService.getAll().stream().map(UserDTO::new).collect(Collectors.toList());
		model.addAttribute("users", users);
		return "admin/users";
	}
	
	@GetMapping("/admin/user-edit")
	public String userEdit(Model model) {
		model.addAttribute("user", new User());
		List<Role> roles = roleService.getAll();
		model.addAttribute("allRoles", roles);
		return "admin/user-edit";
	}
	
	@GetMapping("/admin/user-edit/{id}")
	public String userEdit(@PathVariable Long id, Model model) {
		UserDTO user = new UserDTO(userService.getById(id));
		if (user == null) {
			return "redirect:/error";
		}
		model.addAttribute("user", user);
		List<Role> roles = roleService.getAll();
		model.addAttribute("allRoles", roles);
		return "admin/user-edit";
	}
	
	@PostMapping("/admin/user-save")
	public String userSave(User user) {
		userService.save(user);
		return "redirect:/admin";
	}
	
	@GetMapping("/admin/user-delete/{id}")
	public String userDelete(@PathVariable Long id) {
		userService.deleteById(id);
		return "redirect:/admin";
	}
	
	
	@GetMapping("/user")
	public String userHome(@RequestParam(value = "userId", required = false) Long userId,
	                       @AuthenticationPrincipal UserDetails currentUser,
	                       Model model) {
		
		UserDTO userDTO = new UserDTO(userService.getUserForProfile(userId, currentUser));
		
		if (userDTO == null) {
			return "redirect:/user";
		}
		
		boolean isCurrentUser = userService.isCurrentUser(userService.getById(userDTO.getId()), currentUser);
		
		model.addAttribute("user", userDTO);
		model.addAttribute("isCurrentUser", isCurrentUser);
		
		return "user/profile";
	}
	
	@GetMapping("/user/{id}")
	public String userHome(@PathVariable Long id, Model model) {
		UserDTO userDTO = new UserDTO(userService.getById(id));
		if (userDTO == null) {
			return "redirect:/error?string=userNotFound";
		}
		model.addAttribute("user", userDTO);
		return "user/profile";
	}
	
	
}
