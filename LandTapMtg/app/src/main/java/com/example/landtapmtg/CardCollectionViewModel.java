package com.example.landtapmtg;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class CardCollectionViewModel extends AndroidViewModel {
    private final LiveData<List<CardEntity>> allCards;
    private final CardDao cardDao;

    public CardCollectionViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getDatabase(application);
        cardDao = db.cardDao();
        allCards = cardDao.getAllCards();
    }

    public LiveData<List<CardEntity>> getAllCards() {
        return allCards;
    }

    public void insert(CardEntity card) {
        AppDatabase.databaseWriteExecutor.execute(() -> cardDao.insert(card));
    }

    public void delete(CardEntity card) {
        AppDatabase.databaseWriteExecutor.execute(() -> cardDao.delete(card));
    }

    public LiveData<List<CardEntity>> filterCards(String name) {
        return cardDao.filterCardsByName("%" + name + "%");
    }

}