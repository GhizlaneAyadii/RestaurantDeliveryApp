package com.example.projectgaz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project162.Activity.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ProfileFragment extends Fragment {
    private EditText nomEditText;
    private EditText prenomEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button updateButton;
    private TextView logout_btn;

    private ProgressBar progressBar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialisation des vues
        nomEditText = view.findViewById(R.id.nomEdt);
        prenomEditText = view.findViewById(R.id.prenomEdt);
        emailEditText = view.findViewById(R.id.userEdt);
        passwordEditText = view.findViewById(R.id.passEdtt);
        updateButton = view.findViewById(R.id.profile_update_btn);
        progressBar = view.findViewById(R.id.profile_progress_bar);
        logout_btn = view.findViewById(R.id.logoutBtn);

        // Appeler la méthode pour afficher les informations du profil de l'utilisateur
        displayUserProfile();

        // Écouteur de clics sur le bouton de mise à jour
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                // Enregistrer l'état de connexion comme déconnecté dans les SharedPreferences
                SharedPreferences.Editor editor = requireActivity().getSharedPreferences("user", requireActivity().MODE_PRIVATE).edit();
                editor.putBoolean("isLoggedIn", false);
                editor.apply();
                // Rediriger vers LoginActivity
                startActivity(new Intent(requireActivity(), LoginActivity.class));
                requireActivity().finishAffinity();
            }
        });

        return view;
    }

    private void updateProfile() {
        // Cacher le bouton de mise à jour et afficher la barre de progression
        updateButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        // Récupérer l'utilisateur actuellement connecté
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Récupérer les nouvelles valeurs des champs EditText
            String newNom = nomEditText.getText().toString().trim();
            String newPrenom = prenomEditText.getText().toString().trim();
            String newEmail = emailEditText.getText().toString().trim();
            String newPassword = passwordEditText.getText().toString().trim();

            // Mettre à jour les informations du profil de l'utilisateur
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(newNom + " " + newPrenom) // Mettre à jour le nom d'affichage
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Mettre à jour l'email de l'utilisateur s'il a été modifié
                                if (!newEmail.equals(user.getEmail())) {
                                    user.updateEmail(newEmail)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        // L'email a été mis à jour avec succès
                                                        // Afficher un message de réussite (Toast)
                                                        Toast.makeText(requireContext(), "Profil mis à jour avec succès", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        // Échec de la mise à jour de l'email
                                                        // Afficher un message d'erreur (Toast)
                                                        Toast.makeText(requireContext(), "Échec de la mise à jour de l'email", Toast.LENGTH_SHORT).show();
                                                    }
                                                    // Afficher à nouveau le bouton de mise à jour et cacher la barre de progression
                                                    updateButton.setVisibility(View.VISIBLE);
                                                    progressBar.setVisibility(View.GONE);
                                                }
                                            });
                                } else {
                                    // Masquer la barre de progression après la mise à jour du profil
                                    progressBar.setVisibility(View.GONE);
                                    // Afficher un message de réussite (Toast)
                                    Toast.makeText(requireContext(), "Profil mis à jour avec succès", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Échec de la mise à jour du profil
                                // Masquer la barre de progression après l'échec de la mise à jour
                                progressBar.setVisibility(View.GONE);
                                // Afficher un message d'erreur (Toast)
                                Toast.makeText(requireContext(), "Échec de la mise à jour du profil", Toast.LENGTH_SHORT).show();
                            }
                            // Afficher à nouveau le bouton de mise à jour
                            updateButton.setVisibility(View.VISIBLE);
                        }
                    });
        }
    }

    private void displayUserProfile() {
        // Récupérer l'utilisateur actuellement connecté
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Récupérer le nom et le prénom de l'utilisateur à partir de son nom d'affichage
            String displayName = user.getDisplayName();
            if (displayName != null && !displayName.isEmpty()) {
                String[] parts = displayName.split("\\s+");
                if (parts.length == 2) {
                    String nom = parts[0]; // Nom de l'utilisateur
                    String prenom = parts[1]; // Prénom de l'utilisateur

                    // Définir les valeurs récupérées dans les champs EditText
                    nomEditText.setText(nom);
                    prenomEditText.setText(prenom);
                }
            }
            String email = user.getEmail();

            // Vérifier si l'email est non nul et non vide
            if (email != null && !email.isEmpty()) {
                // Définir la valeur récupérée dans le champ EditText correspondant
                emailEditText.setText(email);
            }
            String password = "**************"; // Ici, vous pouvez définir une chaîne de caractères fictive pour représenter le mot de passe
            passwordEditText.setText(password);
        }
    }
}
