package com.example.newspaper.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newspaper.DaoFirebase.DaoSaveNews;
import com.example.newspaper.Models.NewsItem;
import com.example.newspaper.R;
import com.example.newspaper.RecentNewsActivity;
import com.example.newspaper.databinding.ActivitySaveNewsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SavedNewsAdapter extends RecyclerView.Adapter<SavedNewsAdapter.MyViewHolder>  {
    private ArrayList<NewsItem> newsItemArrayList;
    private Context context;
    private OnClick onClick;


    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }
    public SavedNewsAdapter(ArrayList<NewsItem> newsItemArrayList, Context context) {
        this.newsItemArrayList = newsItemArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public SavedNewsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_saved,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedNewsAdapter.MyViewHolder holder, int position) {
        DaoSaveNews daoSaveNews = new DaoSaveNews(context);
        NewsItem newsItem = newsItemArrayList.get(position);
        
        Glide.with(holder.img_savedNews)
                .load(newsItem.getImage())
                .override(140,90)
                .centerCrop()
                .into(holder.img_savedNews);
        holder.tvTitleSaved.setText(newsItem.getTitle());

        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(newsItemArrayList.size() == 1 ){
                    daoSaveNews.removeValue(newsItem.getTitle());
                    newsItemArrayList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, newsItemArrayList.size());
                    Intent intent = new Intent(context, RecentNewsActivity.class);
                    context.startActivity(intent);
                }else{
                    daoSaveNews.removeValue(newsItem.getTitle());
                    newsItemArrayList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, newsItemArrayList.size());
                }

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClick != null){
                    onClick.OnClickItem(position,newsItem);

                }

            }
        });



    }

    @Override
    public int getItemCount() {
        return newsItemArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img_savedNews;
        TextView tvTitleSaved,tvDelete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img_savedNews = itemView.findViewById(R.id.img_savedNews);
            tvTitleSaved= itemView.findViewById(R.id.tvTitleSaved);
            tvDelete= itemView.findViewById(R.id.tvDelete);

        }
    }
    public interface OnClick{
        void OnClickItem(int position, NewsItem newsItem);
    }
}
