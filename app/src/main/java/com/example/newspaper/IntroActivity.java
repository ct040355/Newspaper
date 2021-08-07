package com.example.newspaper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.example.newspaper.Adapter.IntroViewPagerAdapter;
import com.example.newspaper.Models.ScreenItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {
    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter;
    TabLayout tabIndicator;
    Button btnNext;
    int position = 0;
    Animation btnAnim;
    TextView tvSkip;
    Button btnSignInIntro;
    Button btnSignUpIntro;
    Button btntoMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        if (restorePrefData()) {

            Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainActivity);
            finish();


        }

        setContentView(R.layout.activity_intro);




        btnNext = findViewById(R.id.btn_next);
        btnSignInIntro = findViewById(R.id.btn_signInIntro);
        btnSignUpIntro = findViewById(R.id.btn_signUpIntro);
        btntoMain = findViewById(R.id.btn_toMain);

        tabIndicator = findViewById(R.id.tab_indicator);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_animation);
        tvSkip = findViewById(R.id.tv_skip);


        final List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("Cập nhật nhanh chóng",
                "Cập nhật những tin tức mới nhất, nóng nhất một cách nhanh chóng trên nhiều lĩnh vực, phù hợp với nhiều đối tượng độc giả khác nhau.",
                R.drawable.intro3));
        mList.add(new ScreenItem("Lưu & xem lại tin",
                "Lưu lại những tin bạn quan tâm để tra cứu & đọc lại khi cần",
                R.drawable.intro));
        mList.add(new ScreenItem("Đồng bộ dữ liệu",
                "Để tránh việc mất dữ liệu khi nâng cấp sản phẩm, và xem lại tin tức khi cần",
                R.drawable.intro1));

        screenPager = findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this, mList);
        screenPager.setAdapter(introViewPagerAdapter);


        tabIndicator.setupWithViewPager(screenPager);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                position = screenPager.getCurrentItem();
                if (position < mList.size()) {

                    position++;
                    screenPager.setCurrentItem(position);


                }

                if (position == mList.size() - 1) { // when we rech to the last screen


                    loaddLastScreen();


                }


            }
        });



        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == mList.size() - 1) {

                    loaddLastScreen();

                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        btntoMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                savePrefsData();
                finish();

            }
        });

        btnSignInIntro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intent);
                savePrefsData();
                finish();
            }
        });
        btnSignUpIntro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
                savePrefsData();
                finish();
            }
        });


        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screenPager.setCurrentItem(mList.size());
            }
        });


    }

    private boolean restorePrefData() {


        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        Boolean isIntroActivityOpnendBefore = pref.getBoolean("isIntroOpnend", false);
        return isIntroActivityOpnendBefore;


    }

    private void savePrefsData() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpnend", true);
        editor.commit();


    }

    private void loaddLastScreen() {

        btnNext.setVisibility(View.INVISIBLE);
        btnSignUpIntro.setVisibility(View.VISIBLE);
        btnSignInIntro.setVisibility(View.VISIBLE);
        btntoMain.setVisibility(View.VISIBLE);
        tvSkip.setVisibility(View.INVISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);
           btnSignUpIntro.setAnimation(btnAnim);
           btnSignInIntro.setAnimation(btnAnim);
           btntoMain.setAnimation(btnAnim);
    }
}