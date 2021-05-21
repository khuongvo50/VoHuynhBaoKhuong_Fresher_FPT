package com.fpt.fsher.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fpt.fsher.entity.Role;
import com.fpt.fsher.entity.User;

public interface UserRepository extends JpaRepository<User, String> {

	User findUserByEmail(String email);

	Optional<User> findById(String id);

//	List<User> findAllUserByRole(Role role);
	
}
