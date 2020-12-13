package in.basulabs.audiofocuscontroller.demoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;

import in.basulabs.audiofocuscontroller.AudioFocusController;
import in.basulabs.audiofocuscontroller.demoapp.databinding.ActivityMainBinding;
import in.basulabs.audiofocuscontroller.demoapp.databinding.ActivityMainScrollviewBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
		CompoundButton.OnCheckedChangeListener, AdapterView.OnItemSelectedListener, AudioFocusController.OnAudioFocusChangeListener {

	private ActivityMainScrollviewBinding binding;

	private ViewModel_MainActivity viewModel;

	private AudioFocusController audioFocusController;

	private AudioOptionsDatabase database;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set view:
		ActivityMainBinding mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
		setContentView(mainBinding.getRoot());

		// Initialise variables:
		binding = mainBinding.mainScrollview;
		viewModel = new ViewModelProvider(this).get(ViewModel_MainActivity.class);
		database = AudioOptionsDatabase.getInstance(this);

		// Initialise views:
		ArrayAdapter<String> usageAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
				database.optionsDAO().getAllDisplayStringsForUsage());
		usageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		binding.usageSpinner.setAdapter(usageAdapter);

		ArrayAdapter<String> durationHintAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
				database.optionsDAO().getAllDisplayStringsForDurationHint());
		durationHintAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		binding.durationHintSpinner.setAdapter(durationHintAdapter);

		ArrayAdapter<String> contentTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
				database.optionsDAO().getAllDisplayStringsForContentType());
		contentTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		binding.contentTypeSpinner.setAdapter(contentTypeAdapter);

		ArrayAdapter<String> streamTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
				database.optionsDAO().getAllDisplayStringsForStreamType());
		streamTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		binding.streamTypeSpinner.setAdapter(streamTypeAdapter);

		binding.editingSwitch.setChecked(viewModel.getIsEditingOn());

		binding.usageSpinner.setSelection(usageAdapter.getPosition(database.optionsDAO().getDisplayStringForUsage(viewModel.getUsage())));
		binding.durationHintSpinner.setSelection(durationHintAdapter.getPosition(database.optionsDAO().getDisplayStringForDurationHint(viewModel.getDurationHint())));
		binding.contentTypeSpinner.setSelection(contentTypeAdapter.getPosition(database.optionsDAO().getDisplayStringForContentType(viewModel.getContentType())));
		binding.streamTypeSpinner.setSelection(streamTypeAdapter.getPosition(database.optionsDAO().getDisplayStringForStreamType(viewModel.getStreamType())));

		binding.pauseDuckCheckBox.setChecked(viewModel.getPauseWhenDucked());
		binding.delayedFocusCheckBox.setChecked(viewModel.getAcceptsDelayedFocus());
		binding.audioNoisyCheckBox.setChecked(viewModel.getListenToNoisyAudio());

		binding.currentStatusTextView.setText(viewModel.getStatus());

		onEditingStatusChanged();

		// Set the listeners:

		binding.abandonFocusButton.setOnClickListener(this);
		binding.requestFocusButton.setOnClickListener(this);

		binding.delayedFocusCheckBox.setOnCheckedChangeListener(this);
		binding.pauseDuckCheckBox.setOnCheckedChangeListener(this);
		binding.audioNoisyCheckBox.setOnCheckedChangeListener(this);

		binding.editingSwitch.setOnCheckedChangeListener(this);

		binding.durationHintSpinner.setOnItemSelectedListener(this);
		binding.usageSpinner.setOnItemSelectedListener(this);
		binding.contentTypeSpinner.setOnItemSelectedListener(this);
		binding.streamTypeSpinner.setOnItemSelectedListener(this);

		// Observers:
		viewModel.getLiveIsEditingOn().observe(this, value -> onEditingStatusChanged());
		viewModel.getLiveStatus().observe(this, value -> binding.currentStatusTextView.setText(value));

	}

	//----------------------------------------------------------------------------------------------------------

	@Override
	public void onClick(View view) {

		if (view.getId() == binding.requestFocusButton.getId()){
			viewModel.setStatus("Focus requested from system...");
			audioFocusController.requestFocus();
		} else if (view.getId() == binding.abandonFocusButton.getId()){
			audioFocusController.abandonFocus();
			viewModel.setStatus("Focus abandoned.");
		}

	}

	//----------------------------------------------------------------------------------------------------------

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		if (buttonView.getId() == binding.delayedFocusCheckBox.getId()){
			viewModel.setAcceptsDelayedFocus(isChecked);
		} else if (buttonView.getId() == binding.pauseDuckCheckBox.getId()){
			viewModel.setPauseWhenDucked(isChecked);
		} else if (buttonView.getId() == binding.audioNoisyCheckBox.getId()){
			viewModel.setListenToNoisyAudio(isChecked);
		} else if (buttonView.getId() == binding.editingSwitch.getId()){
			if (isChecked){
				audioFocusController.abandonFocus();
				viewModel.setStatus("Focus abandoned.");
			}
			viewModel.setIsEditingOn(isChecked);
		}

	}

	//----------------------------------------------------------------------------------------------------------

	private void onEditingStatusChanged(){

		if (viewModel.getIsEditingOn()){
			binding.editingSwitch.setText(R.string.editingSwitchOnLabel);
		} else {
			binding.editingSwitch.setText(R.string.editingSwitchOffLabel);

			audioFocusController = new AudioFocusController.Builder(this)
					.setAudioFocusChangeListener(this)
					.setAcceptsDelayedFocus(viewModel.getAcceptsDelayedFocus())
					.setPauseWhenAudioIsNoisy(viewModel.getListenToNoisyAudio())
					.setPauseWhenDucked(viewModel.getPauseWhenDucked())
					.setContentType(viewModel.getContentType())
					.setDurationHint(viewModel.getDurationHint())
					.setUsage(viewModel.getUsage())
					.setStream(viewModel.getStreamType())
					.build();
		}

		binding.durationHintSpinner.setEnabled(viewModel.getIsEditingOn());
		binding.usageSpinner.setEnabled(viewModel.getIsEditingOn());
		binding.contentTypeSpinner.setEnabled(viewModel.getIsEditingOn());
		binding.streamTypeSpinner.setEnabled(viewModel.getIsEditingOn());

		binding.audioNoisyCheckBox.setEnabled(viewModel.getIsEditingOn());
		binding.delayedFocusCheckBox.setEnabled(viewModel.getIsEditingOn());
		binding.pauseDuckCheckBox.setEnabled(viewModel.getIsEditingOn());

		binding.contentTypeLabel.setEnabled(viewModel.getIsEditingOn());
		binding.durationHintLabel.setEnabled(viewModel.getIsEditingOn());
		binding.usageLabel.setEnabled(viewModel.getIsEditingOn());
		binding.streamTypeLabel.setEnabled(viewModel.getIsEditingOn());

		binding.requestFocusButton.setEnabled(! viewModel.getIsEditingOn());
		binding.abandonFocusButton.setEnabled(! viewModel.getIsEditingOn());
	}

	//----------------------------------------------------------------------------------------------------------

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

		if (parent.getId() == binding.durationHintSpinner.getId()){
			viewModel.setDurationHint(database.optionsDAO().getDurationHintValue((String) parent.getItemAtPosition(position)));
		} else if (parent.getId() == binding.usageSpinner.getId()){
			viewModel.setUsage(database.optionsDAO().getUsageValue((String) parent.getItemAtPosition(position)));
		} else if (parent.getId() == binding.contentTypeSpinner.getId()){
			viewModel.setContentType(database.optionsDAO().getContentTypeValue((String) parent.getItemAtPosition(position)));
		} else if (parent.getId() == binding.streamTypeSpinner.getId()){
			viewModel.setStreamType(database.optionsDAO().getStreamTypeValue((String) parent.getItemAtPosition(position)));
		}

	}

	//----------------------------------------------------------------------------------------------------------

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	//----------------------------------------------------------------------------------------------------------

	@Override
	public void decreaseVolume() {
		viewModel.setStatus("decreaseVolume() called.");
	}

	//----------------------------------------------------------------------------------------------------------

	@Override
	public void increaseVolume() {
		viewModel.setStatus("increaseVolume() called.");
	}

	//----------------------------------------------------------------------------------------------------------

	@Override
	public void pause() {
		viewModel.setStatus("pause() called.");
	}

	//----------------------------------------------------------------------------------------------------------

	@Override
	public void resume() {
		viewModel.setStatus("resume() called.");
	}

}