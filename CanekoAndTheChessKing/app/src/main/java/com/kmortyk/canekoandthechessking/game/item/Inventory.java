package com.kmortyk.canekoandthechessking.game.item;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

    public static final int MAX_COUNT = 20;
    private List<GameItem> gameItems;

    public Inventory() {
        gameItems = new ArrayList<>(MAX_COUNT);
    }

    public List<GameItem> getItems() { return gameItems; }

    public void addItem(GameItem gameItem) {

        for (int i = 0; i < gameItems.size(); i++) {
            GameItem it = gameItems.get(i);
            if(it.equalsByName(gameItem) && it.is(GameItem.STACKABLE)) {
                it.add(1); // add one to count
                return;
            }
        }

        gameItems.add(gameItem);

    }

    public void removeItem(GameItem gameItem) { gameItems.remove(gameItem); }

}
