package com.example.landtapmtg;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;

public class CardDialogFragment extends DialogFragment {
    private static final String ARG_CARD = "card";

    public static CardDialogFragment newInstance(CardEntity card) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CARD, card);
        CardDialogFragment fragment = new CardDialogFragment();
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

        // Obtener el objeto CardEntity
        CardEntity card = (CardEntity) getArguments().getSerializable(ARG_CARD);

        if (card != null) {
            cardName.setText(card.name);
            cardPrice.setText(card.price + "$");
            Glide.with(this).load(card.imageUrl).into(cardImage);
        }

        // Botón para añadir la carta a la colección
        addToCollectionButton.setOnClickListener(v -> {
            CardCollectionViewModel viewModel = new ViewModelProvider(requireActivity())
                    .get(CardCollectionViewModel.class);
            viewModel.insert(card);
            dismiss();
        });

        // Botón para cerrar el diálogo
        closeButton.setOnClickListener(v -> dismiss());

        return view;
    }
}