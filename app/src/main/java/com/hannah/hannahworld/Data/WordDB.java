package com.hannah.hannahworld.Data;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class WordDB {
	private static final Logger log = LoggerFactory.getLogger(WordDB.class);
	//data fields
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_WEEK = "week";
	public static final String COLUMN_WORD = "word";
	private Context context;
	private String urlLink;
	// = "http://192.168.1.65/word.json";
	public final String db_name;

	public static final String[] fields = {COLUMN_WEEK, COLUMN_WORD};
	public  final String LAST_UPDATE = "last_update";
	public   String DB_PREFS;//used for remember the data storing in the SharedPreferences
	private static String[] allColumns = {COLUMN_ID, COLUMN_WEEK, COLUMN_WORD};
	private final SharedPreferences prefs;
	private final ProgressDialog progressDialog;
	private SQLiteDatabase database;
	private WordsDbHelper helper;
	private Gson gson = new Gson();
	public  String tableName;
	public WordDB(Context ctx, ProgressDialog progressDialog, String dbName, String urlLink,String tbName) {
		this.db_name = dbName;
		this.urlLink = urlLink;
		this.context = ctx;
		tableName=tbName;

		this.progressDialog = progressDialog;
		helper = new WordsDbHelper(ctx);
		prefs = ctx.getSharedPreferences(DB_PREFS, Context.MODE_PRIVATE);
		open();
		//if (isUpdateDue()) {
			log.info("InitialDB::");
			initializeDb(ctx);
		//}
		close();
	}

	private boolean isUpdateDue() {

		long last = prefs.getLong(LAST_UPDATE, 0);
		return System.currentTimeMillis() - last > 1000 * 60 * 60 * 24; // Update every 24H
	}

	private void initializeDb(final Context ctx) {
		final Handler toastHandler = new Handler(Looper.getMainLooper());

			toastHandler.post(new Runnable() {
					@Override
					public void run() {
					//progressDialog.dismiss();
					progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
					progressDialog.setIndeterminate(true);
					progressDialog.setCancelable(false);
					progressDialog.show();
					}
					});

			fetchAndParseData(ctx, toastHandler);

	}

	private void updateProgressDialog(final String message) {
		final Handler toastHandler = new Handler(Looper.getMainLooper());

		toastHandler.post(new Runnable() {
				@Override
				public void run() {
				progressDialog.setMessage(message);
				}
				});
	}

	private void setProgressMax(final int progressMax) {
		final Handler toastHandler = new Handler(Looper.getMainLooper());

		toastHandler.post(new Runnable() {
				@Override
				public void run() {
				progressDialog.setIndeterminate(false);
				progressDialog.setMax(progressMax);
				}
				});
	}


	private void setProgress(final int progress) {
		final Handler toastHandler = new Handler(Looper.getMainLooper());

		toastHandler.post(new Runnable() {
				@Override
				public void run() {
				progressDialog.setProgress(progress);
				}
				});
	}

	private void fetchAndParseData(final Context ctx, Handler toastHandler) {
        StringBuilder builder = new StringBuilder();
        BufferedReader in;
        List<Words> mWords = new ArrayList<Words>();
        updateProgressDialog("Fetching Data");
        try {
            URL dataUrl = new URL("https://instacoin.ca/json/test.json");
            //URL dataUrl = new URL("https://instacoin.ca/json/spend1.json"); //coinkite.json
            URLConnection connection = dataUrl.openConnection();
            //connection.addRequestProperty("User-Agent", "OneRowp;1.0;Android");
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            while (null != (line = in.readLine())) {
                builder.append(line);
            }
            Type collectionType = new TypeToken<List<Words>>() {
            }.getType();
            mWords = gson.fromJson(builder.toString(), collectionType);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            //log.info("MalFormURL::");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            toastHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ctx, "Updating Data. Failed - maybe no connection", Toast.LENGTH_LONG).show();

                }
            });
        }
		updateProgressDialog("Parsing JSON");
		if(mWords !=null &&  mWords.size()>0){
			this.clearData();
			updateProgressDialog("Inserting Entries");
			setProgressMax(mWords.size());
			log.info("INSERT ENTRIES::");
			int newSize=0;
			this.database.beginTransaction();
			try {
				final int lastSize = getAllEntries().size();
				int i = 1;
				for (Words entry : mWords) {
                    for(String word : entry.getWords()){
                            this.insertWord(word,entry.getWeek());
                    }
					setProgress(i);
					i++;
				}
				newSize = (getAllEntries().size() - lastSize);
				this.database.setTransactionSuccessful();
				//prefs.edit().putLong(LAST_UPDATE, System.currentTimeMillis()).commit();
			}catch(Exception e) {
				//Error in between database transaction 
				e.printStackTrace();
			}finally {
				this.database.endTransaction();

			}
			final int nNewsize = newSize;
			toastHandler.post(new Runnable() {
					@Override
					public void run() {
					Toast.makeText(ctx, "Updating Data. Success - " + nNewsize + " new shops", Toast.LENGTH_LONG).show();
					}
					});
			log.info("RE_CREATEDB::");
		}
	}

	private void clearData() {
		if(database !=null ){
			database.delete(tableName, null, null);
		}
		else {
			File dbFile= context.getDatabasePath(db_name);
			database = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
		}
	}

	public void open() throws SQLException {
		database = helper.getWritableDatabase();
	}

	public void close() {
		helper.close();
	}

	public void deleteWords(OneRow aword) {
		int week = aword.week;
        String word = aword.word;
		database.delete(tableName, COLUMN_WEEK
				+ " = " + week + " and " + COLUMN_WORD+ "=" + word, null);
	}

	public long insertWord(String word, int week) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_WORD, word);
		values.put(COLUMN_WEEK, week);
		return database.insert(tableName, null, values);
	}

	public List<OneRow> getAllEntries() {
		List<OneRow> entries = new ArrayList<OneRow>();

		Cursor cursor = database.query(tableName,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			OneRow comment = cursorToEntry(cursor);
			entries.add(comment);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return entries;
	}

	private OneRow cursorToEntry(Cursor cursor) {
		OneRow entry = new OneRow();
		entry.id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
		entry.word = cursor.getString(cursor.getColumnIndex(COLUMN_WORD));
		entry.week = cursor.getInt(cursor.getColumnIndex(COLUMN_WEEK));
		return entry;
	}


	public class WordsDbHelper extends SQLiteOpenHelper {
		private static final int VERSION = 4;
		public WordsDbHelper(Context context) {
			super(context, db_name, null, VERSION);
		}


		@Override
			public void onCreate(SQLiteDatabase sqLiteDatabase) {

				StringBuilder sqlBuilder = new StringBuilder("create table ");
				sqlBuilder.append(tableName).append("(" + COLUMN_ID + " integer primary key autoincrement");
				for (String field : fields) {
					sqlBuilder.append(", ").append(field).append(" text");
				}
				sqlBuilder.append(");");

				sqLiteDatabase.execSQL(sqlBuilder.toString());
				log.info("BUILD_DATABASE:::");

			}

		@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
				Log.w(WordsDbHelper.class.getName(),
						"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
				db.execSQL("DROP TABLE IF EXISTS " + tableName);
				onCreate(db);
				 //prefs.edit().putLong(LAST_UPDATE,0l).commit();
				
				//initializeDb(context);
			}


	}

}
