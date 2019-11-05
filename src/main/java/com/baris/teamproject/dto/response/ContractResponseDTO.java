package com.baris.teamproject.dto.response;

import java.util.List;

import com.baris.teamproject.domain.Contract;

import lombok.Data;

@Data
public class ContractResponseDTO {
	
	private List<Contract> contracts;

}
