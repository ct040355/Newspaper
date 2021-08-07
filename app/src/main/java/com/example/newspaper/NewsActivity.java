package com.example.newspaper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.newspaper.DaoFirebase.DaoSaveNews;
import com.example.newspaper.Models.NewsItem;
import com.example.newspaper.databinding.ActivityNewsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class NewsActivity extends AppCompatActivity {
    NewsItem newsItem;
    ActivityNewsBinding binding;
    DatabaseReference mData;
    FirebaseAuth auth;
    DaoSaveNews daoSaveNews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mData = FirebaseDatabase.getInstance().getReference();
        binding = ActivityNewsBinding.inflate(getLayoutInflater());
        auth = FirebaseAuth.getInstance();
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String link = intent.getStringExtra("link");
        String titleNews = intent.getStringExtra("titleNews");
        String imageNews = intent.getStringExtra("imageNews");
        String titlePages = intent.getStringExtra("titlePages");
        newsItem = new NewsItem(titleNews, imageNews,link);
        binding.tvTitleNews.setText(titlePages);

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            daoSaveNews = new DaoSaveNews(NewsActivity.this);
            Query query = mData.child("SavedNews").child(auth.getUid()).orderByChild("title").equalTo(titleNews);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                        NewsItem  newsItem = dataSnapshot.getValue(NewsItem.class);
                        String string = newsItem.getTitle();
                        if (string != null){
                            binding.imgUnsave.setVisibility(View.VISIBLE);
                            binding.imgSave.setVisibility(View.GONE);

                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });





            binding.imgSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                mData .child("SavedNews").child(auth.getUid()).push().setValue(newsItem);
                    daoSaveNews.insert(newsItem);
                    binding.imgUnsave.setVisibility(View.VISIBLE);
                    binding.imgSave.setVisibility(View.GONE);

                }
            });

            binding.imgUnsave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    daoSaveNews.removeValue(titleNews);
                    binding.imgUnsave.setVisibility(View.GONE);
                    binding.imgSave.setVisibility(View.VISIBLE);
                }
            });
        }

        WebView webView = findViewById(R.id.webView);
        webView.loadUrl(link);
        webView.setWebViewClient(new WebViewClient());


        binding.imgBackNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(NewsActivity.this, MainActivity.class);
                startActivity(intent1);
            }
        });


    }
}