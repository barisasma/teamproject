package com.baris.teamproject.dto.response;

import java.util.List;

import com.baris.teamproject.domain.Player;

import lombok.Data;

@Data
public class PlayerResponseDTO {

	private List<Player> players;
	
}
