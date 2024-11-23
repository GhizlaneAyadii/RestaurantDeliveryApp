package com.example.project162.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.project162.Domain.Foods;
import com.example.project162.Helper.ManagmentCart;
import com.example.projectgaz.databinding.ActivityDetailBinding;

public class DetailActivity extends BaseActivity {
    ActivityDetailBinding binding;
    private Foods object;
    private int num = 1;
    private int likesCount = 0;
    private ManagmentCart managmentCart;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "MyPrefs";
    private String itemId; // Ajout de l'identifiant unique de l'élément
    private static final String LIKES_COUNT_KEY_PREFIX = "likesCount_";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        getIntentExtra();
        loadLikesCount();
        setVariable();
    }

    private void setVariable() {
        managmentCart = new ManagmentCart(this);
        binding.backbtn.setOnClickListener(v -> finish());
        Glide.with(DetailActivity.this)
                .load(object.getImagePath())
                .into(binding.pic);
        binding.priceTxt.setText(object.getPrice() + "DH");
        binding.titleTxt.setText(object.getTitle());
        binding.descriptionTxt.setText(object.getDescription());
        binding.rateTxt.setText(likesCount + " Likes");
        binding.ratingBar.setRating(calculateStars(likesCount));
        binding.totalTxt.setText((num * object.getPrice() + "DH"));
        binding.plusBtn.setOnClickListener(v -> {
            num = num + 1;
            binding.numTxt.setText(num + " ");
            binding.totalTxt.setText((num * object.getPrice()) + "DH");
        });
        binding.minusBtn.setOnClickListener(v -> {
            if (num > 1) {
                num = num - 1;
                binding.numTxt.setText(num + " ");
                binding.totalTxt.setText((num * object.getPrice()) + "DH");
            }
        });
        binding.FavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likesCount++;
                binding.rateTxt.setText(likesCount + " Likes");
                binding.ratingBar.setRating(calculateStars(likesCount));
                saveLikesCount();
            }
        });
        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                object.setNumberInCart(num);
                managmentCart.insertFood(object);
            }
        });
    }

    private void getIntentExtra() {
        object = (Foods) getIntent().getSerializableExtra("object");
        itemId = String.valueOf(object.getId()); // Obtention de l'identifiant unique de l'élément
    }

    private void loadLikesCount() {
        likesCount = sharedPreferences.getInt(LIKES_COUNT_KEY_PREFIX + itemId, 0); // Utilisation de l'identifiant unique comme clé
    }

    private void saveLikesCount() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(LIKES_COUNT_KEY_PREFIX + itemId, likesCount); // Utilisation de l'identifiant unique comme clé
        editor.apply();
    }

    private float calculateStars(int likesCount) {
        // Par exemple, vous pouvez définir une logique pour calculer le nombre d'étoiles en fonction du nombre de likes
        // Ici, nous supposons que chaque étoile représente 2 likes
        return (float) (likesCount / 10);
    }

}
