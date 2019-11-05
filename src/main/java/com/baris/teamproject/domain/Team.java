package com.baris.teamproject.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data
public class Team {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(unique=true,nullable=false)
	private String name;
	
	@Column(nullable = false)
	private String country;
	
	@Column(nullable = false)
	private String coach;
	
	@JsonIgnore
	@OneToMany(mappedBy = "team",fetch = FetchType.LAZY)
	private List<Contract> contracts = new ArrayList<>();
	
}
