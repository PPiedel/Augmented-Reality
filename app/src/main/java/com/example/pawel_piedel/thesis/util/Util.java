package com.example.pawel_piedel.thesis.util;

import android.location.Location;
import android.util.SparseIntArray;
import android.view.Surface;

import com.google.gson.Gson;

/**
 * Created by Pawel_Piedel on 19.07.2017.
 */

public class Util {
   public static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
   public static final Gson gson = new Gson();
   private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
   static {
      ORIENTATIONS.append(Surface.ROTATION_0, 90);
      ORIENTATIONS.append(Surface.ROTATION_90, 0);
      ORIENTATIONS.append(Surface.ROTATION_180, 270);
      ORIENTATIONS.append(Surface.ROTATION_270, 180);
   }


   public static Location mLastLocation;
}
