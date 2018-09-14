package org.me.rsstrafficscotland;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity {

	static Activity activity;
	static Context context;
	String DATEKEY1 = "FilterDate1";
	String sdate = "Set Date";
	static SharedPreferences datePref1;
	private List<RSSFeed> mRssFeedList; // Instantiates a RssFeed
	private XmlFeedParser mNewsFeeder; // Instantiates a XML FeedParser
	private static final String CURRENT = "http://www.trafficscotland.org/rss/feeds/currentincidents.aspx";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		activity = this;
		context = this.getApplicationContext();
		datePref1 = this.getSharedPreferences(DATEKEY1, Context.MODE_PRIVATE);
	}

	public void clickRoadworks(View v) {
		// after the road works button has been clicked , this will load up the
		// roadworks class as seen in the code
		startActivity(new Intent(this,
				org.me.rsstrafficscotland.Roadworks.class));
	}

	public void clickPlanned(View v) {
		startActivity(new Intent(this,
				org.me.rsstrafficscotland.PlannedRoadworks.class));
	}

	public void clickCurrent(View v) {
		// creates new array list of RssFeed
		mRssFeedList = new ArrayList<RSSFeed>();
		// executes class inside "Planned Road work"
		new DoRssFeedTask().execute(CURRENT);
		final Builder alertDialog = new AlertDialog.Builder(this);
		// Instantiates a layout XML file which is then passed onto the dialog
		// box layout
		LayoutInflater inflater = LayoutInflater.from(this);
		View dialog_layout = inflater.inflate(R.layout.traffic_dialog, null);
		// implements text view to show the startDate on the layout
		TextView tvMessage = (TextView) dialog_layout.findViewById(R.id.tvMessage);

		if (mRssFeedList.size() > 0) {
			String[] desc = mRssFeedList.get(0).getDescription()
					.split("<br />");
			String[] sDate = desc[0].split(": ");
			String[] eDate = desc[1].split(": ");
			Long sd = Utility.dateToTimestamp(Utility.FormatDate(sDate[1]));
			Long ed = Utility.dateToTimestamp(Utility.FormatDate(eDate[1]));

			tvMessage.setText("Start Date: " + sd + "\nEnd Date: " + ed);
			alertDialog.setTitle(mRssFeedList.get(0).getTitle());
			alertDialog.setMessage("Current Roadwork in Scotland"); // sets message
		} else {
			tvMessage.setVisibility(View.GONE);
			alertDialog.setTitle("Current Roadwork"); // sets title
			alertDialog.setMessage("There are no current roadworks to display at this time."); // sets message
		}

		// an ok button is implemented because the app sometimes crashed if i
		// tried to click anywhere else to exit
		alertDialog.setNeutralButton("OK", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});

		alertDialog.setView(dialog_layout); // sets dialogue view
		alertDialog.create(); // creates dialogue
		alertDialog.show(); // shows the dialogue view
	}
	
	public void clickExit(View v) {
		finish();
	}

	public class DoRssFeedTask extends AsyncTask<String, Void, List<RSSFeed>> {
		ProgressDialog prog;

		@Override
		protected void onPreExecute() {
			prog = new ProgressDialog(activity);
			// sets the loading message
			prog.setMessage("Loading....");
			prog.show();
		}

		@Override
		protected List<RSSFeed> doInBackground(String... params) {
			for (String urlVal : params) {
				// opens the url in the background
				mNewsFeeder = new XmlFeedParser(urlVal);
			}
			mRssFeedList = mNewsFeeder.parse();
			return mRssFeedList;
		}

		@Override
		protected void onPostExecute(List<RSSFeed> result) {
			prog.dismiss();
		}

		protected void onProgressUpdate(Void... values) {
		}
	}

	@SuppressLint("SimpleDateFormat")
	public void clickFilter(View v){
		SimpleDateFormat newDate = new SimpleDateFormat("d/M/yyyy");
		String saD = newDate.format(System.currentTimeMillis());
		String title = "Filter Dates";
		String message = "Please select a date to filter.";
		// creates the dialog builder
		final AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				this);
		// creates inflater
		LayoutInflater inflater = LayoutInflater.from(this);
		// this is the dialogue view on the layout
		View dialog_layout = inflater.inflate(R.layout.search_dialog, null);

		alertDialog.setNeutralButton("Cancel", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

			}
		});

		alertDialog.setPositiveButton("Filter", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});

		// gets title from rssFeed and inserts it into Dialogue box
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);

		final Button btnDate1 = (Button) dialog_layout
				.findViewById(R.id.btnDate1);
		final DatePicker dpFilter = (DatePicker) dialog_layout
				.findViewById(R.id.dpFilter);

		if (datePref1.equals(null) == false) {
			btnDate1.setText(datePref1.getString(DATEKEY1, saD));
		}
		
		btnDate1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				datePicker(activity, btnDate1, dpFilter);
			}
		});

		// sets view to dialogue layout
		alertDialog.setView(dialog_layout);
		final AlertDialog dialog = alertDialog.create();
		dialog.show();

		dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (btnDate1.equals(sdate) == false) {
							datePref1.edit().putString(DATEKEY1,
									btnDate1.getText().toString()).commit();
							dialog.dismiss();
						} else {
							Toast.makeText(context, "Enter a date, or click cancel", Toast.LENGTH_LONG).show();
						}

						
					}
				});
	}
	
	private void datePicker(Context context, final Button dateBtn,
			final DatePicker datePickerResult) {
		Calendar date = Calendar.getInstance();
		if (dateBtn.getText().equals("Set Date") == true) {
			new DatePickerDialog(context,
					new DatePickerDialog.OnDateSetListener() {

						public void onDateSet(DatePicker view,
								int selectedYear, int selectedMonth,
								int selectedDay) {
							int year = selectedYear;
							int month = selectedMonth;
							int day = selectedDay;

							dateBtn.setText(new StringBuilder().append(day)
									.append("/").append(month + 1).append("/")
									.append(year));

							datePickerResult.init(year, month, day, null);

						}
					}, date.get(Calendar.YEAR), date.get(Calendar.MONTH),
					date.get(Calendar.DAY_OF_MONTH)).show();
		} else {
			String[] datefbtn = dateBtn.getText().toString().split("/");
			int dyear = Integer.parseInt(datefbtn[2]);
			int dday = Integer.parseInt(datefbtn[0]);
			int dmonth = Integer.parseInt(datefbtn[1]);
			date.set(Calendar.YEAR, dyear);
			date.set(Calendar.MONTH, dmonth - 1);
			date.set(Calendar.DAY_OF_MONTH, dday);
			new DatePickerDialog(context,
					new DatePickerDialog.OnDateSetListener() {

						public void onDateSet(DatePicker view,
								int selectedYear, int selectedMonth,
								int selectedDay) {
							int year = selectedYear;
							int month = selectedMonth;
							int day = selectedDay;

							dateBtn.setText(new StringBuilder().append(day)
									.append("/").append(month + 1).append("/")
									.append(year));

							datePickerResult.init(year, month, day, null);

						}
					}, date.get(Calendar.YEAR), date.get(Calendar.MONTH),
					date.get(Calendar.DAY_OF_MONTH)).show();
		}
	}
}
