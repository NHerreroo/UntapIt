package com.example.landtapmtg;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private final OkHttpClient client = new OkHttpClient();
    private static final String NEWS_API_URL = "https://newsapi.org/v2/everything?q=magic%20the%20gathering&apiKey=8594305cc4414200860823acce82ce5b";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.newsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        newsAdapter = new NewsAdapter(new ArrayList<>());
        recyclerView.setAdapter(newsAdapter);
        fetchNews();
        return view;
    }

    private void fetchNews() {
        Request request = new Request.Builder().url(NEWS_API_URL).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                getActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Error al obtener noticias", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response.body().string());
                        JSONArray articles = jsonResponse.getJSONArray("articles");
                        List<NewsItem> newsList = new ArrayList<>();
                        for (int i = 0; i < articles.length(); i++) {
                            JSONObject article = articles.getJSONObject(i);
                            String title = article.getString("title");
                            String url = article.getString("url");
                            String imageUrl = article.optString("urlToImage", "");
                            newsList.add(new NewsItem(title, url, imageUrl));
                        }
                        getActivity().runOnUiThread(() -> newsAdapter.updateNews(newsList));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
