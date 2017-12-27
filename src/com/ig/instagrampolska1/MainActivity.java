package com.ig.instagrampolska1;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {
	Connection conexionMySQL;
	String user = null;
	String userident = null;
	private static final String TAG = "InstagramAPI";
	private static final String API_URL = "https://api.instagram.com/v1";
	static Integer w = 0;
	static Integer h = 0;

	public static String getUserID(String userid, String licznik) {
		try {

			String link = "http://meetbook.nets.pl/android/selectid.php?userlogged="
					+ URLEncoder.encode(userid) + "&licznik=" + URLEncoder.encode(licznik);

			URL url = new URL(link);
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			request.setURI(new URI(link));
			HttpResponse response = client.execute(request);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			StringBuffer sb = new StringBuffer("");
			String line = "";
			while ((line = in.readLine()) != null) {
				sb.append(line);
				break;
			}
			in.close();
			Log.i(TAG, "Userid " + sb.toString());

			return sb.toString();

		} catch (Exception e) {
			Log.i(TAG, "Exception: " + e.getMessage());
			return null;
		}
	}

	public static String getIdPromoted(String userid) {
		try {

			String link = "http://meetbook.nets.pl/android/selectpromoted.php?userlogged="
					+ URLEncoder.encode(userid);

			URL url = new URL(link);
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			request.setURI(new URI(link));
			HttpResponse response = client.execute(request);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			StringBuffer sb = new StringBuffer("");
			String line = "";
			while ((line = in.readLine()) != null) {
				sb.append(line);
				break;
			}
			in.close();
			Log.i(TAG, "Userid " + sb.toString());

			return sb.toString();

		} catch (Exception e) {
			Log.i(TAG, "Exception: " + e.getMessage());
			return null;
		}
	}

	public static String getUserName(String userid) {
		try {

			String link = "http://meetbook.nets.pl/android/selectname.php?userid="
					+ URLEncoder.encode(userid);
			URL url = new URL(link);
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			request.setURI(new URI(link));
			HttpResponse response = client.execute(request);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			StringBuffer sb = new StringBuffer("");
			String line = "";
			while ((line = in.readLine()) != null) {
				sb.append(line);
				break;
			}
			in.close();

			return sb.toString();

		} catch (Exception e) {
			Log.i(TAG, "Exception: " + e.getMessage());
			return null;
		}
	}

	public static void GetMediaUser(final String userid) {

		new Thread() {
			@Override
			public void run() {
				Log.i(TAG, "Fetching user images");

				try {

					URL example = new URL("https://api.instagram.com/v1/users/"
							+ userid + "/media/recent?access_token="
							+ InstagramApp.mAccessToken);

					URLConnection tc = example.openConnection();
					BufferedReader in = new BufferedReader(
							new InputStreamReader(tc.getInputStream()));

					String line;
					while ((line = in.readLine()) != null) {
						JSONObject ob = new JSONObject(line);

						JSONArray object = ob.getJSONArray("data");

						for (int i = 0; i < 4; i++) {

							JSONObject jo = (JSONObject) object.get(i);
							JSONObject nja = (JSONObject) jo
									.getJSONObject("images");

							JSONObject purl3 = (JSONObject) nja
									.getJSONObject("standard_resolution");
							// PhotoSet set = new PhotoSet();
							// set.setThumb(purl3.getString("url"));
							// thesets.add(set);

							Log.i(TAG, "" + purl3.getString("url"));

							new DownloadImageTask1(LoginActivity.ivProfil)
									.execute(purl3.getString("url"));
							Log.i("TAG", DownloadImageTask1.licznik.toString());

							//DownloadImageTask1.licznik += 1;

							if (DownloadImageTask1.licznik > 4) {
								DownloadImageTask1.licznik = 1;
							}

						}

					}
				} catch (MalformedURLException e) {

					e.printStackTrace();
				} catch (IOException e) {

					e.printStackTrace();
				} catch (JSONException e) {

					e.printStackTrace();
				}

			}
		}.start();

	}
	
	public static void GetMediaUserPromoted(final String userid) {

		new Thread() {
			@Override
			public void run() {
				Log.i(TAG, "Fetching user images");

				try {

					URL example = new URL("https://api.instagram.com/v1/users/"
							+ userid + "/media/recent?access_token="
							+ InstagramApp.mAccessToken);

					URLConnection tc = example.openConnection();
					BufferedReader in = new BufferedReader(
							new InputStreamReader(tc.getInputStream()));

					String line;
					while ((line = in.readLine()) != null) {
						JSONObject ob = new JSONObject(line);

						JSONArray object = ob.getJSONArray("data");

						for (int i = 0; i < 4; i++) {

							JSONObject jo = (JSONObject) object.get(i);
							JSONObject nja = (JSONObject) jo
									.getJSONObject("images");

							JSONObject purl3 = (JSONObject) nja
									.getJSONObject("standard_resolution");
							// PhotoSet set = new PhotoSet();
							// set.setThumb(purl3.getString("url"));
							// thesets.add(set);

							Log.i(TAG, "" + purl3.getString("url"));

							new DownloadImageTask3(LoginActivity.ivProfil)
									.execute(purl3.getString("url"));
							Log.i("TAG", DownloadImageTask3.licznik.toString());

							//DownloadImageTask1.licznik += 1;

							if (DownloadImageTask3.licznik > 4) {
								DownloadImageTask3.licznik = 1;
							}

						}

					}
				} catch (MalformedURLException e) {

					e.printStackTrace();
				} catch (IOException e) {

					e.printStackTrace();
				} catch (JSONException e) {

					e.printStackTrace();
				}

			}
		}.start();

	}

	public static void getProfilePic(final String userid) {

		new Thread() {
			@Override
			public void run() {
				Log.i(TAG, "Fetching user info");

				try {
					URL url = new URL(API_URL + "/users/" + userid
							+ "/?access_token=" + InstagramApp.mAccessToken);

					Log.d(TAG, "Opening URL " + url.toString());
					HttpURLConnection urlConnection = (HttpURLConnection) url
							.openConnection();
					urlConnection.setRequestMethod("GET");
					urlConnection.setDoInput(true);
					urlConnection.setDoOutput(false);
					// urlConnection.connect();
					String response = streamToString(urlConnection
							.getInputStream());
					// System.out.println(response);
					JSONObject jsonObj = (JSONObject) new JSONTokener(response)
							.nextValue();
					String link_url = jsonObj.getJSONObject("data").getString(
							"profile_picture");

					new DownloadImageTask2(LoginActivity.ivProfil)
							.execute(link_url);

				} catch (Exception ex) {

					ex.printStackTrace();
				}

			}
		}.start();

	}

	public static void followUser(final String userid) {

		new Thread() {
			@Override
			public void run() {

				try {
					HttpClient client = new DefaultHttpClient();
					String url = API_URL + "/users/" + userid
							+ "/relationship?" + "access_token="
							+ InstagramApp.mAccessToken;

					Log.d(TAG, "Opening URL " + url.toString());
					HttpPost post = new HttpPost(url);

					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("X-Insta-Forwarded-For",
							"follow"));
					params.add(new BasicNameValuePair("action", "follow"));
					UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,
							HTTP.UTF_8);
					post.setEntity(ent);
					// HttpURLConnection urlConnection = (HttpURLConnection) url
					// .openConnection();
					// urlConnection.setRequestMethod("POST");
					// urlConnection.setDoInput(true);
					// urlConnection.setDoOutput(false);
					// urlConnection.addRequestProperty("action", "follow");
					// urlConnection.connect();

					post.addHeader("X-Insta-Forwarded-For",
							"127.0.0.1|08760dbd75908d15e4356001e625b5959cff64d5b938a3934025ae3a5505bb2c");

					HttpResponse responsePOST = client.execute(post);

					HttpEntity resEntity = responsePOST.getEntity();

					if (resEntity != null) {
						Log.i("RESPONSE", EntityUtils.toString(resEntity));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();

	}

	private static String streamToString(InputStream is) throws IOException {
		String str = "";

		if (is != null) {
			StringBuilder sb = new StringBuilder();
			String line;

			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));

				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}

				reader.close();
			} finally {
				is.close();
			}

			str = sb.toString();
		}

		return str;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Display display = getWindowManager().getDefaultDisplay();
		
		Double width_screen = (double) display.getWidth() / 1.4;
		Double height_screen = (double) display.getHeight() / 2.5;

		w = width_screen.intValue();
		h = height_screen.intValue();
		
		TabHost TabHost = getTabHost();

		// Tab for Browse
		TabSpec browsespec = TabHost.newTabSpec("Browse");
		// setting Title and Icon for the Tab
		browsespec.setIndicator("",
				getResources().getDrawable(R.drawable.icon_browse_tab));
		Intent browseIntent = new Intent(this, BrowseActivity.class);
		browsespec.setContent(browseIntent);

		TabSpec promotedspec = TabHost.newTabSpec("Browse");
		// setting Title and Icon for the Tab
		promotedspec.setIndicator("",
				getResources().getDrawable(R.drawable.icon_promoted_tab));
		Intent promotedIntent = new Intent(this, PromotedActivity.class);
		promotedspec.setContent(promotedIntent);

		// Tab for Settings
		TabSpec settingsspec = TabHost.newTabSpec("Settings");
		// setting Title and Icon for the Tab
		settingsspec.setIndicator("",
				getResources().getDrawable(R.drawable.icon_settings_tab));
		Intent settingsintent = new Intent(this, SettingsActivity.class);
		settingsspec.setContent(settingsintent);

		// Tab for Videos
		TabSpec shopspec = TabHost.newTabSpec("Shop");
		// setting Title and Icon for the Tab
		shopspec.setIndicator("",
				getResources().getDrawable(R.drawable.icon_shop_tab));
		Intent shopIntent = new Intent(this, ShopActivity.class);
		shopspec.setContent(shopIntent);

		// Adding all TabSpec to TabHost
		TabHost.addTab(browsespec); // Adding photos tab
		TabHost.addTab(promotedspec);
		TabHost.addTab(settingsspec); // Adding songs tab
		TabHost.addTab(shopspec); // Adding videos tab
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
}

