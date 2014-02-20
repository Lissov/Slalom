package com.pl.slalom.data;
import android.content.*;

public interface IDataLoader
{
	Data loadData();
	
	public void storeData(Data data);
}
