package com.example.projectgaz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project162.Adapter.CartAdapter;
import com.example.project162.Helper.ManagmentCart;
import com.example.projectgaz.databinding.FragmentCartBinding;

import java.util.Random;

public class CartFragment extends Fragment {
    private FragmentCartBinding binding;
    private ManagmentCart managmentCart;
    private double tax;
    private double discount = 0.0; // Ajouter une variable pour la réduction
    private RecyclerView.Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        managmentCart = new ManagmentCart(requireContext());

        calculateCart();
        initList();
        setVariable();
        initCouponFunctionality(); // Initialiser la fonctionnalité de coupon
    }

    private void setVariable() {
        binding.placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double totalPrice = calculateTotalPrice();
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setMessage("Merci d'avoir payé votre commande")
                        .setCancelable(false)
                        .setPositiveButton("Payer à la livraison", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                LivraisonFragment livraisonFragment = new LivraisonFragment();
                                Bundle bundle = new Bundle();
                                bundle.putDouble("totalPrice", totalPrice);
                                livraisonFragment.setArguments(bundle);

                                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.frameLayout, livraisonFragment);
                                transaction.addToBackStack(null);
                                transaction.commit();
                            }
                        })
                        .setNegativeButton("Payer maintenant", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                PaimentFragment paimentFragment = new PaimentFragment();
                                Bundle bundle = new Bundle();
                                bundle.putDouble("totalPrice", totalPrice);
                                paimentFragment.setArguments(bundle);

                                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.frameLayout, paimentFragment);
                                transaction.addToBackStack(null);
                                transaction.commit();
                            }
                        })
                        .setNeutralButton("Annuler", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    private double calculateTotalPrice() {
        double percentTax = 0.02;
        double delivery = 10;

        double totalFee = managmentCart.getTotalFee();
        double discountAmount = totalFee * discount;

        double tax = Math.round((totalFee - discountAmount) * percentTax * 100.0) / 100;
        return Math.round((totalFee - discountAmount + tax + delivery) * 100) / 100;
    }

    private boolean couponUsed = false; // Variable pour suivre l'état d'utilisation du coupon

    private void initCouponFunctionality() {
        binding.applyCouponButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String couponCode = binding.editTextCoupon.getText().toString().trim();
                if ("M2I".equalsIgnoreCase(couponCode)) {
                    if (!couponUsed) { // Vérifier si le coupon n'a pas déjà été utilisé
                        discount = 0.1; // 10% discount
                        calculateCart();
                        Toast.makeText(requireContext(), "Vous avez profité de 10% du coupon", Toast.LENGTH_SHORT).show();
                        couponUsed = true; // Marquer le coupon comme utilisé
                    } else {
                        Toast.makeText(requireContext(), "Vous avez déjà utilisé ce coupon", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    discount = 0.0; // No discount
                    Toast.makeText(requireContext(), "Code de coupon invalide", Toast.LENGTH_SHORT).show();
                }
                calculateCart(); // Recalculate cart totals
            }
        });
    }


    private void calculateCart() {
        double percentTax = 0.02;
        double delivery = 10;

        double totalFee = managmentCart.getTotalFee();
        double discountAmount = totalFee * discount;

        tax = Math.round((totalFee - discountAmount) * percentTax * 100.0) / 100;

        double total = Math.round((totalFee - discountAmount + tax + delivery) * 100) / 100;
        double itemTotal=Math.round(managmentCart.getTotalFee()*100)/100;

        binding.totalFeeTxt.setText(itemTotal + " DH");
        binding.taxTXt.setText(tax + " DH");
        binding.deliveryTxt.setText(delivery + " DH");
        binding.totalTxt.setText(total + " DH");
    }

    private void initList() {
        if (managmentCart.getListCart().isEmpty()) {
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollviewCart.setVisibility(View.GONE);
        } else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollviewCart.setVisibility(View.VISIBLE);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        binding.cardView.setLayoutManager(linearLayoutManager);
        adapter = new CartAdapter(managmentCart.getListCart(), requireContext(), this::calculateCart);
        binding.cardView.setAdapter(adapter);
    }
}
