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

import com.baris.teamproject.domain.Player;
import com.baris.teamproject.dto.PlayerRequestDTO;
import com.baris.teamproject.dto.response.ContractResponseDTO;
import com.baris.teamproject.dto.response.PlayerResponseDTO;
import com.baris.teamproject.repository.PlayerRepository;
import com.baris.teamproject.service.PlayerService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("players")
@Slf4j
public class PlayerResource {
	
	@Autowired
	private PlayerRepository playerRepository;
	@Autowired
	private PlayerService playerService;
	
	public static final String NOT_FOUND_TEXT = "Player not found!";
	
	@PostMapping("/insert")
	public ResponseEntity<Player> savePlayer(@RequestBody PlayerRequestDTO playerDTO) {
		try {
			Player persistentPlayer = new Player();
			playerService.updatePersistentPlayerVariables(playerDTO, persistentPlayer);
			return new ResponseEntity<>(playerRepository.save(persistentPlayer), HttpStatus.ACCEPTED);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<Player> deletePlayer(@RequestParam("id") long id) {
		Optional<Player> playerOpt = playerRepository.findById(id);
		if(playerOpt.isPresent()) {
			playerRepository.deleteById(playerOpt.get().getId());
			return new ResponseEntity<>(HttpStatus.OK);
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_TEXT);
	}
	
	@PostMapping("/update")
	public ResponseEntity<Player> updatePlayer(@RequestParam("id") long id, @RequestBody PlayerRequestDTO playerDTO) {
		Optional<Player> playerOpt = playerRepository.findById(id);
		try {
			if (playerOpt.isPresent()) {
				Player persistentPlayer = playerOpt.get();
				playerService.updatePersistentPlayerVariables(playerDTO, persistentPlayer);
				return new ResponseEntity<>(playerRepository.save(persistentPlayer), HttpStatus.OK);
			}
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_TEXT);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, NOT_FOUND_TEXT);
		}
	}
	
	@GetMapping("/getAllPlayers")
	public ResponseEntity<PlayerResponseDTO> getAllPlayers() {
		List<Player> players = playerRepository.findAll();
		if (players.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_TEXT);
		}
		PlayerResponseDTO response = new PlayerResponseDTO();
		response.setPlayers(players);
		return new ResponseEntity<>(response, HttpStatus.FOUND);
	}
	
	@GetMapping("/getPlayerTeams/{id}")
	public ResponseEntity<ContractResponseDTO> getPlayerTeams(@PathVariable long id) {
		Optional<Player> player = playerRepository.findById(id);
		if (player.isPresent()) {
			ContractResponseDTO response =  new ContractResponseDTO();
			response.setContracts(player.get().getContracts());
			return new ResponseEntity<>(response, HttpStatus.FOUND);
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_TEXT);
	}

}
