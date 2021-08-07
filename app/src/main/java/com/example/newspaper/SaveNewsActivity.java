package com.example.newspaper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.newspaper.Adapter.SavedNewsAdapter;
import com.example.newspaper.Adapter.SavedNewsAdapter.OnClick;
import com.example.newspaper.DaoFirebase.DaoRecentNews;
import com.example.newspaper.DaoFirebase.DaoSaveNews;
import com.example.newspaper.Models.NewsItem;
import com.example.newspaper.databinding.ActivitySaveNewsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SaveNewsActivity extends AppCompatActivity {
    ActivitySaveNewsBinding binding;
    ArrayList<NewsItem> newsItems;
    DatabaseReference mData;
    DaoRecentNews daoRecentNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaoSaveNews daoSaveNews = new DaoSaveNews(SaveNewsActivity.this);
        daoRecentNews = new DaoRecentNews(SaveNewsActivity.this);
        mData = FirebaseDatabase.getInstance().getReference();

        newsItems = new ArrayList<>();
        binding = ActivitySaveNewsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SavedNewsAdapter savedNewsAdapter = new SavedNewsAdapter(newsItems, this);
        daoSaveNews.getAll(newsItems);



        mData.child("SavedNews").child(FirebaseAuth.getInstance().getUid())
                .addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                NewsItem newsItem = snapshot.getValue(NewsItem.class);
                String check = newsItem.getTitle();

                if ( check != null){
                    binding.recyclerViewSaved.setVisibility(View.VISIBLE);
                    binding.textView.setVisibility(View.GONE);
                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.recyclerViewSaved.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewSaved.setAdapter(savedNewsAdapter);
        savedNewsAdapter.setOnClick(onClick);

        binding.tvDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daoSaveNews.deleteAll();
                binding.textView.setVisibility(View.VISIBLE);
                binding.recyclerViewSaved.setVisibility(View.GONE);
            }
        });

        binding.imgBackSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SaveNewsActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

    }

    private SavedNewsAdapter.OnClick onClick = new OnClick() {
        @Override
        public void OnClickItem(int position, NewsItem newsItem) {
            NewsItem newsItem1 = new NewsItem(
                    newsItems.get(position).getTitle(),
                    newsItems.get(position).getImage(),
                    newsItems.get(position).getLink());

            if(FirebaseAuth.getInstance().getCurrentUser() != null ){
                daoRecentNews.removeValue(newsItems.get(position).getTitle());
                daoRecentNews.insert(newsItem1);
            }



            Intent intent = new Intent(SaveNewsActivity.this,NewsActivity.class);
            intent.putExtra("link",newsItems.get(position).getLink());
            intent.putExtra("titleNews", newsItems.get(position).getTitle());
            intent.putExtra("imageNews", newsItems.get(position).getImage());
            startActivity(intent);
        }
    };
}