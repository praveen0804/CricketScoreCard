package com.cricket.score.board.Vo;

import lombok.Data;

@Data
public class Match {
	
	private Team TeamOne= new Team();
	private Team TeamTwo=new Team();
	private int totalNoOfOvers;
	

}
