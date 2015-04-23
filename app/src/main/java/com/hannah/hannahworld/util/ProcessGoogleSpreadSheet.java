package com.hannah.hannahworld.util;

import com.hannah.hannahworld.data.Words;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessGoogleSpreadSheet {

    private List<Words> mWords = new ArrayList<Words>();
    public HashMap<Integer, List<String>> wWords = new HashMap<Integer, List<String>>();
    private String url;
    public ProcessGoogleSpreadSheet(String url){
        this.url = url;
    }
public List<Words> getOutput(){
	return mWords;

}
public void Output() {
    try {

        String result = downloadUrl(url);

        // remove the unnecessary parts from the response and construct a JSON
        int start = result.indexOf("{", result.indexOf("{") + 1);
        int end = result.lastIndexOf("}");
        String jsonResponse = result.substring(start, end);
        try {
            JSONObject object = new JSONObject(jsonResponse);

            JSONArray rows = object.getJSONArray("rows");

            for (int r = 0; r < rows.length(); ++r) {
                JSONObject row = rows.getJSONObject(r);
                JSONArray columns = row.getJSONArray("c");

                int id = columns.getJSONObject(0).getInt("v");
                int week = columns.getJSONObject(1).getInt("v");
                String word = columns.getJSONObject(2).getString("v");
                if (wWords.get(week) == null) {
                    wWords.put(week, new ArrayList<String>());
                }
                wWords.get(week).add(word);
            }
            for (Map.Entry<Integer, List<String>> entry : wWords.entrySet()) {
                Words tWord = new Words();
                tWord.setWeek(entry.getKey());
                tWord.setWords(entry.getValue());
                mWords.add(tWord);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

   public  String downloadUrl(String urlString) throws IOException {
        InputStream is = null;

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int responseCode = conn.getResponseCode();
            is = conn.getInputStream();
            String contentAsString = convertStreamToString(is);
            return contentAsString;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
