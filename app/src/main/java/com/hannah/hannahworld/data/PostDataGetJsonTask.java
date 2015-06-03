package com.hannah.hannahworld.data;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostDataGetJsonTask extends AsyncTask<String, Void, String> {
	OnFinishedAsyncTaskWithJson callback;

	public PostDataGetJsonTask(OnFinishedAsyncTaskWithJson callback) {
		this.callback = callback;
	}

	@Override
	protected String doInBackground(String... urls) {// params comes from the execute() call: params[0] is the url.
		try {
			return downloadUrl(urls[0]);
		} catch (IOException e) {
			return "Fail to post and get data.";
		}
	}

	@Override
	protected void onPostExecute(String result) {
		try {
			Log.i("Async::Out1", result);
			//result = result.substring(result.indexOf("{"), result.lastIndexOf("}") + 1);
			//Log.i("Async::Out2", result);
			JSONObject output = new JSONObject(result);
			callback.onFinishedAsyncTaskWithJson(output);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private String downloadUrl(String urlString) throws IOException {
		InputStream is = null;

		try {
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000 /* milliseconds */);
			conn.setConnectTimeout(10000 /* milliseconds */);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
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

	private String convertStreamToString(InputStream is) {
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
