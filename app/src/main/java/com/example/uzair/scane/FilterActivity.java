package com.example.uzair.scane;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;

import net.doo.snap.lib.detector.ContourDetector;
import net.doo.snap.ui.EditPolygonImageView;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class FilterActivity extends Fragment {

    ImageView image;
    RadioGroup radioGroup;
    Bitmap orgImage;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View baseView =  inflater.inflate(R.layout.activity_filter, container, false);

            orgImage=((main_dashbord) getActivity()).getCenterImage();
            image=baseView.findViewById(R.id.filterImage);
            image.setImageBitmap(orgImage);
        baseView.findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removethisFragement(true);
            }
        });
        baseView.findViewById(R.id.Cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removethisFragement(false);
            }
        });

        radioGroup=baseView.findViewById(R.id.radio_btn);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged (RadioGroup group,int checkedId){
                // checkedId is the RadioButton selected
                if (orgImage == null)
                    return;
                int result = ContourDetector.IMAGE_FILTER_NONE;
                switch (checkedId) {
                    case R.id._b_w:
                        result = ContourDetector.IMAGE_FILTER_PURE_BINARIZED;
                        break;
                    case R.id._black:
                        result = ContourDetector.IMAGE_FILTER_BINARIZED;
                        break;
                    case R.id._clr:
                        result = ContourDetector.IMAGE_FILTER_COLOR_DOCUMENT;
                        break;
                    case R.id._gray:
                        result = ContourDetector.IMAGE_FILTER_GRAY;
                        break;
                    case R.id._enh:
                        result = ContourDetector.IMAGE_FILTER_COLOR_ENHANCED;
                        break;
                    case R.id._none:
                        result = ContourDetector.IMAGE_FILTER_NONE;
                        break;
                }

                Bitmap documentImage = new ContourDetector().processImageF(
                        orgImage, new ArrayList<PointF>(), result);
                image.setImageBitmap(documentImage);

                }
            });

        return  baseView;
    }
    private void removethisFragement(boolean save)
    {
        if(save)
        ((main_dashbord) getActivity()).setCenterImage(((BitmapDrawable) image.getDrawable()).getBitmap());


        getActivity().getSupportFragmentManager()
                .beginTransaction().remove(this).commit();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().findViewById(R.id.main_screen).setVisibility(View.VISIBLE);

    }

}
