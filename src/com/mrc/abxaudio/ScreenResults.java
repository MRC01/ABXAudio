/* Copyright 2014 by Michael R. Clements
 * This source code is the intellectual property of the author.
 * This is not open source. All rights reserved.
*/
package com.mrc.abxaudio;

import android.os.*;
import android.view.*;
import android.widget.*;
import android.app.*;

public class ScreenResults extends Activity {
	TextView	tv_resultstrials, tv_resultsptile;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screenresults);
		tv_resultstrials = (TextView)findViewById(R.id.tv_resultstrials);
		tv_resultsptile = (TextView)findViewById(R.id.tv_resultsptile);
	}

	protected void onResume() {
		super.onResume();
		setGui();
	}

	// user pressed 'back' - tell the test to continue
    public void onBackPressed() {
		setResult(Activity.RESULT_OK);
		super.onBackPressed();
    }

	// user pressed 'quit' - tell the test to quit
	public void butQuit(View pView) {
		setResult(Activity.RESULT_CANCELED);
		finish();
	}

	// set the GUI from my values
	void setGui() {
		int			trialsComplete, trialsCorrect;
		double		ptile;
		String		msg;
		AbxTest		test = AbxTest.get();

		trialsComplete = test.getTrialsCompleted();
		trialsCorrect = test.getTrialsCorrect();
		msg = trialsCorrect + " for " + trialsComplete;
		tv_resultstrials.setText(msg);

		ptile = test.getPercentile();
		msg = String.format("%.1f", (ptile * 100.0));
		tv_resultsptile.setText(msg);
	}
}
