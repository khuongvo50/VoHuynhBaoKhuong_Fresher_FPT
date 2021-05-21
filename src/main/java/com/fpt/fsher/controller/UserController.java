package com.fpt.fsher.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fpt.fsher.entity.Role;
import com.fpt.fsher.entity.User;
import com.fpt.fsher.repository.RoleRepository;
import com.fpt.fsher.repository.UserRepository;

@Controller
public class UserController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@RequestMapping("/")
	public String Home(Model model) {
		// User user =
		// userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
		// List<User> list = userRepository.findAllUserByRole();
		List<User> list = userRepository.findAll();
		model.addAttribute("usall", list);

		return "home";
	}

	@RequestMapping("/login")
	public String enterLoginPage(Model model) {
		return "login";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logoutPage(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}

		return "redirect:/";
	}

	@RequestMapping("/signup")
	public String createUserForm(Model model) {
		User user = new User();

		model.addAttribute("users", user);
		return "signup";
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public ModelAndView createUser(ModelAndView modelAndView, @ModelAttribute("user") User user) {

		user.setPassWord(bCryptPasswordEncoder.encode(user.getPassWord()));

		Set<Role> ltk = new HashSet<Role>();
		Role role = roleRepository.findById((long) 1);
		ltk.add(role);

		user.setRole(ltk);
		user.setStatus("enable");

		User exitEmail = userRepository.findUserByEmail(user.getEmail());
		if (exitEmail != null) {
			modelAndView.setViewName("signup");
		} else if (userRepository.save(user) != null) {
			modelAndView.setViewName("/login");
		} else {
			modelAndView.setViewName("/signup");
		}

		return modelAndView;
	}

	@GetMapping("/delete-user/{id}")
	public String deleteUser(@PathVariable("id") String id, Model model) {
		User user = userRepository.findById(id).get();
		if (user != null) {
			userRepository.delete(user);
		}

		return "redirect:/";
	}

	@GetMapping("/update-user/{id}")
	public String showUpdateForm(@PathVariable("id") String id, Model model) {
		User user = userRepository.findById(id).get();
		model.addAttribute("user", user);
		return "update-user";
	}

	@PostMapping("/update-user/{id}")
	public String updateUser(@PathVariable("id") String id, User user, BindingResult result, Model model) {
		if (result.hasErrors()) {
			user.setId(id);
		}

		userRepository.save(user);
		return "redirect:/";
	}

	@RequestMapping(value = "/create-user", method = RequestMethod.POST)
	public ModelAndView createUserAdmin(ModelAndView modelAndView, @ModelAttribute("user") User user,
			@RequestParam("idLoai") String id) {

		user.setPassWord(bCryptPasswordEncoder.encode(user.getPassWord()));

		Set<Role> ltk = new HashSet<Role>();
		Role role = roleRepository.findById(Long.parseLong(id));
		ltk.add(role);

		user.setRole(ltk);
		user.setStatus("enable");

		User exitEmail = userRepository.findUserByEmail(user.getEmail());
		if (exitEmail != null) {
			modelAndView.setViewName("/create-user");
		} else if (userRepository.save(user) != null) {
			modelAndView.setViewName("/create-user");
		} else {
			modelAndView.setViewName("/create-user");
		}

		return modelAndView;
	}

	@RequestMapping("/create-user")
	public String createUserFormAdmin(Model model) {
		User user = new User();
		List<Role> roles = roleRepository.findAll();
		model.addAttribute("roles", roles);
		model.addAttribute("users", user);
		return "create-user";
	}

}
