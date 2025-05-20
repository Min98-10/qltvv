package com.example.quanlythuvien.api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GoogleBooksAPI {

    public static BookInfo fetchByISBN(String isbn) {
        try {
            String urlStr = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) json.append(line);
            reader.close();

            JSONObject root = new JSONObject(json.toString());
            JSONArray items = root.optJSONArray("items");
            if (items != null && items.length() > 0) {
                JSONObject volumeInfo = items.getJSONObject(0).getJSONObject("volumeInfo");
                String title = volumeInfo.optString("title", "N/A");

                String authors = volumeInfo.optJSONArray("authors") != null
                        ? String.join(", ", volumeInfo.getJSONArray("authors").toList().stream().map(Object::toString).toList())
                        : "Unknown";

                String imageUrl = volumeInfo.has("imageLinks")
                        ? volumeInfo.getJSONObject("imageLinks").optString("thumbnail", "")
                        : "";

                return new BookInfo(title, authors, imageUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static class BookInfo {
        public final String title;
        public final String author;
        public final String imageUrl;

        public BookInfo(String title, String author, String imageUrl) {
            this.title = title;
            this.author = author;
            this.imageUrl = imageUrl;
        }
    }
}
