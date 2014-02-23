package com.pl.slalom.data.database;
import com.pl.slalom.data.*;
import android.database.sqlite.*;
import android.content.*;
import android.database.*;
import android.widget.*;
import java.util.*;
import com.pl.slalom.data.race.*;

public class SqlLiteDataLoader extends SQLiteOpenHelper implements IDataLoader
{
	private static final int DB_VERSION = 4;
	private Context context;
	public SqlLiteDataLoader(Context context)
	{
		super(context, "slalom_db", null, DB_VERSION);
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
		int firstS = 0;
		switch (oldver){
			case 1: firstS = 6; break;
			case 2: firstS = 10; break;
			case 3: firstS = 11; break;
			case 4: firstS = 13; break;
		}
		
		int lastS = 0;
		switch (newver){
			case 1: lastS = 5; break;
			case 2: lastS = 9; break;
			case 3: lastS = 10; break;
			case 4: lastS = 12; break;
		}	
		Toast.makeText(context, "Upgrading database", Toast.LENGTH_SHORT).show();
		runScripts(db, firstS, lastS);
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
		"experience INTEGER)",
		//1
		"INSERT INTO player(name, money, experience) values ('', 0, 0)",
		//2
		"CREATE TABLE ski_availability (" +
		"id INTEGER PRIMARY KEY AUTOINCREMENT," +
		"skiId INTEGER," +
		"playerId Integer)",
		//3
		"INSERT INTO ski_availability(skiId, playerId)" +
		" select 10, p.id from player p",
		//4
		"CREATE TABLE track_availability (" +
		"id INTEGER PRIMARY KEY AUTOINCREMENT," +
		"trackId INTEGER," +
		"playerId Integer)",
		//5
		"INSERT INTO track_availability(trackId, playerId)" +
		" select 3, p.id from player p",
		//v2
		//6
		"CREATE TABLE competition (" +
		"id INTEGER PRIMARY KEY AUTOINCREMENT," +
		"playerId INTEGER)",
		//7
		"CREATE TABLE race (" +
		"id INTEGER PRIMARY KEY AUTOINCREMENT," +
		"competitionId INTEGER," +
		"trackId INTEGER," +
		"orderNum INTEGER," +
		"runCount INTEGER)",
		//8
		"CREATE TABLE competitor (" +
		"id INTEGER PRIMARY KEY AUTOINCREMENT," +
		"competitionId INTEGER," +
		"name TEXT," +
		"countryId INTEGER," +
		"skiId INTEGER," +
		"aiId INTEGER," +
		"points INTEGER)",
		//9
		"CREATE TABLE run (" +
		"id INTEGER PRIMARY KEY AUTOINCREMENT," +
		"competitorId INTEGER," +
		"raceId INTEGER," +
		"runNumber INTEGER," +
		"turns INTEGER," +
		"time DECIMAL," +
		"status INTEGER)",
		//v3
		//10
		"ALTER TABLE competition ADD COLUMN currentRace INTEGER",
		//v4
		//11
		"DELETE FROM track_availability where trackId = 3",
		//12
		"INSERT INTO track_availability(trackId, playerId)" +
		" select 10, p.id from player p"
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
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put("name", data.name);
		values.put("money", data.money);
		values.put("experience", data.experience);
		db.update("player", values, "id = ?", 
			new String[] {String.valueOf(data.id)});
		
		db.close();
	}
	
	private void storeAvailableSkis(SQLiteDatabase db){
		
	}
	
	private void storeAvailableSlopes(SQLiteDatabase db){

	}
	
	public void deleteCompetition(Competition competition){
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.execSQL("delete from run where competitorId in (select id from competitor where competitionId = " + competition.id + ")");
		db.execSQL("delete from race where competitionId = " + competition.id);
		db.execSQL("delete from competitor where competitionId = " + competition.id);
		db.execSQL("delete from competition where id = " + competition.id);
		
		db.close();
	}

