package com.pl.slalom;
import android.app.*;
import android.os.*;
import com.pl.slalom.data.database.*;
import android.widget.*;
import android.database.*;
import android.view.*;

public class DatabaseActivity extends Activity
{
	SqlLiteDataLoader db;
	EditText etQuery;
	TextView tvResult;
	CheckBox cbReader;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.database);
		
		etQuery = (EditText)findViewById(R.id.db_sql);
		cbReader = (CheckBox)findViewById(R.id.db_cbQuery);
		tvResult = (TextView)findViewById(R.id.db_tvResult);
		
		db = new SqlLiteDataLoader(this);
	}
	
	public void queryExecute(View view){
		try{
			tvResult.setText("");
			
			if (cbReader.isChecked()){
				Cursor c = db.ExecuteQuery(etQuery.getText().toString());
				showCursorResults(c);
			} else{
				db.ExecuteNonQuery(etQuery.getText().toString());
				tvResult.setText("Complete");
			}
			
		} catch(Exception ex){
			tvResult.setText(ex.getMessage());			
		}
	}
	
	private void showCursorResults(Cursor c){
		StringBuilder sb = new StringBuilder();
		int cc = c.getColumnCount();
		for (int i = 0; i<cc; i++){
			sb.append(String.format("%1$20s\t\t", c.getColumnName(i)));
		}
		sb.append("\n");
		
		c.moveToFirst();
		do{
			for (int i = 0; i<cc; i++){
				sb.append(String.format("%1$20s\t\t", c.getString(i)));
			}
			sb.append("\n");
		} while (c.moveToNext());
		
		tvResult.setText(sb.toString());
	}
}
