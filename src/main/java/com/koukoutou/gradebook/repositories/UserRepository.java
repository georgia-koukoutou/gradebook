package com.koukoutou.gradebook.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.koukoutou.gradebook.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsername(String username);

}
