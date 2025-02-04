package com.example.landtapmtg;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
    private EditText searchInput, filterInput;
    private Button searchButton;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final OkHttpClient client = new OkHttpClient();
    private CardCollectionViewModel viewModel;
    private CardAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cards, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CardAdapter();
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(CardCollectionViewModel.class);
        viewModel.getAllCards().observe(getViewLifecycleOwner(), adapter::setCards);

        searchInput = view.findViewById(R.id.searchInput);
        filterInput = view.findViewById(R.id.filterInput);
        searchButton = view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(v -> searchCard());

        filterInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCollection(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                CardEntity cardToDelete = adapter.getCardAtPosition(position);
                viewModel.delete(cardToDelete);
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);
        return view;
    }

    private void searchCard() {
        String cardName = searchInput.getText().toString().trim();
        if (cardName.isEmpty()) {
            Toast.makeText(getContext(), "Introduce el nombre de una carta", Toast.LENGTH_SHORT).show();
            return;
        }

        String apiUrl = "https://api.scryfall.com/cards/named?exact=" + cardName.replace(" ", "+");
        Request request = new Request.Builder().url(apiUrl).get().build();

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
                    String scryfallId = jsonResponse.getString("id");
                    String name = jsonResponse.getString("name");
                    String manaCost = jsonResponse.optString("mana_cost", "");
                    String typeLine = jsonResponse.getString("type_line");
                    String oracleText = jsonResponse.optString("oracle_text", "");
                    String price = jsonResponse.getJSONObject("prices").optString("usd", "Sin precio");
                    String imageUrl = getImageUrl(jsonResponse);

                    CardEntity newCard = new CardEntity(scryfallId, name, manaCost, typeLine, oracleText, imageUrl, price);
                    mainHandler.post(() -> {
                        CardDialogFragment dialogFragment = CardDialogFragment.newInstance(newCard);
                        dialogFragment.show(getParentFragmentManager(), "CardDialog");
                    });
                } catch (JSONException e) {
                    mainHandler.post(() -> Toast.makeText(getContext(), "Error al procesar la respuesta", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    private String getImageUrl(JSONObject jsonResponse) throws JSONException {
        if (jsonResponse.has("image_uris")) {
            return jsonResponse.getJSONObject("image_uris").getString("png");
        } else if (jsonResponse.has("card_faces")) {
            return jsonResponse.getJSONArray("card_faces").getJSONObject(0).getJSONObject("image_uris").getString("png");
        }
        return null;
    }

    private void filterCollection(String query) {
        if (query.isEmpty()) {
            viewModel.getAllCards().removeObservers(getViewLifecycleOwner());
            viewModel.getAllCards().observe(getViewLifecycleOwner(), adapter::setCards);
        } else {
            viewModel.filterCards(query).observe(getViewLifecycleOwner(), adapter::setCards);
        }
    }
}