class DownloadImageTask1 extends AsyncTask<String, Void, Bitmap> {
	ImageView bmImage;
	static Integer licznik = 1;

	public DownloadImageTask1(ImageView bmImage) {
		this.bmImage = bmImage;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();

	}

	protected Bitmap doInBackground(String... urls) {
		String urldisplay = urls[0];
		Bitmap mIcon11 = null;
		try {
			InputStream in = new java.net.URL(urldisplay).openStream();
			mIcon11 = BitmapFactory.decodeStream(in);

		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
		}
		return mIcon11;
	}

	private boolean storeImage(Bitmap imageData, String filename) {
		// get path to external storage (SD card)
		String iconsStoragePath = Environment.getExternalStorageDirectory()
				+ "/Android/InstagramPolska/";
		File sdIconStorageDir = new File(iconsStoragePath);

		// create storage directories, if they don't exist
		sdIconStorageDir.mkdirs();

		try {
			String filePath = sdIconStorageDir.toString() + "/" + filename;
			FileOutputStream fileOutputStream = new FileOutputStream(filePath);

			BufferedOutputStream bos = new BufferedOutputStream(
					fileOutputStream);

			// choose another format if PNG doesn't suit you
			imageData.compress(Bitmap.CompressFormat.PNG, 100, bos);

			bos.flush();
			bos.close();
			Log.i("TAG", "Path of file " + filePath);
		} catch (FileNotFoundException e) {
			Log.w("TAG", "Error saving image file: " + e.getMessage());
			return false;
		} catch (IOException e) {
			Log.w("TAG", "Error saving image file: " + e.getMessage());
			return false;
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@SuppressWarnings("unused")
	@Override
	protected void onPostExecute(Bitmap result) {
		super.onPostExecute(result);
		Bitmap bitmap;
		MainActivity main = new MainActivity();

		if (licznik == 1) {
			Log.i("TAG", "bmimage");
			Double width = result.getWidth() / 1.4;
			Double height = result.getHeight() / 1.2;

			int h = height.intValue();
			int w = width.intValue();

			BrowseActivity.bmimage = result;
			bitmap = result.createScaledBitmap(result, MainActivity.w,
					MainActivity.h, true);
			BrowseActivity.ivSelected.setImageBitmap(bitmap);

		}

		if (licznik == 2) {
			Log.i("TAG", "bmimage0");
			int width = result.getWidth();
			int height = result.getHeight(); 
			BrowseActivity.bmimage0 = result;
			bitmap = result.createScaledBitmap(result, MainActivity.w / 3, MainActivity.h / 3, true);
			BrowseActivity.image0.setImageBitmap(bitmap);
		}
		if (licznik == 3) {
			Log.i("TAG", "bmimage1");
			int width = result.getWidth();
			int height = result.getHeight();
			BrowseActivity.bmimage1 = result;
			bitmap = result.createScaledBitmap(result, MainActivity.w / 3, MainActivity.h / 3, true);
			BrowseActivity.image1.setImageBitmap(bitmap);
		}
		if (licznik == 4) {
			Log.i("TAG", "bmimage2");
			int width = result.getWidth();
			int height = result.getHeight();
			BrowseActivity.bmimage2 = result;
			bitmap = result.createScaledBitmap(result, MainActivity.w / 3, MainActivity.h / 3, true);
			BrowseActivity.image2.setImageBitmap(bitmap);
		}

		licznik += 1;
		if (licznik > 4) {
			licznik = 1;
		}

	}
}

class DownloadImageTask2 extends AsyncTask<String, Void, Bitmap> {
	ImageView bmImage;
	static Integer licznik = 1;

	public DownloadImageTask2(ImageView bmImage) {
		this.bmImage = bmImage;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();

	}

	protected Bitmap doInBackground(String... urls) {
		String urldisplay = urls[0];
		Bitmap mIcon11 = null;
		try {
			InputStream in = new java.net.URL(urldisplay).openStream();
			mIcon11 = BitmapFactory.decodeStream(in);

		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
		}
		return mIcon11;
	}

	private boolean storeImage(Bitmap imageData, String filename) {
		// get path to external storage (SD card)
		String iconsStoragePath = Environment.getExternalStorageDirectory()
				+ "/Android/InstagramPolska/";
		File sdIconStorageDir = new File(iconsStoragePath);

		// create storage directories, if they don't exist
		sdIconStorageDir.mkdirs();

		try {
			String filePath = sdIconStorageDir.toString() + "/" + filename;
			FileOutputStream fileOutputStream = new FileOutputStream(filePath);

			BufferedOutputStream bos = new BufferedOutputStream(
					fileOutputStream);

			// choose another format if PNG doesn't suit you
			imageData.compress(Bitmap.CompressFormat.PNG, 100, bos);

			bos.flush();
			bos.close();
			Log.i("TAG", "Path of file " + filePath);
		} catch (FileNotFoundException e) {
			Log.w("TAG", "Error saving image file: " + e.getMessage());
			return false;
		} catch (IOException e) {
			Log.w("TAG", "Error saving image file: " + e.getMessage());
			return false;
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(Bitmap result) {
		super.onPostExecute(result);

		// BrowseActivity.ivSelected.setImageBitmap(result);

	}
}
class DownloadImageTask3 extends AsyncTask<String, Void, Bitmap> {
	ImageView bmImage;
	static Integer licznik = 0;

	public DownloadImageTask3(ImageView bmImage) {
		this.bmImage = bmImage;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();

	}

	protected Bitmap doInBackground(String... urls) {
		String urldisplay = urls[0];
		Bitmap mIcon11 = null;
		try {
			InputStream in = new java.net.URL(urldisplay).openStream();
			mIcon11 = BitmapFactory.decodeStream(in);

		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
		}
		return mIcon11;
	}

	private boolean storeImage(Bitmap imageData, String filename) {
		// get path to external storage (SD card)
		String iconsStoragePath = Environment.getExternalStorageDirectory()
				+ "/Android/InstagramPolska/";
		File sdIconStorageDir = new File(iconsStoragePath);

		// create storage directories, if they don't exist
		sdIconStorageDir.mkdirs();

		try {
			String filePath = sdIconStorageDir.toString() + "/" + filename;
			FileOutputStream fileOutputStream = new FileOutputStream(filePath);

			BufferedOutputStream bos = new BufferedOutputStream(
					fileOutputStream);

			// choose another format if PNG doesn't suit you
			imageData.compress(Bitmap.CompressFormat.PNG, 100, bos);

			bos.flush();
			bos.close();
			Log.i("TAG", "Path of file " + filePath);
		} catch (FileNotFoundException e) {
			Log.w("TAG", "Error saving image file: " + e.getMessage());
			return false;
		} catch (IOException e) {
			Log.w("TAG", "Error saving image file: " + e.getMessage());
			return false;
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@SuppressWarnings("unused")
	@Override
	protected void onPostExecute(Bitmap result) {
		super.onPostExecute(result);
		Bitmap bitmap;
		MainActivity main = new MainActivity();

		if (licznik == 1) {
			Log.i("TAG", "bmimage");
			Double width = result.getWidth() / 1.4;
			Double height = result.getHeight() / 1.2;

			int h = height.intValue();
			int w = width.intValue();

			PromotedActivity.bmimage = result;
			bitmap = result.createScaledBitmap(result, MainActivity.w,
					MainActivity.h, true);
			PromotedActivity.ivSelected.setImageBitmap(bitmap);

		}

		if (licznik == 2) {
			Log.i("TAG", "bmimage0");
			int width = result.getWidth();
			int height = result.getHeight(); 
			PromotedActivity.bmimage0 = result;
			bitmap = result.createScaledBitmap(result, MainActivity.w / 3, MainActivity.h / 3, true);
			PromotedActivity.image0.setImageBitmap(bitmap);
		}
		if (licznik == 3) {
			Log.i("TAG", "bmimage1");
			int width = result.getWidth();
			int height = result.getHeight();
			PromotedActivity.bmimage1 = result;
			bitmap = result.createScaledBitmap(result, MainActivity.w / 3, MainActivity.h / 3, true);
			PromotedActivity.image1.setImageBitmap(bitmap);
		}
		if (licznik == 4) {
			Log.i("TAG", "bmimage2");
			int width = result.getWidth();
			int height = result.getHeight();
			PromotedActivity.bmimage2 = result;
			bitmap = result.createScaledBitmap(result, MainActivity.w / 3, MainActivity.h / 3, true);
			PromotedActivity.image2.setImageBitmap(bitmap);
		}

		licznik += 1;
		if (licznik > 4) {
			licznik = 1;
		}

	}
}
