package com.edu.hanu.repository;

import com.edu.hanu.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);
	List<User> findAll();
	User getById(long id);

}
