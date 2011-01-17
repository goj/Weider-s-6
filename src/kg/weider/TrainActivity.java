package kg.weider;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class TrainActivity extends WeiderActivity implements OnClickListener {
	private static final int REPETITION_FINISHED = 0;
	private static final String SAVED_SERIES = "series";
	private static final String SAVED_EXERCISES = "exercises";
	private static final String SAVED_REPETITIONS = "repetitions";

	private final class FinishRepetition extends TimerTask {

		@Override
		public void run() {
			repetitionFinishedHandler.sendEmptyMessage(REPETITION_FINISHED);
		}
	}

	private View trainLayout;
	private ImageView exerciseImg;
	
	private final int[] exerciseResources = {
			R.drawable.a6w1,
			R.drawable.a6w2,
			R.drawable.a6w3,
			R.drawable.a6w4,
			R.drawable.a6w5,
			R.drawable.a6w6
	};
	private Timer repetitionTimer;
	private Handler repetitionFinishedHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			onRepetitionFinished();
		};
	};
	private MediaPlayer finishedMP, exerciseFinishedMP;
	
	private int dayNo;
	private int seriesNo;
	private int exerciseNo;
	private int repetitionNo;
	
	private TextView repetitionTV;
	private TextView exerciseTV;
	private TextView seriesTV;
	
	private boolean doingRepetition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.train);
		
		trainLayout = findViewById(R.id.TrainLayout);
		exerciseImg = (ImageView) findViewById(R.id.ExerciseImage);
		
		trainLayout.setOnClickListener(this);
		
		repetitionTimer = new Timer("repetition timer");
		finishedMP = MediaPlayer.create(this, R.raw.done);
		exerciseFinishedMP = MediaPlayer.create(this, R.raw.exercise_done);
		
		dayNo = getCurrentDay();
		if (savedInstanceState != null) {
			seriesNo = savedInstanceState.getInt(SAVED_SERIES, 0);
			exerciseNo = savedInstanceState.getInt(SAVED_EXERCISES, 0);
			repetitionNo = savedInstanceState.getInt(SAVED_REPETITIONS, 0);
		}
		
		repetitionTV = (TextView) findViewById(R.id.RepetitionTV);
		exerciseTV = (TextView) findViewById(R.id.ExerciseTV);
		seriesTV = (TextView) findViewById(R.id.SeriesTV);
		
		String dayCountFormatString = getString(R.string.day_count);
		TextView dayTV = (TextView) findViewById(R.id.TrainDayCount);
		dayTV.setText(String.format(dayCountFormatString, dayNo));
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateExerciseImage();
		updateRepetitionTV();
		updateExerciseTV();
		updateSeriesTV();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(SAVED_SERIES, seriesNo);
		outState.putInt(SAVED_EXERCISES, exerciseNo);
		outState.putInt(SAVED_REPETITIONS, repetitionNo);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.TrainLayout:
				if (!doingRepetition) {
					v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
					repetitionStarted();
				}
				break;
		}
	}

	private void repetitionStarted() {
		doingRepetition = true;
		repetitionTimer.schedule(new FinishRepetition(), repetitionLength());
	}

	private void onRepetitionFinished() {
		doingRepetition = false;
		if (++repetitionNo >= repetitionCount()) {
			exerciseFinishedMP.start();
			nextExercise();
		} else
			finishedMP.start();
		updateRepetitionTV();
	}

	private void nextExercise() {
		repetitionNo = 0;
		if (++exerciseNo >= 6)
			nextSeries();
		updateExerciseImage();
		updateExerciseTV();
	}

	private void updateExerciseImage() {
		exerciseImg.setImageResource(exerciseResources[exerciseNo]);
	}

	private void nextSeries() {
		exerciseNo = 0;
		if (++seriesNo >= seriesCount())
			trainingDone();
		updateSeriesTV();
	}

	private void trainingDone() {
		if (++dayNo <= 42)
			saveCurrentDay(dayNo);
		else // play congratulations
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=us3dQ0nnlHY")));
		finish();
	}

	private void updateSeriesTV() {
		seriesTV.setText(String.format(getString(R.string.series_no), seriesNo + 1, seriesCount()));
	}

	private void updateExerciseTV() {
		exerciseTV.setText(String.format(getString(R.string.exercise_no), exerciseNo + 1, 6));
		
	}

	private void updateRepetitionTV() {
		repetitionTV.setText(String.format(getString(R.string.repetition_no), repetitionNo + 1, repetitionCount()));
	}

	private long repetitionLength() {
		return exerciseNo == 4 ? 1000 : 3000;
	}

	private int repetitionCount() {
		int pairity = exerciseNo % 2 == 0 ? 2 : 1;
		int base = dayNo <= 6 ? 6 : (dayNo - 3) / 4 * 2 + 6;
		return base * pairity;
	}

	private int seriesCount() {
		switch (dayNo) {
		case 1: return 1;
		case 2: return 2;
		case 3: return 2;
		default:
			return 3;
		}
	}
	
}
