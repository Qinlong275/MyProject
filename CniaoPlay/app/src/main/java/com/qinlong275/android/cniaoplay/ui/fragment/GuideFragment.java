package com.qinlong275.android.cniaoplay.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qinlong275.android.cniaoplay.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 秦龙 on 2018/2/7.
 */

public class GuideFragment extends Fragment {


    public static final String IMG_ID = "IMG_ID";
    public static final String COLOR_ID = "COLOR_ID";
    public static final String TEXT_ID = "TEXT_ID";
    @BindView(R.id.imgView)
    ImageView mImgView;
    @BindView(R.id.text)
    TextView mText;
    @BindView(R.id.rootView)
    LinearLayout mRootView;
    Unbinder unbinder;

    private View mView;

    public static GuideFragment newInstance(int imgId, int bgColorResId, int textResId) {
        GuideFragment fragment = new GuideFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(IMG_ID, imgId);
        bundle.putInt(COLOR_ID, bgColorResId);
        bundle.putInt(TEXT_ID, textResId);

        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_guide, container, false);
        unbinder = ButterKnife.bind(this, mView);
        initData();
        return mView;
    }

    private void initData() {
        Bundle  args = getArguments();

        int colorId = args.getInt(COLOR_ID);
        int imgId = args.getInt(IMG_ID);
        int textId = args.getInt(TEXT_ID);


        mRootView.setBackgroundColor(getResources().getColor(colorId));
        mImgView.setImageResource(imgId);
        mText.setText(textId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
