package com.ig.instagrampolska1;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Display 37Signals authentication dialog.
 * 
 * @author Thiago Locatelli <thiago.locatelli@gmail.com>
 * @author Lorensius W. L T <lorenz@londatiga.net>
 * 
 */
@SuppressLint("NewApi") @SuppressWarnings("deprecation")
public class InstagramDialog extends Dialog {

	static final float[] DIMENSIONS_LANDSCAPE = { 490, 290 };
	static final float[] DIMENSIONS_PORTRAIT = { 320, 420 };
	static final FrameLayout.LayoutParams FILL = new FrameLayout.LayoutParams(
			ViewGroup.LayoutParams.FILL_PARENT,
			ViewGroup.LayoutParams.FILL_PARENT);
	static final int MARGIN = 4;
	static final int PADDING = 2;

	private String mUrl;
	private OAuthDialogListener mListener;
	private ProgressDialog mSpinner;
	private WebView mWebView;
	private LinearLayout mContent;
	private TextView mTitle;

	private static final String TAG = "Instagram-WebView";

	public InstagramDialog(Context context, String url,
			OAuthDialogListener listener) {
		super(context);

		mUrl = url;
		mListener = listener;
	}

	@SuppressWarnings({ "deprecation", "deprecation", "deprecation", "deprecation", "deprecation" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mSpinner = new ProgressDialog(getContext());
		mSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mSpinner.setMessage("Loading...");
		mContent = new LinearLayout(getContext());
		mContent.setOrientation(LinearLayout.VERTICAL);
		setUpTitle();
		setUpWebView();

		Display display = getWindow().getWindowManager().getDefaultDisplay();
		final float scale = getContext().getResources().getDisplayMetrics().density;
		float[] dimensions = (display.getWidth() < display.getHeight()) ? DIMENSIONS_PORTRAIT
				: DIMENSIONS_LANDSCAPE;

		addContentView(mContent, new FrameLayout.LayoutParams(
				(int) (dimensions[0] * scale + 0.5f), (int) (dimensions[1]
						* scale + 0.5f)));
	
		
		CookieSyncManager.createInstance(getContext());
		CookieManager cookieManager = CookieManager.getInstance();
		//cookieManager.removeAllCookies(null);				nie dzia�a na starszych API
	}

	private void setUpTitle() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mTitle = new TextView(getContext());
		mTitle.setText("Instagram");
		mTitle.setTextColor(Color.WHITE);
		mTitle.setTypeface(Typeface.DEFAULT_BOLD);
		mTitle.setBackgroundColor(Color.BLACK);
		mTitle.setPadding(MARGIN + PADDING, MARGIN, MARGIN, MARGIN);
		mContent.addView(mTitle);
	}

	private void setUpWebView() {
		mWebView = new WebView(getContext());
		mWebView.setVerticalScrollBarEnabled(false);
		mWebView.setHorizontalScrollBarEnabled(false);
		mWebView.setWebViewClient(new OAuthWebViewClient());
		mWebView.getSettings().setJavaScriptEnabled(true);
		WebSettings settings =  mWebView.getSettings(); 
		settings.setDefaultTextEncodingName("iso-8859-2");
		mWebView.loadUrl(mUrl);
		//mWebView.loadDataWithBaseURL(mUrl, "text/html; charset=utf-8", "utf-8", null, null);
		mWebView.setLayoutParams(FILL);
		mContent.addView(mWebView);
	}

	private class OAuthWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.d(TAG, "Redirecting URL " + url);

			if (url.startsWith(InstagramApp.mCallbackUrl)) {
				String urls[] = url.split("=");
				mListener.onComplete(urls[1]);
				InstagramDialog.this.dismiss();
				return true;
			}
			return false;
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			Log.d(TAG, "Page error: " + description);

			super.onReceivedError(view, errorCode, description, failingUrl);
			mListener.onError(description);
			InstagramDialog.this.dismiss();
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			Log.d(TAG, "Loading URL: " + url);

			super.onPageStarted(view, url, favicon);
			mSpinner.show();
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			String title = mWebView.getTitle();
			if (title != null && title.length() > 0) {
				mTitle.setText(title);
			}
			Log.d(TAG, "onPageFinished URL: " + url);
			mSpinner.dismiss();
		}

	}

	public interface OAuthDialogListener {
		public abstract void onComplete(String accessToken);

		public abstract void onError(String error);
	}

}