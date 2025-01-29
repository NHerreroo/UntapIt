package com.example.landtapmtg;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;

public class CardDialogFragment extends DialogFragment {

    private static final String ARG_NAME = "name";
    private static final String ARG_IMAGE = "image";
    private static final String ARG_DESCRIPTION = "description";

    public static CardDialogFragment newInstance(String name, String imageUrl, String description) {
        CardDialogFragment fragment = new CardDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putString(ARG_IMAGE, imageUrl);
        args.putString(ARG_DESCRIPTION, description);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_card, container, false);

        TextView cardName = view.findViewById(R.id.cardName);
        ImageView cardImage = view.findViewById(R.id.cardImage);
        TextView cardDescription = view.findViewById(R.id.cardDescription);

        if (getArguments() != null) {
            cardName.setText(getArguments().getString(ARG_NAME));
            cardDescription.setText(getArguments().getString(ARG_DESCRIPTION));

            String imageUrl = getArguments().getString(ARG_IMAGE);
            Glide.with(this).load(imageUrl).into(cardImage);
        }

        return view;
    }
}
