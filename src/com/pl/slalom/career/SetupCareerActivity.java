package com.pl.slalom.career;
import com.pl.slalom.*;
import android.app.*;
import android.os.*;
import android.widget.*;
import com.pl.slalom.Controls.Adapters.*;
import com.pl.slalom.data.statics.*;
import java.util.*;
import android.view.*;
import com.pl.slalom.data.*;
import android.content.*;

public class SetupCareerActivity extends Activity
{
	EditText etName;
	EditText etLastName;
	Spinner spCountry;
	List<Country> countries;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.career_setup);
		
		countries = StaticManager.getCountries();
		
		etName = (EditText)findViewById(R.id.careersetup_name);
		etLastName = (EditText)findViewById(R.id.careersetup_lastName);
		spCountry = (Spinner)findViewById(R.id.careersetup_spCountry);
		spCountry.setAdapter(CountryAdapter.getAdapter(this, countries));
	}
	
	public void onStart(View view){
		Data data = DataManager.getInstance().getData();
		data.name = etName.getText().toString();
		data.lastname = etLastName.getText().toString();
		data.countryId = countries.get(spCountry.getSelectedItemPosition()).id;
		
		data.experience = -1;
		DataManager.getInstance().storeData();
		
		startActivity(new Intent(this, CareerActivity.class));
	}
}
