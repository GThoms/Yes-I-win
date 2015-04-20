package com.jhua.assassin;

import java.util.ArrayList;
import com.parse.ParseObject;
import com.parse.ParseClassName;


/**
 * Created by Rebecca on 4/14/2015.
 */
@ParseClassName("Game")
public class Game extends ParseObject {

    public Game(ArrayList<String> players, char status) {
    }

    public void setGameName(String name) {
        put("gameName", name);
    }

    public String getGameName() {
        return getString("gameName");
    }
}
