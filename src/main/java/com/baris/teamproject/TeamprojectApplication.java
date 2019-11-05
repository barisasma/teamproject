package com.baris.teamproject;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.baris.teamproject.domain.Contract;
import com.baris.teamproject.domain.Player;
import com.baris.teamproject.domain.Team;
import com.baris.teamproject.repository.PlayerRepository;
import com.baris.teamproject.repository.TeamRepository;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class TeamprojectApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeamprojectApplication.class, args);
	}
	
	@Bean
    ApplicationRunner init(TeamRepository teamRepository,PlayerRepository playerRepository) {
        return args -> {
        	
        	insertTeams(teamRepository);
        	insertPlayers(playerRepository,teamRepository);
    		
        	log.info("Teams Created");
        };
    }

	private void insertTeams(TeamRepository repository) {
		Team team1 = new Team();
		team1.setCoach("Manuel Peleggrini");
		team1.setCountry("Spain");
		team1.setName("Real Madrid");
		repository.save(team1);
		
		Team team2 = new Team();
		team2.setCoach("Marco Silva");
		team2.setCountry("England");
		team2.setName("Everton");
		repository.save(team2);
		
		Team team3 = new Team();
		team3.setCoach("Fernando Santos");
		team3.setCountry("Portugal");
		team3.setName("Porto");
		repository.save(team3);
		
		Team team4 = new Team();
		team4.setCoach("Fatih Terim");
		team4.setCountry("Turkey");
		team4.setName("Galatasaray");
		repository.save(team4);
		
		Team team5 = new Team();
		team5.setCoach("Stefano Pioli");
		team5.setCountry("Italy");
		team5.setName("Milan");
		repository.save(team5);
	}
	
	
	private void insertPlayers(PlayerRepository playerRepository,TeamRepository teamRepository) {
		
		Optional<Team> gs = teamRepository.findByName("Galatasaray");
		Optional<Team> milan = teamRepository.findByName("Milan");
		
		
		Player player1 = new Player();
		player1.setBirthDate(LocalDate.of(1990, 05, 16));
		player1.setFirstName("Ryan");
		player1.setLastName("Babel");
		
		Contract contractExample = new Contract();
		contractExample.setPlayer(player1);
		contractExample.setStartDate(LocalDate.of(2016, 01, 15));
		contractExample.setEndDate(LocalDate.of(2020, 02, 15));
		contractExample.setTeam(gs.get());
		
		player1.getContracts().add(contractExample);
		
		Contract contractExample2 = new Contract();
		contractExample2.setPlayer(player1);
		contractExample2.setStartDate(LocalDate.of(2014, 01, 12));
		contractExample2.setEndDate(LocalDate.of(2015, 06, 15));
		contractExample2.setTeam(milan.get());
		
		player1.getContracts().add(contractExample2);
		
		playerRepository.save(player1);
		
		Player player2 = new Player();
		player2.setBirthDate(LocalDate.of(1995, 03, 16));
		player2.setFirstName("Emre");
		player2.setLastName("Mor");
		
		Contract contractExampleEmre = new Contract();
		contractExampleEmre.setPlayer(player2);
		contractExampleEmre.setStartDate(LocalDate.of(2019, 01, 15));
		contractExampleEmre.setEndDate(LocalDate.of(2023, 02, 15));
		contractExampleEmre.setTeam(gs.get());
		
		player2.getContracts().add(contractExampleEmre);
		
		playerRepository.save(player2);
		
		
	}
	
	
	

}
