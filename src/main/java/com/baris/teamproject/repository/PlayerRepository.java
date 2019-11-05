package com.baris.teamproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.baris.teamproject.domain.Player;


public interface PlayerRepository extends JpaRepository<Player, Long>{
	
	 List<Player> findByFirstName(String firstName);

}
