package com.example.newspaper.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newspaper.DaoFirebase.DaoRecentNews;
import com.example.newspaper.Models.NewsItem;
import com.example.newspaper.R;
import com.example.newspaper.RecentNewsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecentNewsAdapter extends RecyclerView.Adapter<RecentNewsAdapter.MyViewHolder> {

    private ArrayList<NewsItem> newsItemArrayList;
    private Context context;
    DatabaseReference databaseReference;
    FirebaseDatabase database;
    private OnClickRecent onClickRecent;


    public void setOnClickRecent(OnClickRecent onClickRecent){
        this.onClickRecent = onClickRecent;
    }
    public RecentNewsAdapter(ArrayList<NewsItem> newsItemArrayList, Context context) {
        this.newsItemArrayList = newsItemArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recent_news,parent,false);
        return new RecentNewsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DaoRecentNews daoRecentNews = new DaoRecentNews(context);
        NewsItem newsItem = newsItemArrayList.get(position);

        Glide.with(holder.img_recentNews)
                .load(newsItem.getImage())
                .override(140,90)
                .centerCrop()
                .into(holder.img_recentNews);
        holder.tvTitleRecent.setText(newsItem.getTitle());

        holder.tvDeleteRecent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(newsItemArrayList.size() == 1 ){
                    daoRecentNews.removeValue(newsItem.getTitle());
                    newsItemArrayList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, newsItemArrayList.size());
                    Intent intent = new Intent(context, RecentNewsActivity.class);
                    context.startActivity(intent);
                }else{
                    daoRecentNews.removeValue(newsItem.getTitle());
                    newsItemArrayList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, newsItemArrayList.size());
                }

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickRecent != null){
                    onClickRecent.OnClickItem(position,newsItem);

                }

            }
        });



    }

    @Override
    public int getItemCount() {
        return newsItemArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img_recentNews;
        TextView tvTitleRecent,tvDeleteRecent;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img_recentNews = itemView.findViewById(R.id.img_recentNews);
            tvTitleRecent= itemView.findViewById(R.id.tvTitleRecent);
            tvDeleteRecent = itemView.findViewById(R.id.tvDeleteRecent);
        }
    }


    public interface OnClickRecent{
        void OnClickItem(int position, NewsItem newsItem);
    }
}
