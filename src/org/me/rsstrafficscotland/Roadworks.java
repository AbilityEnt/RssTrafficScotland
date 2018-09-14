package org.me.rsstrafficscotland;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class Roadworks extends BaseActivity implements OnItemClickListener {

	String DATEKEY1 = "FilterDate1";
	String sdate = "Set Date";
	SharedPreferences datePref1;
	private ListView mRssListView;
	private XmlFeedParser mNewsFeeder;
	private List<RSSFeed> mRssFeedList;
	private RssAdapter mRssAdap;
	Activity activity;
	private static final String ROADWORK = "http://www.trafficscotland.org/rss/feeds/roadworks.aspx";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// creates the saved instance
		super.onCreate(savedInstanceState);
		// sets layout to "Roadworks"
		setContentView(R.layout.roadworks);
		activity = this;
		// gets id of Planned Roadwork list view
		mRssListView = (ListView) findViewById(R.id.lvRoadworks);
		// creates new array list of RssFeed
		mRssFeedList = new ArrayList<RSSFeed>();
		// executes class inside "Planned Road work"
		new DoRssFeedTask().execute(ROADWORK);
		// sets onClick Listener to the called view
		mRssListView.setOnItemClickListener(this);

		datePref1 = this.getSharedPreferences(DATEKEY1, Context.MODE_PRIVATE);
	}

	private class RssAdapter extends ArrayAdapter<RSSFeed> {
		private List<RSSFeed> rssFeedLst;

		public RssAdapter(Context context, int textViewResourceId,
				List<RSSFeed> rssFeedLst) {
			super(context, textViewResourceId, rssFeedLst);
			this.rssFeedLst = rssFeedLst;
		}

		@SuppressLint("SimpleDateFormat")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			// sets list view
			View view = convertView;
			RssHolder rssHolder = null;
			if (convertView == null) {
				// if view is null then creates new list view
				view = View.inflate(activity, R.layout.listview_row, null);
				// creates new RssFeed view Holder
				rssHolder = new RssHolder();
				// sets the title
				rssHolder.rssTitleView = (TextView) view
				// gets the id of the XML Item
						.findViewById(R.id.tvlistTitle);
				rssHolder.rssDateView = (TextView) view
						.findViewById(R.id.tvlistDate);
				view.setTag(rssHolder);
			} else {
				rssHolder = (RssHolder) view.getTag();
			}

			// gets position of RssFeed
			RSSFeed rssFeed = rssFeedLst.get(position);
			// sets title of from RssFeed
			rssHolder.rssTitleView.setText(rssFeed.getTitle());
			SimpleDateFormat newDate = new SimpleDateFormat("d/M/yyyy");
			String saD = newDate.format(System.currentTimeMillis());
			// gets description from planned road works and splits the
			// description
			String[] disStrings = rssFeed.getDescription()
			// splits it into the array
					.split("<br />");
			String[] startDate = disStrings[0].split(": ");
			String[] endDate = disStrings[1].split(": ");
			
			rssHolder.rssDateView.setText(Utility.FormatDate(startDate[1]) + " - " + Utility.FormatDate(endDate[1]));
			
			Long sd = Utility.dateToTimestamp(Utility.FormatDate(startDate[1]));
			Long ed = Utility.dateToTimestamp(Utility.FormatDate(endDate[1]));
			Long psd = Utility.dateToTimestamp(datePref1.getString(DATEKEY1,
					saD));

			if (psd >= sd && ed >= psd) {
				view.setBackgroundColor(Color.parseColor("#74FF74"));
			} else if (psd + (24 * 60 * 60) >= sd && ed >= psd + (24 * 60 * 60)
					|| psd - (24 * 60 * 60) >= sd && ed >= psd - (24 * 60 * 60)) {
				view.setBackgroundColor(Color.parseColor("#F7FF74"));
			} else {
				view.setBackgroundColor(Color.parseColor("#FF7474"));
			}

			// returns completed view
			return view;
		}
	}

	static class RssHolder {
		public TextView rssTitleView;
		public TextView rssDateView;
	}

	public class DoRssFeedTask extends AsyncTask<String, Void, List<RSSFeed>> {
		ProgressDialog prog;
		String jsonStr = null;
		Handler innerHandler;

		@Override
		protected void onPreExecute() {
			prog = new ProgressDialog(activity);
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
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					mRssAdap = new RssAdapter(activity, R.layout.listview_row,
							mRssFeedList);
					int count = mRssAdap.getCount();
					// runs the RSSAdapter which opens up list view
					if (count != 0 && mRssAdap != null) {
						mRssListView.setAdapter(mRssAdap);
					}
				}
			});
		}

		protected void onProgressUpdate(Void... values) {
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View v, int pos, long id) {
		// creates the dialog builder
		final Builder alertDialog = new AlertDialog.Builder(activity);
		// creates inflater
		LayoutInflater inflater = LayoutInflater.from(activity);
		// this is the dialogue view on the layout
		View dialog_layout = inflater.inflate(R.layout.traffic_dialog, null);

		// creates text view for start date
		TextView tvMessage = (TextView) dialog_layout.findViewById(R.id.tvMessage);

		// gets description from planned road works and splits the description
		String[] disStrings = mRssFeedList.get(pos).getDescription()
		// splits it into the array
				.split("<br />");

		alertDialog.setNeutralButton("OK", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

			}
		});
		// gets title from rssFeed and inserts it into Dialogue box
		alertDialog.setTitle(mRssFeedList.get(pos).getTitle());
		if (disStrings.length > 2) {
			if (disStrings[2].contains("<br/>") == true) {
				String[] disS = disStrings[2].split("<br/>");
				String dString = "";
				for (int i = 0; i < disS.length; i++) {
					dString = dString + disS[i] + "\n";
				}
				tvMessage.setText(dString);
			} else {
				alertDialog.setMessage("No description to display.");
				tvMessage.setVisibility(View.GONE);
			}
		} else {
			alertDialog.setMessage("No description to display.");
			tvMessage.setVisibility(View.GONE);
		}
		// sets view to dialogue layout
		alertDialog.setView(dialog_layout);
		// Create dialogue box
		alertDialog.create();
		// shows dialogue
		alertDialog.show();
	}

	public void clickHome(View v) {
		// once home button is clicked it will start a new activity and return
		// to main page
		startActivity(new Intent(this,
				org.me.rsstrafficscotland.MainActivity.class));
		finish();
	}
}
