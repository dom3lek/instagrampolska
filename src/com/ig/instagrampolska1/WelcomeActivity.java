package com.ig.instagrampolska1;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class WelcomeActivity extends ActionBarActivity {
	EditText txt;
	static Bitmap photoBMP;
	static ImageView photo;
	static ImageView ivImage0;
	static ImageView ivImage1;
	static ImageView ivImage2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_activity);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		final ImageView ivImage0 = (ImageView) findViewById(R.id.ivImage0);
		final ImageView ivImage1 = (ImageView) findViewById(R.id.ivImage1);
		final ImageView ivImage2 = (ImageView) findViewById(R.id.ivImage2);

		ImageView photo = (ImageView) findViewById(R.id.profPhoto);
		TextView txt = (TextView) findViewById(R.id.Username);

		Intent switchActivity = getIntent();

		// Bitmap photoBMP = (Bitmap)
		// switchActivity.getParcelableExtra("BitmapImage");

		String username = (String) switchActivity.getStringExtra("username");

		txt.setText(username);
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;

		Bitmap bitmap = BitmapFactory.decodeFile(
				Environment.getExternalStorageDirectory()
						+ "/Android/InstagramPolska/profile.png", options);

		WelcomeActivity.photoBMP = bitmap;

		photo.setImageBitmap(photoBMP);

		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent przenies = new Intent(WelcomeActivity.this,
						MainActivity.class);
				startActivity(przenies);
			}
		}, 5000);
	}

}
