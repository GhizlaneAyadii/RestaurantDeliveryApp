package com.example.projectgaz;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.content.ClipData;
import android.content.ClipboardManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.projectgaz.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class PaimentFragment extends Fragment {

    private TextView dateTextView;
    private TextView numeroTextView;
    private TextView codeTextView;
    private TextView tempTextView;
    private AppCompatButton copyButton;
    private TextView remainingTimeTextView;
    private String paymentCode;
    private CountDownTimer countDownTimer;

    public PaimentFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_paiment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Récupérer les références des vues
        dateTextView = view.findViewById(R.id.date);
        numeroTextView = view.findViewById(R.id.numero);
        codeTextView = view.findViewById(R.id.code);
        tempTextView = view.findViewById(R.id.temp);
        copyButton =  view.findViewById(R.id.copyButton);
        remainingTimeTextView = view.findViewById(R.id.remainingTimeTextView);

        // Générer et afficher le code de paiement
        generatePaymentCode();

        // Afficher la date de la commande (jour actuel)
        displayOrderDate();

        // Gérer le clic sur le bouton de copie
        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyPaymentCodeToClipboard();
            }
        });

        // Définir et démarrer le compte à rebours pour la durée restante
        startCountDownTimer();
    }

    private void generatePaymentCode() {
        // Générer un code de paiement aléatoire de 10 chiffres
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            stringBuilder.append(random.nextInt(10)); // Ajoute un chiffre aléatoire de 0 à 9
        }
        paymentCode = stringBuilder.toString();

        // Afficher le code de paiement dans le TextView correspondant
        codeTextView.setText(paymentCode);

        // Générer et afficher le numéro de commande en hexadécimal
        String orderNumber = generateOrderNumber();
        numeroTextView.setText(orderNumber);
    }

    private String generateOrderNumber() {
        // Générer un nombre aléatoire
        Random random = new Random();
        long number = random.nextLong();

        // Convertir le nombre en une chaîne hexadécimale
        String hexString = Long.toHexString(number);

        // Si la longueur de la chaîne est supérieure à 10, tronquer les caractères supplémentaires
        if (hexString.length() > 10) {
            hexString = hexString.substring(0, 10);
        }

        // Ajouter des zéros en tête si nécessaire pour atteindre une longueur de 10 caractères
        while (hexString.length() < 10) {
            hexString = "0" + hexString;
        }

        return hexString.toUpperCase(); // Convertir en majuscules pour une représentation hexadécimale cohérente
    }


    private void copyPaymentCodeToClipboard() {
        // Copier le code de paiement dans le presse-papiers
        ClipboardManager clipboard = (ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Code de paiement", paymentCode);
        clipboard.setPrimaryClip(clip);

        // Afficher un message Toast pour indiquer que le code a été copié
        Toast.makeText(requireContext(), "Code copié dans le presse-papiers", Toast.LENGTH_SHORT).show();
    }

    private void startCountDownTimer() {
        // Définir le temps restant à 24 heures (en millisecondes)
        long remainingTimeInMillis = TimeUnit.HOURS.toMillis(24);

        // Démarrer le compte à rebours
        countDownTimer = new CountDownTimer(remainingTimeInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Convertir le temps restant en heures, minutes et secondes
                long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60;
                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60;

                // Afficher le temps restant dans le TextView correspondant
                tempTextView.setText( String.format("%02d:%02d:%02d", hours, minutes, seconds));
            }

            @Override
            public void onFinish() {
                // Gérer les actions à effectuer lorsque le compte à rebours est terminé
            }
        }.start(); // Ne pas oublier de démarrer le compte à rebours avec la méthode start()
    }

    private void displayOrderDate() {
        // Obtenir la date actuelle
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = dateFormat.format(calendar.getTime());

        // Afficher la date de la commande dans le TextView correspondant
        dateTextView.setText(currentDate);
    }
}
