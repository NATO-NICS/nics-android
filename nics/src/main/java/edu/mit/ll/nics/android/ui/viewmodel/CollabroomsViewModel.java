/*
 * Copyright (c) 2008-2021, Massachusetts Institute of Technology (MIT)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package edu.mit.ll.nics.android.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import edu.mit.ll.nics.android.database.entities.Collabroom;
import edu.mit.ll.nics.android.repository.CollabroomRepository;
import edu.mit.ll.nics.android.repository.PreferencesRepository;
import edu.mit.ll.nics.android.utils.livedata.NonNullMutableLiveData;

@HiltViewModel
public class CollabroomsViewModel extends ViewModel {

    private final LiveData<Collabroom> mActiveCollabroom;
    private final LiveData<List<Collabroom>> mCollabrooms;
    private final NonNullMutableLiveData<Boolean> mIsLoading = new NonNullMutableLiveData<>(false);

    @Inject
    public CollabroomsViewModel(PreferencesRepository preferences,
                                CollabroomRepository collabroomRepository) {

        mCollabrooms = Transformations.switchMap(preferences.getSelectedIncidentLiveData(), incident -> collabroomRepository.getCollabroomsLiveData(incident.getIncidentId()));
        mActiveCollabroom = preferences.getSelectedCollabroomLiveData();
    }

    public LiveData<List<Collabroom>> getCollabrooms() {
        return mCollabrooms;
    }

    public LiveData<Collabroom> getActiveCollabroom() {
        return mActiveCollabroom;
    }

    public NonNullMutableLiveData<Boolean> isLoading() {
        return mIsLoading;
    }

    public void setLoading(boolean isLoading) {
        mIsLoading.postValue(isLoading);
    }
}
