package com.cricket.score.board.Vo;

import lombok.Data;

@Data 
public class Player extends Score {
	
	private String playerName;
	private Integer playerId;
	private boolean  isOut;
	private boolean isPlaying;
	private boolean onStrike;
	
	
}



