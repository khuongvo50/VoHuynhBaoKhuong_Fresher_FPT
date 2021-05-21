package com.fpt.fsher.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fpt.fsher.entity.Role;

public interface RoleRepository extends JpaRepository<Role, String> {

	Role findById(long id);

}
