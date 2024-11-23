package com.example.project162.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.projectgaz.R;
import com.example.projectgaz.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class LoginActivity extends BaseActivity {
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setVariable();
        binding.singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirection vers l'activité de connexion
                Intent intent = new Intent(LoginActivity.this, SingupActivity.class);
                startActivity(intent);
            }
        });
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.userEdt.getText().toString();
                String password = binding.passEdtt.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    binding.userError.setVisibility(View.VISIBLE);
                    binding.userError.setText("Veuillez saisir votre adresse e-mail.");
                    return;
                } else {
                    binding.userError.setVisibility(View.GONE);
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.userError.setVisibility(View.VISIBLE);
                    binding.userError.setText("Veuillez saisir une adresse e-mail valide.");
                    return;
                } else {
                    binding.userError.setVisibility(View.GONE);
                }

                if (TextUtils.isEmpty(password)) {
                    binding.passwordError.setVisibility(View.VISIBLE);
                    binding.passwordError.setText("Veuillez saisir votre mot de passe.");
                    return;
                } else {
                    binding.passwordError.setVisibility(View.GONE);
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
                                    editor.putBoolean("isLoggedIn", true);
                                    editor.apply();
                                    // L'utilisateur est authentifié avec succès, rediriger vers MainActivity
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                } else {
                                    // L'authentification a échoué
                                    showErrorAlert("L'authentification a échoué. Veuillez réessayer.");
                                }
                            }
                        });
            }
        });

// Dans la méthode onCreate() de votre activité
        ImageView facebookImageView = findViewById(R.id.facebook);
        facebookImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirigez vers le lien Facebook ici
                String facebookUrl = "https://www.facebook.com"; // URL de la page Facebook
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl));
                startActivity(intent);
            }
        });

        ImageView googleImageView = findViewById(R.id.google);
        googleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirigez vers le lien Google ici
                String googleUrl = "https://www.google.com"; // URL de la page Google
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(googleUrl));
                startActivity(intent);
            }
        });

        ImageView twitterImageView = findViewById(R.id.twitter);
        twitterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirigez vers le lien Twitter ici
                String twitterUrl = "https://www.twitter.com"; // URL de la page Twitter
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterUrl));
                startActivity(intent);
            }
        });
    }

    // Méthode pour afficher une alerte avec un message d'erreur
    private void showErrorAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("OK", null); // Vous pouvez ajouter un écouteur pour le bouton si nécessaire
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setVariable() {
        // Ce bloc de code ne devrait pas être nécessaire car vous avez déjà défini un écouteur de clic sur le bouton loginBtn dans onCreate()
    }
}