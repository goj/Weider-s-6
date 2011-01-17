package kg.weider;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;

public class WeiderActivity extends Activity {
	
	private static final String PREFS_NAME = "kg.weider.preferences";
	private static final String CURRENT_DAY = "current_day";
	
	public int getCurrentDay() {
		return getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE).getInt(CURRENT_DAY, 1);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
	}

	public void saveCurrentDay(int day) {
		SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
		preferences.edit().putInt(CURRENT_DAY, day).commit();
	}
	
}
