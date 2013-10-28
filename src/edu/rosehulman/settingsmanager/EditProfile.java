package edu.rosehulman.settingsmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

public class EditProfile extends Activity implements OnClickListener {
	private Profile mProfile;
	private SeekBar mSystemVolumeControl;
	private SeekBar mRingerVolumeControl;
	private Button mRingerSoundButton;
	private SeekBar mNotificationVolumeControl;
	private Button mNotificationSoundButton;
	private SeekBar mAlarmVolumeControl;
	private AudioManager mAudioManager;
	private Button mSaveButton;
	private Button mCancelButton;
	private EditText mNameView;
	private int requestCode;

	private static final int REQUEST_CODE_NOTIFICATION_SOUND = 1;
	private static final int REQUEST_CODE_RINGTONE_SOUND = 2;
	
	private Uri mCurrentNotificationSound;
	private Uri mCurrentRingerSound;

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
		mRingerSoundButton = (Button) findViewById(R.id.ring_sound_button);
		mRingerSoundButton.setOnClickListener(this);
		mNotificationVolumeControl = (SeekBar) findViewById(R.id.notification_volume_control);
		mNotificationSoundButton = (Button) findViewById(R.id.notification_sound_button);
		mNotificationSoundButton.setOnClickListener(this);
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
		mProfile.setRingerRingtone(mCurrentRingerSound, this);
		mProfile.setNotificationVolume(mNotificationVolumeControl.getProgress());
		mProfile.setNotificationRingtone(mCurrentNotificationSound, this);
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
		mCurrentRingerSound = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_RINGTONE);
		Ringtone ringtone = RingtoneManager.getRingtone(this, mCurrentRingerSound);
		mNotificationSoundButton.setText(ringtone.getTitle(this));
		mNotificationVolumeControl.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION));
		mAlarmVolumeControl.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM));
		mCurrentNotificationSound = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_NOTIFICATION);
		ringtone = RingtoneManager.getRingtone(this, mCurrentNotificationSound);
		mNotificationSoundButton.setText(ringtone.getTitle(this));
	}
	private void intializeEditProfile(){
		initializeBasicProfileSettings();
		mSystemVolumeControl.setProgress(mProfile.getSystemVolume());
		mRingerVolumeControl.setProgress(mProfile.getRingerVolume());
		mCurrentRingerSound = mProfile.getRingerRingtone();
		mRingerSoundButton.setText(mProfile.getRingerRingtoneName());
		mNotificationVolumeControl.setProgress(mProfile.getNotificationVolume());
		mCurrentNotificationSound = mProfile.getNotificationRingtone();
		mNotificationSoundButton.setText(mProfile.getNotificationRingtoneName());
		mAlarmVolumeControl.setProgress(mProfile.getAlarmVolume());
		mNameView.setText(mProfile.toString());
	}
	
	private void initializeBasicProfileSettings(){
		mSystemVolumeControl.setMax(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM));
		mRingerVolumeControl.setMax(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING));
		mNotificationVolumeControl.setMax(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION));
		mAlarmVolumeControl.setMax(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM));
		mNameView.setActivated(false);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.notification_sound_button:
			Intent notificationIntent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
			notificationIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
			notificationIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Notification Tone");
			notificationIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, false);
			notificationIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, mCurrentNotificationSound);
			
			this.startActivityForResult(notificationIntent, REQUEST_CODE_NOTIFICATION_SOUND);
			break;
		case R.id.ring_sound_button:
			Intent ringtoneIntent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
			ringtoneIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_RINGTONE);
			ringtoneIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Ring Tone");
			ringtoneIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, false);
			ringtoneIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, mCurrentRingerSound);
			
			this.startActivityForResult(ringtoneIntent, REQUEST_CODE_RINGTONE_SOUND);
			break;
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		if (requestCode == REQUEST_CODE_NOTIFICATION_SOUND && resultCode == RESULT_OK){
			mCurrentNotificationSound = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
			Ringtone ringtone = RingtoneManager.getRingtone(this, mCurrentNotificationSound);
			mNotificationSoundButton.setText(ringtone.getTitle(this));
		}
		else if (requestCode == REQUEST_CODE_RINGTONE_SOUND && resultCode == RESULT_OK){
			mCurrentRingerSound = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
			Ringtone ringtone = RingtoneManager.getRingtone(this, mCurrentRingerSound);
			mRingerSoundButton.setText(ringtone.getTitle(this));
		}
	}

}
