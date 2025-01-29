package com.example.landtapmtg;

public class PlayerLife {
    private int life;

    public PlayerLife() {
        this.life = 40; // Valor inicial de vidas en MTG Commander
    }

    public int getLife() {
        return life;
    }

    public void increaseLife() {
        this.life++;
    }

    public void decreaseLife() {
        this.life--;
    }

    public void resetLife() {
        this.life = 40;
    }
}
