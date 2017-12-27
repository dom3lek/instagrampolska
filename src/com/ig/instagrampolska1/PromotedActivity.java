package com.ig.instagrampolska1;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.CookieStore;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PromotedActivity extends ActionBarActivity {
	static String TAG = "InstagramApp";
	public static ImageView image0, image1, image2, ivSelected;
	public static Bitmap bmimage, bmimage0, bmimage1, bmimage2;
	TextView tvUsername;
	Button btAccept, btIgnore;
	CookieStore cookies;
	String userid = "";
	String urll = "instafollowers.pl";
	boolean valid = true;
	boolean search_result;
	int licznik = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_promoted);
		ivSelected = (ImageView) findViewById(R.id.ivSelected);
		image0 = (ImageView) findViewById(R.id.ivImage0);
		image1 = (ImageView) findViewById(R.id.ivImage1);
		image2 = (ImageView) findViewById(R.id.ivImage2);
		btAccept = (Button) findViewById(R.id.btAccept);
		btIgnore = (Button) findViewById(R.id.btIgnore);
		tvUsername = (TextView) findViewById(R.id.tvUsername);

		final String breakline = "\n";
		String s = null;
		final String filename = "VisitedUsersPromoted.txt";

		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		do {

			userid = MainActivity.getIdPromoted(LoginActivity.mApp.getId().trim());
			if (userid != LoginActivity.mApp.getId().trim() && userid != null) {

				valid = true;
			
				String username = MainActivity.getUserName(userid);

				tvUsername.setText(username);

				MainActivity.GetMediaUserPromoted(userid);

			}

			else {
				valid = false;
			}

		} while (valid == false);
		
		Log.i("Tag", "ID MOJE " + LoginActivity.mApp.getId());

		image0.setOnClickListener(new View.OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bitmap bitmap, bitmap2, bitmap3;

				Display display = getWindowManager().getDefaultDisplay();

				Double width_screen = (double) display.getWidth() / 1.4;
				Double height_screen = (double) display.getHeight() / 2.5;
				int w = width_screen.intValue();
				int h = height_screen.intValue();

				bitmap = ((BitmapDrawable) ivSelected.getDrawable())
						.getBitmap();
				bitmap3 = bitmap.createScaledBitmap(bitmap, MainActivity.w / 3,
						MainActivity.h / 3, true);

				image0.setImageBitmap(bitmap3);
				bitmap2 = Bitmap.createScaledBitmap(bmimage0, MainActivity.w, h, true);

				ivSelected.setImageBitmap(bitmap2);
				bmimage0 = bitmap;
			}
		});
		image1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bitmap bitmap, bitmap2, bitmap3;

				Display display = getWindowManager().getDefaultDisplay();

				Double width_screen = (double) display.getWidth() / 1.4;
				Double height_screen = (double) display.getHeight() / 2.5;
				int w = width_screen.intValue();
				int h = height_screen.intValue();

				bitmap = ((BitmapDrawable) ivSelected.getDrawable())
						.getBitmap();
				bitmap3 = bitmap.createScaledBitmap(bitmap, MainActivity.w / 3,
						MainActivity.h / 3, true);

				image1.setImageBitmap(bitmap3);
				bitmap2 = Bitmap.createScaledBitmap(bmimage1, w, h, true);

				ivSelected.setImageBitmap(bitmap2);
				bmimage1 = bitmap;
			}
		});

		image2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bitmap bitmap, bitmap2, bitmap3;

				Display display = getWindowManager().getDefaultDisplay();

				Double width_screen = (double) display.getWidth() / 1.4;
				Double height_screen = (double) display.getHeight() / 2.5;
				int w = width_screen.intValue();
				int h = height_screen.intValue();
				
				bitmap = ((BitmapDrawable) ivSelected.getDrawable())
						.getBitmap();
				bitmap3 = bitmap.createScaledBitmap(bitmap, MainActivity.w / 3,
						MainActivity.h / 3, true);

				image2.setImageBitmap(bitmap3);
				bitmap2 = Bitmap.createScaledBitmap(bmimage2, w, h, true);

				ivSelected.setImageBitmap(bitmap2);
				bmimage2 = bitmap;
			}
		});

		btAccept.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				MainActivity.followUser(userid);

				ivSelected.setImageBitmap(null);
				image0.setImageBitmap(null);
				image1.setImageBitmap(null);
				image2.setImageBitmap(null);
				tvUsername.setText("Username: ");

				bmimage = null;
				bmimage0 = null;
				bmimage1 = null;
				bmimage2 = null;

				do {
					userid = MainActivity.getIdPromoted(LoginActivity.mApp.getId()
							.trim());
					if (userid != LoginActivity.mApp.getId().trim()
							&& userid != null) {
						if (SearchID(userid) == true) {
							valid = false;
						} else {
							valid = true;
							// valid = true;
							SaveIdToFile(userid);
							String username = MainActivity.getUserName(userid);
							tvUsername.setText(username);

							MainActivity.GetMediaUserPromoted(userid);
							licznik = 1;

						}
					}

					else {
						valid = false;
					}
					licznik++;

					

					if (licznik > 30) {
						Log.i("Tag", "ERROR");
						licznik = 1;
						Okienko();
						break;
					}

				} while (valid == false);

				// MainActivity.getProfilePic(userid);

			}
		});
		btIgnore.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ivSelected.setImageBitmap(null);
				image0.setImageBitmap(null);
				image1.setImageBitmap(null);
				image2.setImageBitmap(null);
				tvUsername.setText("Username: ");
				

				bmimage = null;
				bmimage0 = null;
				bmimage1 = null;
				bmimage2 = null;

				try {
					do {
						userid = MainActivity.getIdPromoted(LoginActivity.mApp
								.getId().trim());
						if (userid != LoginActivity.mApp.getId().trim()
								&& userid != null) {
							if (SearchID(userid) == true) {
								valid = false;
							} else {
								valid = true;
								// valid = true;
								SaveIdToFile(userid);
								String username = MainActivity
										.getUserName(userid);
								tvUsername.setText(username);

								MainActivity.GetMediaUserPromoted(userid);
								licznik = 1;

							}
						}

						else {
							valid = false;
						}
						licznik++;

						

						if (licznik > 30) {
							Log.i("Tag", "ERROR");
							licznik = 1;
							Okienko();
							break;
						}

					} while (valid == false);
				} catch (Exception e) {
					Log.i("Tag", "ERROR");
				}

				// MainActivity.getProfilePic(userid);

			}
		});

	}

	@SuppressWarnings("deprecation")
	public void Okienko() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Users not found or try again")
				.setCancelable(false)
				.setPositiveButton("Try Again",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								do {

									userid = MainActivity
											.getIdPromoted(LoginActivity.mApp
													.getId().trim());
									if (userid != LoginActivity.mApp.getId()
											.trim() && userid != null) {
										if (SearchID(userid) == true) {
											valid = false;
										} else {
											valid = true;
											// valid = true;
											SaveIdToFile(userid);
											String username = MainActivity
													.getUserName(userid);

											tvUsername.setText(username);

											MainActivity.GetMediaUserPromoted(userid);
										}
									}

									else {
										valid = false;
									}

									licznik++;

									if (licznik > 30) {
										Log.i("Tag", "ERROR");
										licznik = 1;
										Okienko();
										break;
									}

								} while (valid == false);
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

		AlertDialog alertDialog = builder.create();
		// final AlertDialog alertDialog = new
		// AlertDialog.Builder(this).create();
		alertDialog.setTitle("Error");
		alertDialog.setMessage("Users not found or try again");
		alertDialog.setIcon(R.drawable.ic_drawer);

		alertDialog.show();
	}

	public void GetUserId() {
		do {
			userid = MainActivity.getIdPromoted(LoginActivity.mApp.getId());
			if (userid != LoginActivity.mApp.getId() && userid != null) {
				valid = true;
			} else {
				valid = false;
			}
		} while (valid == false);

	}

	public boolean SearchID(String userid) {
		BufferedReader reader = null;
		boolean a = false;
		try {
			reader = new BufferedReader(new InputStreamReader(
					openFileInput("VisitedUsersPromoted.txt")));

			// do reading, usually loop until end of file reading

			String mLine = reader.readLine();
			String b = "";

			while (mLine != null) {
				// process line
				b += mLine;

				if (mLine.equals(userid)) {
					// Log.i("Tag", "Znalezionio");
					// a = true;
				}

				else {
					// Log.i("Tag", "Nie Znalezionio");

				}
				mLine = reader.readLine();

			}
			Integer indexfound = b.indexOf(userid, 0);
			if (indexfound > -1) {
				a = true;
			}

			Log.i("Tag", "indeks " + indexfound.toString());

		} catch (IOException e) {
			// log the exception

		} finally {
			if (reader != null) {
				try {
					reader.close();

				} catch (IOException e) {
					// log the exception

				}
			}
		}
		return a;

	}

	public void SaveIdToFile(String userid) {
		final String breakline = "\n";
		final String filename = "VisitedUsersPromoted.txt";

		try {
			FileOutputStream fOut = openFileOutput(filename,
					Context.MODE_APPEND);
			fOut.write(userid.getBytes());
			fOut.write(breakline.getBytes());
			fOut.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.promoted, menu);
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
