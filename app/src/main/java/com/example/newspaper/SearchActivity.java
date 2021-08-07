package com.example.newspaper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.newspaper.Adapter.SearchAdapter;
import com.example.newspaper.DaoFirebase.DaoRecentNews;
import com.example.newspaper.Models.NewsItem;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchActivity extends AppCompatActivity {


    ArrayList<NewsItem> newsItems;
    SearchAdapter searchAdapter;
    RecyclerView recyclerView;
    SearchView searchView;
    EditText searchInput;
    ImageButton btn_remove;
    TextView tv_exit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        new ReadRSS().execute("https://vnexpress.net/rss/the-gioi.rss");
        new ReadRSS().execute("https://vnexpress.net/rss/suc-khoe.rss");
        new ReadRSS().execute("https://vnexpress.net/rss/kinh-doanh.rss");
        new ReadRSS().execute("https://vnexpress.net/rss/thoi-su.rss");
        new ReadRSS().execute("https://vnexpress.net/rss/du-lich.rss");
        new ReadRSS().execute("https://vnexpress.net/rss/khoa-hoc.rss");

        newsItems = new ArrayList<>();

        recyclerView = findViewById(R.id.rvSearch);
        searchInput = findViewById(R.id.searchInput);
        btn_remove = findViewById(R.id.btn_remove);
        tv_exit = findViewById(R.id.tv_exit);

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchAdapter.getFilter().filter(s);
                if (s != null){
                    btn_remove.setVisibility(View.VISIBLE);
                }else {
                    btn_remove.setVisibility(View.INVISIBLE);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchInput.setText("");
                btn_remove.setVisibility(View.INVISIBLE);
            }
        });
        tv_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        searchAdapter = new SearchAdapter(newsItems,SearchActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(searchAdapter);
        searchAdapter.setOnClickItemSearch(onClickItemSearch);




    }
    private class ReadRSS extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder content = new StringBuilder();
            try {
                URL url = new URL(strings[0]);

                InputStreamReader inputStreamReader = new InputStreamReader(url.openConnection().getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String line = "";
                while((line = bufferedReader.readLine()) != null){
                    content.append(line);
                }

                bufferedReader.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return content.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            XMLDOMParser parser = new XMLDOMParser();
            Document document = parser.getDocument(s);

            NodeList nodeList = document.getElementsByTagName("item");
            NodeList nodeListDescription  = document.getElementsByTagName("description");


            for (int i = 0; i < 30 ; i++) {
                Element element = (Element) nodeList.item(i);
                String data = nodeListDescription.item(i+1).getTextContent();
                Pattern pattern = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
                Matcher matcher = pattern.matcher(data);
                String image = "";
                if(matcher.find()){
                    image = matcher.group(1);
                }
                newsItems.add(new NewsItem(
                        parser.getValue(element, "title"),
                        parser.getValue(element, "pubDate").substring(0,22),
                        image,
                        parser.getValue(element, "link")));

                searchAdapter.notifyDataSetChanged();
            }

        }
    }
    private SearchAdapter.OnClickItemSearch onClickItemSearch = new SearchAdapter.OnClickItemSearch() {
        @Override
        public void onClickItem(int position, NewsItem newsItem) {
            DaoRecentNews daoRecentNews = new DaoRecentNews(SearchActivity.this);
            NewsItem newsItem1 = new NewsItem(
                    newsItems.get(position).getTitle(),
                    newsItems.get(position).getImage(),
                    newsItems.get(position).getLink());

            daoRecentNews.removeValue(newsItems.get(position).getTitle());
            daoRecentNews.insert(newsItem1);

            Intent intent = new Intent(SearchActivity.this, NewsActivity.class);
            intent.putExtra("link", newsItems.get(position).getLink());
            intent.putExtra("titleNews", newsItems.get(position).getTitle());
            intent.putExtra("imageNews", newsItems.get(position).getImage());
            startActivity(intent);

        }
    };
}

