/* Copyright 2014 by Michael R. Clements
 * This source code is the intellectual property of the author.
 * This is not open source. All rights reserved.
*/
package com.mrc.abxaudio;

import java.io.*;

import com.mrc.util.*;

/* Everything we need to know about an audio clip
*/
public class AudioClip {
	String		fName;
	float		vLevel;
	SoundLong	sound;

	public AudioClip() {
		fName = Config.get().clipDir;
		vLevel = 1F;
		sound = null;
	}
	
	// You must call init() before playing the sound.
	// This call is idempotent
	public void init() throws IOException {
		if(sound == null)
			sound = new SoundLong(fName, vLevel, true);
	}

	public boolean isReady() {
		return (sound != null);
	}

	// You should call close() when done with a sound to release its resources
	public void close() {
		if(sound != null) {
			sound.close();
			sound = null;
		}
	}

	public static AudioClip read(BufferedReader inStream) throws IOException {
		AudioClip	rc = new AudioClip();
		rc.fName = Util.readString(inStream);
		rc.vLevel = (float)Util.readDouble(inStream);
		return rc;
	}

	public void write(BufferedWriter outStream) throws IOException {
		Util.writeString(outStream,  fName);
		Util.writeDouble(outStream, vLevel);
	}
}
