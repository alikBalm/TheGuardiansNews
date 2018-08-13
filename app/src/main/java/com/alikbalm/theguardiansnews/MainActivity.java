package com.alikbalm.theguardiansnews;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    //наш список
    ListView listView;
    Button refreshNews;

    List<News> newsList;
    ArrayList<String> newsTitle;
    //SQLiteDatabase guardiansBD;

    //API ключ и сам url https://content.guardianapis.com/news?api-key=dc5fa5c7-98f1-4386-8f12-651cb412c87c
    private final static String API_KEY ="dc5fa5c7-98f1-4386-8f12-651cb412c87c";
    private final static String NEWS ="https://content.guardianapis.com/news?api-key=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*

        //инициируем базу , и создаём в ней таблицу с ячейками для наших данных
        guardiansBD = this.openOrCreateDatabase("guardiansDB",MODE_PRIVATE,null);
        guardiansBD.execSQL("CREATE TABLE IF NOT EXISTS 'news' (webPublicationDate VARCHAR, webTitle VARCHAR, webUrl VARCHAR, id INT PRIMARY KEY)");

        */

        //наш список
        listView = (ListView)findViewById(R.id.listView);
        //кнопка для обновления
        refreshNews = (Button)findViewById(R.id.refreshNews);

        refreshNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //здесь пишем код для обновления новостей по нажатию на Renew News

            }
        });

        new GetJSONFile().execute(NEWS + API_KEY);







        /*1
        //!-->блок для проверки

        GetJSONFile getJSONFile = new GetJSONFile();

        //

        getJSONFile.execute(NEWS + API_KEY);
        //!-->блок для проверки 1*/

    }

    //класс для извлечения объектов из json файла из url, и добавления записей в базу
    class GetJSONFile extends AsyncTask<String, Void, String> {

        private String jsonFileString = "";


        @Override
        protected String doInBackground(String... strings) {

            //здесь пишем код для открытия url и извлечения текста или json файла
            try {

                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inReader = new InputStreamReader(inputStream);


                int data = inReader.read() ;

                while (data!=-1){
                    char i = (char) data;
                    jsonFileString +=i;
                    data = inReader.read();

                }
                //строка jsonFileString сформирована
                //Log.i("JSONFILE", jsonFileString);

                // извлекаем из строки данные json
                JSONObject jsonObject = new JSONObject(jsonFileString);
                JSONObject response = jsonObject.getJSONObject("response");
                JSONArray results = response.getJSONArray("results");

                /*
                теперь из массива объектов results нужно извлечь у каждого объекта
                "webPublicationDate", "webTitle", "webUrl"
                 и добавить их в базу в таблицу news
                 */

                /*

                //это вспомогательные строки для добавления в таблицу базы записей чтоб каждый раз не копировать и не вставлять
                String sqlExecStart = "INSERT INTO 'news' (webPublicationDate, webTitle, webUrl) VALUES (";
                String sqlExecEnd = ")";
                String values = "";


                //проходим по элементам массива
                for (int i = 0; i < results.length(); i++) {

                    values = results.getJSONObject(i).getString("webPublicationDate") + ", "
                      + results.getJSONObject(i).getString("webTitle") + ", "
                      + results.getJSONObject(i).getString("webUrl");
                    //guardiansBD.execSQL(sqlExecStart + values + sqlExecEnd);

                }
                */
                //после этого цикла в базе должны лежать все значения из объектов из массива results
                //здесь нужно переделать на использование SugarOrm

                //сначала чистим всю базу от старых новостей

                News.deleteAll(News.class);

                //вставляем в базу все новости из полученного файла JSON
                for (int i = 0; i < results.length(); i++) {

                    new News(results.getJSONObject(i).getString("webPublicationDate"),
                             results.getJSONObject(i).getString("webTitle"),
                             results.getJSONObject(i).getString("webUrl")).save();
                }







            } catch (Exception e) {
                e.printStackTrace();
            }



            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //достаём из базы все новости и помещаем их в список для listView
            newsList = News.listAll(News.class);
            newsTitle = new ArrayList<>();
            for (News news :
                    newsList) {
                newsTitle.add(news.webTitle);
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MainActivity.this,android.R.layout.simple_list_item_1,newsTitle);

            listView.setAdapter(arrayAdapter);


        }
    }
    /*
    //это пока оставим пока не научусь работать с SQLiteOpenHelper, потому как именно при помощи него можно добавить значения из базы в список в активности
    class guardiansDBHelper extends SQLiteOpenHelper {

        public guardiansDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        public guardiansDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
            super(context, name, factory, version, errorHandler);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }

    */
}
