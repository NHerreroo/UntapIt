package com.example.landtapmtg;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class GameFragment extends Fragment {

    private GameViewModel gameViewModel;

    public GameFragment() {
        // Constructor público requerido
    }

    public static GameFragment newInstance(String param1, String param2) {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Configuración de botones y contadores (superiores e inferiores)
        setupPlayerControls(view, R.id.buttonAddTopLeft, R.id.buttonSubtractTopLeft, R.id.counterTopLeft,
                gameViewModel.getTopLeftLife(), gameViewModel::increaseTopLeftLife, gameViewModel::decreaseTopLeftLife);

        setupPlayerControls(view, R.id.buttonAddTopRight, R.id.buttonSubtractTopRight, R.id.counterTopRight,
                gameViewModel.getTopRightLife(), gameViewModel::increaseTopRightLife, gameViewModel::decreaseTopRightLife);

        setupPlayerControls(view, R.id.buttonAddBotLeft, R.id.buttonSubtractBotLeft, R.id.counterBotLeft,
                gameViewModel.getBotLeftLife(), gameViewModel::increaseBotLeftLife, gameViewModel::decreaseBotLeftLife);

        setupPlayerControls(view, R.id.buttonAddBotRight, R.id.buttonSubtractBotRight, R.id.counterBotRight,
                gameViewModel.getBotRightLife(), gameViewModel::increaseBotRightLife, gameViewModel::decreaseBotRightLife);

        // Botón de reiniciar
        Button buttonRestart = view.findViewById(R.id.buttonRestart);
        buttonRestart.setOnClickListener(v -> gameViewModel.resetAllLives());
    }

    /**
     * Configura los botones de sumar, restar y la visualización del contador de un jugador.
     *
     * @param view         Vista raíz del fragmento
     * @param addButtonId  ID del botón de sumar
     * @param subButtonId  ID del botón de restar
     * @param counterId    ID del TextView del contador
     * @param liveData     LiveData que representa las vidas del jugador
     * @param increaseFunc Función para incrementar las vidas
     * @param decreaseFunc Función para disminuir las vidas
     */
    private void setupPlayerControls(View view, int addButtonId, int subButtonId, int counterId,
                                     androidx.lifecycle.LiveData<Integer> liveData,
                                     Runnable increaseFunc, Runnable decreaseFunc) {
        Button addButton = view.findViewById(addButtonId);
        Button subtractButton = view.findViewById(subButtonId);
        TextView counter = view.findViewById(counterId);

        // Observador para actualizar el contador
        liveData.observe(getViewLifecycleOwner(), life -> counter.setText(String.valueOf(life)));

        // Configuración de botones
        addButton.setOnClickListener(v -> increaseFunc.run());
        subtractButton.setOnClickListener(v -> decreaseFunc.run());
    }
}
