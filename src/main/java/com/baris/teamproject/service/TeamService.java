package com.baris.teamproject.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baris.teamproject.domain.Contract;
import com.baris.teamproject.domain.Player;
import com.baris.teamproject.domain.Team;
import com.baris.teamproject.dto.PlayerTransferInfoDTO;
import com.baris.teamproject.dto.TeampPlayerRequestDTO;
import com.baris.teamproject.repository.TeamRepository;

@Service
public class TeamService {
	
	@Autowired
	private TeamRepository teamRepository;
	
	public List<Player> findPlayersExistingContrat(TeampPlayerRequestDTO request) {
		Optional<Team> teamOpt = teamRepository.findByName(request.getTeamName());
		if (teamOpt.isPresent()) {
			Team team = teamOpt.get();
			List<Contract> contracts = team.getContracts();
			int year = request.getYear();
			return getPlayersInYear(contracts, year);
		}
		return Collections.emptyList();
	}
	
	private List<Player> getPlayersInYear(List<Contract> contracts, int year) {
		List<Player> players = new LinkedList<>();
		for (Contract contract : contracts) {
			int startYear = contract.getStartDate().getYear();
			int endYear = contract.getEndDate().getYear();
			if (year >= startYear && year <= endYear) {
				players.add(contract.getPlayer());
			}
		}
		return players;
	}
	
	public List<PlayerTransferInfoDTO> getCurrentPlayers(String teamName){
		Optional<Team> teamOpt = teamRepository.findByName(teamName);
		List<PlayerTransferInfoDTO> playersInfos = new LinkedList<>();
		if(teamOpt.isPresent()) {
			Team team = teamOpt.get();
			Map<Player, Contract> playersActiveContract = getPlayersActiveContract(team.getContracts());
			Set<Player> keySet = playersActiveContract.keySet();
			for (Player player : keySet) {
				PlayerTransferInfoDTO playerTransferInfoDTO = new PlayerTransferInfoDTO();
				playerTransferInfoDTO.setContract(playersActiveContract.get(player));
				playerTransferInfoDTO.setPlayer(player);
				playersInfos.add(playerTransferInfoDTO);
			}
			return playersInfos;
		}
		return Collections.emptyList();
	}
	
	private Map<Player,Contract> getPlayersActiveContract(List<Contract> contracts) {
		Map<Player,Contract> players = new HashMap<>();
		for (Contract contract : contracts) {
			boolean before = contract.getStartDate().isBefore(LocalDate.now());
			boolean after = contract.getEndDate().isAfter(LocalDate.now());
			if (before && after) {
				players.put(contract.getPlayer(), contract);
			}
		}
		return players;
	}

}
