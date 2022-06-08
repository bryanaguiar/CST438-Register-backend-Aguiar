package com.cst438.domain;

import org.springframework.data.repository.CrudRepository;

public interface AdminRepository extends CrudRepository <Admin, Integer> {
	
	// declare the following method to return a single Admin object
	// default JPA behavior that findBy methods return List<Admin> except for findById.
	public Admin findByEmail(String email);

}
