package com.pl.slalom.data.statics;
import java.util.*;

public class StaticManager
{
	private static List<Country> countries;
	public static List<Country> getCountries(){
		if (countries == null){
			countries = new LinkedList<Country>();
			
			countries.add(new Country(1, "Ukraine", "UA"));
			countries.add(new Country(2, "Austria", "AUT"));
		}
		
		return countries;
	} 
}
