/* Copyright 2014 by Michael R. Clements
 * This software is open source and free to distribute and create derivative works.
*/
package com.mrc.abxaudiofree;

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

		/* Ensure user has granted storage permissions to read/write my config
		 * If already granted, this does nothing.
		 * If not granted, this does not block, so the config won't load.
		*/
		Permissions.confirmPermissions(this);
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
				String s1 = e.getMessage();
				Toast.makeText(this, "Could not save config: " + s1, Toast.LENGTH_SHORT).show();
				Log.e(this.getClass().getName(), "Could not save config", e);
			}
		}
	}

	@Override
	// Android calls this when this app requests permissions.
	public void onRequestPermissionsResult(int rCode, String[] perms, int[] grants) {
		Permissions.permissionCallback(this, rCode, perms, grants);
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
