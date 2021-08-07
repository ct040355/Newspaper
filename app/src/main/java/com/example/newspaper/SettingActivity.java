package com.example.newspaper;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.newspaper.Models.Users;
import com.example.newspaper.databinding.ActivityMainBinding;
import com.example.newspaper.databinding.ActivitySettingBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.newspaper.R.drawable.*;

public class SettingActivity extends AppCompatActivity {


    ActivitySettingBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference mData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        auth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference();
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.imgBackSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(SettingActivity.this,MainActivity.class);
                startActivity(intent3);
            }
        });
        binding.btnSignInSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,SignInActivity.class);
                startActivity(intent);
            }
        });
        binding.btnSaveNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(FirebaseAuth.getInstance().getCurrentUser() != null ){
                   Intent intent = new Intent(SettingActivity.this,SaveNewsActivity.class);
                   startActivity(intent);


               }else{
                   AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                   builder.setTitle("Tin đã lưu");
                   builder.setMessage("Bạn cần đăng nhập tài khoản");
                   builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {
                           Intent intent = new Intent(SettingActivity.this,SignInActivity.class);
                           startActivity(intent);

                       }
                   });
                   builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {
                           dialog.cancel();
                       }
                   });
                   AlertDialog alertDialog = builder.create();
                   alertDialog.show();

               }
            }
        });
        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent(SettingActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        binding.btnReadRecentNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    Intent intent = new Intent(SettingActivity.this, RecentNewsActivity.class);
                    startActivity(intent);


                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                    builder.setTitle("Tin đã đọc gần đây");
                    builder.setMessage("Bạn cần đăng nhập tài khoản");
                    builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(SettingActivity.this, SignInActivity.class);
                            startActivity(intent);

                        }
                    });
                    builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }
            }
        });

        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        Boolean isIntroActivityOpnendBefore = pref.getBoolean("isMode", false);

        if (isIntroActivityOpnendBefore){
            binding.switchBigMode.setChecked(true);
        }else {
            binding.switchBigMode.setChecked(false);
        }

        binding.switchBigMode.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){

                    SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("isMode", true);
                    editor.commit();
                }else {
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("isMode", false);
                    editor.commit();
                }
            }
        });



        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            mData.child("Users").child(FirebaseAuth.getInstance().getUid()).child("userName").addValueEventListener(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            binding.tvUserName.setText(snapshot.getValue().toString());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
            mData.child("Users").child(FirebaseAuth.getInstance().getUid()).child("profilepic").addValueEventListener(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Glide.with(binding.imgProfilepic).load(snapshot.getValue().toString()).into(binding.imgProfilepic);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }else {
            binding.imgProfilepic.setImageResource(img_user);
            binding.btnSignInSetting.setVisibility(View.VISIBLE);

        }
    }
}