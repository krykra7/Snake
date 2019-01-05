package com.kkrawczyk.snake;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by kk on 1/4/2019.
 * <p>
 * Klasa dialogu wyświetlanego podczas przegranej
 */
public class GameSummaryDialog extends BottomSheetDialogFragment {
    /**
     * Stała wykorzystywana jako klucz argumetnu przy przekazywaniu zdobytych punktów
     */
    private static final String SCORE_KEY = "score_key";

    /**
     * Interfejs słuchacza obsługujący kliknięcia w przyciski dialogu
     */
    private OnDialogButtonsClickedListener dialogButtonsClickedListener;

    /**
     * Metoda tworząca instancję dialogu, przekazuje do dialogu punkty w postaci bundla,
     * które później odzyskiwane są w odpowiedniej metodzie cyklu życia cialogu
     * {@link BottomSheetDialogFragment#onCreateView(LayoutInflater, ViewGroup, Bundle)},
     * dodatkowo ustawiany jest listener
     *
     * @param score                        ilość punktów zdobytych przez graczka
     * @param dialogButtonsClickedListener implementacja słuchacza obsługującego akcje dialogu
     * @return zwraca nową instancję dialogu
     */
    public static GameSummaryDialog newInstance(int score,
                                                OnDialogButtonsClickedListener dialogButtonsClickedListener) {
        GameSummaryDialog dialog = new GameSummaryDialog();
        dialog.setDialogButtonsClickedListener(dialogButtonsClickedListener);
        dialog.setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.BottomSheetDialog);

        Bundle args = new Bundle();
        args.putInt(SCORE_KEY, score);
        dialog.setArguments(args);

        return dialog;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View dialogView = inflater.inflate(R.layout.fragment_summary, container, false);
        TextView summaryTv = dialogView.findViewById(R.id.tv_summary_scores);
        summaryTv.append(String.valueOf(getArguments() != null ? getArguments().getInt(SCORE_KEY) : 0));
        setActionButtonListeners(dialogView);

        return dialogView;
    }


    /**
     * Metoda ustawia listenery na przyciskach dialogu,
     * a po kliknięciu wywołuje callback na {@link OnDialogButtonsClickedListener}
     *
     * @param dialogView widok dialogu
     */
    private void setActionButtonListeners(View dialogView) {
        dialogView.findViewById(R.id.btn_summary_continue).setOnClickListener(v -> {
            this.dismiss();
            dialogButtonsClickedListener.newGameClicked();
        });

        dialogView.findViewById(R.id.btn_summary_cancel).setOnClickListener(v -> {
            this.dismiss();
            dialogButtonsClickedListener.leaveGameClicked();
        });
    }


    /**
     * Setter dla interfejsu obsługującego kliknięć w przycisku dialogu
     *
     * @param dialogButtonsClickedListener implementacja listenera
     */
    public void setDialogButtonsClickedListener(OnDialogButtonsClickedListener dialogButtonsClickedListener) {
        this.dialogButtonsClickedListener = dialogButtonsClickedListener;
    }


    /**
     * Definicja interfejsu obsługującego kliknięcia w przyciski dialogu
     */
    public interface OnDialogButtonsClickedListener {

        /**
         * Metoda(callback) wywoływana przy kliknięci w przycisk rozpoczęcia nowej gry
         */
        void newGameClicked();


        /**
         * Metoda(callback) wywoływana przy kliknięcie w przycisk opuszczenia gry
         */
        void leaveGameClicked();
    }
}
