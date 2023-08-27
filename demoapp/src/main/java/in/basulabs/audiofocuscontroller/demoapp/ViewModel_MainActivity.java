/*
Copyright (C) 2022  Wrichik Basu (basulabs.developer@gmail.com)

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/
package in.basulabs.audiofocuscontroller.demoapp;

import android.media.AudioAttributes;
import android.media.AudioManager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ViewModel_MainActivity extends ViewModel {

	private MutableLiveData<Boolean> isEditingOn, acceptsDelayedFocus, pauseWhenDucked,
		listenToNoisyAudio;
	private MutableLiveData<Integer> usage, contentType, durationHint, streamType;
	private MutableLiveData<String> status;

	public String getStatus() {
		if (status == null) {
			status = new MutableLiveData<>("N/A");
		}
		return status.getValue();
	}

	public void setStatus(String status) {
		if (this.status == null) {
			this.status = new MutableLiveData<>();
		}
		this.status.setValue(status);
	}

	public LiveData<String> getLiveStatus() {
		if (status == null) {
			status = new MutableLiveData<>("N/A");
		}
		return status;
	}

	public boolean getListenToNoisyAudio() {
		if (listenToNoisyAudio == null) {
			listenToNoisyAudio = new MutableLiveData<>(false);
		}
		return listenToNoisyAudio.getValue() != null && listenToNoisyAudio.getValue();
	}

	public void setListenToNoisyAudio(boolean listenToNoisyAudio) {
		if (this.listenToNoisyAudio == null) {
			this.listenToNoisyAudio = new MutableLiveData<>();
		}
		this.listenToNoisyAudio.setValue(listenToNoisyAudio);
	}

	public boolean getIsEditingOn() {
		if (isEditingOn == null) {
			isEditingOn = new MutableLiveData<>(true);
		}
		return isEditingOn.getValue() == null || isEditingOn.getValue();
	}

	public void setIsEditingOn(boolean isEditingOn) {
		if (this.isEditingOn == null) {
			this.isEditingOn = new MutableLiveData<>();
		}
		this.isEditingOn.setValue(isEditingOn);
	}

	public boolean getAcceptsDelayedFocus() {
		if (acceptsDelayedFocus == null) {
			acceptsDelayedFocus = new MutableLiveData<>(true);
		}
		return acceptsDelayedFocus.getValue() == null || acceptsDelayedFocus.getValue();
	}

	public void setAcceptsDelayedFocus(boolean acceptsDelayedFocus) {
		if (this.acceptsDelayedFocus == null) {
			this.acceptsDelayedFocus = new MutableLiveData<>();
		}
		this.acceptsDelayedFocus.setValue(acceptsDelayedFocus);
	}

	public boolean getPauseWhenDucked() {
		if (pauseWhenDucked == null) {
			pauseWhenDucked = new MutableLiveData<>(false);
		}
		return pauseWhenDucked.getValue() != null && pauseWhenDucked.getValue();
	}

	public void setPauseWhenDucked(boolean pauseWhenDucked) {
		if (this.pauseWhenDucked == null) {
			this.pauseWhenDucked = new MutableLiveData<>();
		}
		this.pauseWhenDucked.setValue(pauseWhenDucked);
	}

	public int getUsage() {
		if (usage == null) {
			usage = new MutableLiveData<>(AudioAttributes.USAGE_MEDIA);
		}
		return usage.getValue() == null ? AudioAttributes.USAGE_MEDIA : usage.getValue();
	}

	public void setUsage(int usage) {
		if (this.usage == null) {
			this.usage = new MutableLiveData<>(AudioAttributes.USAGE_UNKNOWN);
		}
		this.usage.setValue(usage);
	}

	public int getContentType() {
		if (contentType == null) {
			contentType = new MutableLiveData<>(AudioAttributes.CONTENT_TYPE_MUSIC);
		}
		return contentType.getValue() == null
			? AudioAttributes.CONTENT_TYPE_MUSIC
			: contentType.getValue();
	}

	public void setContentType(int contentType) {
		if (this.contentType == null) {
			this.contentType = new MutableLiveData<>(
				AudioAttributes.CONTENT_TYPE_UNKNOWN);
		}
		this.contentType.setValue(contentType);
	}

	public int getDurationHint() {
		if (durationHint == null) {
			durationHint = new MutableLiveData<>(AudioManager.AUDIOFOCUS_GAIN);
		}
		return durationHint.getValue() == null
			? AudioManager.AUDIOFOCUS_GAIN
			: durationHint.getValue();
	}

	public void setDurationHint(int durationHint) {
		if (this.durationHint == null) {
			this.durationHint = new MutableLiveData<>(AudioManager.AUDIOFOCUS_GAIN);
		}
		this.durationHint.setValue(durationHint);
	}

	public int getStreamType() {
		if (streamType == null) {
			streamType = new MutableLiveData<>(AudioManager.STREAM_MUSIC);
		}
		return streamType.getValue() == null
			? AudioManager.STREAM_MUSIC
			: streamType.getValue();
	}

	public void setStreamType(int streamType) {
		if (this.streamType == null) {
			this.streamType =
				new MutableLiveData<>(AudioManager.USE_DEFAULT_STREAM_TYPE);
		}
		this.streamType.setValue(streamType);
	}

	public LiveData<Boolean> getLiveIsEditingOn() {
		if (isEditingOn == null) {
			isEditingOn = new MutableLiveData<>(true);
		}
		return isEditingOn;
	}

}
