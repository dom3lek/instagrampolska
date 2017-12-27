package com.ig.instagrampolska1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.CookieStore;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import android.widget.Toast;

public class BrowseActivity extends ActionBarActivity {
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
	Integer obecnyprofil = 1;

	int limitprofili = 20;
	static boolean mIsPremium = false;

	Integer licznikprofili = 0;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SharedPreferences spr = getPreferences(MODE_PRIVATE);
		obecnyprofil = spr.getInt("obecnyprofil", 0); 
	Log.i("Tag", obecnyprofil.toString());	
		if (obecnyprofil == 0) {
			obecnyprofil = 1;  
		}

		if (mIsPremium) {
			limitprofili = 50;

		} else {
			limitprofili = 20;
		}

		SharedPreferences sp = getPreferences(MODE_PRIVATE);
		licznikprofili = sp.getInt("licznik", 0);
		if (licznikprofili == 0) {
			licznikprofili = 1;
		}

		setContentView(R.layout.activity_browse);
		ivSelected = (ImageView) findViewById(R.id.ivSelected);
		image0 = (ImageView) findViewById(R.id.ivImage0);
		image1 = (ImageView) findViewById(R.id.ivImage1);
		image2 = (ImageView) findViewById(R.id.ivImage2);
		btAccept = (Button) findViewById(R.id.btAccept);
		btIgnore = (Button) findViewById(R.id.btIgnore);
		tvUsername = (TextView) findViewById(R.id.tvUsername);
		String f = "VisitedUsers.txt";
		final String breakline = "\n";
		String s = null;
		final String filename = "VisitedUsers.txt";

		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		if (obecnyprofil <= limitprofili) {
			try {
				do {
					Log.i("Tag", licznikprofili.toString());
					userid = MainActivity.getUserID(LoginActivity.mApp
							.getId().trim(), licznikprofili.toString());
					if (userid != LoginActivity.mApp.getId().trim()
							&& userid != null && userid != "") {
						if (userid.equals("a")) {
							Log.i("Tag", "ERROR");
							licznik = 1;
							Okienko();
							break;
						} else {
							if (SearchID(userid) == true) {
								valid = false;
								licznikprofili++;
							} else {
								valid = true;
								// valid = true;
								// SaveIdToFile(userid);
								String username = MainActivity
										.getUserName(userid);

								tvUsername.setText(username);

								MainActivity.GetMediaUser(userid);

							}
						}
					}

					else {
						valid = false;
						licznikprofili++;
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
		} else {
			OkienkoPremium();
		}

		// MainActivity.getProfilePic(userid);

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
				bitmap2 = Bitmap.createScaledBitmap(bmimage0, MainActivity.w,
						h, true);

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
				SaveIdToFile(userid);
				SharedPreferences.Editor spe = getPreferences(MODE_PRIVATE)
						.edit();
				licznikprofili++;
				obecnyprofil++;
				spe.putInt("licznik", licznikprofili);
				spe.putInt("obecnyprofil", obecnyprofil);
				spe.commit();

				SharedPreferences spr = getPreferences(MODE_PRIVATE);
				obecnyprofil = spr.getInt("obecnyprofil", 0);
				if (obecnyprofil == 0) {
					obecnyprofil = 1; 
				}
				ivSelected.setImageBitmap(null);
				image0.setImageBitmap(null);
				image1.setImageBitmap(null);
				image2.setImageBitmap(null);
				tvUsername.setText("Username: ");

				bmimage = null;
				bmimage0 = null;
				bmimage1 = null;
				bmimage2 = null;

				if (obecnyprofil <= limitprofili) {
					try {
						do {
							Log.i("Tag", licznikprofili.toString());
							userid = MainActivity.getUserID(LoginActivity.mApp
									.getId().trim(), licznikprofili.toString());
							if (userid != LoginActivity.mApp.getId().trim()
									&& userid != null && userid != "") {
								if (userid.equals("a")) {
									Log.i("Tag", "ERROR");
									licznik = 1;
									Okienko();
									break;
								} else {
									if (SearchID(userid) == true) {
										valid = false;
										licznikprofili++;
									} else {
										valid = true;
										// valid = true;
										// SaveIdToFile(userid);
										String username = MainActivity
												.getUserName(userid);

										tvUsername.setText(username);

										MainActivity.GetMediaUser(userid);

									}
								}
							}

							else {
								valid = false;
								licznikprofili++;
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
				} else {
					OkienkoPremium();
				}

			}
		});
		btIgnore.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SaveIdToFile(userid);
				SharedPreferences.Editor spe = getPreferences(MODE_PRIVATE)
						.edit();
				licznikprofili++;
				obecnyprofil++;
				spe.putInt("licznik", licznikprofili);
				spe.putInt("obecnyprofil", obecnyprofil);
				spe.commit();
				
				SharedPreferences spr = getPreferences(MODE_PRIVATE);
				obecnyprofil = spr.getInt("obecnyprofil", 0);
				if (obecnyprofil == 0) {
					obecnyprofil = 1; 
				}

				ivSelected.setImageBitmap(null);
				image0.setImageBitmap(null);
				image1.setImageBitmap(null);
				image2.setImageBitmap(null);
				tvUsername.setText("Username: ");

				bmimage = null;
				bmimage0 = null;
				bmimage1 = null;
				bmimage2 = null;

				if (obecnyprofil <= limitprofili) {
					try {
						do {
							Log.i("Tag", licznikprofili.toString());
							userid = MainActivity.getUserID(LoginActivity.mApp
									.getId().trim(), licznikprofili.toString());
							if (userid != LoginActivity.mApp.getId().trim()
									&& userid != null && userid != "") {
								if (userid.equals("a")) {
									Log.i("Tag", "ERROR");
									licznik = 1;
									Okienko();
									break;
								} else {
									if (SearchID(userid) == true) {
										valid = false;
										licznikprofili++;
									} else {
										valid = true;
										// valid = true;
										// SaveIdToFile(userid);
										String username = MainActivity
												.getUserName(userid);

										tvUsername.setText(username);

										MainActivity.GetMediaUser(userid);
										

									}
								}
							}

							else {
								valid = false;
								licznikprofili++;
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
				} else {
					OkienkoPremium(); 
				}

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
								try {
									do {
										Log.i("Tag", licznikprofili.toString());
										userid = MainActivity.getUserID(
												LoginActivity.mApp.getId()
														.trim(), licznikprofili
														.toString());
										if (userid != LoginActivity.mApp
												.getId().trim()
												&& userid != null
												&& userid != "") {
											if (userid.equals("a")) {
												Log.i("Tag", "ERROR");
												licznik = 1;
												Okienko();
												break;
											} else {
												if (SearchID(userid) == true) {
													valid = false;
													licznikprofili++;
												} else {
													valid = true;
													// valid = true;
													// SaveIdToFile(userid);
													String username = MainActivity
															.getUserName(userid);

													tvUsername
															.setText(username);

													MainActivity
															.GetMediaUser(userid);

												}
											}
										}

										else {
											valid = false;
											licznikprofili++;
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

	public void OkienkoPremium() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				"Wykorzystales limit przegladania profili, sprobuj za godzine")
				.setCancelable(false)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();

					}
				});

		AlertDialog alertDialog = builder.create();
		// final AlertDialog alertDialog = new
		// AlertDialog.Builder(this).create(); 
		alertDialog.setTitle("Limit");
		alertDialog
				.setMessage("Wykorzystales limit przegladania profili, sprobuj za godzine");
		alertDialog.setIcon(R.drawable.ic_drawer);

		alertDialog.show();
	}

	public boolean SearchID(String userid) {
		BufferedReader reader = null;
		boolean a = false;
		try {
			reader = new BufferedReader(new InputStreamReader(
					openFileInput("VisitedUsers.txt")));

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
		final String filename = "VisitedUsers.txt";

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
		getMenuInflater().inflate(R.menu.browse, menu);
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