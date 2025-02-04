package com.example.landtapmtg;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

public class CardDetailActivity extends AppCompatActivity {
    private static final String SCRYFALL_API_URL = "https://api.scryfall.com/cards/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);

        String scryfallId = getIntent().getStringExtra("SCRYFALL_ID");
        fetchCardDetails(scryfallId);
    }

    private void fetchCardDetails(String scryfallId) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(SCRYFALL_API_URL + scryfallId)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject cardData = new JSONObject(response.body().string());
                        runOnUiThread(() -> {
                            try {
                                updateUI(cardData);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    } catch (JSONException e) {
                        showError();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                showError();
            }
        });
    }

    private void updateUI(JSONObject cardData) throws JSONException {
        TextView nameView = findViewById(R.id.detail_card_name);
        TextView typeView = findViewById(R.id.detail_card_type);
        TextView textView = findViewById(R.id.detail_card_text);
        ImageView imageView = findViewById(R.id.detail_card_image);
        TextView costView = findViewById(R.id.mana_cost);
        TextView powerView = findViewById(R.id.detail_card_power);
        TextView toughnessView = findViewById(R.id.detail_card_thougness);

        // Asignar valores con optString() para evitar errores si faltan datos
        nameView.setText(cardData.getString("name"));
        typeView.setText(cardData.getString("type_line"));
        textView.setText(cardData.optString("oracle_text", "Sin descripción"));
        costView.setText(cardData.optString("mana_cost", "No tiene costo"));
        powerView.setText("Power: " + cardData.optString("power", "-"));
        toughnessView.setText("Toughness: " + cardData.optString("toughness", "-"));

        // Manejo de imágenes (usar art_crop si está disponible, si no usar normal)
        if (cardData.has("image_uris")) {
            JSONObject imageUris = cardData.getJSONObject("image_uris");
            String imageUrl = imageUris.optString("art_crop", imageUris.optString("normal", ""));
            Glide.with(this).load(imageUrl).into(imageView);
        }
    }


    private void showError() {
        runOnUiThread(() ->
                Toast.makeText(this, "Error cargando detalles", Toast.LENGTH_SHORT).show());
    }
}