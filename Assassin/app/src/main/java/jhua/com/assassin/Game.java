package jhua.com.assassin;

import java.util.ArrayList;

/**
 * Created by Rebecca on 4/14/2015.
 */
public class Game {

    private ArrayList<String> players;
    private ArrayList<String> eliminated;
    private char status;

    public Game(ArrayList<String> players, char status) {

        this.players = players;
        this.status = status;
        this.eliminated = new ArrayList<String>();

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
}
