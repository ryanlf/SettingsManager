package edu.rosehulman.settingsmanager;

import android.media.AudioManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

public class EditProfile extends Activity {
	private Profile mProfile;
	private SeekBar mVolumeControl;
	private AudioManager mAudioManager;
	private Button mSaveButton;
	private Button mCancelButton;
	private EditText mNameView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile);
		Intent intent = this.getIntent();
		mProfile = (Profile) intent.getSerializableExtra(Profiles.KEY_EDIT_PROFILE);
		mVolumeControl = (SeekBar) findViewById(R.id.system_volume_control);
		mAudioManager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
		mVolumeControl.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM));
		mVolumeControl.setMax(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM));
		mSaveButton = (Button) findViewById(R.id.save_button);
		mCancelButton = (Button) findViewById(R.id.cancel_button);
		mNameView = (EditText) findViewById(R.id.editText1);
		
		mVolumeControl.setProgress(mProfile.getVolumeLevel());
		mNameView.setText(mProfile.toString());
		mSaveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				save();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.edit_profile, menu);
		return true;
	}
	private void save(){
		mProfile.setVolume(mVolumeControl.getProgress());
		mProfile.setName(mNameView.getText().toString());
		Intent result = new Intent();
		result.putExtra(Profiles.KEY_EDIT_PROFILE, mProfile);
		this.setResult(RESULT_OK,result);
		this.finish();
	}

}
