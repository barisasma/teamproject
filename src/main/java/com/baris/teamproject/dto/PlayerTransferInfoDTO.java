package com.baris.teamproject.dto;

import java.math.BigDecimal;

import com.baris.teamproject.domain.Contract;
import com.baris.teamproject.domain.Player;

import lombok.Data;

@Data
public class PlayerTransferInfoDTO {
	
	private Player player;
	private Contract contract;
	
	private BigDecimal transferFee;
	
	private BigDecimal teamCommission;
	
	private BigDecimal salary;

}
