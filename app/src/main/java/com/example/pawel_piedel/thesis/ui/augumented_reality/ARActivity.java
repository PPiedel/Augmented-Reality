package com.example.pawel_piedel.thesis.ui.augumented_reality;

import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pawel_piedel.thesis.R;
import com.example.pawel_piedel.thesis.data.model.Business;
import com.example.pawel_piedel.thesis.ui.base.BaseActivity;

import java.util.Arrays;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ARActivity extends BaseActivity implements ARContract.View {
    private final String TAG = ARActivity.class.getSimpleName();
    protected CameraCaptureSession cameraCaptureSessions;
    protected CaptureRequest.Builder captureRequestBuilder;
    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            //open your camera here
            presenter.openCamera(width,height);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            presenter.configureTransform(width,height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    };


    @BindView(R.id.texture)
    AutoFitTextureView textureView;

    @BindView(R.id.azimuth)
    TextView azimuthTextView;

    @BindView(R.id.location)
    TextView locationTextView;

    @BindView(R.id.businessTitle)
    TextView businessTitle;

    @Inject
    ARPresenter<ARContract.View> presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        presenter.attachView(this);
        presenter.managePermissions();
        presenter.setReactiveSensors(this);

        textureView.setSurfaceTextureListener(textureListener);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
        presenter.startBackgroundThread();
        if (textureView.isAvailable()) {
            presenter.openCamera(textureView.getWidth(),textureView.getHeight());
        } else {
            textureView.setSurfaceTextureListener(textureListener);
        }

        presenter.startObservingAzimuth();
        presenter.startObservingLocation();
    }

    @Override
    protected void onPause() {
        Log.e(TAG, "onPause");
        //closeCamera();
        presenter.stopBackgroundThread();
        presenter.unsubscribeAll();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.closeCamera();
        presenter.detachView();
    }

    @Override
    public void showBusinessOnScreen(Business business) {
       businessTitle.setVisibility(View.VISIBLE);
       businessTitle.setText(""+business.getName());
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
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    //The camera is already closed
                    if (null == cameraDevice) {
                        return;
                    }
                    // When the session is ready, we start displaying the preview.
                    cameraCaptureSessions = cameraCaptureSession;
                    updatePreview(mBackgroundHandler,cameraDevice);
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

    protected void updatePreview(Handler mBackgroundHandler,CameraDevice cameraDevice) {
        if (null == cameraDevice) {
            Log.e(TAG, "updatePreview error, return");
        }
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try {
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void setTransform(Matrix matrix){
        textureView.setTransform(matrix);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void setAzimuthText(int  azimuth) {
        azimuthTextView.setText(String.format("%d", azimuth));
    }

    @Override
    public void setLocationText(Location location){
        locationTextView.setText(String.format("Lat %s Long %s", location.getLatitude(), location.getLongitude()));
    }

    @Override
    public void setAspectRatio(int x,int y){
        textureView.setAspectRatio(x,y);
    }


}
