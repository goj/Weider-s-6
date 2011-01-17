package kg.weider;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
public class SetDayActivity extends WeiderActivity implements OnClickListener, OnSeekBarChangeListener {
	
	private SeekBar currentDaySB;
	private TextView currentDaySBTV;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_day);
		
		int currentDay = getCurrentDay();
		
		currentDaySB = (SeekBar) findViewById(R.id.CurrentDaySeekBar);
		currentDaySBTV = (TextView) findViewById(R.id.CurrentDaySBTV);
		
		currentDaySB.setProgress(currentDay - 1);
		currentDaySB.setOnSeekBarChangeListener(this);
		
		findViewById(R.id.day_ok).setOnClickListener(this);
		findViewById(R.id.day_cancel).setOnClickListener(this);
		
		setCurrentDayOnTV(currentDay);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.day_ok:
				saveCurrentDay();
				// break omited
			case R.id.day_cancel:
				finish();
				break;
		}
	}

	private void saveCurrentDay() {
		saveCurrentDay(currentDaySB.getProgress() + 1);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		setCurrentDayOnTV(progress + 1);
	}

	private void setCurrentDayOnTV(int progress) {
		currentDaySBTV.setText(String.format(getString(R.string.day_count), progress));
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) { }

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) { }
	
}
