package org.me.rsstrafficscotland;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity>{

	private MainActivity mMainActivity;
    private Button mBtnRoadworks, mBtnPlanned, mBtnCurrent;
	
	public MainActivityTest() {
		super(MainActivity.class);
	}
	
	@Override
    protected void setUp() throws Exception {
        super.setUp();
        mMainActivity = getActivity();
        mBtnRoadworks =
                (Button) mMainActivity
                .findViewById(R.id.btnRoadworks);
        mBtnCurrent =
                (Button) mMainActivity
                .findViewById(R.id.btnCurrent);
        mBtnPlanned =
                (Button) mMainActivity
                .findViewById(R.id.btnPlanned);
    }

	public void testPreconditions() {
	    assertNotNull("mMainActivity is null", mMainActivity);
	    assertNotNull("mBtnRoadworks is null", mBtnRoadworks);
	    assertNotNull("mBtnCurrent is null", mBtnCurrent);
	    assertNotNull("mBtnPlanned is null", mBtnPlanned);
	}
	
	public void testBtnCurrent_labelText() {
	    final String expected =
	            mMainActivity.getString(R.id.btnCurrent);
	    final String actual = mBtnCurrent.getText().toString();
	    assertEquals(expected, actual);
	}
	
	public void testBtnPlanned_labelText() {
	    final String expected =
	            mMainActivity.getString(R.id.btnPlanned);
	    final String actual = mBtnPlanned.getText().toString();
	    assertEquals(expected, actual);
	}
	
	public void testBtnRoadworks_labelText() {
	    final String expected =
	            mMainActivity.getString(R.id.btnRoadworks);
	    final String actual = mBtnRoadworks.getText().toString();
	    assertEquals(expected, actual);
	}
}
