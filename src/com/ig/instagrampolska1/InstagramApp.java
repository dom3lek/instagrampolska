package com.ig.instagrampolska1;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.ig.instagrampolska1.InstagramDialog.OAuthDialogListener;

/**
 * 
 * @author Thiago Locatelli <thiago.locatelli@gmail.com>
 * @author Lorensius W. L T <lorenz@londatiga.net>
 * 
 */
public class InstagramApp extends Activity {

	public static Bitmap result;
	static InstagramSession mSession;
	private InstagramDialog mDialog;
	private static OAuthAuthenticationListener mListener;
	private static ProgressDialog mProgress;
	private String mAuthUrl;
	private String mTokenUrl;
	public static String mAccessToken;
	private Context mCtx;

	public  String mClientId;
	private String mClientSecret;

	private static int WHAT_FINALIZE = 0;
	private static int WHAT_ERROR = 1;
	private static int WHAT_FETCH_INFO = 2;

	/**
	 * Callback url, as set in 'Manage OAuth Costumers' page
	 * (https://developer.github.com/)
	 */

	public static String mCallbackUrl = "";
	private static final String AUTH_URL = "https://api.instagram.com/oauth/authorize/";
	private static final String TOKEN_URL = "https://api.instagram.com/oauth/access_token";
	private static final String API_URL = "https://api.instagram.com/v1";

	private static final String TAG = "InstagramAPI";

	int licznik = 0;
	
	static Bitmap image0;
	static Bitmap image1;
	static Bitmap image2;

	static ArrayList<Bitmap> bitmapArray = new ArrayList<Bitmap>(3);

	public InstagramApp(Context context, String clientId, String clientSecret,
			String callbackUrl) {

		mClientId = clientId;
		mClientSecret = clientSecret;
		mCtx = context;
		mSession = new InstagramSession(context);
		mAccessToken = mSession.getAccessToken();
		mCallbackUrl = callbackUrl;
		mTokenUrl = TOKEN_URL + "?client_id=" + clientId + "&client_secret="
				+ clientSecret + "&redirect_uri=" + mCallbackUrl
				+ "&grant_type=authorization_code";
		mAuthUrl = AUTH_URL
				+ "?client_id="
				+ clientId
				+ "&redirect_uri="
				+ mCallbackUrl
				+ "&response_type=code&display=touch&scope=likes+comments+relationships";

		OAuthDialogListener listener = new OAuthDialogListener() {
			@Override
			public void onComplete(String code) {
				getAccessToken(code);
			}

			@Override
			public void onError(String error) {
				mListener.onFail("Authorization failed");
			}
		};

		mDialog = new InstagramDialog(context, mAuthUrl, listener);
		mProgress = new ProgressDialog(context);
		mProgress.setCancelable(false);
	}

	private void getAccessToken(final String code) {
		mProgress.setMessage("Getting access token ...");
		mProgress.show();

		new Thread() {
			@Override
			public void run() {
				Log.i(TAG, "Getting access token");
				int what = WHAT_FETCH_INFO;
				try {
					URL url = new URL(TOKEN_URL);
					// URL url = new URL(mTokenUrl + "&code=" + code);
					Log.i(TAG, "Opening Token URL " + url.toString());
					HttpURLConnection urlConnection = (HttpURLConnection) url
							.openConnection();
					urlConnection.setRequestMethod("POST");
					urlConnection.setDoInput(true);
					urlConnection.setDoOutput(true);
					// urlConnection.connect();
					OutputStreamWriter writer = new OutputStreamWriter(
							urlConnection.getOutputStream());
					writer.write("client_id=" + mClientId + "&client_secret="
							+ mClientSecret + "&grant_type=authorization_code"
							+ "&redirect_uri=" + mCallbackUrl + "&code=" + code);
					writer.flush();
					String response = streamToString(urlConnection
							.getInputStream());
					Log.i(TAG, "response " + response);
					JSONObject jsonObj = (JSONObject) new JSONTokener(response)
							.nextValue();

					mAccessToken = jsonObj.getString("access_token");
					Log.i(TAG, "Got access token: " + mAccessToken);

					String id = jsonObj.getJSONObject("user").getString("id");
					String user = jsonObj.getJSONObject("user").getString(
							"username");
					String name = jsonObj.getJSONObject("user").getString(
							"full_name");

					mSession.storeAccessToken(mAccessToken, id, user, name);

				} catch (Exception ex) {
					what = WHAT_ERROR;
					ex.printStackTrace();
				}

				mHandler.sendMessage(mHandler.obtainMessage(what, 1, 0));
			}
		}.start();
	}

	public static void fetchUserName() {
		mProgress.setMessage("Finalizing ...");

		new Thread() {
			@Override
			public void run() {
				Log.i(TAG, "Fetching user info");
				int what = WHAT_FINALIZE;

				try {
					URL url = new URL(API_URL + "/users/" + mSession.getId()
							+ "/?access_token=" + mAccessToken);

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
					String username = jsonObj.getJSONObject("data").getString(
							"username");
					String bio = jsonObj.getJSONObject("data").getString("bio");
					String link_url = jsonObj.getJSONObject("data").getString(
							"profile_picture");
					Log.i(TAG, "Got name: " + username + ", bio [" + bio + "]"
							+ " link profile: " + link_url);

					new DownloadImageTask(LoginActivity.ivProfil)
							.execute(link_url);

				} catch (Exception ex) {
					what = WHAT_ERROR;
					ex.printStackTrace();
				}

				mHandler.sendMessage(mHandler.obtainMessage(what, 2, 0));
			}
		}.start();

	}

	private static Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == WHAT_ERROR) {
				mProgress.dismiss();
				if (msg.arg1 == 1) {
					mListener.onFail("Failed to get access token");
				} else if (msg.arg1 == 2) {
					mListener.onFail("Failed to get user information");
				}
			} else if (msg.what == WHAT_FETCH_INFO) {
				fetchUserName();
			} else {
				mProgress.dismiss();
				mListener.onSuccess();
			}
		}
	};

	public boolean hasAccessToken() {
		return (mAccessToken == null) ? false : true;
	}

	public void setListener(OAuthAuthenticationListener listener) {
		mListener = listener;
	}

	public String getUserName() {
		return mSession.getUsername();
	}

	public static String getId() {
		return mSession.getId();
	}

	public String getName() {
		return mSession.getName();
	}

	public void authorize() {
		// Intent webAuthIntent = new Intent(Intent.ACTION_VIEW);
		// webAuthIntent.setData(Uri.parse(AUTH_URL));
		// mCtx.startActivity(webAuthIntent);
		mDialog.show();
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

	public void resetAccessToken() {
		if (mAccessToken != null) {
			mSession.resetAccessToken();
			mAccessToken = null;
		}
	}

	public interface OAuthAuthenticationListener {
		public abstract void onSuccess();

		public abstract void onFail(String error);
	}
}

class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	ImageView bmImage;
	static int licznik = 0;

	public DownloadImageTask(ImageView bmImage) {
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

		// LoginActivity.ivProfil.setImageBitmap(result);
		storeImage(result, "profile.png");

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;

		Bitmap bitmap = BitmapFactory.decodeFile(
				Environment.getExternalStorageDirectory()
						+ "/Android/InstagramPolska/profile.png", options);

		WelcomeActivity.photoBMP = bitmap;

	}
}
