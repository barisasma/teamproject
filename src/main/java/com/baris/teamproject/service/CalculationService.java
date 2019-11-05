package com.baris.teamproject.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baris.teamproject.dto.PlayerTransferInfoDTO;

@Service
public class CalculationService {

	@Autowired
	private TeamService teamService;
	
	private static final BigDecimal COMMISSION_RATE = BigDecimal.valueOf(0.1);
	private static final BigDecimal TRANSFER_BASE_FEE = BigDecimal.valueOf(100000);
	
	public List<PlayerTransferInfoDTO> getPlayerTransferValues(String teamName) {
		List<PlayerTransferInfoDTO> currentPlayers = teamService.getCurrentPlayers(teamName);
		return calculateValues(currentPlayers);
	}
	
	private List<PlayerTransferInfoDTO> calculateValues(List<PlayerTransferInfoDTO> playersTransferInfos){
		for (PlayerTransferInfoDTO playerTransferInfoDTO : playersTransferInfos) {
			long monthExperience = ChronoUnit.MONTHS.between(playerTransferInfoDTO.getContract().getStartDate(),LocalDate.now());
			int age = Period.between(playerTransferInfoDTO.getPlayer().getBirthDate(), LocalDate.now()).getYears();
			BigDecimal fee = BigDecimal.valueOf(monthExperience).multiply(TRANSFER_BASE_FEE);
			BigDecimal transferFee = fee.divide(BigDecimal.valueOf(age),2, RoundingMode.HALF_UP);
			playerTransferInfoDTO.setTransferFee(transferFee);
			playerTransferInfoDTO.setTeamCommission(COMMISSION_RATE.multiply(transferFee));
			playerTransferInfoDTO.setSalary(playerTransferInfoDTO.getTeamCommission().add(playerTransferInfoDTO.getTransferFee()));
		}
		return playersTransferInfos;
	}
}
