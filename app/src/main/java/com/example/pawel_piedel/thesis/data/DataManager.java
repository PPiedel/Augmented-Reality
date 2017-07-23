package com.example.pawel_piedel.thesis.data;

import android.util.Log;

import com.example.pawel_piedel.thesis.data.local.SharedPreferencesHelper;
import com.example.pawel_piedel.thesis.data.model.AccessToken;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.example.pawel_piedel.thesis.data.ServiceFactory.CLIENT_ID;
import static com.example.pawel_piedel.thesis.data.ServiceFactory.CLIENT_SECRET;
import static com.example.pawel_piedel.thesis.data.ServiceFactory.GRANT_TYPE;
import static com.example.pawel_piedel.thesis.data.ServiceFactory.accessToken;

/**
 * Created by Pawel_Piedel on 21.07.2017.
 */
@Singleton
public class DataManager {
    private final String LOG_TAG = DataManager.class.getSimpleName();

    private final SharedPreferencesHelper preferencesHelper;
    private final ApiService apiService;

    @Inject
    public DataManager(ApiService apiService,SharedPreferencesHelper preferencesHelper) {
        this.preferencesHelper = preferencesHelper;
        this.apiService = apiService;
    }

    public SharedPreferencesHelper getPreferencesHelper() {
        return preferencesHelper;
    }

    public Observable<AccessToken> getAccessToken(){
        AccessToken accessToken = preferencesHelper.getAccessToken();
        //just return value without call if cached
        if (accessToken!=null){
            return Observable.just(accessToken);
        }
        else {
            return apiService.getAccessToken(CLIENT_ID, CLIENT_SECRET, GRANT_TYPE);
        }
    }

    public void saveAccessToken(AccessToken accessToken){
        ServiceFactory.accessToken = accessToken;
        preferencesHelper.saveAccessToken(accessToken);
    }
}
