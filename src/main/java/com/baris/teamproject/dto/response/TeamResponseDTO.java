package com.baris.teamproject.dto.response;

import java.util.List;

import com.baris.teamproject.domain.Team;

import lombok.Data;

@Data
public class TeamResponseDTO {
	
	private List<Team> responseList;
	
}
