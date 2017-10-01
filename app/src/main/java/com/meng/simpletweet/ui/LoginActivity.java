package com.meng.simpletweet.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.codepath.oauth.OAuthLoginActionBarActivity;
import com.meng.simpletweet.R;
import com.meng.simpletweet.net.TweetClient;

public class LoginActivity extends OAuthLoginActionBarActivity<TweetClient> {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		getClient().clearAccessToken();
	}

	// Inflate the menu; this adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.home_menu, menu);
		return true;
	}

	// OAuth authenticated successfully, launch primary authenticated activity
	// i.e Display application "homepage"
	@Override
	public void onLoginSuccess() {
		Intent i = new Intent(this, TimeLineActivity.class);
		startActivity(i);
		Toast.makeText(this, "Success", Toast.LENGTH_LONG).show();
	}

	// OAuth authentication flow failed, handle the error
	// i.e Display an error dialog or toast
	@Override
	public void onLoginFailure(Exception e) {
		Toast.makeText(this, "Failure", Toast.LENGTH_LONG).show();
		e.printStackTrace();
	}

	// Click handler method for the button used to start OAuth flow
	// Uses the client to initiate OAuth authorization
	// This should be tied to a button used to home_menu
	public void loginToRest(View view) {
		getClient().connect();
	}

}
