package com.baris.teamproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

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
import com.baris.teamproject.dto.TeamDTO;
import com.baris.teamproject.dto.TeampPlayerRequestDTO;
import com.baris.teamproject.dto.response.PlayerResponseDTO;
import com.baris.teamproject.repository.PlayerRepository;
import com.baris.teamproject.repository.TeamRepository;
import com.baris.teamproject.service.TeamService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PlayerResourceTest {
    
	@LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private TeamService teamService;
    
    String teamEndPoint = "http://localhost:%s/teams";
    String playerEndPoint = "http://localhost:%s/players";
	 
    @Test
    public void test1() {
    	TeamDTO teamDTO = new TeamDTO();
    	teamDTO.setName("Chelsea");
    	teamDTO.setCountry("England");
    	teamDTO.setCoach("Pep Guardiola");
    	ResponseEntity<Team> response = restTemplate.postForEntity(String.format(teamEndPoint, port) + "/insert", teamDTO, Team.class);
    	Team teamEntity = response.getBody();
    	assertEquals("Chelsea", teamEntity.getName());
    	assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }
    
    
    @Test
    public void test4() {
    	PlayerRequestDTO playerRequest = new PlayerRequestDTO();
    	playerRequest.setFirstName("Burak");
    	playerRequest.setLastName("Yılmaz");
    	playerRequest.setBirthDate(LocalDate.of(1988, 07, 15));
    	ContractDTO contract1 = new ContractDTO();
    	contract1.setStartDate(LocalDate.of(2013, 01, 31));
    	contract1.setEndDate(LocalDate.of(2015, 01, 31));
    	Optional<Team> team = teamRepository.findByName("Chelsea");
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
    	PlayerRequestDTO playerRequest = new PlayerRequestDTO();
    	playerRequest.setFirstName("Baris");
    	playerRequest.setLastName("Asma");
    	playerRequest.setBirthDate(LocalDate.of(1985, 07, 15));
    	ContractDTO contract1 = new ContractDTO();
    	contract1.setStartDate(LocalDate.of(2017, 01, 31));
    	contract1.setEndDate(LocalDate.of(2018, 01, 31));
    	Optional<Team> team = teamRepository.findByName("Chelsea");
    	contract1.setTeam(team.get().getName());
    	ContractDTO contract2 = new ContractDTO();
    	contract2.setStartDate(LocalDate.of(2019, 01, 31));
    	contract2.setEndDate(LocalDate.of(2022, 01, 31));
    	Optional<Team> teamMadrid = teamRepository.findByName("Real Madrid");
    	contract2.setTeam(teamMadrid.get().getName());
    	playerRequest.setContracts(Arrays.asList(contract1,contract2));
    	ResponseEntity<Player> response = restTemplate.postForEntity(String.format(playerEndPoint, port) + "/update?id="+getExamplePlayerId("Burak","Yılmaz"), playerRequest,Player.class);
    	assertEquals(HttpStatus.OK, response.getStatusCode());
    	assertEquals("Baris", response.getBody().getFirstName());
    }
    
    @Test
    @Transactional
    public void test6() {
    	TeampPlayerRequestDTO teampPlayerRequestDTO = new TeampPlayerRequestDTO();
    	teampPlayerRequestDTO.setTeamName("Chelsea");
    	teampPlayerRequestDTO.setYear(2013);
    	List<Player> findPlayersExistingContrat = teamService.findPlayersExistingContrat(teampPlayerRequestDTO);
    	assertTrue(findPlayersExistingContrat.isEmpty());
    	teampPlayerRequestDTO.setYear(2017);
    	findPlayersExistingContrat = teamService.findPlayersExistingContrat(teampPlayerRequestDTO);
    	assertTrue(!findPlayersExistingContrat.isEmpty());
    }
    
    @Test
    public void test7() {
    	long examplePlayerId = getExamplePlayerId("Baris","Asma");
    	restTemplate.delete(String.format(playerEndPoint, port) + "/delete?id="+examplePlayerId);
    	assertTrue(!playerRepository.findById(examplePlayerId).isPresent());
    }
    
    @Test
    public void test8() {
    	
    }
    
    public long getExamplePlayerId(String firstName,String lastName) {
		ResponseEntity<PlayerResponseDTO> entity = restTemplate
				.getForEntity(String.format(playerEndPoint, port) + "/getAllPlayers", PlayerResponseDTO.class);
		Player player = entity.getBody().getPlayers().stream().filter(p->p.getFirstName().equals(firstName) && p.getLastName().equals(lastName)).findFirst().orElse(null);
		return player.getId();
    }
    
    
	
}
