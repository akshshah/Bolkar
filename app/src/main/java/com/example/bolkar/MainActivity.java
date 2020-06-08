package com.example.bolkar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.example.bolkar.Adapters.CategoryAdapter;
import com.example.bolkar.Models.Category;
import com.example.bolkar.Models.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private RecyclerView categoriesRecView;
    private CategoryAdapter categoryAdapter;
    private ArrayList<Category> categories;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        back = findViewById(R.id.back);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        categories = new ArrayList<>();
        categoriesRecView = findViewById(R.id.categoriesRecView);
        categoriesRecView.setHasFixedSize(true);
        categoriesRecView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        categoryAdapter =  new CategoryAdapter(this,categories);
        categoriesRecView.setAdapter(categoryAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        GetDataAsyncTask getDataAsyncTask = new GetDataAsyncTask();
        getDataAsyncTask.execute();
    }

    private class GetDataAsyncTask extends AsyncTask<Void,Void,Void>{

        String data = "";
        @Override
        protected Void doInBackground(Void... voids) {

            try {
                URL url = new URL("https://d51md7wh0hu8b.cloudfront.net/android/v1/prod/Radio/hi/show.json");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line = "";
                while(line != null){
                    line = bufferedReader.readLine();
                    data = data + line;
                }

                JSONArray jsonArray = new JSONArray(data);
                for(int i=0; i<jsonArray.length(); i++){
                    Category category = new Category();
                    ArrayList<Item> items = new ArrayList<>();

                    String title = "";
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    title = (String) jsonObject.get("title");
                    category.setTitle(title);

                    JSONArray itemArray = (JSONArray) jsonObject.get("data");
                    for(int j=0; j<itemArray.length(); j++){

                        Item item = new Item();

                        JSONObject jsonObject1 = (JSONObject) itemArray.get(j);
                        String name = (String) jsonObject1.get("t");
                        String imgUrl = (String) jsonObject1.get("pF");
                        item.setName(name);
                        item.setImgUrl(imgUrl);
                        items.add(item);
                    }
                    category.setItems(items);
                    categories.add(category);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            categoryAdapter.notifyDataSetChanged();
            super.onPostExecute(aVoid);
        }
    }

}
