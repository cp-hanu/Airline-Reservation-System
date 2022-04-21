package com.edu.hanu.controller;


import com.edu.hanu.model.Role;
import com.edu.hanu.model.User;
import com.edu.hanu.repository.RoleRepository;
import com.edu.hanu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

//import javax.validation.Valid;
import java.util.HashSet;

@Controller
public class MainController {
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
//	PasswordEncoder passwordEncoder;

	@GetMapping("/test")
	public String test(){
		return "user/test2";
	}
 	@GetMapping("/web")
	public String food(){
		return "web";
	}

//	@GetMapping("/home")
//	public String homepage(){
//		return "user/home";
//	}

	@GetMapping("/admin")
	public String admin() {
		return "admin/index";
	}
	
	@GetMapping("/403")
	public String accessDenied() {
		return "user/403";
	}
	
	@GetMapping("/login") 
	public String getLogin() {
		return "user/login";
	}

	@GetMapping("/signup")
	public String signup(Model model) {
		User user = new User();
		model.addAttribute("user",user);

		return "user/signup";
	}
//	@PostMapping("/signup")
//		public String submit(@Valid User user, Model model, BindingResult result){
//		if(result.hasErrors()){
//			return "user/signup";
//		}
//		model.addAttribute("user", user);
//		user.setPassword(passwordEncoder.encode(
//				user.getPassword()
//		));
//		HashSet<Role> roles = new HashSet<>();
//		roles.add(roleRepository.findByName("ROLE_CUSTOMER"));
//		user.setRoles(roles);
//		userRepository.save(user);
//		return "redirect:signup?success";
//	}
	
}
