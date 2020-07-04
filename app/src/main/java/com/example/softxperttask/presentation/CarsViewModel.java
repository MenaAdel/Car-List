package com.example.softxperttask.presentation;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.softxperttask.domain.repository.CarsRepository;
import com.example.softxperttask.entites.CarResponse;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CarsViewModel extends ViewModel {

    private int offset = 1;
    private boolean isLoading = false;
    private boolean loadMoreData = true;

    private CarsRepository repository = new CarsRepository();
    final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    final MutableLiveData<Boolean> error = new MutableLiveData<>();
    final MutableLiveData<List<CarResponse.Data>> data = new MutableLiveData<>();
    final MutableLiveData<List<CarResponse.Data>> newData = new MutableLiveData<>();

    private CompositeDisposable disposable = new CompositeDisposable();

    void fetchCars(int offset) {
        checkOffsetToObserve();
        isLoading = true;
        Disposable d = repository.getCars(offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    isLoading = false;
                    loading.postValue(false);
                    error.postValue(false);
                    handleSuccess(response.data);
                }, th -> {
                    loading.postValue(false);
                    error.postValue(true);
                });

        disposable.add(d);
    }

    private void handleSuccess(List<CarResponse.Data> list) {
        if (list != null) {
            if (offset == 1) {
                loadMoreData = true;
                data.postValue(list);
            } else {
                newData.postValue(list);
            }
            loadMoreData = !list.isEmpty();
            setOffset(false, list.size());
        }
    }

    private void setOffset(boolean resetOffset, int offsetSize) {
        if (resetOffset) {
            offset = 1;
        } else {
            offset ++;
        }
    }

    boolean isLoadingData() {
        if (!isLoading && loadMoreData) {
            fetchCars(offset);
            return true;
        } else {
            return false;
        }
    }

    private void checkOffsetToObserve(){
        if (offset == 1) {
            loading.postValue(true);
        }
    }

    void reloadReviewData() {
        setOffset(true ,offset);
        fetchCars(offset);
    }

    @Override
    protected void onCleared() {
        disposable.clear();
        super.onCleared();
    }
}
