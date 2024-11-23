package com.example.project162.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project162.Adapter.BestFoodsAdapter;
import com.example.project162.Adapter.LikedFoodsAdapter;
import com.example.project162.Domain.Foods;
import com.example.projectgaz.R;
import com.example.projectgaz.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "MyPrefs";
    private static final String LIKES_COUNT_KEY_PREFIX = "likesCount_";
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String displayName = user.getDisplayName();
            if (displayName != null && !displayName.isEmpty()) {
                TextView nomCompletTextView = findViewById(R.id.nom_complet);
                nomCompletTextView.setText(displayName);
            }
        }

        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", true);

        if (!isLoggedIn) {
            startActivity(new Intent(MainActivity.this, IntroActivity.class));
            finish();
        }

        initLikedFoods();
        setVariable();
        initBestFood();

    }

    private void setVariable() {
        binding.logoutBtn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
            editor.putBoolean("isLoggedIn", false);
            editor.apply();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finishAffinity();
        });

        binding.searchBtn.setOnClickListener(v -> {
            String text = binding.searchEdt.getText().toString();
            if (!text.isEmpty()) {
                Intent intent = new Intent(MainActivity.this, ListFoodsActivity.class);
                intent.putExtra("text", text);
                intent.putExtra("isSearch", true);
                startActivity(intent);
            }
        });

        binding.cartBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MaincartActivity2.class)));

        binding.filterBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ProfileActivity.class)));
    }

    private void initBestFood() {
        DatabaseReference myRef=database.getReference("Foods");
        binding.progressBarBestFood.setVisibility(View.VISIBLE);
        ArrayList<Foods> list= new ArrayList<>();
        Query query=myRef.orderByChild("BestFood").equalTo(true);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue: snapshot.getChildren()){
                        list.add(issue.getValue(Foods.class));
                    }
                    if (list.size()>0){
                        binding.bestFoodView.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false));
                        RecyclerView.Adapter adapter=new BestFoodsAdapter(list);
                        binding.bestFoodView.setAdapter(adapter);

                    }
                    binding.progressBarBestFood.setVisibility(View.GONE);

                }}
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void initLikedFoods() {
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        DatabaseReference myRef = database.getReference("Foods");
        ArrayList<Foods> likedFoods = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot issue : snapshot.getChildren()) {
                    Foods food = issue.getValue(Foods.class);
                    if (food != null) {
                        String itemId = String.valueOf(food.getId());
                        int likesCount = sharedPreferences.getInt(LIKES_COUNT_KEY_PREFIX + itemId, 0);
                        if (likesCount > 0) {
                            food.setLikesCount(likesCount);
                            likedFoods.add(food);
                        }
                    }
                }

                Collections.sort(likedFoods, (o1, o2) -> Integer.compare(o2.getLikesCount(), o1.getLikesCount()));

                if (!likedFoods.isEmpty()) {
                    LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    binding.likedFoodView.setLayoutManager(layoutManager);
                    LikedFoodsAdapter adapter = new LikedFoodsAdapter(likedFoods, MainActivity.this);
                    binding.likedFoodView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
