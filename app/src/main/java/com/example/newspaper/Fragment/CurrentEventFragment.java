package com.example.newspaper.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.newspaper.Adapter.CurrentEventFragmentAdapter;
import com.example.newspaper.Adapter.WorldFragmentAdapter;
import com.example.newspaper.DaoFirebase.DaoRecentNews;
import com.example.newspaper.Models.NewsItem;
import com.example.newspaper.NewsActivity;
import com.example.newspaper.R;
import com.example.newspaper.XMLDOMParser;
import com.google.firebase.auth.FirebaseAuth;

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



public class CurrentEventFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<NewsItem> newsItems;
    CurrentEventFragmentAdapter currentEventFragmentAdapter;
    DaoRecentNews daoRecentNews;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_current_event, container, false);
        new ReadRSS().execute("https://vnexpress.net/rss/thoi-su.rss");
        recyclerView = view.findViewById(R.id.rvCurrentEvent);

        newsItems = new ArrayList<>();


        currentEventFragmentAdapter = new CurrentEventFragmentAdapter(newsItems,getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(currentEventFragmentAdapter);
        currentEventFragmentAdapter.setOnClickItemCurrent(onClickItemCurrent);
        return view;
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

                currentEventFragmentAdapter.notifyDataSetChanged();
            }

        }
    }
    private CurrentEventFragmentAdapter.OnClickItemCurrent onClickItemCurrent = new CurrentEventFragmentAdapter.OnClickItemCurrent() {
        @Override
        public void onClickItem(int position, NewsItem newsItem) {
            NewsItem newsItem1 = new NewsItem(
                    newsItems.get(position).getTitle(),
                    newsItems.get(position).getImage(),
                    newsItems.get(position).getLink());


            if(FirebaseAuth.getInstance().getCurrentUser() != null ){
                daoRecentNews = new DaoRecentNews(getContext());
                daoRecentNews.removeValue(newsItems.get(position).getTitle());
                daoRecentNews.insert(newsItem1);
            }



            Intent intent = new Intent(getContext(), NewsActivity.class);
            intent.putExtra("link",newsItems.get(position).getLink());
            intent.putExtra("titleNews", newsItems.get(position).getTitle());
            intent.putExtra("imageNews", newsItems.get(position).getImage());
            intent.putExtra("titlePages","Thời sự");


            startActivity(intent);

        }
    };
}