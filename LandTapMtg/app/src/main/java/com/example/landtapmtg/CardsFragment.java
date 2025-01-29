package com.example.landtapmtg;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class CardsFragment extends Fragment {

    private EditText searchInput;
    private Button searchButton;
    private RequestQueue requestQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cards, container, false);

        searchInput = view.findViewById(R.id.searchInput);
        searchButton = view.findViewById(R.id.searchButton);
        requestQueue = Volley.newRequestQueue(requireContext());

        searchButton.setOnClickListener(v -> searchCard());

        return view;
    }

    private void searchCard() {
        String cardName = searchInput.getText().toString().trim();
        if (cardName.isEmpty()) {
            Toast.makeText(getContext(), "Introduce el nombre de una carta", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Codificar el nombre de la carta correctamente para la URL
            String encodedCardName = URLEncoder.encode(cardName, "UTF-8");
            String url = "https://api.scryfall.com/cards/named?fuzzy=" + encodedCardName;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    response -> {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String name = jsonResponse.getString("name");

                            // Manejar diferentes estructuras de imágenes
                            String imageUrl = null;
                            if (jsonResponse.has("image_uris")) {
                                imageUrl = jsonResponse.getJSONObject("image_uris").getString("normal");
                            } else if (jsonResponse.has("card_faces")) {
                                // Si es una carta de dos caras, toma la primera imagen
                                imageUrl = jsonResponse.getJSONArray("card_faces")
                                        .getJSONObject(0)
                                        .getJSONObject("image_uris")
                                        .getString("normal");
                            }

                            String description = jsonResponse.optString("oracle_text", "Sin descripción");

                            // Mostrar el DialogFragment con la información de la carta
                            CardDialogFragment dialogFragment = CardDialogFragment.newInstance(name, imageUrl, description);
                            dialogFragment.show(getParentFragmentManager(), "CardDialog");

                        } catch (JSONException e) {
                            Toast.makeText(getContext(), "Error al procesar la respuesta", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> Toast.makeText(getContext(), "Carta no encontrada", Toast.LENGTH_SHORT).show());

            requestQueue.add(stringRequest);

        } catch (UnsupportedEncodingException e) {
            Toast.makeText(getContext(), "Error al codificar el nombre", Toast.LENGTH_SHORT).show();
        }
    }
}
