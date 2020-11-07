package com.cricket.score.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cricket.score.board.Vo.MatchDetailsRequestVO;
import com.cricket.score.board.Vo.MatchDetailsResponseVO;
import com.cricket.score.board.service.CricketScoreBoardService;

@RestController
public class CricketScoreBoardController {

	@Autowired
	private CricketScoreBoardService cricketScoreBoardService;



	@RequestMapping(value="/getScoreCard",method = RequestMethod.POST)
	public MatchDetailsResponseVO getScoreCards( @RequestBody MatchDetailsRequestVO matchDetailsRequestVO)
	{

		
		System.out.println("Request for getScoreCard API :: "+matchDetailsRequestVO);
		return cricketScoreBoardService.execute(matchDetailsRequestVO);

	}

}
