package com.example.pawel_piedel.thesis.data.model;

import android.location.*;
import android.location.Location;

/**
 * Created by Pawel_Piedel on 23.07.2017.
 */

public class TokenAndLocation {
    public AccessToken accessToken;
    public android.location.Location location;

    public TokenAndLocation(AccessToken accessToken, Location location) {
        this.accessToken = accessToken;
        this.location = location;
    }
}
