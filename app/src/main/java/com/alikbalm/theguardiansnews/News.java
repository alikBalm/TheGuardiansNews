package com.alikbalm.theguardiansnews;

import com.orm.SugarRecord;

//вспомогательный класс для хранения новостей в БД при помощи SugarOrm

public class News extends SugarRecord {

    String webTitle, webPublicationDate, webUrl;


    public News(){

    }

    public News(String webPublicationDate,String webTitle, String webUrl ){
        this.webPublicationDate = webPublicationDate;
        this.webTitle = webTitle;
        this.webUrl = webUrl;
    }
}
