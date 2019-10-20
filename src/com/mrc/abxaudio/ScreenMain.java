/* Copyright 2014 by Michael R. Clements
 * This source code is the intellectual property of the author.
 * This is not open source. All rights reserved.
*/
package com.mrc.abxaudio;

import android.os.*;
import android.app.*;
import android.util.Log;
import android.view.*;
import android.widget.Toast;
import android.content.*;

public class ScreenMain extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screenmain);
		try {
			Config.get().load();
		}
		catch(Exception e) {
			// Expected on first-time load (no prior config file)
			Log.i(this.getClass().getName(), "Could not load config", e);
		}
	}

	protected void onPause() {
		super.onPause();
		if(isFinishing()) {
			try {
				Config.get().save();
			}
			catch(Exception e) {
				// couldn't save config
				Log.i(this.getClass().getName(), "Could not save config", e);
			}
		}
	}

	public void butClipA(View pView) {
		Intent	i;
		i = new Intent(this, ScreenClip.class);
		// tell ScreenClip which clip to use
		i.putExtra("whichClip", Config.clipNameA);
		startActivity(i);
	}

	public void butClipB(View pView) {
		Intent	i;
		i = new Intent(this, ScreenClip.class);
		// tell ScreenClip which clip to use
		i.putExtra("whichClip", Config.clipNameB);
		startActivity(i);
	}

	public void butTest(View pView) {
		// The clips must be valid before we can start a test
		try {
			Config.get().clipA.init();
			Config.get().clipB.init();
			startActivity(new Intent(this, ScreenTest.class));
		}
		catch(Exception e) {
			Toast.makeText(this, "Invalid clips - can't start a test!", Toast.LENGTH_SHORT).show();
		}
	}

	public void butSetup(View pView) {
		startActivity(new Intent(this, ScreenSetup.class));
	}

	public void butInstruct(View pView) {
		startActivity(new Intent(this, ScreenInstruct.class));
	}
}
