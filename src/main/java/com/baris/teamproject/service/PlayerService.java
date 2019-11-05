package com.baris.teamproject.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.baris.teamproject.domain.Contract;
import com.baris.teamproject.domain.Player;
import com.baris.teamproject.domain.Team;
import com.baris.teamproject.dto.ContractDTO;
import com.baris.teamproject.dto.PlayerRequestDTO;
import com.baris.teamproject.repository.TeamRepository;

@Service
public class PlayerService {
	
	@Autowired
	private TeamRepository teamRepository;
	
	public List<Contract> getContracts(Player player,List<ContractDTO> contracts) {
		List<Contract> persistentContracts = player.getContracts();
		if(!persistentContracts.isEmpty()) {
			removePersistentContracts(player,persistentContracts);
		}
		List<Contract> contractList = new LinkedList<>();
		for (ContractDTO contractDTO : contracts) {
			Contract contract = new Contract();
			contract.setStartDate(contractDTO.getStartDate());
			contract.setEndDate(contractDTO.getEndDate());
			contract.setPlayer(player);
			Optional<Team> teamOpt = teamRepository.findByName(contractDTO.getTeam());
			if(teamOpt.isPresent()) {
				Team team = teamOpt.get();
				contract.setTeam(team);
				contractList.add(contract);
			}else {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team Not Found");
			}
		}
		return contractList;
	}
	
	private void removePersistentContracts(Player player,List<Contract> persistentContracts) {
		player.getContracts().clear();
	}

	public void updatePersistentPlayerVariables(PlayerRequestDTO playerDTO, Player persistentPlayer) {
		persistentPlayer.setFirstName(playerDTO.getFirstName());
		persistentPlayer.setLastName(playerDTO.getLastName());
		persistentPlayer.setBirthDate(playerDTO.getBirthDate());
		persistentPlayer.getContracts().addAll(getContracts(persistentPlayer,playerDTO.getContracts()));
	}

}
