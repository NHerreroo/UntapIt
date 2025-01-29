package com.example.landtapmtg;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CardsFragment extends Fragment {

    private EditText searchInput;
    private Button searchButton;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final OkHttpClient client = new OkHttpClient();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cards, container, false);

        searchInput = view.findViewById(R.id.searchInput);
        searchButton = view.findViewById(R.id.searchButton);

        searchButton.setOnClickListener(v -> searchCard());

        return view;
    }

    private void searchCard() {
        String cardName = searchInput.getText().toString().trim();
        if (cardName.isEmpty()) {
            Toast.makeText(getContext(), "Introduce el nombre de una carta", Toast.LENGTH_SHORT).show();
            return;
        }

        String apiUrl = "https://api.scryfall.com/cards/named?exact=" + cardName.replace(" ", "+");

        Request request = new Request.Builder()
                .url(apiUrl)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                mainHandler.post(() -> Toast.makeText(getContext(), "Error en la búsqueda", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    mainHandler.post(() -> Toast.makeText(getContext(), "Carta no encontrada", Toast.LENGTH_SHORT).show());
                    return;
                }

                try {
                    JSONObject jsonResponse = new JSONObject(response.body().string());
                    String name = jsonResponse.getString("name");

                    String imageUrl = null;
                    if (jsonResponse.has("image_uris")) {
                        imageUrl = jsonResponse.getJSONObject("image_uris").getString("normal");
                    } else if (jsonResponse.has("card_faces")) {
                        imageUrl = jsonResponse.getJSONArray("card_faces")
                                .getJSONObject(0)
                                .getJSONObject("image_uris")
                                .getString("normal");
                    }

                    String description = jsonResponse.optString("oracle_text", "Sin descripción");

                    String finalImageUrl = imageUrl;
                    mainHandler.post(() -> {
                        CardDialogFragment dialogFragment = CardDialogFragment.newInstance(name, finalImageUrl, description);
                        dialogFragment.show(getParentFragmentManager(), "CardDialog");
                    });

                } catch (JSONException e) {
                    mainHandler.post(() -> Toast.makeText(getContext(), "Error al procesar la respuesta", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
}
