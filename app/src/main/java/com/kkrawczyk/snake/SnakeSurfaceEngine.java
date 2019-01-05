package com.kkrawczyk.snake;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

/**
 * Created by kkrawczyk on 04.01.2019.
 * <p>
 * Główna klasa programu obsługująca rozgrywkę oraz działająca w roli silnika gry
 */
public class SnakeSurfaceEngine extends SurfaceView implements Runnable {

    /**
     * Pole reprezentujące wątek na którym działa główna pętla gry
     */
    private Thread thread;
    /**
     * Kontekst aplikacji w tym przypadku {@link SnakeActivity}
     */
    private Context context;
    /**
     * Flaga determinująca czy gra działa czy nie
     */
    private boolean isRunning = false;
    /**
     * Pole reprezentujące czas obecnej klatki gry
     */
    private long frameTime;
    /**
     * Pole reprezentujące obecne punkty zdobyte przez gracza
     */
    private int score = 0;

    /**
     * Pole reprezentujące jedzenie dla węża na mapie rozgrywki
     */
    private SnakeSegment foodSegment;

    /**
     * Pole reprezentujące obiekt węża
     */
    private Snake snake;

    /**
     * Interfejs pozwalający na kontrolowanie, formatu, rozmiaru, pixeli znajdujących się na {@link SurfaceView}
     */
    private SurfaceHolder surfaceHolder;
    /**
     * Klasa trzymająca informację jak i w jakim kolorze rysować gemetrie, bitmapy, teksty
     */
    private Paint paint;
    /**
     * Klasa przetrzymująca "płótno" i pozwalająca na nim rysować,
     * aby okresli np. kolor rysowanego pixela używamy {@link android.graphics.Paint}
     */
    private Canvas canvas;

    /**
     * Rozmiar ściany segmentu w pikselach
     */
    private int segmentSize;
    /**
     * Ilość segmentów, które mieszczą się w osi pionowej
     */
    private int numberOfSegmentInVerticalAxis;

    /**
     * Konstruktor
     *
     * @param context kontekst aplikacji - w tym wypadku {@link SnakeActivity}
     */
    public SnakeSurfaceEngine(Context context) {
        super(context);
        this.context = context;
    }

