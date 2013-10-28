package com.example.lab_1;

import android.os.Bundle;

import com.example.lab_1.DataBaseHelper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


public class MainActivity extends ListActivity {

	private DataBaseHelper dataBaseHelper;
	private ListView list;
	private CursorAdapter notes;
	private EditText searchText;
	AlertDialog.Builder ad;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		
		list = getListView();
		
		searchText = (EditText) findViewById(R.id.search_text);
		
		searchText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                            int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                            int count) {
                    updateList(searchText.getText().toString());
            }
		});
        
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
		    public boolean onItemLongClick(AdapterView<?> parent, View v, int position,long id)
		    {
				
				final long item = getListAdapter().getItemId(position);
		    	
		    	String title = "Confirm";
		        String message = "Delete?";
		        String button1String = "YES";
		        String button2String = "NO";
		        
		        ad = new AlertDialog.Builder(MainActivity.this);
		        ad.setTitle(title);
		        ad.setMessage(message);
		        ad.setPositiveButton(button1String, new OnClickListener() {
		            public void onClick(DialogInterface dialog, int arg1) {
		            	dataBaseHelper.removeItem(item);
				    	
				    	updateList(null);
		            }
		        });
		        ad.setNegativeButton(button2String, new OnClickListener() {
		            public void onClick(DialogInterface dialog, int arg1) {
		            	
		            }
		        });
		        ad.setCancelable(true);
		        
		        ad.show();
		    	
		    	
				return true;

		    }
		});

		list.post(new Runnable() {
            @Override
            public void run() {
            	dataBaseHelper = new DataBaseHelper(MainActivity.this);
            	dataBaseHelper.open();
                    updateList(null);
            }
		});
	}
	
	private void updateList(String query) {
		Cursor c = null;
		
        if (query != null && query.length() > 0) {
        	c = dataBaseHelper.fetchRecordsByQuery(query);
        } else {
        	c = dataBaseHelper.fetchAll();
        }
        
        startManagingCursor(c);
        String[] from = new String[] { DataBaseHelper.KEY_NAME };
        int[] to = new int[] { android.R.id.text1 };
        notes = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, c, from, to);
        setListAdapter(notes);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {		
		long item = getListAdapter().getItemId(position);
		
		Intent intent = new Intent(this, EditNoteActivity.class);
		intent.putExtra("id", item);
		startActivityForResult(intent, 1);
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity_menu, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.item1:
	        	ShowAddRecord();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void ShowAddRecord() {
		Intent intent = new Intent(this, EditNoteActivity.class);
		startActivityForResult(intent, 1);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			updateList(null);
		}
	}

}
