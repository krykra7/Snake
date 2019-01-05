package com.kkrawczyk.snake;

/**
 * Created by kkrawczyk on 04.01.2019.
 * <p>
 * Klasa reprezentująca segment węża
 */
public class SnakeSegment {
    /**
     * Pole reprezentujące pozycje w osi horyzontalnej
     */
    private int posX;
    /**
     * Pole reprezentujące pozycję w osi wertykalnej
     */
    private int posY;


    /**
     * Konstruktor
     *
     * @param posX pozycja w osi horyzontalej
     * @param posY pozycja w osi wertykalnej
     */
    public SnakeSegment(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    /**
     * Getter dla pozycji w osi horyzontalnej
     *
     * @return pozycja w osi horyzontalnej
     */
    public int getPosX() {
        return posX;
    }

    /**
     * Setter dla pozycji horyzontalnej
     *
     * @param posX nowa pozycja w osi horyzontalnej
     */
    public void setPosX(int posX) {
        this.posX = posX;
    }

    /**
     * Getter dla pozycji w osi wertykalnej
     *
     * @return pozycja w osi wertykalnej
     */
    public int getPosY() {
        return posY;
    }

    /**
     * Setter dla pozycji wertykalnej
     *
     * @param posY nowa pozycja w osi wertykanlen
     */
    public void setPosY(int posY) {
        this.posY = posY;
    }
}
