package com.example.landtapmtg;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GameViewModel extends ViewModel {
    private final MutableLiveData<Integer> topLeftLife = new MutableLiveData<>(40);
    private final MutableLiveData<Integer> topRightLife = new MutableLiveData<>(40);
    private final MutableLiveData<Integer> botLeftLife = new MutableLiveData<>(40);
    private final MutableLiveData<Integer> botRightLife = new MutableLiveData<>(40);

    public LiveData<Integer> getTopLeftLife() {
        return topLeftLife;
    }

    public LiveData<Integer> getTopRightLife() {
        return topRightLife;
    }

    public LiveData<Integer> getBotLeftLife() {
        return botLeftLife;
    }

    public LiveData<Integer> getBotRightLife() {
        return botRightLife;
    }

    public void increaseTopLeftLife() {
        topLeftLife.setValue(topLeftLife.getValue() + 1);
    }

    public void decreaseTopLeftLife() {
        topLeftLife.setValue(topLeftLife.getValue() - 1);
    }

    public void increaseTopRightLife() {
        topRightLife.setValue(topRightLife.getValue() + 1);
    }

    public void decreaseTopRightLife() {
        topRightLife.setValue(topRightLife.getValue() - 1);
    }

    public void increaseBotLeftLife() {
        botLeftLife.setValue(botLeftLife.getValue() + 1);
    }

    public void decreaseBotLeftLife() {
        botLeftLife.setValue(botLeftLife.getValue() - 1);
    }

    public void increaseBotRightLife() {
        botRightLife.setValue(botRightLife.getValue() + 1);
    }

    public void decreaseBotRightLife() {
        botRightLife.setValue(botRightLife.getValue() - 1);
    }

    public void resetAllLives() {
        topLeftLife.setValue(40);
        topRightLife.setValue(40);
        botLeftLife.setValue(40);
        botRightLife.setValue(40);
    }
}
