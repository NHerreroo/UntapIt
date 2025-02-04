package com.example.landtapmtg;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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
import androidx.recyclerview.widget.ItemTouchHelper;

public class CardsFragment extends Fragment {
    private EditText searchInput, filterInput;
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

        // Referencias a los EditText
        searchInput = view.findViewById(R.id.searchInput);
        filterInput = view.findViewById(R.id.filterInput);

        // Filtrar cartas en la colección en tiempo real
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

        // Permitir eliminar cartas de la colección deslizando
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

    private void filterCollection(String query) {
        if (query.isEmpty()) {
            viewModel.getAllCards().observe(getViewLifecycleOwner(), adapter::setCards);
        } else {
            viewModel.filterCards(query).observe(getViewLifecycleOwner(), adapter::setCards);
        }
    }
}
