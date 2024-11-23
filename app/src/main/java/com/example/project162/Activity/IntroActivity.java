package com.example.project162.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.projectgaz.R;
import com.example.projectgaz.databinding.ActivityIntroBinding;

public class IntroActivity extends BaseActivity {
ActivityIntroBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setVariable();
        getWindow().setStatusBarColor(Color.parseColor("#FFE485"));

    }

    private void setVariable() {
        binding.loginBtn.setOnClickListener(v -> {
            if (mAuth.getCurrentUser()!=null){
                startActivity(new Intent(IntroActivity.this,MainActivity.class));
            }else {
                startActivity(new Intent(IntroActivity.this,LoginActivity.class));

            }
        });

        binding.singupBtn.setOnClickListener(v -> startActivity(new Intent(IntroActivity.this,SingupActivity.class)));
    }
}