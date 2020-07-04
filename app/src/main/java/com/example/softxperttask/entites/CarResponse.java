package com.example.softxperttask.entites;

import java.util.List;

public class CarResponse {

    public int status;
    public List<Data> data;

    public static class Data {
        public int id;
        public String brand;
        public String constructionYear;
        public boolean isUsed;
        public String imageUrl;
    }
}
