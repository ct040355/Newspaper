package com.example.newspaper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.newspaper.Adapter.RecentNewsAdapter;
import com.example.newspaper.Adapter.SavedNewsAdapter;
import com.example.newspaper.DaoFirebase.DaoRecentNews;
import com.example.newspaper.Models.NewsItem;
import com.example.newspaper.databinding.ActivityRecentNewsBinding;
import com.example.newspaper.databinding.ActivitySaveNewsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecentNewsActivity extends AppCompatActivity {
    ActivityRecentNewsBinding binding;
    ArrayList<NewsItem> newsItems;
    DatabaseReference mData;
    FirebaseAuth auth;
    DaoRecentNews daoRecentNews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_news);

        daoRecentNews = new DaoRecentNews(RecentNewsActivity.this);
        mData = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        newsItems = new ArrayList<>();
        binding = ActivityRecentNewsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        RecentNewsAdapter recentNewsAdapter = new RecentNewsAdapter(newsItems, this);
        daoRecentNews.getAll(newsItems);

        mData.child("RecentNews").child(auth.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                NewsItem newsItem = snapshot.getValue(NewsItem.class);
                String check = newsItem.getTitle();

                if ( check != null){
                    binding.recyclerViewRecent.setVisibility(View.VISIBLE);
                    binding.textView.setVisibility(View.GONE);
                }else {

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




        binding.recyclerViewRecent.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewRecent.setAdapter(recentNewsAdapter);
        recentNewsAdapter.setOnClickRecent(onClickRecent);

        binding.tvDeleteAllRecent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daoRecentNews.deleteAll();
                binding.textView.setVisibility(View.VISIBLE);
                binding.recyclerViewRecent.setVisibility(View.GONE);
            }
        });

        binding.imgBackRecent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecentNewsActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

    }

    private RecentNewsAdapter.OnClickRecent onClickRecent = new RecentNewsAdapter.OnClickRecent() {

        @Override
        public void OnClickItem(int position, NewsItem newsItem) {

            NewsItem newsItem1 = new NewsItem(
                    newsItems.get(position).getTitle(),
                    newsItems.get(position).getImage(),
                    newsItems.get(position).getLink());

            if(FirebaseAuth.getInstance().getCurrentUser() != null ){
                daoRecentNews.removeValue(newsItems.get(position).getTitle());
                daoRecentNews.insert(newsItem1);
            };

            Intent intent = new Intent(RecentNewsActivity.this, NewsActivity.class);
            intent.putExtra("link", newsItems.get(position).getLink());
            intent.putExtra("titleNews", newsItems.get(position).getTitle());
            intent.putExtra("imageNews", newsItems.get(position).getImage());
            startActivity(intent);

        }
    };
}