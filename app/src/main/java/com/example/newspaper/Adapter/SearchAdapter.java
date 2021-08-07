package com.example.newspaper.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newspaper.Models.NewsItem;
import com.example.newspaper.R;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> implements Filterable {
    private ArrayList<NewsItem> newsItemArrayList;
    private ArrayList<NewsItem> newsItemsOld;

    private Context context;
    private OnClickItemSearch onClickItemSearch;

    public SearchAdapter(ArrayList<NewsItem> newsItemArrayList, Context context) {
        this.newsItemArrayList = newsItemArrayList;

        this.newsItemsOld = newsItemArrayList;

        this.context = context;
    }

    public void setOnClickItemSearch(OnClickItemSearch onClickItemSearch) {
        this.onClickItemSearch = onClickItemSearch;


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_fragment_small,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        NewsItem newsItem = newsItemArrayList.get(position);
        Glide.with(holder.imageView)
                .load(newsItem.getImage())
                .override(140,90)
                .centerCrop()
                .into(holder.imageView);
        holder.tvTitle.setText(newsItem.getTitle());
        holder.tvDate.setText(newsItem.getDate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickItemSearch != null){
                    onClickItemSearch.onClickItem(position,newsItem);

                }

            }
        });


    }

    @Override
    public int getItemCount() {
        if(newsItemArrayList != null){
            return  newsItemArrayList.size();
        }
        return 0;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvTitle,tvDate;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDate = itemView.findViewById(R.id.tvDate);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
    public interface OnClickItemSearch{
        void onClickItem(int position, NewsItem newsItem);
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if(strSearch.isEmpty()){
                    newsItemArrayList = newsItemsOld;
                }else {
                    ArrayList<NewsItem> list = new ArrayList<>();
                    for (NewsItem news : newsItemsOld){
                        if (news.getTitle().toLowerCase().contains(strSearch.toLowerCase())){
                            list.add(news);
                        }
                    }


                    newsItemArrayList = list;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = newsItemArrayList;
                return  filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                newsItemArrayList = (ArrayList<NewsItem>) results.values;
                notifyDataSetChanged();

            }
        };
    }
}
