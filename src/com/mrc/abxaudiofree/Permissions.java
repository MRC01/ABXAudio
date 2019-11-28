/* Copyright 2012 by Michael R. Clements
 * This software is open source and free to distribute and create derivative works.
*/
package com.mrc.abxaudiofree;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

// This class handles Android permission requests
public class Permissions {

	public static final int		myPermissionCode = 1;

	/* Confirm that Android has granted this app the permissions it needs.
	 * If permission is already granted, this does nothing.
	 * Otherwise, it asks Android to prompt the user to grant the storage permission.
	 */
	public static void confirmPermissions(Activity self) {
		if(ContextCompat.checkSelfPermission(self, Manifest.permission.WRITE_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED) {
		    /* Permission not granted; request it!
		     * This returns immediately.
		     * When user responds to dialog, Android calls the activity callback with the code.
		     */
	        ActivityCompat.requestPermissions(
	        		self,
	                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
	                myPermissionCode);
		}
	}

	/* This is the callback that Android calls after user responds to the permission request dialog.
	 * If user denies storage permission, emit a Toast so he knows what it's for.
	 */
	public static void permissionCallback(Activity self, int rCode, String[] perms, int[] grants) {
		switch(rCode) {
		case myPermissionCode:
			if(grants != null && grants.length > 0) {
				if(grants[0] == PackageManager.PERMISSION_GRANTED) {
					// User granted permission; nothing else to do.
				}
				else {
					// User denied permission
					Toast.makeText(self, "Could not save config: storage permission not granted!", Toast.LENGTH_LONG).show();
				}
			}
			break;
		default:
			break;
		}
	}
}