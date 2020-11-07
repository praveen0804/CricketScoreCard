package com.cricket.score.board.Vo;

import com.cricket.score.board.constants.BallEnum;

import lombok.Data;

@Data
public class Ball {
	
	private int runScore;
	BallEnum typeOfBall;
	private boolean wicketTakenBall;

}
