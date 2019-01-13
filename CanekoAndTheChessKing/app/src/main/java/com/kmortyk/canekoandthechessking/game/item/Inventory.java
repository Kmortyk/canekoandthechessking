package com.kmortyk.canekoandthechessking.game.item;

import java.util.ArrayList;

public class Inventory {

    private ArrayList<GameItem> gameItems;

    public void addItem(GameItem gameItem) { gameItems.add(gameItem); }

    public ArrayList<GameItem> getGameItems() { return gameItems; }

}
