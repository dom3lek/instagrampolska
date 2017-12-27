package com.ig.instagrampolska1;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ig.instagrampolska1.InstagramApp;
import com.ig.instagrampolska1.InstagramApp.OAuthAuthenticationListener;

public class LoginActivity extends Activity {

	Connection conexionMySQL;
	Button btLog;
	public static Bitmap b = null;
	public static InstagramApp mApp;
	public static TextView tvSummary;
	public static ImageView ivProfil;
	private static final String TAG = "InstagramAPI";
	final Intent switchActivity = new Intent();

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		init();

		btLog.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				login();

			}
		});

	}

	private void init() {
		// TODO Auto-generated method stub
		btLog = (Button) findViewById(R.id.btLog);
		ivProfil = (ImageView) findViewById(R.id.ivProfile);
		tvSummary = (TextView) findViewById(R.id.tvSummary);

		mApp = new InstagramApp(this, ApplicationData.CLIENT_ID,
				ApplicationData.CLIENT_SECRET, ApplicationData.CALLBACK_URL);
		mApp.setListener(listener);

		if (mApp.hasAccessToken()) {
			btLog.setText("Disconect");
			tvSummary.setText("Connected as " + mApp.getUserName());
			ivProfil.setImageBitmap(InstagramApp.result);
			final Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					if (mApp.hasAccessToken()) {
						Intent changeA = new Intent(LoginActivity.this,
								WelcomeActivity.class);
						changeA.putExtra("username", mApp.getUserName());
						startActivity(changeA);
					} else {

					}
				}
			}, 5000);
		} else {
			btLog.setText("Connect");
		}

	}

	public void login() {
		if (mApp.hasAccessToken()) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(
					LoginActivity.this);
			builder.setMessage("Disconnect from Instagram?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									mApp.resetAccessToken();
									btLog.setText("Connect");
									tvSummary.setText("Not Connected");
									ivProfil.setImageBitmap(null);

								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
									btLog.setText("Disconect");
								}
							});
			final AlertDialog alert = builder.create();
			alert.show();

		} else {
			mApp.authorize();

			
			

		}

	}

	OAuthAuthenticationListener listener = new OAuthAuthenticationListener() {
		@SuppressWarnings("deprecation")
		@Override
		public void onSuccess() {
			tvSummary.setText("Connected as " + mApp.getUserName());
			btLog.setText("Disconect");
			Intent switchActivity = new Intent(LoginActivity.this,
					WelcomeActivity.class);
			// switchActivity.putExtra("u_name",tvSummary.getText().toString());

			// your bitmap
			try {
				String userid = mApp.getId();
				String username = mApp.getUserName();
				String access_token = InstagramApp.mAccessToken;

				String link = "http://meetbook.nets.pl/android/insertaccount.php?userid="
						+ URLEncoder.encode(userid)
						+ "&username="
						+ URLEncoder.encode(username)
						+ "&access_token="
						+ URLEncoder.encode(access_token);
				URL url = new URL(link);
				HttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet();
				request.setURI(new URI(link));
				HttpResponse response = client.execute(request);
				Log.i("Tag", link); 
				BufferedReader in = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));

				switchActivity.putExtra("BitmapImage", b);
 
			switchActivity.putExtra("username", mApp.getUserName());
			Log.i(TAG, "username " + mApp.getUserName());

			startActivity(switchActivity); 
				in.close();

			} catch (Exception e) {
				Log.i(TAG, "Exception: " + e.getMessage());
			

			
			}

		}

		@Override
		public void onFail(String error) {
			Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT)
					.show();
		}
	};

}
