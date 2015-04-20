package com.jhua.assassin;

import java.util.ArrayList;
import com.parse.ParseObject;
import com.parse.ParseClassName;


/**
 * Created by Rebecca on 4/14/2015.
 */
@ParseClassName("Game")
public class Game extends ParseObject {

    private ArrayList<String> players;
    private ArrayList<String> eliminated;
    private char status;

    public Game(ArrayList<String> players, char status) {
    }

    public void changeStatus(char status) {
        this.status = status;
    }

    public void eliminate(String player) {
        this.eliminated.add(player);
    }

    public void removePlayer(String player) {
        this.players.remove(this.players.indexOf(player));
    }

    public void setGameName(String name) {
        put("gameName", name);
    }

    public String getGameName() {
        return getString("gameName");
    }
}
