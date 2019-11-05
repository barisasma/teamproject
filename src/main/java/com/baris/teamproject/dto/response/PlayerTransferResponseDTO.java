package com.baris.teamproject.dto.response;

import java.util.List;

import com.baris.teamproject.dto.PlayerTransferInfoDTO;

import lombok.Data;

@Data
public class PlayerTransferResponseDTO {

	List<PlayerTransferInfoDTO> playerTransferInfos;
	
}
