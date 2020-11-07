package com.cricket.score.board.Vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerDataResponseVo {
	
	
	@JsonProperty
	private String playerName;
	@JsonProperty
	private Integer score;
	@JsonProperty
	private Integer fours;
	@JsonProperty
	private Integer sixs;
	@JsonProperty
	private Integer balls;
	

}
