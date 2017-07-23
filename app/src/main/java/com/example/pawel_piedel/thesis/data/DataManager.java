package com.example.pawel_piedel.thesis.data;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.example.pawel_piedel.thesis.data.local.SharedPreferencesHelper;
import com.example.pawel_piedel.thesis.data.model.AccessToken;
import com.example.pawel_piedel.thesis.data.model.SearchResponse;
import com.example.pawel_piedel.thesis.injection.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.example.pawel_piedel.thesis.data.ServiceFactory.CLIENT_ID;
import static com.example.pawel_piedel.thesis.data.ServiceFactory.CLIENT_SECRET;
import static com.example.pawel_piedel.thesis.data.ServiceFactory.GRANT_TYPE;

/**
 * Created by Pawel_Piedel on 21.07.2017.
 */
@Singleton
public class DataManager {
    private final String LOG_TAG = DataManager.class.getSimpleName();

    private final SharedPreferencesHelper preferencesHelper;
    private ApiService apiService;
    private Context context;

    @Inject
    public DataManager(@ApplicationContext Context context,
                       ApiService apiService,
                       SharedPreferencesHelper preferencesHelper) {
        this.preferencesHelper = preferencesHelper;
        this.apiService = apiService;
        this.context = context;
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

    public Observable<Location> getLastKnownLocation(){
        if (LocationService.mLastLocation != null) {
            return Observable.just(LocationService.mLastLocation);
        }
        else {
            ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(context);
            return locationProvider.getLastKnownLocation();
        }
    }

    public Observable<SearchResponse> loadBusinesses(String term){
        apiService = ServiceFactory.createService(ApiService.class);
        return apiService.getBusinessesList(
                term,
                LocationService.mLastLocation.getLatitude(),
                LocationService.mLastLocation.getLongitude(),
                null,
                null);

    }


    public void saveAccessToken(AccessToken accessToken){
        ServiceFactory.accessToken = accessToken;
        preferencesHelper.saveAccessToken(accessToken);
    }
}
