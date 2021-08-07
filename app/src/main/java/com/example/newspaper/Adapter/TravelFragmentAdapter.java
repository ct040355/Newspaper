package com.example.newspaper.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newspaper.Models.NewsItem;
import com.example.newspaper.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class TravelFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<NewsItem> newsItemArrayList;
    private Context context;
    private OnClickItemTravel onClickItemTravel;

    private static int TYPE_SMALL = 1;
    private static int TYPE_BIG = 2;


    public void setOnClickItemTravel(OnClickItemTravel onClickItemTravel) {
        this.onClickItemTravel = onClickItemTravel;
    }

    public TravelFragmentAdapter(ArrayList<NewsItem> newsItemArrayList, Context context) {
        this.newsItemArrayList = newsItemArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (TYPE_SMALL == viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fragment_small,parent,false);
            return new TravelFragmentAdapter.SmallViewHolder(view);
        }else  if(TYPE_BIG== viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fragment_big,parent,false);
            return new TravelFragmentAdapter.BigViewHolder(view);
        }
        return null;


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NewsItem newsItem = newsItemArrayList.get(position);
        if (TYPE_SMALL == holder.getItemViewType()){
            TravelFragmentAdapter.SmallViewHolder smallViewHolder = (TravelFragmentAdapter.SmallViewHolder) holder;
            Glide.with(smallViewHolder.imageView)
                    .load(newsItem.getImage())
                    .override(140,90)
                    .centerCrop()
                    .into(smallViewHolder.imageView);
            smallViewHolder.tvDate.setText(newsItem.getDate());
            smallViewHolder.tvTitle.setText(newsItem.getTitle());
            smallViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickItemTravel != null){
                        onClickItemTravel.onClickItem(position,newsItem);

                    }

                }
            });


        }else
        if (TYPE_BIG == holder.getItemViewType()){
            TravelFragmentAdapter.BigViewHolder bigViewHolder = (TravelFragmentAdapter.BigViewHolder) holder;
            Glide.with(bigViewHolder.imageViewBig)
                    .load(newsItem.getImage())
                    .override(360,185)
                    .centerCrop()
                    .into(bigViewHolder.imageViewBig);
            bigViewHolder.tvDateBig.setText(newsItem.getDate());
            bigViewHolder.tvTitleBig.setText(newsItem.getTitle());
            bigViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickItemTravel != null){
                        onClickItemTravel.onClickItem(position,newsItem);


                    }

                }
            });

        }



    }

    @Override
    public int getItemCount() {
        return newsItemArrayList.size();
    }
    @Override
    public int getItemViewType(int position) {

        SharedPreferences pref = context.getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        Boolean isIntroActivityOpnendBefore = pref.getBoolean("isMode", false);

        if (isIntroActivityOpnendBefore){
            return TYPE_BIG;
        }else{
            return TYPE_SMALL;
        }


    }
    public class SmallViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView tvTitle,tvDate;

        public SmallViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDate = itemView.findViewById(R.id.tvDate);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }



    public class BigViewHolder extends RecyclerView.ViewHolder{
        ImageView imageViewBig;
        TextView tvTitleBig,tvDateBig;


        public BigViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitleBig = itemView.findViewById(R.id.tvTitleBig);
            tvDateBig = itemView.findViewById(R.id.tvDateBig);
            imageViewBig = itemView.findViewById(R.id.imageViewBig);
        }
    }


    public interface OnClickItemTravel{
        void onClickItem(int position, NewsItem newsItem);
    }
}

