package com.example.newspaper;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.newspaper.Adapter.HomeAdapter;
import com.example.newspaper.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth auth;
    DatabaseReference mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mData = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        binding= ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        HomeAdapter homeAdapter = new HomeAdapter(getSupportFragmentManager());
        binding.viewPager.setAdapter(homeAdapter);

        binding.tabLayout.setupWithViewPager(binding.viewPager);
        if(FirebaseAuth.getInstance().getCurrentUser() != null ){
            mData.child("Users").child(FirebaseAuth.getInstance().getUid()).child("profilepic").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Glide.with(binding.imgSetting).load(snapshot.getValue().toString()).into(binding.imgSetting);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }else {
            binding.imgSetting.setImageResource(R.drawable.img_user);
        }

        binding.imgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SettingActivity.class);
                startActivity(intent);

            }
        });
        binding.imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                startActivity(intent);

            }
        });

    }

}