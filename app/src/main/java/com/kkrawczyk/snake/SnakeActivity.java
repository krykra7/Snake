package com.kkrawczyk.snake;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * Created by kkrawczyk on 04.01.2019.
 * <p>
 * Główna aktywność odpowiedzialna za rozpoczynanie i kończenie rozgrywki
 */
public class SnakeActivity extends AppCompatActivity implements GameSummaryDialog.OnDialogButtonsClickedListener {

    /**
     * Pole reprezentujące silnik obsługujący planszę na której odbywa się rozgrywka
     */
    private SnakeSurfaceEngine surfaceEngine;
    /**
     * Pole reprezentujące widok strzałki w lewo
     */
    private ImageView leftArrowIv;

    /**
     * Pole reprezentujące widok strzałki w prawo
     */
    private ImageView rightArrowIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snake);

        surfaceEngine = findViewById(R.id.sv_game_field);
        leftArrowIv = findViewById(R.id.iv_control_panel_left);
        rightArrowIv = findViewById(R.id.iv_control_panel_right);

        setupControlListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        runGame();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.surfaceEngine.stopGame();
    }

    /**
     * Metoda ustawiająca listenery dla panelu kontroli kierunku poruszania
     */
    private void setupControlListeners() {
        leftArrowIv.setOnClickListener(v -> surfaceEngine.changeDirection(AppConstants.LEFT_ARROW_CLICKED));
        rightArrowIv.setOnClickListener(v -> surfaceEngine.changeDirection(AppConstants.RIGHT_ARROW_CLICKED));
    }

    /**
     * Metoda rozpoczynająca rozgrywkę, w metodzie ustawiany jest listener wykrywający moment
     * w cyklu życia widoku w którym poprawnie można obliczyć wysokość i szerokość planszy,
     * następnie po wydobyciu wysokości i szerokości inicjalizowany jest silnik gry.
     */
    private void runGame() {
        surfaceEngine.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width = surfaceEngine.getWidth();
                int height = surfaceEngine.getHeight();

                surfaceEngine.setupGame(width, height);
                surfaceEngine.startNewGame();

                surfaceEngine.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    /**
     * Metoda pokazująca na ekranie dialog sygnalizujący zakończenie rozgrywki
     *
     * @param score punkty po zakończonej rozgrywce
     */
    public void showSummaryDialog(int score) {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        BottomSheetDialogFragment bottomSheetDialogFragment = GameSummaryDialog.newInstance(score, this);
        bottomSheetDialogFragment.show(fragmentManager, "Bottom Sheet");
    }

    /**
     * implementacja metody listenera {@link GameSummaryDialog.OnDialogButtonsClickedListener#newGameClicked()}
     * tak aby rozpoczynała się nowa gra
     */
    @Override
    public void newGameClicked() {
        surfaceEngine.startNewGame();
    }

    /**
     * implementacja metody listenera {@link GameSummaryDialog.OnDialogButtonsClickedListener#leaveGameClicked()}
     * tak aby powrócić do ekranu z {@link MainActivity} i przerwać rozgrywkę
     */
    @Override
    public void leaveGameClicked() {
        surfaceEngine.stopGame();
        this.onBackPressed();
    }
}
