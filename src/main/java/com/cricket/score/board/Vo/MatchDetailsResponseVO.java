package com.cricket.score.board.Vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_EMPTY)
public class MatchDetailsResponseVO {
	
	
	@JsonProperty
	private String description;
	
	@JsonProperty
	private List<PlayerDataResponseVo> playerData;
	
	@JsonProperty
	private String total;
	
	@JsonProperty
	private Double overs;
	
	@JsonProperty
	private String finalResult;
	

}
