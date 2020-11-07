package com.cricket.score.board.Vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class MatchDetailsRequestVO {
	
	@JsonProperty
	private Integer noOfPlayer;
	
	@JsonProperty
	private Integer noOfOvers;
	
	@JsonProperty
	List<String> teamOneOrder;
	
	@JsonProperty
	List<String> teamTwoOrder;
	
	@JsonProperty
	List<String> overDetails;

}
