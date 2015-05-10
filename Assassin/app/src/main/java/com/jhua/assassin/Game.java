package com.jhua.assassin;

import android.util.Log;

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
        addAllUnique("players", players);
        addAllUnique("activePlayers", players);
    }

    public void addPlayer(ParseUser player) {
        Log.d("Game/AddPlayer", player.getUsername());
        Log.d("Game/AddPlayer", player.getObjectId());
        //why the fuck is it not adding the player? This doesn't make any sense!
        addUnique("players", player);
        addUnique("activePlayers", player);
    }

    public void removePlayer(String player) {
        addUnique("eliminatedPlayers", player);
        removeAll("players", Arrays.asList(player));
    }
    
    public void setTargets(ArrayList<ParseUser> targets) {
        put("targets", targets);
        // Get target from targets list
        String name = ParseUser.getCurrentUser().getUsername().toString();
        ParseUser myTarget = null;
        ParseUser[] targetArray = new ParseUser[targets.size()];
        targetArray = targets.toArray(targetArray);
        for (int i = 0; i < targetArray.length; i++) {
            ParseUser p = targetArray[i];
            if (p.getUsername().toString().equals(name)) {
                myTarget = targetArray[(i+1) % targetArray.length];
            }
        }
        ParseUser.getCurrentUser().put("target", myTarget);  // puts ParseUser object in target field for this ParseUser
    }

    public void setCreator(String name) {
        put("creator", name);
    }

    public void setStatus(String status) {put("status", status); }

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
