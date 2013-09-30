package edu.rosehulman.settingsmanager;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Profiles extends Activity implements OnItemClickListener,OnItemLongClickListener {
	
	private ArrayList<Profile> mTestData = new ArrayList<Profile>();
	private ArrayAdapter<Profile> ad;
	private int addNum = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profiles);
		mTestData.add(new Profile("Test1",0));
		mTestData.add(new Profile("Test2",1));
		mTestData.add(new Profile("Test3",2));
		
		mTestData.add(new Profile());
		
		
		ad = new ArrayAdapter<Profile>(this, android.R.layout.simple_list_item_1,
				android.R.id.text1, mTestData);
		
		ListView listView = (ListView)findViewById(R.id.profile_list);
		
		listView.setAdapter(ad);
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profiles, menu);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (mTestData.get(arg2).toString() == "Add New"){
			ad.add(new Profile("Object" + addNum, 10));
			ad.remove(mTestData.get(arg2));
			addNum++;
			Toast.makeText(this, "Added Object", Toast.LENGTH_SHORT).show();
			ad.add(new Profile());
			
		} else
		Toast.makeText(this, ""+mTestData.get(arg2).getVolumeLevel(), Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		Toast.makeText(this, "Long Click" + mTestData.get(arg2), Toast.LENGTH_SHORT).show();
		return true;
	}

}
