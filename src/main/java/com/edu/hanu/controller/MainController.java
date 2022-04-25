package com.edu.hanu.controller;


import com.edu.hanu.model.*;
import com.edu.hanu.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;

@Controller
public class MainController {
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	PlaneRepository planeRepository;
	@Autowired
	AirlineRepository airlineRepository;

	@Autowired
	AirportRepository airportRepository;

	@Autowired
	FlightRepository flightRepository;


//	homepage admin
	@GetMapping("/admin")
	public String admin(Model model) {
		List<Flight> flights = flightRepository.findAll();
		List<Airline> airlines = airlineRepository.findAll();
		List<Plane> planes = planeRepository.findAll();
		List<User> users = userRepository.findAll();
		model.addAttribute("flights",flights);
		model.addAttribute("airlines",airlines);
		model.addAttribute("planes",planes);
		model.addAttribute("users",users);
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
	@PostMapping("/signup")
		public String submit(@Valid User user, Model model, BindingResult result){
		if(result.hasErrors()){
			return "user/signup";
		}
		model.addAttribute("user", user);
		user.setPassword(passwordEncoder.encode(
				user.getPassword()
		));
		HashSet<Role> roles = new HashSet<>();
		roles.add(roleRepository.findByName("ROLE_CUSTOMER"));
		user.setRoles(roles);
		userRepository.save(user);
		return "redirect:signup?success";
	}

}
