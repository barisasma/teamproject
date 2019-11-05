package com.baris.teamproject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.baris.teamproject.domain.Team;

public interface TeamRepository extends JpaRepository<Team, Long>{
	
	Optional<Team> findByName(String name);

}
