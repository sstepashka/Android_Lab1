package com.example.lab_1;

import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class EditNoteActivity extends Activity {

	private EditText editText;
	private DataBaseHelper dataBaseHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_note);
		editText = (EditText) findViewById(R.id.editText1);
		
		editText.post(new Runnable() {
            @Override
            public void run() {
            	dataBaseHelper = new DataBaseHelper(EditNoteActivity.this);
            	dataBaseHelper.open();
                    update();
            }
		});
	}
	
	private void update() {
		Bundle extras = getIntent().getExtras();
		
		if (extras != null) {
			long id = getIntent().getExtras().getLong("id");
			Cursor cursor = dataBaseHelper.getById(id);
			startManagingCursor(cursor);
			cursor.moveToFirst();
			
			@SuppressWarnings("static-access")
			String text = cursor.getString(cursor.getColumnIndex(dataBaseHelper.KEY_NAME));
			editText.setText(text);
		} else {
			editText.setText("");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_note, menu);
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.done:
	        	SaveOrCreateNote();
	            return true;
	        case R.id.cancel:
	        	CancelEditNote();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void SaveOrCreateNote() {
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			long id = getIntent().getExtras().getLong("id");
			dataBaseHelper.updateRecord(id, editText.getText().toString());
		} else {
			DataBaseHelper.createRecord(editText.getText().toString());
		}
		setResult(RESULT_OK);
		finish();
	}
	
	private void CancelEditNote() { 
		setResult(RESULT_CANCELED);
		finish();
	}

}