	public void insertCompetition(long playerId, Competition competition){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues v = new ContentValues();
		v.put("playerId", playerId);
		v.put("currentRace", competition.currentRace);
		competition.id = db.insert("competition", null, v);
		
		for (int i = 0; i < competition.competitors.size(); i++){
			Competitor c = competition.competitors.get(i);
			v = new ContentValues();
			v.put("competitionId", competition.id);
			v.put("name", c.name);
			v.put("countryId", c.countryId);
			v.put("skiId", c.skiId);
			v.put("aiId", c.ai_id);
			v.put("points", c.points);
			c.id = db.insert("competitor", null, v);
		}
		
		for (int i = 0; i < competition.races.size(); i++){
			Race r = competition.races.get(i);
			v = new ContentValues();
			v.put("competitionId", competition.id);
			v.put("orderNum", r.order);
			v.put("trackId", r.trackId);
			v.put("runCount", r.runCount);
			r.id = db.insert("race", null, v);
		

			for (int ic = 0; ic < competition.competitors.size(); ic++){
				for (int rc = 0; rc < r.runCount; rc++){
					RaceRun run = r.playerRuns[ic][rc];
					v = new ContentValues();
					v.put("competitorId", competition.competitors.get(ic).id);
					v.put("raceId", r.id);
					v.put("runNumber", rc);
					db.insert("run", null, v);
				}
			}
		}
	}
	
	public void updateCompetition(Competition competition){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues v;

		for (int ir = 0; ir < competition.races.size(); ir++){
			Race r = competition.races.get(ir);
			for (int ic = 0; ic < competition.competitors.size(); ic++){
				for (int irc = 0; irc < r.runCount; irc++){
					RaceRun run = r.playerRuns[ic][irc];
					v = new ContentValues();
					v.put("status", getIntStatus(run.runResult.status));
					v.put("turns", run.runResult.turns);
					v.put("time", run.runResult.time);
					db.update("run", v, "id = ?",
						new String[] {String.valueOf(run.id)});
				}
			}
		}

		db.close();		
	}
	
	private int getIntStatus(RunStatus status){
		switch (status){
			case NotStarted: return 0;
			case Finished: return 10;
			case Failed: return 20;
			default:
				return -1;
		}
	}

	private RunStatus getStatus(int code){
		switch (code){
			case 0: return RunStatus.NotStarted;
			case 10: return RunStatus.Finished;
			case 20: return RunStatus.Failed;
			default:
				return RunStatus.NotStarted;
		}
	}
	
	public Competition getCompetition(long playerId){
		
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.rawQuery("Select id, currentRace from competition where playerId = " + playerId, null);
		if (!cursor.moveToFirst())
			return null;
		
		Competition c = new Competition();
		c.id = cursor.getInt(0);
		c.currentRace = cursor.getInt(1);
		cursor.close();
		
		c.competitors = new LinkedList<Competitor>();
		cursor = db.rawQuery("select id, name, skiId, countryId, aiId, points from competitor where competitionId = " + c.id, null);
		if (cursor.moveToFirst()){
			do{
				long id = cursor.getInt(0);
				Competitor pl = new Competitor(
					cursor.getString(1),
					cursor.getInt(2),
					cursor.getInt(3),
					cursor.getInt(4),
					cursor.getInt(5));
				pl.id = id;
				c.competitors.add(pl);
			} while (cursor.moveToNext());
		}
		cursor.close();

		c.races = new LinkedList<Race>();
		cursor = db.rawQuery("select id, trackId, orderNum, runCount from race where competitionId = " + c.id, null);
		if (cursor.moveToFirst()){
			do{
				long id = cursor.getInt(0);
				int runc = cursor.getInt(3);
				Race r = new Race(
					cursor.getInt(1),
					cursor.getInt(2),
					runc,
					new RaceRun[c.competitors.size()][runc]);
				r.id = id;
				c.races.add(r);
			} while (cursor.moveToNext());
		}
		cursor.close();
		

		///
		for (int ir = 0; ir < c.races.size(); ir++){
			Race r = c.races.get(ir);
		 	cursor = db.rawQuery("select id, competitorId, runNumber, turns, time, status from run where raceId = " + r.id, null);
			
			cursor.moveToFirst();
			do{
				int id = cursor.getInt(0);
				int compId = cursor.getInt(1);
				int runN = cursor.getInt(2);
				
				int plN = 0;
				while (c.competitors.get(plN).id != compId) plN++;
				
				RaceRun rr = new RaceRun(getStatus(cursor.getInt(5)));
				rr.runResult.turns = cursor.getInt(3);
				float time = cursor.getFloat(4);
				rr.runResult.time = Math.round(time * 1000) / 1000f;
				rr.id = id;
				r.playerRuns[plN][runN] = rr;
			} while (cursor.moveToNext());
			cursor.close();
		}
		
		return c;
	}

	public void ExecuteNonQuery(String sql){
		SQLiteDatabase db = getWritableDatabase();
		
		db.execSQL(sql);
		
		db.close();
	}
	
	public Cursor ExecuteQuery(String sql){
		SQLiteDatabase db = getWritableDatabase();

		return db.rawQuery(sql, null);
	}
}
