package com.example.landtapmtg;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
    private List<CardEntity> cards;

    public void setCards(List<CardEntity> cards) {
        this.cards = cards;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        CardEntity current = cards.get(position);

        holder.name.setText(current.name);
        holder.manaCost.setText(current.manaCost);
        holder.type.setText(current.typeLine);
        holder.oracleText.setText(current.oracleText);

        Glide.with(holder.itemView.getContext())
                .load(current.imageUrl)
                .into(holder.image);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), CardDetailActivity.class);
            intent.putExtra("SCRYFALL_ID", current.scryfallId);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cards != null ? cards.size() : 0;
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {
        TextView name, manaCost, type, oracleText;
        ImageView image;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_card_name);
            manaCost = itemView.findViewById(R.id.item_card_mana_cost);
            type = itemView.findViewById(R.id.item_card_type);
            oracleText = itemView.findViewById(R.id.item_card_text);
            image = itemView.findViewById(R.id.item_card_image);
        }
    }
}