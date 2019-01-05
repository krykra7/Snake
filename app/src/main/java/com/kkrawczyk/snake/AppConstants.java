package com.kkrawczyk.snake;

/**
 * Created by kkrawczyk on 04.01.2019.
 * <p>
 * Klasa pomocnicza zawierająca stałe wartości dla całej aplikacji
 */
public class AppConstants {

    /**
     * Stała używana w instrukcji warunkowej do określenia naciśniętej strzałki kierunku - w lewo
     */
    public static final int LEFT_ARROW_CLICKED = 1;
    /**
     * Stała używana w instrukcji warunkowej do określenia naciśniętej strzałki kierunku - w prawo
     */
    public static final int RIGHT_ARROW_CLICKED = 0;
    /**
     * Ilość "bloków" po których porusza się gracz w osi horyzontalnej
     */
    public static final int NUMBER_OF_BLOCK_HORIZONTAL = 30;
    /**
     * Pomocnicza stała określająca ilość milisekund w sekundzie
     */
    public static final int MILLIS_IN_SECOND = 1000;
    /**
     * Szybkość odświeżania ekranu podczas rozgrywki
     */
    public static final int FPS = 7;
}
