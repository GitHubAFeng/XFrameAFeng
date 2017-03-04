package com.youth.xf.ui.demo.test;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import com.youth.xf.R;
import com.youth.xf.ui.demo.AFengFragment;

/**
 * 作者： AFeng
 * 时间：2017/3/2
 */


@SuppressLint("ValidFragment")
public class SimpleCardFragment extends AFengFragment {
    private String mTitle;
    public static SimpleCardFragment getInstance(String title) {
        SimpleCardFragment sf = new SimpleCardFragment();
        sf.mTitle = title;
        return sf;
    }

    @Override
    protected void onVisible() {

    }

    @Override
    protected void onInvisible() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.test_simple_card;
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {
        TextView card_title_tv = getViewById(R.id.card_title_tv);
        card_title_tv.setText(mTitle);
    }
}