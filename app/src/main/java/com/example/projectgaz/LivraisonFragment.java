package com.example.projectgaz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project162.Domain.Commande;
import com.example.project162.Domain.ProduitCommande;
import com.example.project162.Domain.Foods;
import com.example.project162.Helper.ManagmentCart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LivraisonFragment extends Fragment {

    private EditText editTextCity, editTextStreet;
    private Button buttonSaveAddress;

    private DatabaseReference databaseReference;
    private double prixTotal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_livraison, container, false);

        editTextCity = rootView.findViewById(R.id.editTextCity);
        editTextStreet = rootView.findViewById(R.id.editTextStreet);
        buttonSaveAddress = rootView.findViewById(R.id.buttonSaveAddress);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        buttonSaveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAddressToFirebase();
            }
        });

        if (getArguments() != null) {
            prixTotal = getArguments().getDouble("totalPrice");
        }

        return rootView;
    }

    private void saveAddressToFirebase() {
        ManagmentCart managmentCart = new ManagmentCart(getContext());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("commandes");
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            String nomComplet = currentUser.getDisplayName(); // Assuming you have set the display name during registration
            String ville = editTextCity.getText().toString().trim();
            String rue = editTextStreet.getText().toString().trim();

            // Formater la date et l'heure
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
            String dateCommande = sdf.format(new Date());

            // Récupérer les titres des produits et leurs quantités dans le panier
            List<Foods> listeCart = managmentCart.getListCart();
            List<ProduitCommande> listeProduits = new ArrayList<>();
            for (Foods food : listeCart) {
                listeProduits.add(new ProduitCommande(food.getTitle(), food.getNumberInCart()));
            }

            Commande commande = new Commande(userId, ville, rue, nomComplet, dateCommande, prixTotal, listeProduits);

            String produitId = reference.push().getKey();

            reference.child(produitId).setValue(commande)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setMessage("Votre commande est bien effectuée!")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                managmentCart.clearCart();
                                                getActivity().finish();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        }
                    });
        }
    }
}
