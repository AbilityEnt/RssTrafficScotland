package org.me.rsstrafficscotland;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.MenuItem;

public class BaseActivity extends Activity{
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@SuppressLint("SimpleDateFormat")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
				
			case R.id.menu_about:
				new AlertDialog.Builder(this)
				.setTitle("Info")
				.setMessage(
						"This is the prototype for the future app completed by Harpreet Grewal)")
				.setNeutralButton("OK", new DialogInterface.OnClickListener() {
					// sets a button which will display information about the
					// app on the home screen

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				}).show();
		}

		return true;
	}
	
	
	
}
