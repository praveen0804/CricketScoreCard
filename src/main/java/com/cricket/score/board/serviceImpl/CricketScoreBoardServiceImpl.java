package com.cricket.score.board.serviceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cricket.score.board.Vo.Ball;
import com.cricket.score.board.Vo.MatchDetailsRequestVO;
import com.cricket.score.board.Vo.MatchDetailsResponseVO;
import com.cricket.score.board.Vo.Player;
import com.cricket.score.board.Vo.PlayerDataResponseVo;
import com.cricket.score.board.Vo.Team;
import com.cricket.score.board.constants.BallEnum;
import com.cricket.score.board.constants.CricketScoreCardConstants;
import com.cricket.score.board.service.CricketScoreBoardService;
import com.cricket.score.board.util.InMemoryMapUtils;

@Service
public class CricketScoreBoardServiceImpl  implements CricketScoreBoardService{

	@Override
	public MatchDetailsResponseVO execute(MatchDetailsRequestVO matchDetailsRequestVO) {
		MatchDetailsResponseVO matchDetailsResponseVO = new MatchDetailsResponseVO();

        boolean isTeamOnePlaying=isTeamOnePlaying(matchDetailsRequestVO);
		if(isTeamOnePlaying)

		{
			InMemoryMapUtils.teamDataMap.put(CricketScoreCardConstants.TEAM_ONE_PLAYING, Boolean.TRUE.toString());
			matchDetailsResponseVO.setDescription(CricketScoreCardConstants.TEAM_ONE_DESCRIPTION);
			populateDataInMap(matchDetailsRequestVO,true);
			

		}

		else 
		{
			populateDataInMap(matchDetailsRequestVO,false);
			matchDetailsResponseVO.setDescription(CricketScoreCardConstants.TEAM_TWO_DESCRIPTION);

		}
		processOverData(matchDetailsRequestVO);
		prepareScoreBoard(matchDetailsResponseVO,isTeamOnePlaying);

		return matchDetailsResponseVO;

	}

	

	private void prepareScoreBoard(MatchDetailsResponseVO matchDetailsResponseVO, boolean isTeamOnePlaying) {
		
		    Team team=isTeamOnePlaying?InMemoryMapUtils.scoreCardData.getMatchDetails().getTeamOne():
			InMemoryMapUtils.scoreCardData.getMatchDetails().getTeamTwo();
		
			matchDetailsResponseVO.setOvers(team.getTotalOver());
			matchDetailsResponseVO.setTotal(team.getTotalRun()+"/"+team.getTotalWicket());
			List<PlayerDataResponseVo> playerDataResponseList = new ArrayList<PlayerDataResponseVo>();
			Collections.sort(team.getPlayers(), Comparator.comparing(Player::getPlayerId));
			for(Player player: team.getPlayers())
			{
				PlayerDataResponseVo playerDataResponseVo = new PlayerDataResponseVo();
				playerDataResponseVo.setPlayerName(player.getPlayerName());
				playerDataResponseVo.setBalls(player.getNoOfBallPlayed());
				playerDataResponseVo.setScore(player.getRunScored());
				playerDataResponseVo.setFours(player.getNoOfFourPlayed());
				playerDataResponseVo.setSixs(player.getNoOfSixPlayed());
				playerDataResponseList.add(playerDataResponseVo);
			}
			
			
			matchDetailsResponseVO.setPlayerData(playerDataResponseList);
			
			if(isMatchComplete())
			{
				
				String finalResult=null;
				if(InMemoryMapUtils.scoreCardData.getMatchDetails().getTeamOne().isWon())
				{
					finalResult="TeamOne won the match by "+(InMemoryMapUtils.scoreCardData.getMatchDetails().getTeamOne().getTotalRun()-
				                 InMemoryMapUtils.scoreCardData.getMatchDetails().getTeamTwo().getTotalRun())+" runs";
				}else
				{
					finalResult="TeamTwo won the match by "+
							(InMemoryMapUtils.scoreCardData.getMatchDetails().getTeamOne().getNoOfPlayer()-
					                 InMemoryMapUtils.scoreCardData.getMatchDetails().getTeamTwo().getTotalWicket())+" wicket";	
				}
				matchDetailsResponseVO.setFinalResult(finalResult);
			}
			
			
			
		}
		
	



	private boolean isMatchComplete() {
		
		int totalOver=InMemoryMapUtils.scoreCardData.getMatchDetails().getTotalNoOfOvers();
		if(totalOver==InMemoryMapUtils.scoreCardData.getMatchDetails().getTeamOne().getTotalOver() &&
				totalOver==InMemoryMapUtils.scoreCardData.getMatchDetails().getTeamTwo().getTotalOver()
				|| (InMemoryMapUtils.scoreCardData.getMatchDetails().getTeamTwo().isWon() ||
				InMemoryMapUtils.scoreCardData.getMatchDetails().getTeamOne().isWon()))
		{
			return true;
		}
		return false;
	}



