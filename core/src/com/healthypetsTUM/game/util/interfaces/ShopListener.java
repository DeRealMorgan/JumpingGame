package com.healthypetsTUM.game.util.interfaces;

public interface ShopListener {
    void buy(int item, int cost);
    void equip(int item);

    void buyWorld(int item, int cost);
    void equipWorld(int item);

    void buyFood(int item, int cost);
}
