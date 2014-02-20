package com.pl.slalom.data.database;
import com.pl.slalom.data.*;
import android.database.sqlite.*;
import android.content.*;
import android.database.*;
import android.widget.*;
import java.util.*;

public class SqlLiteDataLoader extends SQLiteOpenHelper implements IDataLoader
{
	private Context context;
	public SqlLiteDataLoader(Context context)
	{
		super(context, "slalom_db", null, 1);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		Toast.makeText(context, "Creating database", Toast.LENGTH_SHORT).show();
		runScripts(db, 0, scripts.length - 1);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldver, int newver)
	{
		Toast.makeText(context, "Upgrading database", Toast.LENGTH_SHORT).show();
		// TODO: Implement this method
	}
	
	private void runScripts(SQLiteDatabase db, int fromN, int toN){
		for (int i = fromN; i<=toN; i++){
			db.execSQL(scripts[i]);
		}
	}

	private String[] scripts = new String[]{
		//0
		"CREATE TABLE player (" +
		"id INTEGER PRIMARY KEY AUTOINCREMENT," +
		"name TEXT," +
		"money INTEGER," +
		"experience INTEGER" +
		")",
		//1
		"INSERT INTO player(name, money, experience) values ('', 0, 0)",
		//2
		"CREATE TABLE ski_availability (" +
		"id INTEGER PRIMARY KEY AUTOINCREMENT," +
		"skiId INTEGER," +
		"playerId Integer" +
		")",
		//3
		"INSERT INTO ski_availability(skiId, playerId)" +
		" select 10, p.id from player p",
		//4
		"CREATE TABLE track_availability (" +
		"id INTEGER PRIMARY KEY AUTOINCREMENT," +
		"trackId INTEGER," +
		"playerId Integer" +
		")",
		//5
		"INSERT INTO track_availability(trackId, playerId)" +
		" select 3, p.id from player p"
	};

	@Override
	public Data loadData()
	{
		Toast.makeText(context, "Loading data", Toast.LENGTH_SHORT).show();
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("Select id, name, money, experience from player", null);
		if (cursor.moveToFirst()){
			Data data = new Data();
			data.id = cursor.getInt(0);
			data.name = cursor.getString(1);
			data.money = cursor.getInt(2);
			data.experience = cursor.getInt(3);
			cursor.close();
			data.availableSkiIds = getAvailableSkis(db, data.id);
			data.availableTrackIds = getAvailableSlopes(db, data.id);
			return data;
		}
		
		return null;
	}
	
	private List<Integer> getAvailableSkis(SQLiteDatabase db, int playerId){
		List<Integer> result = new LinkedList<Integer>();
		
		Cursor cursor = db.rawQuery("Select skiId from ski_availability where playerId = " + playerId, null);
		if (cursor.moveToFirst()){
			do{
				result.add(cursor.getInt(0));
			} while (cursor.moveToNext());
		}
		
		return result;
	}

	private List<Integer> getAvailableSlopes(SQLiteDatabase db, int playerId){
		List<Integer> result = new LinkedList<Integer>();

		Cursor cursor = db.rawQuery("Select trackId from track_availability where playerId = " + playerId, null);
		if (cursor.moveToFirst()){
			do{
				result.add(cursor.getInt(0));
			} while (cursor.moveToNext());
		}

		return result;
	}
	
	@Override
	public void storeData(Data data)
	{
		// TODO: Implement this method
	}
}