	private void processOverData(MatchDetailsRequestVO matchDetailsRequestVO) {

		boolean isTeamOnePlaying=Boolean.valueOf(InMemoryMapUtils.teamDataMap.get(CricketScoreCardConstants.TEAM_ONE_PLAYING));

	
			
			
			Team TeamPlaying=isTeamOnePlaying?InMemoryMapUtils.scoreCardData.getMatchDetails().getTeamOne():
				InMemoryMapUtils.scoreCardData.getMatchDetails().getTeamTwo();
			Player playerOne=InMemoryMapUtils.getFirstPlayer(isTeamOnePlaying);
			Player playerTwo=InMemoryMapUtils.getSecondPlayer(isTeamOnePlaying);
			int noOfBallBowled=0;
			for(Ball ball: InMemoryMapUtils.over.getBalls())
			{
				
				Player playerOnStrike=playerOne.isOnStrike()?playerOne:playerTwo;
				
				if(ball.isWicketTakenBall())
				{
					TeamPlaying.setTotalWicket(TeamPlaying.getTotalWicket()+1);
					noOfBallBowled++;
					
					playerOnStrike.setNoOfBallPlayed(playerOnStrike.getNoOfBallPlayed()+1);
					
					//changeStrikeOfPlayer(playerOne, playerTwo);
					if(playerOne.isOnStrike())
					{
					  playerOnStrike.setOnStrike(false);
					  playerOnStrike.setOut(true);
					  playerOnStrike.setPlaying(false);
					  playerOne=InMemoryMapUtils.getFirstPlayer(isTeamOnePlaying);
					}else 
					{
					  playerOnStrike.setOnStrike(false);	
					  playerOnStrike.setOut(true);
					  playerOnStrike.setPlaying(false);
					  playerTwo=InMemoryMapUtils.getFirstPlayer(isTeamOnePlaying);
					}
					playerOnStrike.setPlaying(false);
					TeamPlaying.getPlayers().removeIf(player->player.getPlayerId().equals(playerOnStrike.getPlayerId()));
					TeamPlaying.getPlayers().add(playerOnStrike);
					//popualtePlayeronStrike with next player
				}
				else if(ball.getTypeOfBall().equals(BallEnum.WIDE) || ball.getTypeOfBall().equals(BallEnum.NOBALL))
				{
					TeamPlaying.setTotalRun(TeamPlaying.getTotalRun()+1);
				}
				else if(ball.getTypeOfBall().equals(BallEnum.DOT))
				{
					noOfBallBowled++;
					playerOnStrike.setNoOfBallPlayed(playerOnStrike.getNoOfBallPlayed()+1);
				}
				else if(ball.getTypeOfBall().equals(BallEnum.NORMAL))
				{
					TeamPlaying.setTotalRun(TeamPlaying.getTotalRun()+ball.getRunScore());
					noOfBallBowled++;
					
					playerOnStrike.setNoOfBallPlayed(playerOnStrike.getNoOfBallPlayed()+1);
					playerOnStrike.setRunScored(playerOnStrike.getRunScored()+ball.getRunScore());
					if(ball.getRunScore()==4)
					playerOnStrike.setNoOfFourPlayed(playerOnStrike.getNoOfFourPlayed()+1);
					if(ball.getRunScore()==6)
					playerOnStrike.setNoOfSixPlayed(playerOnStrike.getNoOfSixPlayed()+1);
					
					if(ball.getRunScore()%2!=0)
					{
						
						 changeStrikeOfPlayer(playerOne, playerTwo);
					}	
					//Add run to player which is on strike
					//populatePlayerOnStrike and PlayeronNonStrike in case of odd run 
					
				}
				if(noOfBallBowled==6)
				{
					//populatePlayerOnStrike and PlayeronNonStrike
					playerOne.setPlaying(true);
					playerTwo.setPlaying(true);
					changeStrikeOfPlayer(playerOne, playerTwo);
					
					//TeamPlaying.getPlayers().removeIf(player->player.getPlayerId().equals(playerOne.getPlayerId()));
					//TeamPlaying.getPlayers().add(playerOne);
					
					//TeamPlaying.getPlayers().removeIf(player->player.getPlayerId().equals(playerTwo.getPlayerId()));
					//TeamPlaying.getPlayers().add(playerTwo);
					
					
					
				}
				isAnyTeamWon(isTeamOnePlaying);
				
					
				
			}
			if(noOfBallBowled==6)
			TeamPlaying.setTotalOver(TeamPlaying.getTotalOver()+1);
			else
				TeamPlaying.setTotalOver(TeamPlaying.getTotalOver()+InMemoryMapUtils.over.getBalls().size()/10d);
			if(isTeamOnePlaying && isTeamOneBattingComplete())
			{
				InMemoryMapUtils.teamDataMap.put(CricketScoreCardConstants.TEAM_ONE_PLAYING,Boolean.FALSE.toString());
			}
			
	}

