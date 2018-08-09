package com.alikbalm.theguardiansnews;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {
    //наш список
    ListView listView;

    //API ключ и сам url
    private final static String API_KEY ="dc5fa5c7-98f1-4386-8f12-651cb412c87c";
    private final static String NEWS ="https://content.guardianapis.com/news?api-key=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //наш список
        listView = (ListView)findViewById(R.id.listView);



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
                Log.i("JSONFILE", jsonFileString);




            } catch (Exception e) {
                e.printStackTrace();
            }



            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //здесь пишем код для вставки извлечённого текста или файла в базу


        }
    }
}
