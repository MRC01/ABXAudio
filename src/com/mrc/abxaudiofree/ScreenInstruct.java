/* Copyright 2014 by Michael R. Clements
 * This software is open source and free to distribute and create derivative works.
*/
package com.mrc.abxaudiofree;

import android.os.*;
import android.text.*;
import android.widget.*;
import android.app.*;

public class ScreenInstruct extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		TextView	tv;

		super.onCreate(savedInstanceState);
		setContentView(R.layout.screeninstruct);
        tv = (TextView)findViewById(R.id.tv_instruct);
        tv.setText(Html.fromHtml(getString(R.string.instruct)));
	}
}
