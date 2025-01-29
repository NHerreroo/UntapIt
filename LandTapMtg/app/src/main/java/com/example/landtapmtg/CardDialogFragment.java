package com.example.landtapmtg;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;

public class CardDialogFragment extends DialogFragment {

    private static final String ARG_NAME = "name";
    private static final String ARG_IMAGE = "image";
    private static final String ARG_PRICE = "price";

    public static CardDialogFragment newInstance(String name, String imageUrl, String price) {
        CardDialogFragment fragment = new CardDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putString(ARG_IMAGE, imageUrl);
        args.putString(ARG_PRICE, price);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_card, container, false);

        TextView cardName = view.findViewById(R.id.cardName);
        ImageView cardImage = view.findViewById(R.id.cardImage);
        TextView cardPrice = view.findViewById(R.id.cardPrice);
        Button addToCollectionButton = view.findViewById(R.id.addToCollectionButton);
        Button closeButton = view.findViewById(R.id.closeButton);

        if (getArguments() != null) {
            cardName.setText(getArguments().getString(ARG_NAME));
            cardPrice.setText(getArguments().getString(ARG_PRICE));
            String imageUrl = getArguments().getString(ARG_IMAGE);
            Glide.with(this).load(imageUrl).into(cardImage);
        }

        // Botón para añadir la carta a la colección
        addToCollectionButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Carta añadida a la colección", Toast.LENGTH_SHORT).show();
            dismiss();
        });

        // Botón para cerrar el diálogo
        closeButton.setOnClickListener(v -> dismiss());

        return view;
    }
}
