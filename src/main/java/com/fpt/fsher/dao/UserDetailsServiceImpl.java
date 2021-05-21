package com.fpt.fsher.dao;

import java.util.ArrayList;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.fpt.fsher.entity.Role;
import com.fpt.fsher.entity.User;
import com.fpt.fsher.repository.UserRepository;

public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findUserByEmail(username);
		System.err.println(user);
		if (user != null && user.getStatus().equals("enable")) {
			return toUserDetails(user);
		} else {
			throw new UsernameNotFoundException("Tai Khoan Khong Ton Tai");
		}
	}

	private UserDetails toUserDetails(User user) {
		
		Set<Role> rolelist = user.getRole();
		
		ArrayList<String> strs = new ArrayList<String>();
		
		for (Role role : rolelist) {
			strs.add(role.getRole());
		}
		String[] roles = strs.toArray(new String[0]);
		return org.springframework.security.core.userdetails.User
				.withUsername(user.getEmail())
				.password(user.getPassWord())
				.roles(roles).build();
		
		

	}

}
