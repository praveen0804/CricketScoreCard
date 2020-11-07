package com.cricket.score.board.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.cricket.score.board.Vo.Over;
import com.cricket.score.board.Vo.Player;
import com.cricket.score.board.Vo.PlayerDataResponseVo;
import com.cricket.score.board.Vo.ScoreCard;
import com.cricket.score.board.Vo.Team;

public class InMemoryMapUtils {
	
	
	public static  Map<String, Player> playersMap= new HashMap<String, Player>();
	
	
	public static ScoreCard scoreCardData= new ScoreCard();
	
	
	public static Over over= new Over();
	
	
	public static Map<String,String> teamDataMap = new HashMap<String, String>();
	
	public static Player getFirstPlayer(boolean isTeamOnePlaying)
	{
		Team team =isTeamOnePlaying?InMemoryMapUtils.scoreCardData.getMatchDetails().getTeamOne():
			InMemoryMapUtils.scoreCardData.getMatchDetails().getTeamTwo();
		Collections.sort(team.getPlayers(), Comparator.comparing(Player::getPlayerId));
		for(Player player: team.getPlayers())
		{
			if(player.isOnStrike() || (!player.isOut() && !player.isPlaying()))
			{
				player.setPlaying(true);
			    player.setOnStrike(true);
			return 	player;
			}
		}
		team.getPlayers().get(0).setPlaying(true);
		team.getPlayers().get(0).setOnStrike(true);
		return team.getPlayers().get(0);
	}
	public static Player getSecondPlayer(boolean isTeamOnePlaying)
	{
		Team team =isTeamOnePlaying?InMemoryMapUtils.scoreCardData.getMatchDetails().getTeamOne():
			InMemoryMapUtils.scoreCardData.getMatchDetails().getTeamTwo();
		Collections.sort(team.getPlayers(), Comparator.comparing(Player::getPlayerId));
		for(Player player: team.getPlayers())
		{
			if(!player.isOnStrike() && !player.isOut())
			{
				player.setPlaying(true);
			return 	player;
			}
		}
		return null;
		
	}
	
	

}
