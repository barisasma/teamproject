package com.baris.teamproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.baris.teamproject.domain.Player;
import com.baris.teamproject.domain.Team;
import com.baris.teamproject.dto.ContractDTO;
import com.baris.teamproject.dto.PlayerRequestDTO;
import com.baris.teamproject.dto.PlayerTransferInfoDTO;
import com.baris.teamproject.dto.TeamDTO;
import com.baris.teamproject.dto.TeampPlayerRequestDTO;
import com.baris.teamproject.dto.response.PlayerResponseDTO;
import com.baris.teamproject.dto.response.PlayerTransferResponseDTO;
import com.baris.teamproject.dto.response.TeamResponseDTO;
import com.baris.teamproject.repository.TeamRepository;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Slf4j
public class TeamResourceTest {
	
	@LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TeamRepository teamRepository;
    
    String teamEndPoint = "http://localhost:%s/teams";
    String playerEndPoint = "http://localhost:%s/players";
    
	String teamNameEx = "Manchester United";
    		
    @Test
    public void test1() {
    	TeamDTO teamDTO = new TeamDTO();
    	teamDTO.setName(this.teamNameEx);
    	teamDTO.setCountry("England");
    	teamDTO.setCoach("Paul Scholes");
    	ResponseEntity<Team> response = restTemplate.postForEntity(String.format(teamEndPoint, port) + "/insert", teamDTO, Team.class);
    	Team teamEntity = response.getBody();
    	assertEquals(teamNameEx, teamEntity.getName());
    	assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }

	@Test
    public void test2(){
    	ResponseEntity<TeamResponseDTO> response = restTemplate.getForEntity(String.format(teamEndPoint, port)+ "/getAllTeams", TeamResponseDTO.class);
    	assertEquals(HttpStatus.FOUND, response.getStatusCode());
    	assertTrue(!response.getBody().getResponseList().isEmpty());
    }
    
    @Test  
    public void test3() {
    	TeamDTO obj = new TeamDTO();
		ResponseEntity<Team> response = restTemplate.postForEntity(String.format(teamEndPoint, port) + "/insert", obj,Team.class);
    	assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
    }
    
    @Test
    public void test4() {
    	PlayerRequestDTO playerRequest = new PlayerRequestDTO();
    	playerRequest.setFirstName("Burak");
    	playerRequest.setLastName("Yılmaz");
    	playerRequest.setBirthDate(LocalDate.of(1985, 07, 15));
    	ContractDTO contract1 = new ContractDTO();
    	contract1.setStartDate(LocalDate.of(2016, 01, 31));
    	contract1.setEndDate(LocalDate.of(2018, 01, 31));
    	Optional<Team> team = teamRepository.findByName(this.teamNameEx);
    	contract1.setTeam(team.get().getName());
    	ContractDTO contract2 = new ContractDTO();
    	contract2.setStartDate(LocalDate.of(2019, 01, 31));
    	contract2.setEndDate(LocalDate.of(2022, 01, 31));
    	Optional<Team> teamMadrid = teamRepository.findByName("Real Madrid");
    	contract2.setTeam(teamMadrid.get().getName());
    	playerRequest.setContracts(Arrays.asList(contract1,contract2));
    	ResponseEntity<Player> response = restTemplate.postForEntity(String.format(playerEndPoint, port) + "/insert", playerRequest,Player.class);
    	assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    	assertEquals("Burak", response.getBody().getFirstName());
    }
    
    @Test
    public void test5() {
    	TeampPlayerRequestDTO request = new TeampPlayerRequestDTO();
    	request.setYear(2017);
    	request.setTeamName(this.teamNameEx);
    	ResponseEntity<PlayerResponseDTO> response = restTemplate.postForEntity(String.format(teamEndPoint, port)+"/getTeamPlayers",request,PlayerResponseDTO.class);
    	assertEquals(HttpStatus.FOUND, response.getStatusCode());
    	assertTrue(!response.getBody().getPlayers().isEmpty());
    }
    
    @Test
    public void test6() {
    	ResponseEntity<PlayerTransferResponseDTO> response = restTemplate.getForEntity(String.format(teamEndPoint, port)+"/getTeamPlayerValues/Real Madrid"
    			,PlayerTransferResponseDTO.class);
    	assertEquals(HttpStatus.FOUND, response.getStatusCode());
    	for (PlayerTransferInfoDTO transferInfo : response.getBody().getPlayerTransferInfos()) {
			log.info("Salary: "+transferInfo.getSalary());
			log.info("Transfer Fee: "+transferInfo.getTransferFee());
			log.info("Team Commission: "+transferInfo.getTeamCommission());
			assertEquals(BigDecimal.valueOf(29117.649), transferInfo.getSalary());
			assertEquals(BigDecimal.valueOf(26470.59), transferInfo.getTransferFee());
			assertEquals(BigDecimal.valueOf(2647.059), transferInfo.getTeamCommission());
		}
    }
    
    @Test
    public void test7() {
    	long examplePlayerId = getExampleTeamID(teamNameEx);
    	TeamDTO teamDTO = new TeamDTO();
    	teamDTO.setName(teamNameEx);
    	teamDTO.setCountry("England");
    	teamDTO.setCoach("Jose Mourinho");
    	ResponseEntity<Team> updatedTeam = restTemplate.postForEntity(String.format(teamEndPoint, port) + "/update?id="+examplePlayerId,teamDTO,Team.class);
    	assertEquals("Jose Mourinho",updatedTeam.getBody().getCoach());
    }
    
    @Test
    public void test8() {
    	long chelSeaId = getExampleTeamID(teamNameEx);
    	restTemplate.delete(String.format(teamEndPoint, port) + "/delete?id="+chelSeaId);
    	assertTrue(teamRepository.findById(chelSeaId).isPresent());
    	long portoId = getExampleTeamID("Porto");
    	restTemplate.delete(String.format(teamEndPoint, port) + "/delete?id="+portoId);
    	assertTrue(!teamRepository.findById(portoId).isPresent());
    }
    
    public long getExampleTeamID(String teamName) {
    	Optional<Team> teamOpt = teamRepository.findByName(teamName);
		Team team = teamOpt.get();
		return team.getId();
    }
    

}
