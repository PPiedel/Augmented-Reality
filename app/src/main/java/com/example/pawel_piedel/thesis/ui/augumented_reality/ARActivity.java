package com.example.pawel_piedel.thesis.ui.augumented_reality;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.transition.TransitionManager;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pawel_piedel.thesis.R;
import com.example.pawel_piedel.thesis.data.model.Business;
import com.example.pawel_piedel.thesis.ui.base.BaseActivity;
import com.example.pawel_piedel.thesis.ui.detail.DetailActivity;
import com.example.pawel_piedel.thesis.ui.network_connection.NetworkFragment;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.pawel_piedel.thesis.ui.main.BusinessAdapter.BUSINESS;

public class ARActivity extends BaseActivity implements ARContract.View {
    private final String LOG_TAG = ARActivity.class.getSimpleName();
    private CameraCaptureSession cameraCaptureSessions;
    private CaptureRequest.Builder captureRequestBuilder;
    private final TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            Log.d(LOG_TAG,"Heigth : "+height+" width "+height);
            presenter.setStateCallback(stateCallback);
            presenter.openCamera(width, height, ARActivity.this);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            presenter.configureTransform(width, height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    };

    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            presenter.setCameraDevice(camera);
            presenter.onCameraOpened();
        }

        @Override
        public void onDisconnected(CameraDevice camera) {
            presenter.closeCamera();
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            presenter.closeCamera();
            presenter.setCameraDevice(null);
        }
    };

    @BindView(R.id.textureaAR)
    AutoFitTextureView textureView;

    @BindView(R.id.businessViewAR)

    LinearLayout businessView;

    @BindView(R.id.businessTitleAR)

    TextView businessTitle;

    @BindView(R.id.businessDistanceAR)

    TextView businessDistance;

    @BindView(R.id.businessAddress1AR)
    TextView businessAddress1;

    @BindView(R.id.businessAddress2AR)
    TextView businessAddress2;

    @BindView(R.id.rating_barAR)
    RatingBar ratingBar;

    @BindView(R.id.ratingAR)
    TextView rating;

    @BindView(R.id.review_countAR)
    TextView reviewCount;

    @BindView(R.id.priceRangeAR)
    TextView priceRange;

    @BindView(R.id.touch_ar)
    ImageView touchIcon;

    @Inject
    ARPresenter<ARContract.View> presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ar);
        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        addNetworkConnectionFragment();
        presenter.attachView(this);

        presenter.setReactiveSensors(this);

        textureView.setSurfaceTextureListener(textureListener);

    }

    private void addNetworkConnectionFragment() {
        NetworkFragment networkFragment = (NetworkFragment) getFragmentManager().findFragmentByTag(NetworkFragment.LOG_TAG);
        if (networkFragment == null) {
            networkFragment = NetworkFragment.newInstance();
            getFragmentManager().beginTransaction()
                    .add(networkFragment, NetworkFragment.LOG_TAG)
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.managePermissions();
    }

    public void startObserving() {
        presenter.startCameraBackgroundThread();
        if (textureView.isAvailable()) {
            presenter.openCamera(textureView.getWidth(), textureView.getHeight(), this);
        } else {
            textureView.setSurfaceTextureListener(textureListener);
        }

        presenter.observeDeviceAzimuth();
        presenter.observeDeviceLocation();
        presenter.observeDeviceAzimuthAccuracy();
    }

    @Override
    protected void onPause() {
        // Log.e(LOG_TAG, "onPause");
        presenter.closeCamera();
        presenter.stopBackgroundThread();
        presenter.unsubscribeAll();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void showBusinessOnScreen(Business business) {
        TransitionManager.beginDelayedTransition(businessView);
        businessView.setVisibility(View.VISIBLE);
        businessTitle.setText(String.format("%s", business.getName()));
        Log.d(LOG_TAG, "" + business.getDistance());
        if (business.getDistance() < 1000) {
            businessAddress1.setVisibility(View.VISIBLE);
            businessAddress2.setVisibility(View.VISIBLE);
            ratingBar.setVisibility(View.VISIBLE);
            rating.setVisibility(View.VISIBLE);
            reviewCount.setVisibility(View.VISIBLE);
            priceRange.setVisibility(View.VISIBLE);

            businessAddress1.setText((String.valueOf(business.getLocation().getAddress1())));
            businessAddress2.setText(String.format("%s %s", business.getLocation().getZipCode(), business.getLocation().getCity()));
            ratingBar.setRating((float) business.getRating());
            rating.setText(String.format("%s", business.getRating()));
            reviewCount.setText(String.format("(%s)", business.getReviewCount()));
            StringBuilder dolars = new StringBuilder();
            if (business.getPrice()!=null){
                List<String> prices = Arrays.asList(business.getPrice().split(","));
                for (String price : prices) {
                    if (Integer.parseInt(price) == 1) {
                        dolars.append("$");
                    } else if (Integer.parseInt(price) == 2) {
                        dolars.append("$$");
                    } else if (Integer.parseInt(price) == 3) {
                        dolars.append("$$$");
                    } else if (Integer.parseInt(price) == 4) {
                        dolars.append("$$$$");
                    }
                }
                priceRange.setText(dolars.toString());
            }


        } else if (business.getDistance() < 5000) {
            businessAddress1.setVisibility(View.VISIBLE);
            businessAddress2.setVisibility(View.VISIBLE);
            ratingBar.setVisibility(View.GONE);
            rating.setVisibility(View.GONE);
            reviewCount.setVisibility(View.GONE);
            priceRange.setVisibility(View.GONE);
            businessAddress1.setText((String.valueOf(business.getLocation().getAddress1())));
            businessAddress2.setText(String.format("%s %s", business.getLocation().getZipCode(), business.getLocation().getCity()));
        } else {
            businessAddress1.setVisibility(View.GONE);
            businessAddress2.setVisibility(View.GONE);
            ratingBar.setVisibility(View.GONE);
            rating.setVisibility(View.GONE);
            reviewCount.setVisibility(View.GONE);
            priceRange.setVisibility(View.GONE);
        }
        touchIcon.setVisibility(View.VISIBLE);
        businessDistance.setText(String.format("%.1f km", business.getDistance() / 1000));
    }

    @Override
    public void hideBusiness() {
        TransitionManager.beginDelayedTransition(businessView);
        // Log.i(LOG_TAG, "Hiding busines...");
        businessView.setVisibility(View.GONE);
    }

    @Override
    public Activity getViewActivity() {
        return this;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        presenter.onPermissionResult(requestCode, permissions, grantResults);

    }

    public void showCameraPreview(Size imageDimension, Handler mBackgroundHandler, CameraDevice cameraDevice) {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
            Surface surface = new Surface(texture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Collections.singletonList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    //The camera is already closed
                    if (null == cameraDevice) {
                        return;
                    }
                    // When the session is ready, we start displaying the preview.
                    cameraCaptureSessions = cameraCaptureSession;
                    updatePreview(mBackgroundHandler, cameraDevice);
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(ARActivity.this, "Configuration change", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    private void updatePreview(Handler mBackgroundHandler, CameraDevice cameraDevice) {
        if (null == cameraDevice) {
            Log.e(LOG_TAG, "updatePreview error, return");
        }
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try {
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void setTransform(Matrix matrix) {
        textureView.setTransform(matrix);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showAlert(String message) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Alert")
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    // continue with delete
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void setAspectRatio(int x, int y) {
        textureView.setAspectRatio(x, y);
    }

    @Override
    public void startDetailActivity(Business business) {
        Intent detailActivityIntent = new Intent(getViewActivity(), DetailActivity.class);
        detailActivityIntent.putExtra(BUSINESS, business);
        getViewActivity().startActivity(detailActivityIntent);
    }

    @OnClick(R.id.businessViewAR)
    public void businessViewOnClick() {
        presenter.openDetailActivity();
    }


}