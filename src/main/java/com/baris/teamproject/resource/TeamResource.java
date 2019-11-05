package com.baris.teamproject.resource;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.baris.teamproject.domain.Contract;
import com.baris.teamproject.domain.Player;
import com.baris.teamproject.domain.Team;
import com.baris.teamproject.dto.PlayerTransferInfoDTO;
import com.baris.teamproject.dto.TeamDTO;
import com.baris.teamproject.dto.TeampPlayerRequestDTO;
import com.baris.teamproject.dto.response.PlayerResponseDTO;
import com.baris.teamproject.dto.response.PlayerTransferResponseDTO;
import com.baris.teamproject.dto.response.TeamResponseDTO;
import com.baris.teamproject.repository.TeamRepository;
import com.baris.teamproject.service.CalculationService;
import com.baris.teamproject.service.TeamService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("teams")
@Slf4j
public class TeamResource {
	
	@Autowired
	private TeamRepository teamRepository;
	@Autowired
	private TeamService teamService;
	@Autowired
	private CalculationService calculationService;
	
	public static final String NOT_FOUND_TEXT = "TEAM not found!";
	public static final String NOT_CREATED = "Team not created!";
	
	@PostMapping("/insert")
	public ResponseEntity<Team> saveTeam(@RequestBody TeamDTO teamDTO) {
		try {
			Team persistentTeam = new Team();
			persistentTeam.setName(teamDTO.getName());
			persistentTeam.setCountry(teamDTO.getCountry());
			persistentTeam.setCoach(teamDTO.getCoach());
			Team saved = teamRepository.save(persistentTeam);
			return new ResponseEntity<>(saved,HttpStatus.ACCEPTED);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,NOT_CREATED,e);
		}
	}

	@DeleteMapping("/delete")
	public ResponseEntity<Team> deleteTeam(@RequestParam("id") long id) {
		Optional<Team> teamOpt = teamRepository.findById(id);
		if(teamOpt.isPresent()) {
			Team team = teamOpt.get();
			List<Contract> contracts = team.getContracts();
			if (contracts.isEmpty()) {
				teamRepository.deleteById(team.getId());
				return new ResponseEntity<>(HttpStatus.OK);
			}
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Existing contracts found on Team");
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_TEXT);
	}
	
	
	@PostMapping("/update")
	public ResponseEntity<Team> updateTeam(@RequestParam("id") long id, @RequestBody TeamDTO teamDTO) {
		try {
			Optional<Team> teamOpt = teamRepository.findById(id);
			if(teamOpt.isPresent()) {
				Team persistentTeam = teamOpt.get();
				persistentTeam.setName(teamDTO.getName());
				persistentTeam.setCountry(teamDTO.getCountry());
				persistentTeam.setCoach(teamDTO.getCoach());
				return new ResponseEntity<>(teamRepository.save(persistentTeam),HttpStatus.ACCEPTED);
			}
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,NOT_FOUND_TEXT);
		} 
		catch (Exception e) {
			log.error(e.getMessage());
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,NOT_CREATED,e);
		}
	}

	@GetMapping("/getAllTeams") 
	public ResponseEntity<TeamResponseDTO> getAllTeams() {
		List<Team> teams = teamRepository.findAll();
		if(teams.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_TEXT);
		}
		TeamResponseDTO responseDTO = new TeamResponseDTO();
		responseDTO.setResponseList(teams);
		return new ResponseEntity<>(responseDTO,HttpStatus.FOUND);
	}
	
	@PostMapping("/getTeamPlayers")
	public ResponseEntity<PlayerResponseDTO> getTeamPlayers(@RequestBody TeampPlayerRequestDTO request){
		List<Player> players = teamService.findPlayersExistingContrat(request);
		if(players.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No player found for team/year");
		}
		PlayerResponseDTO response = new PlayerResponseDTO();
		response.setPlayers(players);
		return new ResponseEntity<>(response,HttpStatus.FOUND);
	}
	
	@GetMapping("/getTeamPlayerValues/{teamName}")
	public ResponseEntity<PlayerTransferResponseDTO> getTeamPlayerValues(@PathVariable String teamName){
		List<PlayerTransferInfoDTO> playersTransferInfos = calculationService.getPlayerTransferValues(teamName);
		if(playersTransferInfos.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No player transfer fee calculated for team");
		}
		PlayerTransferResponseDTO response = new PlayerTransferResponseDTO();
		response.setPlayerTransferInfos(playersTransferInfos);
		return new ResponseEntity<>(response,HttpStatus.FOUND);
	}

}
