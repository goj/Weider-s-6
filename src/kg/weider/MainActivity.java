package kg.weider;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.TextView;

public class MainActivity extends WeiderActivity implements OnLongClickListener {
	private static final int EDIT_DAY_COUNT_ID = 0;
	private String dayCountFormatString;
	private TextView dayCountTV;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        dayCountTV = (TextView) findViewById(R.id.day_count_tv);
        dayCountFormatString = getString(R.string.day_count);
		dayCountTV.setOnLongClickListener(this);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
		dayCountTV.setText(String.format(dayCountFormatString, getCurrentDay()));
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	MenuItem editDayCount = menu.add(0, EDIT_DAY_COUNT_ID, 1, R.string.edit_current_day);
    	editDayCount.setIcon(android.R.drawable.ic_menu_month);
    	return true;
    }
    
	@Override
	public boolean onLongClick(View target) {
		switch (target.getId()) {
		case R.id.day_count_tv:
			startActivity(new Intent(this, SetDayActivity.class));
			return true;
		}
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case EDIT_DAY_COUNT_ID:
			startActivity(new Intent(this, SetDayActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void trainButtonClicked(View view) {
			startActivity(new Intent(this, TrainActivity.class));
	}
	
}