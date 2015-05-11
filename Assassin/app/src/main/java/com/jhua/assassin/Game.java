package com.jhua.assassin;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.parse.ParseObject;
import com.parse.ParseClassName;
import com.parse.ParsePush;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


//Game object, holds player, settings and other stuff related
//to a certain game
@ParseClassName("Game")
public class Game extends ParseObject {

    private static final String GAME_KEY = "10";

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

    public void addPlayer(String player) {
        // Log.d("Game/AddPlayer", player.getUsername());
        // Log.d("Game/AddPlayer", player.getObjectId());
        // why the fuck is it not adding the player? This doesn't make any sense!
        // ^ LOL
        addUnique("players", player);
        addUnique("activePlayers", player);
    }

    public void removePlayer(String player) {
        addUnique("eliminatedPlayers", player);
        removeAll("players", Arrays.asList(player));
    }
    
    public void setTargets(ArrayList<ParseUser> targets) throws JSONException {

        JSONObject json = new JSONObject();
        json.put("id", this.getObjectId());

        // Get target from targets list
        ParseUser myTarget = null;

        for (int x = 0; x < targets.size(); x++) {
            ParseUser p = targets.get(x);
            myTarget = targets.get((x+1) % targets.size());
            p.put("target", myTarget);

            ParsePush push = new ParsePush();
            push.setData(json);
            push.setMessage(this.getObjectId());
            push.setChannel(p.getUsername());
            push.sendInBackground();
        }
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
