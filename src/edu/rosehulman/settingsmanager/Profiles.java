package edu.rosehulman.settingsmanager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import android.media.AudioManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class Profiles extends Activity implements OnItemClickListener,OnItemLongClickListener {
	
	private ArrayList<Profile> mProfileData;
	private ArrayAdapter<Profile> ad;
	private Button mAddNew;
	public static final String KEY_EDIT_PROFILE = "KEY_EDIT_PROFILE";
	public static final String KEY_REQUEST_CODE = "KEY_REQUEST_CODE";
	public static final int REQUEST_CODE_EDIT_PROFILE = 1;
	public static final int REQUEST_CODE_ADD_PROFILE = 2;
	private Profile inEdit = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profiles);
		
		try {
			FileInputStream fileRead = openFileInput(getString(R.string.data_file_name));
			ObjectInputStream objectInput = new ObjectInputStream(fileRead);
			mProfileData = (ArrayList<Profile>) objectInput.readObject();
			objectInput.close();
			fileRead.close();
		} catch (FileNotFoundException e) {
			mProfileData = new ArrayList<Profile>();
		} catch (IOException e) {
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		mAddNew =(Button) findViewById(R.id.add_new_button);
		mAddNew.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent addIntent = new Intent(v.getContext(),EditProfile.class);
				addIntent.putExtra(KEY_EDIT_PROFILE, new Profile());
				addIntent.putExtra(KEY_REQUEST_CODE, REQUEST_CODE_ADD_PROFILE);
				startActivityForResult(addIntent, REQUEST_CODE_ADD_PROFILE);
			}
		});
		ad = new ArrayAdapter<Profile>(this, android.R.layout.simple_list_item_1,
				android.R.id.text1, mProfileData);
		ListView listView = (ListView)findViewById(R.id.profile_list);
		listView.setAdapter(ad);
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.profiles, menu);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		mProfileData.get(arg2).setSettings(this);
		Toast.makeText(this, mProfileData.get(arg2).toString() + " profile loaded", Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int arg2,
			long arg3) {
		DialogFragment df = new DialogFragment() {
			
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				AlertDialog.Builder editBuilder = new AlertDialog.Builder(
						getActivity());
				editBuilder.setMessage(R.string.long_hold_message);
				editBuilder.setPositiveButton(R.string.edit,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								inEdit = mProfileData.get(arg2);
								Intent editIntent = new Intent(getActivity(),EditProfile.class);
								editIntent.putExtra(KEY_EDIT_PROFILE, inEdit);
								editIntent.putExtra(KEY_REQUEST_CODE, REQUEST_CODE_EDIT_PROFILE);
								getActivity().startActivityForResult(editIntent, REQUEST_CODE_EDIT_PROFILE);
							}
						});
				editBuilder.setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						removeProfile(mProfileData.get(arg2));
					}
				});
				editBuilder.setNeutralButton(R.string.cancel,
						null);

				return editBuilder.create();

			}
		};
		df.show(getFragmentManager(), "");
		return true;
	}
	
	private boolean addNewProfile(Profile profile){
		mProfileData.add(profile);
		try {
			FileOutputStream fileCreate = openFileOutput(getString(R.string.data_file_name), Context.MODE_PRIVATE);
			ObjectOutputStream objectCreate = new ObjectOutputStream(fileCreate);
			objectCreate.writeObject(mProfileData);
			objectCreate.close();
			fileCreate.close();
		} catch (FileNotFoundException e) {
			mProfileData.remove(profile);
			return false;
		} catch (IOException e) {
			mProfileData.remove(profile);
			e.printStackTrace();
			return false;
		}
		ad.notifyDataSetChanged();
		return true;
	}
	private boolean removeProfile(Profile profile){
		mProfileData.remove(profile);
		try {
			FileOutputStream fileCreate = openFileOutput(getString(R.string.data_file_name), Context.MODE_PRIVATE);
			ObjectOutputStream objectCreate = new ObjectOutputStream(fileCreate);
			objectCreate.writeObject(mProfileData);
			objectCreate.close();
			fileCreate.close();
		} catch (FileNotFoundException e) {
			mProfileData.add(profile);
			return false;
		} catch (IOException e) {
			mProfileData.add(profile);
			e.printStackTrace();
			return false;
		}
		ad.notifyDataSetChanged();
		return true;
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_EDIT_PROFILE && resultCode == RESULT_OK){
			mProfileData.remove(inEdit);
			addNewProfile((Profile) data.getSerializableExtra(KEY_EDIT_PROFILE));
		}
		if (requestCode == REQUEST_CODE_EDIT_PROFILE){
			inEdit = null;
		}
		if (requestCode == REQUEST_CODE_ADD_PROFILE && resultCode == RESULT_OK){
			addNewProfile((Profile) data.getSerializableExtra(KEY_EDIT_PROFILE));
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case R.id.reset_app_settings:
			resetApp();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void resetApp() {
		DialogFragment df = new DialogFragment() {
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				AlertDialog.Builder alertBuilder = new AlertDialog.Builder(
						getActivity());
				alertBuilder.setMessage(R.string.reset_description);
				alertBuilder.setIcon(R.drawable.ic_dialog_alert);
				alertBuilder.setTitle(R.string.reset);
				alertBuilder.setPositiveButton(R.string.yes,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								int k =0;
								while(!mProfileData.isEmpty()){
									removeProfile(mProfileData.get(0));
								}
								dialog.dismiss();
							}
						});
				alertBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						
						dialog.dismiss();
					}
				});

				return alertBuilder.create();
			}
		};
		df.show(getFragmentManager(), "");		
	}
}