	private boolean isTeamOneBattingComplete() {
		
		if(InMemoryMapUtils.scoreCardData.getMatchDetails().getTeamOne().getNoOfPlayer()==
				InMemoryMapUtils.scoreCardData.getMatchDetails().getTeamOne().getTotalWicket() || (InMemoryMapUtils.scoreCardData.getMatchDetails().getTotalNoOfOvers()==
						InMemoryMapUtils.scoreCardData.getMatchDetails().getTeamOne().getTotalOver()))
		{
			return true;
		}
		return false;
	}



	private void isAnyTeamWon(boolean isTeamOnePlaying) {
		
		if(!isTeamOnePlaying && InMemoryMapUtils.scoreCardData.getMatchDetails().getTeamTwo().getTotalRun()>
		InMemoryMapUtils.scoreCardData.getMatchDetails().getTeamOne().getTotalRun())
		{
			InMemoryMapUtils.scoreCardData.getMatchDetails().getTeamTwo().setWon(true);
		}	
		
		else if(!isTeamOnePlaying && InMemoryMapUtils.scoreCardData.getMatchDetails().getTeamTwo().getTotalWicket()==
				InMemoryMapUtils.scoreCardData.getMatchDetails().getTeamTwo().getNoOfPlayer()-1)
		{
			InMemoryMapUtils.scoreCardData.getMatchDetails().getTeamOne().setWon(true);
		}
	}



	private void changeStrikeOfPlayer(Player playerOne, Player playerTwo) {
		if(playerOne.isOnStrike())
		 {
			 playerOne.setOnStrike(false);
			 playerTwo.setOnStrike(true);
			 
		 }
		 else
		 {
			 playerOne.setOnStrike(true);
			 playerTwo.setOnStrike(false); 
		 }
	}

	private void populateDataInMap(MatchDetailsRequestVO matchDetailsRequestVO, boolean isTeamOneData) {

		populatePlayerOrder(matchDetailsRequestVO, isTeamOneData);
		populateOverAndPlayerCount(matchDetailsRequestVO);
		populateOverDetails(matchDetailsRequestVO);





	}

	private void populateOverAndPlayerCount(MatchDetailsRequestVO matchDetailsRequestVO) {
		if(matchDetailsRequestVO.getNoOfOvers() !=null && matchDetailsRequestVO.getNoOfPlayer()!=null)
		{
			InMemoryMapUtils.scoreCardData.getMatchDetails().setTotalNoOfOvers(matchDetailsRequestVO.getNoOfOvers());
			InMemoryMapUtils.scoreCardData.getMatchDetails().getTeamOne().setNoOfPlayer(matchDetailsRequestVO.getNoOfPlayer());
			InMemoryMapUtils.scoreCardData.getMatchDetails().getTeamTwo().setNoOfPlayer(matchDetailsRequestVO.getNoOfPlayer());
		}
	}

	private void populatePlayerOrder(MatchDetailsRequestVO matchDetailsRequestVO, boolean isTeamOneData) {
		if(matchDetailsRequestVO.getTeamOneOrder()!=null || matchDetailsRequestVO.getTeamTwoOrder()!=null)
		{
			List<Player> playerListOrder= new ArrayList<Player>();
			Integer count=0;
			for(String playerName: isTeamOneData ? matchDetailsRequestVO.getTeamOneOrder():matchDetailsRequestVO.getTeamTwoOrder())
			{   
				count++;
				Player player = new Player();
				player.setPlayerId(count);
				player.setPlayerName(playerName);
				playerListOrder.add(player);
				if(isTeamOneData)
					InMemoryMapUtils.scoreCardData.getMatchDetails().getTeamOne().setPlayers(playerListOrder);
				else
					InMemoryMapUtils.scoreCardData.getMatchDetails().getTeamTwo().setPlayers(playerListOrder);	
			}
		}
	}

	private void populateOverDetails(MatchDetailsRequestVO matchDetailsRequestVO) {
		List<Ball> ballList = new ArrayList<Ball>();

		for(String ball: matchDetailsRequestVO.getOverDetails())
		{
			Ball ballData = new Ball();


			switch(ball) {

			case "W":
				ballData.setWicketTakenBall(true);
				break;
			case "Wd":
				ballData.setTypeOfBall(BallEnum.WIDE);
				break;
			case "No":
				ballData.setTypeOfBall(BallEnum.NOBALL);
				break;
			default:
				if(Integer.valueOf(ball).equals(0))
				{
					ballData.setTypeOfBall(BallEnum.DOT);

				}
				else
				{
					ballData.setTypeOfBall(BallEnum.NORMAL);
					ballData.setRunScore(Integer.valueOf(ball));
				}

				break;

			}
			ballList.add(ballData);	
			
			InMemoryMapUtils.over.setBalls(ballList);


		}
	}



	private boolean isTeamOnePlaying(MatchDetailsRequestVO matchDetailsRequestVO) {
		return matchDetailsRequestVO.getTeamOneOrder()!=null || 
				Boolean.valueOf(InMemoryMapUtils.teamDataMap.get(CricketScoreCardConstants.TEAM_ONE_PLAYING));
	}


}
