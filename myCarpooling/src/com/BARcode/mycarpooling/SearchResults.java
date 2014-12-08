package com.BARcode.mycarpooling;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import com.BARcode.databaseModels.Carpool;
import com.BARcode.utilities.CarpoolAdapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ExpandableListView;

public class SearchResults extends Activity {

	private String source;
	private String destination;
	public String searchResultsString = "";
	public HashMap<String, List<String>> carpoolHistory;
	public ExpandableListView Exp_list;
	public List<String> carpools;
	public CarpoolAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_results);
		//get source && destination to search for
		source = getIntent().getExtras().getString("source");
		destination = getIntent().getExtras().getString("destination");
		carpoolHistory = new HashMap<String, List<String>>();
		new SearchResultsDB().execute(source, destination);
		/*EditText test = (EditText) findViewById(R.id.mere);
		test.setText(source + " " + destination);*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_results, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	class SearchResultsDB extends AsyncTask<String, String, String> {

		private ProgressDialog progressMessage;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressMessage = new ProgressDialog(SearchResults.this);
			progressMessage.setMessage("Loading ...");
			progressMessage.setIndeterminate(false);
			progressMessage.setCancelable(false);
			progressMessage.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String source = (String) params[0];
			String destination = (String) params[1];
			
			String link = String.format(com.BARcode.utilities.Constants.SERVER_URL
					+ "search_carpools.php?source=%s&destination=%s", source, destination);

			try {
				HttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet();
				request.setURI(new URI(link));

				HttpResponse response = client.execute(request);
				BufferedReader br = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));

				StringBuilder sb = new StringBuilder();
				String line = "";

				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}

				String[] lines = sb.toString().split("\n");
				for (int i = 0; i < lines.length - 3; i++) {
					searchResultsString += lines[i] + "\n";
				}
				runOnUiThread(new Runnable() {
				     @Override
				     public void run() {
				    	 	List<Carpool> all = getDB();
					 		for(int i = 0; i < all.size(); i++){
					 			carpoolHistory.put(all.get(i).getDate() + " " + all.get(i).getTime(), all.get(i).getInfo());
					 		}
					 		Exp_list = (ExpandableListView) findViewById(R.id.exp_list_search);
					 		carpools = new ArrayList<String>(carpoolHistory.keySet());
					 		adapter = new CarpoolAdapter(SearchResults.this, carpoolHistory, carpools);
					 		Exp_list.setAdapter(adapter);
				    }
				});
				
				progressMessage.dismiss();
			} catch (Exception e) {
				Log.e("ERROR:", e.getMessage());
			}
			return null;
		}
		public List<Carpool> getDB(){
			
			List<Carpool> carpoolHistoryList = new ArrayList<Carpool>();
			JSONArray ja = null;
		
				try {
					ja = new JSONArray(searchResultsString);
					for(int i = 0; i < ja.length(); i++){
						Carpool c = new Carpool(ja.getJSONObject(i));
						carpoolHistoryList.add(c);
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return carpoolHistoryList;
		}


	}
}

