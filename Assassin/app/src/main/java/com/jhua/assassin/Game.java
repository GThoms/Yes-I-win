package com.jhua.assassin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.parse.ParseObject;
import com.parse.ParseClassName;
import com.parse.ParseUser;


//Game object, holds player, settings and other stuff related
//to a certain game
@ParseClassName("Game")
public class Game extends ParseObject {

    public void setGameName(String name) {
        put("gameName", name);
    }

    public String getGameName() {
        return getString("gameName");
    }

    public void addPlayers(ArrayList<ParseUser> players) {
        put("players", players);
        put("activePlayers", players);
    }

    public void removePlayer(String player) {
        addUnique("eliminatedPlayers", player);
        removeAll("players", Arrays.asList(player));
    }
    
    public void setTargets(ArrayList<ParseUser> targets) {
        put("targets", targets);
    }

    public void setCreator(String name) {
        put("creator", name);
    }

    public void setGameDuration(float gameDuration) {
        put("gameDuration", gameDuration);
    }

    public void setBlockDuration(float blockDuration) {
        put("blockDuration", blockDuration);
    }

    public void setAttackRadius(float attackRadius) {
        put("attackRadius", attackRadius);
    }
}
