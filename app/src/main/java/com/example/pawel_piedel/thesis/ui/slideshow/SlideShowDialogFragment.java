package com.example.pawel_piedel.thesis.ui.slideshow;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.pawel_piedel.thesis.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Pawel_Piedel on 17.08.2017.
 */

public class SlideShowDialogFragment extends DialogFragment {
    private static final String LOG_TAG = SlideShowDialogFragment.class.getSimpleName();

    @BindView(R.id.image_slider_view_pager)
    private
    ViewPager viewPager;

    private ImagesPagerAdapter imagesPagerAdapter;
    private ArrayList<String> images;

    public static SlideShowDialogFragment newInstance() {
        return new SlideShowDialogFragment();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d!=null){
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
        d.setCancelable(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MY_DIALOG);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialogfragment_slideshow, container, false);

        ButterKnife.bind(this,v);

        getDialog().requestWindowFeature(STYLE_NO_TITLE);
        setCancelable(true);

        images = (ArrayList<String>) getArguments().getSerializable("images");
       // Log.d(LOG_TAG, Arrays.toString(images.toArray()));
        int selectedPosition = getArguments().getInt("position");
       // Log.d(LOG_TAG,""+selectedPosition);


        imagesPagerAdapter = new ImagesPagerAdapter();
        viewPager.setAdapter(imagesPagerAdapter);

        viewPager.setCurrentItem(selectedPosition, false);

        return v;
    }


    public class ImagesPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        @BindView(R.id.image_preview)
        ImageView imageViewPreview;


        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.slideshow_image_item, container, false);

            ButterKnife.bind(this,view);



            Glide.with(getActivity()).load(images.get(position))
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageViewPreview);

            container.addView(view);

            return view;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
