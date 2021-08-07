package com.example.newspaper.DaoFirebase;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.newspaper.Models.NewsItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DaoRecentNews {
    Context context;
    DatabaseReference mData;
    public DaoRecentNews(Context context){
        this.context = context;
        mData = FirebaseDatabase.getInstance().getReference()
                .child("RecentNews")
                .child(FirebaseAuth.getInstance().getUid());
    }
    public void insert(NewsItem newsItem){
        mData.push().setValue(newsItem);
    }


    public void getAll(final ArrayList<NewsItem> newsItems){

        mData.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                NewsItem newsItem = snapshot.getValue(NewsItem.class);
                newsItems.add(0,new NewsItem(newsItem.getTitle(), newsItem.getImage(), newsItem.getLink()));
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

    }

    public void removeValue(String str){
        Query query = mData.orderByChild("title").equalTo(str);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    dataSnapshot.getRef().removeValue();

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void deleteAll(){
        mData.removeValue();
    }
}
