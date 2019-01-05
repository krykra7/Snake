package com.kkrawczyk.snake;

import java.util.ArrayList;

/**
 * Created by kkrawczyk on 04.01.2019.
 * <p>
 * Klasa reprezentująca węża
 */
public class Snake {
    /**
     * Stała pomocnicza używana do określenia indexu głowy węża w jego ciele
     */
    private static final int HEAD_INDEX = 0;

    /**
     * Lista reprezentująca segmenty składające się na ciało węża
     */
    private ArrayList<SnakeSegment> body = new ArrayList<>();

    /**
     * Zmienna określająca aktualny kierunek poruszania się głowy węża
     */
    private Direction headDirection = Direction.LEFT;


    /**
     * Metoda poruszająca głową i całem węża zależnie od stanu zmiennej {@link Snake#headDirection},
     * metoda przechodzi po wszystkich segmentach węża i przestawia je jeden po drugim na miejsce
     * segmentu przed nim. Wyjątek stanowi głowa, która nie ma segmentu przed nią i jest przesuwana
     * zgodnie z okreslonym kierunkiem.
     */
    public void move() {
        for (int i = body.size() - 1; i > HEAD_INDEX; i--) {
            body.get(i).setPosX(body.get(i - 1).getPosX());
            body.get(i).setPosY(body.get(i - 1).getPosY());
        }

        int headPosX = getHead().getPosX();
        int headPosY = getHead().getPosY();

        switch (headDirection) {
            case UP:
                getHead().setPosY(--headPosY);
                break;
            case RIGHT:
                getHead().setPosX(++headPosX);
                break;
            case LEFT:
                getHead().setPosX(--headPosX);
                break;
            case DOWN:
                getHead().setPosY(++headPosY);
                break;
        }
    }

    /**
     * Metoda reprezentująca zjedzenie kolejnego segmentu poprzed dodanie go do ciała węża
     *
     * @param eatenSegment repreznetuje zjedzony segment
     */
    public void eat(SnakeSegment eatenSegment) {
        body.add(eatenSegment);
    }

    /**
     * Getter dla kierunku węża
     *
     * @return zwraca akutalny kierunek poruszania się węża
     */
    public Direction getHeadDirection() {
        return headDirection;
    }

    /**
     * Setter dla kierunku węża
     *
     * @param headDirection nowy kierunek węża
     */
    public void setHeadDirection(Direction headDirection) {
        this.headDirection = headDirection;
    }

    /**
     * Metoda pomocnicza wydobywająca z ciałą głowę węża
     *
     * @return zwraca segment reprezentujący głowę węża
     */
    public SnakeSegment getHead() {
        return body.get(HEAD_INDEX);
    }

    /**
     * Getter dla ciała węża
     *
     * @return zwraca ciało węża
     */
    public ArrayList<SnakeSegment> getBody() {
        return body;
    }
}
