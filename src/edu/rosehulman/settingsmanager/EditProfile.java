package edu.rosehulman.settingsmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

public class EditProfile extends Activity {
	private Profile mProfile;
	private SeekBar mSystemVolumeControl;
	private SeekBar mRingerVolumeControl;
	private SeekBar mNotificationVolumeControl;
	private Button mNotificationSoundButton;
	private SeekBar mAlarmVolumeControl;
	private AudioManager mAudioManager;
	private Button mSaveButton;
	private Button mCancelButton;
	private EditText mNameView;
	private int requestCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile);
		Intent intent = this.getIntent();
		mProfile = (Profile) intent.getSerializableExtra(Profiles.KEY_EDIT_PROFILE);
		mAudioManager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
		requestCode = intent.getIntExtra(Profiles.KEY_REQUEST_CODE, 1);
		//Identify controls
		mNameView = (EditText) findViewById(R.id.editText1);
		mSystemVolumeControl = (SeekBar) findViewById(R.id.system_volume_control);
		mRingerVolumeControl = (SeekBar) findViewById(R.id.ring_volume_control);
		mNotificationVolumeControl = (SeekBar) findViewById(R.id.notification_volume_control);
		mNotificationSoundButton = (Button) findViewById(R.id.notification_sound_spinner);
		mAlarmVolumeControl = (SeekBar) findViewById(R.id.alarm_volume_control);
		
		if (requestCode == Profiles.REQUEST_CODE_EDIT_PROFILE)
			intializeEditProfile();
		else
			intializeNewProfile();
		
		//Initialize buttons
		mSaveButton = (Button) findViewById(R.id.save_button);
		mCancelButton = (Button) findViewById(R.id.cancel_button);
		mSaveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				save();
			}
		});
		mCancelButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				cancel();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.edit_profile, menu);
		return true;
	}
	private void save(){
		mProfile.setSystemVolume(mSystemVolumeControl.getProgress());
		mProfile.setRingerVolume(mRingerVolumeControl.getProgress());
		mProfile.setNotificationVolume(mNotificationVolumeControl.getProgress());
		mProfile.setAlarmVolume(mAlarmVolumeControl.getProgress());
		mProfile.setName(mNameView.getText().toString());
		Intent result = new Intent();
		result.putExtra(Profiles.KEY_EDIT_PROFILE, mProfile);
		this.setResult(RESULT_OK,result);
		this.finish();
	}
	private void cancel() {
		this.setResult(RESULT_CANCELED);
		this.finish();
	}
	private void intializeNewProfile(){
		initializeBasicProfileSettings();
		mSystemVolumeControl.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM));
		mRingerVolumeControl.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_RING));
		mNotificationVolumeControl.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION));
		mAlarmVolumeControl.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM));
		updateNotificationButton(null);
	}
	private void intializeEditProfile(){
		initializeBasicProfileSettings();
		mSystemVolumeControl.setProgress(mProfile.getSystemVolume());
		mRingerVolumeControl.setProgress(mProfile.getRingerVolume());
		mNotificationVolumeControl.setProgress(mProfile.getNotificationVolume());
		mNotificationSoundButton.setText(mProfile.getNotificationRingtone());
		mAlarmVolumeControl.setProgress(mProfile.getAlarmVolume());
		mNameView.setText(mProfile.toString());
	}
	
	private void initializeBasicProfileSettings(){
		mSystemVolumeControl.setMax(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM));
		mRingerVolumeControl.setMax(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING));
		mNotificationVolumeControl.setMax(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION));
		mAlarmVolumeControl.setMax(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM));
		mNotificationSoundButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
				intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
				intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");
				intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, false);
				updateNotificationButton(intent);
				
				ME.startActivityForResult(intent, 5);
			}
		});
		mNameView.setActivated(false);
	}
	
	private void updateNotificationButton(Intent intent){
		Uri current = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_NOTIFICATION);
		Ringtone ringtone = RingtoneManager.getRingtone(this, current);
		mNotificationSoundButton.setText(ringtone.getTitle(this));
		if(intent != null)
			intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, current);
	}
	
	private EditProfile ME = this;

}
