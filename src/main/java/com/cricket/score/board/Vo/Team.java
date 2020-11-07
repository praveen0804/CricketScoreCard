package com.cricket.score.board.Vo;

import java.util.List;

import lombok.Data;

@Data
public class Team {
	
	
	private String teamName;
	private List<Player> players;
	private int noOfPlayer;
	private boolean isWon;
	private int totalRun;
	private int totalWicket;
	private double totalOver;
	
	

}
