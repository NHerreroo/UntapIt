package com.example.landtapmtg;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "cards")
public class CardEntity implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "scryfall_id")
    public String scryfallId;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "mana_cost")
    public String manaCost;

    @ColumnInfo(name = "type_line")
    public String typeLine;

    @ColumnInfo(name = "oracle_text")
    public String oracleText;

    @ColumnInfo(name = "image_url")
    public String imageUrl;

    @ColumnInfo(name = "price")
    public String price;

    public CardEntity(String scryfallId, String name, String manaCost, String typeLine,
                      String oracleText, String imageUrl, String price) {
        this.scryfallId = scryfallId;
        this.name = name;
        this.manaCost = manaCost;
        this.typeLine = typeLine;
        this.oracleText = oracleText;
        this.imageUrl = imageUrl;
        this.price = price;
    }
}