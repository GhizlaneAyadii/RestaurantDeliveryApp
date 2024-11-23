package com.example.project162.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.projectgaz.CartFragment;
import com.example.projectgaz.HomeFragment;
import com.example.projectgaz.ProfileFragment;
import com.example.projectgaz.R;
import com.example.projectgaz.CategoryFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
public class MaincartActivity2 extends AppCompatActivity {
private BottomNavigationView bottomNavigationView;
private FrameLayout frameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maincart2);

        bottomNavigationView = findViewById(R.id.bottomNavView);
        frameLayout = findViewById(R.id.frameLayout);

        // Chargement initial du fragment CartFragment
        loadFragment(new CartFragment());

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navHome) {
                    // Lorsque l'utilisateur appuie sur "Home",
                    // on affiche l'activité principale (MainActivity)
                    Intent intent = new Intent(MaincartActivity2.this, MainActivity.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.navSearch) {
                    loadFragment(new CategoryFragment());
                    return true;
                } else if (itemId == R.id.navCart) {
                    loadFragment(new CartFragment());
                    return true;
                } else {
                    loadFragment(new ProfileFragment());
                    return true;
                }
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }


    private void loadFragment(Fragment fragment,boolean isAppInitialized){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        if (isAppInitialized){
            fragmentTransaction.add(R.id.frameLayout, fragment); // Correction ici

        }else {
            fragmentTransaction.replace(R.id.frameLayout, fragment);
        }
        fragmentTransaction.commit();
    }

    }
