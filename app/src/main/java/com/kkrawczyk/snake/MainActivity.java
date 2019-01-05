package com.kkrawczyk.snake;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Główna aktywność programu,posiada jeden przycisk pozwalający na rozpoczęcie gry i przejście do nowej aktywności
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Pole reprezentujące przycisk rozpoczęcia nowej gry
     */
    private Button startNewGameBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startNewGameBtn = findViewById(R.id.btn_new_game);

        setUpNewGameButtonListener();
    }

    /**
     * Metoda ustawiająca listenera dla przycisku rozpoczęcia nowej gry,
     * po kliknięciu uruchamia nową aktywność
     * {@link SnakeActivity}
     */
    private void setUpNewGameButtonListener() {
        this.startNewGameBtn.setOnClickListener((View btn) -> {
            Intent intent = new Intent(this, SnakeActivity.class);
            startActivity(intent);
        });
    }
}
