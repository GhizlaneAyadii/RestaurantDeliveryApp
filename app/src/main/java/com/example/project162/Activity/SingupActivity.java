package com.example.project162.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.projectgaz.R;
import com.example.projectgaz.databinding.ActivityIntroBinding;
import com.example.projectgaz.databinding.ActivitySingupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SingupActivity extends BaseActivity {
    ActivitySingupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySingupBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        setVariable();
        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirection vers l'activité de connexion
                Intent intent = new Intent(SingupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setVariable() {
        binding.singupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.userEdt.getText().toString();
                String password = binding.passEdtt.getText().toString();
                String nom = binding.nomEdt.getText().toString();
                String prenom = binding.prenomEdt.getText().toString();
                if (TextUtils.isEmpty(nom)) {
                    binding.nomError.setVisibility(View.VISIBLE);
                    binding.nomError.setText("Veuillez saisir votre nom.");
                    return;
                } else {
                    binding.nomError.setVisibility(View.GONE);
                }
                if (TextUtils.isEmpty(prenom)) {
                    binding.prenomError.setVisibility(View.VISIBLE);
                    binding.prenomError.setText("Veuillez saisir votre prénom.");
                    return;
                } else {
                    binding.prenomError.setVisibility(View.GONE);
                }

                if (TextUtils.isEmpty(email)) {
                    binding.userError.setVisibility(View.VISIBLE);
                    binding.userError.setText("Veuillez saisir votre adresse e-mail.");
                    return;
                } else {
                    binding.userError.setVisibility(View.GONE);
                }
                if (password.isEmpty()) {
                    binding.passwordError.setVisibility(View.VISIBLE);
                    binding.passwordError.setText("Veuillez saisir votre mot de passe.");
                    return;
                } else if (password.length() < 6) {
                    binding.passwordError.setVisibility(View.VISIBLE);
                    binding.passwordError.setText("Votre mot de passe doit comporter au moins 6 caractères.");
                    return;
                } else {
                    binding.passwordError.setVisibility(View.GONE);
                }

                // Validation de l'email


                // Si l'email est non vide, mais ne correspond pas au format attendu
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.userError.setVisibility(View.VISIBLE);
                    binding.userError.setText("Veuillez saisir une adresse e-mail valide.");
                    return;
                } else {
                    binding.userError.setVisibility(View.GONE);
                }

                // Créer un utilisateur avec l'email et le mot de passe fournis
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SingupActivity.this, task -> {
                            if (task.isSuccessful()) {
                                // Enregistrement du nom et prénom dans la base de données Firebase
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(nom + " " + prenom) // Concaténation du nom et prénom
                                        .build();

                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                Log.d(TAG, "User profile updated.");
                                            }
                                        });

                                showSuccessDialog();
                            } else {
                                Log.i(TAG, "failure " + task.getException());
                                // Traiter les différentes erreurs d'inscription ici
                                // Par exemple, si l'email est déjà utilisé
                                // afficher un message sous le champ email
                                binding.userError.setVisibility(View.VISIBLE);
                                binding.userError.setText("L'adresse e-mail est déjà utilisée.");
                            }
                        });
            }
        });
    }



    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Inscription réussie!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Redirection vers l'activité de connexion
                        Intent intent = new Intent(SingupActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}