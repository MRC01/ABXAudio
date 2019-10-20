/* Copyright 2014 by Michael R. Clements
 * This source code is the intellectual property of the author.
 * This is not open source. All rights reserved.
*/
package com.mrc.abxaudio;

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