    /**
     * Obowiązkowy kontruktora dla inicjalizacji widoku przez Framework Androida
     *
     * @param context kontekst aplikacji - w tym wypadku {@link SnakeActivity}
     * @param attrs   atrybuty widoku - przekazywane automatycznie przez Framework Androida przy inicjalizacji widoku
     */
    public SnakeSurfaceEngine(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    /**
     * Metoda ustawia podstawowe właściowści rozgrywki, takie jak wielkość segmentu,
     * ilość segmentów w płaszczyźnie pionowej.
     *
     * @param surfaceWidth  obliczona szerokość planszy
     * @param surfaceHeight obliczona wysokość planszy
     */
    public void setupGame(int surfaceWidth, int surfaceHeight) {
        segmentSize = surfaceWidth / AppConstants.NUMBER_OF_BLOCK_HORIZONTAL;
        numberOfSegmentInVerticalAxis = surfaceHeight / segmentSize;

        surfaceHolder = this.getHolder();
        paint = new Paint();
    }

    /**
     * Metoda działająca w nowym wątku i przechodząca przez główną pętlę gry tak długo,
     * aż wartość pola {@link SnakeSurfaceEngine#isRunning} będzie równa true,
     * ponadto ilość cykli jest ograniczona przez metodę {@link SnakeSurfaceEngine#isFrameElapsed()}
     * tak, aby spełniona była zadana ilośc klatek na sekundę.
     */
    @Override
    public void run() {
        while (isRunning) {
            if (isFrameElapsed()) {
                updateSnake();
                draw();
            }
        }
    }

    /**
     * Metoda odpowiedzialna za uruchomienie nowej rozgrywki, ustawia aktualny czas systemu dla obecnej klatki
     * {@link SnakeSurfaceEngine#frameTime}, następnie inicjuje węża z jednym segmentem - głową,
     * tworzy segment jedzenia i rozpoczyna nowy wątek
     */
    public void startNewGame() {
        score = 0;
        snake = new Snake();
        this.frameTime = System.currentTimeMillis();

        snake.getBody().add(new SnakeSegment(AppConstants.NUMBER_OF_BLOCK_HORIZONTAL / 2,
                numberOfSegmentInVerticalAxis / 2));

        createFood();
        startThread();
    }

    /**
     * Metoda inicjalizująca nowy wątek dla gry
     */
    private void startThread() {
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    /**
     * Metoda zabijająca wątek na którym działa gra
     */
    public void stopGame() {
        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda aktualizująca węża poprzez sprawdzenie czy zjadł on segment pokarmu,
     * jeśli tak to wąż wydłużany jest o 1 segment, oraz tworzony jest nowy segment pokarmu.
     * Następnie w metodzie wąż poruszany jest o 1 pole do przdu wraz z całym ciałem.
     * Na koniec sprawdzane jest czy wąż zdeżył się ze sobą lub ze ścianą,
     * co jest równoznaczne z wyświetleniem dialogu i przerwaniem rozgrywki.
     */
    private void updateSnake() {
        if (isOnFood()) {
            snake.eat(foodSegment);
            score++;
            createFood();
        }

        snake.move();

        if (isCrashed()) {
            isRunning = false;
            SnakeActivity parentActivity = (SnakeActivity) context;
            parentActivity.runOnUiThread(() -> parentActivity.showSummaryDialog(score));
        }
    }

    /**
     * Funkcja sprawdzająca czy głowa węża trafiła na segment pokarmu na planszy rozgrywki
     *
     * @return zwraca 'true' jeśli wąż zjadł pokarm i 'false' jeśli nie
     */
    private boolean isOnFood() {
        return snake.getHead().getPosX() == foodSegment.getPosX() && snake.getHead().getPosY() == foodSegment.getPosY();
    }

    /**
     * Metoda w pierwszej kolejności sprawdza czy głowa węża, zderzyła się z którąkolwiek ze ścian,
     * a następnie sprawdza czy głowa zderzyła się z którymkolwiek segmentem ciała węża.
     *
     * @return zwraca 'true' jeśli wąż zderzył się i 'false' jeśli się nie zderzył
     */
    private boolean isCrashed() {
        boolean crashed = false;
        SnakeSegment head = snake.getHead();

        if (head.getPosX() == -1) crashed = true;
        if (head.getPosX() >= AppConstants.NUMBER_OF_BLOCK_HORIZONTAL) crashed = true;
        if (head.getPosY() > numberOfSegmentInVerticalAxis) crashed = true;
        if (head.getPosY() == -1) crashed = true;

        for (SnakeSegment snakeSegment : snake.getBody().subList(1, snake.getBody().size())) {
            if (snakeSegment.getPosY() == head.getPosY() && snakeSegment.getPosX() == head.getPosX()) {
                crashed = true;
            }
        }

        return crashed;
    }

    /**
     * Metoda generuje nowy pokarm dla węża poprzez wylosowanie położenia na planszy gry
     */
    private void createFood() {
        Random random = new Random();
        int foodX = random.nextInt(AppConstants.NUMBER_OF_BLOCK_HORIZONTAL - 1) + 1;
        int foodY = random.nextInt(numberOfSegmentInVerticalAxis - 1) + 1;
        foodSegment = new SnakeSegment(foodX, foodY);
    }

    /**
     * Metoda odpowiedzialna za rysowanie całego ciała węża i segmentu pokarmu,
     * zmianę koloru tła na zielony.
     * Metoda wyzwalana jest z częstotliwością zadanej liczby klatek na sekundę
     * {@link AppConstants#FPS}
     */
    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();

            canvas.drawColor(Color.argb(255, 142, 179, 103));

            paint.setColor(Color.argb(255, 0, 0, 0));
            snake.getBody().forEach(snakeSegment -> canvas.drawRect(
                    snakeSegment.getPosX() * segmentSize,
                    snakeSegment.getPosY() * segmentSize,
                    snakeSegment.getPosX() * segmentSize + segmentSize,
                    snakeSegment.getPosY() * segmentSize + segmentSize,
                    paint));

            paint.setColor(Color.argb(255, 255, 0, 0));
            canvas.drawRect(
                    foodSegment.getPosX() * segmentSize,
                    foodSegment.getPosY() * segmentSize,
                    foodSegment.getPosX() * segmentSize + segmentSize,
                    foodSegment.getPosY() * segmentSize + segmentSize,
                    paint);

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    /**
     * Metoda zmienia aktualny kierunek poruszania się głowy węża
     * na podstawie klikniętej strzałki w panelu sterowania.
     *
     * @param clickedArrowCode liczba reprezentująca klikniętą strzałkę zmiany kierunku
     */
    public void changeDirection(int clickedArrowCode) {
        if (clickedArrowCode == AppConstants.RIGHT_ARROW_CLICKED) {
            switch (snake.getHeadDirection()) {
                case UP:
                    snake.setHeadDirection(Direction.RIGHT);
                    break;
                case RIGHT:
                    snake.setHeadDirection(Direction.DOWN);
                    break;
                case LEFT:
                    snake.setHeadDirection(Direction.UP);
                    break;
                case DOWN:
                    snake.setHeadDirection(Direction.LEFT);
                    break;
            }
        } else {
            switch (snake.getHeadDirection()) {
                case UP:
                    snake.setHeadDirection(Direction.LEFT);
                    break;
                case RIGHT:
                    snake.setHeadDirection(Direction.UP);
                    break;
                case LEFT:
                    snake.setHeadDirection(Direction.DOWN);
                    break;
                case DOWN:
                    snake.setHeadDirection(Direction.RIGHT);
                    break;
            }
        }
    }

    /**
     * Metoda zapewnia odpowiednią ilość klatek na sekundę,
     * poprzez sprawdzanie czy czas obecnej klatki jest już mniejszy lub równy
     * obecnemu.
     * Jeśli tak ustawiany jest nowy czas klatki na obecny + ilość czasu potrzebnego by zapewnić
     * odpowiednią ilośc klatek na sekunde.
     *
     * @return zwraca 'true' jeśli minął odpowiedni czas dla klatki, lub 'false' gdy upłynęło za mało czasu
     */
    private boolean isFrameElapsed() {
        if (frameTime <= System.currentTimeMillis()) {
            frameTime = System.currentTimeMillis() + AppConstants.MILLIS_IN_SECOND / AppConstants.FPS;

            return true;
        } else {
            return false;
        }
    }
}
