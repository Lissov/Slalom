package com.pl.slalom.data;
import java.util.List;

public class Data
{
	public int id;
	public String name;
	public String lastname;
	public int countryId;
	public int experience;
	public int money;
	
	public int selectedSkiId;
	
	public List<Integer> availableSkiIds;
	public List<Integer> availableTrackIds;
	
	public String getFullName(){
		return name + lastname.toUpperCase();
	}
}
