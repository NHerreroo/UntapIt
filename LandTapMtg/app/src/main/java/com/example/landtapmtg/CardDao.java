package com.example.landtapmtg;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CardDao {
    @Insert
    void insert(CardEntity card);

    @Delete
    void delete(CardEntity card);

    @Query("SELECT * FROM cards ORDER BY id DESC")
    LiveData<List<CardEntity>> getAllCards();

    @Query("SELECT * FROM cards WHERE name LIKE :query ORDER BY id DESC")
    LiveData<List<CardEntity>> filterCardsByName(String query);


}
