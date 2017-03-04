package com.youth.xf;


import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.youth.xf.utils.AFengUtils.PerfectClickListener;
import com.youth.xframe.base.XFragment;
import com.youth.xframe.utils.log.XLog;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;


public abstract class BaseFragment extends XFragment {
    protected View mContentView;
    protected boolean mIsVisible = false;
    private LinearLayout mProgressBar, mRefresh;
    protected RelativeLayout mContainer, mContent;
    private AnimationDrawable mAnimationDrawable;
    private CompositeSubscription mCompositeSubscription;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView= inflater.inflate(R.layout.afeng_fragment_base, container, false);

        LayoutInflater mInflater = LayoutInflater.from(getContext());
        mContent = (RelativeLayout) mInflater.inflate(setContent(), null, false);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mContent.setLayoutParams(params);
        mContainer = this.getView(R.id.container);
        mContainer.addView(mContent);

        return mContentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRefresh = this.getView(R.id.ll_error_refresh);
        mProgressBar = this.getView(R.id.ll_progress_bar);
        ImageView img = this.getView(R.id.img_progress);

        mAnimationDrawable = (AnimationDrawable) img.getDrawable();
        if (mAnimationDrawable.isRunning() == false) {
            mAnimationDrawable.start();
        }

        mRefresh.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                showLoading();
                onRefresh();
            }
        });

        mContent.setVisibility(View.GONE);
    }

    /**
     * 布局
     */
    public abstract int setContent();


    /**
     * 显示加载中状态
     */
    protected void showLoading() {
        if (mProgressBar.getVisibility() != View.VISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
        if (mAnimationDrawable.isRunning() == false) {
            mAnimationDrawable.start();
        }
        if (mContent.getVisibility() != View.GONE) {
            mContent.setVisibility(View.GONE);
        }
        if (mRefresh.getVisibility() != View.GONE) {
            mRefresh.setVisibility(View.GONE);
        }
    }

    /**
     * 加载完成的状态
     */
    protected void showContentView() {
        if (mProgressBar.getVisibility() != View.GONE) {
            mProgressBar.setVisibility(View.GONE);
        }
        // 停止动画
        if (mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
        }
        if (mRefresh.getVisibility() != View.GONE) {
            mRefresh.setVisibility(View.GONE);
        }
        if (mContent.getVisibility() != View.VISIBLE) {
            mContent.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 加载失败点击重新加载的状态
     */
    protected void showError() {
        if (mProgressBar.getVisibility() != View.GONE) {
            mProgressBar.setVisibility(View.GONE);
        }
        // 停止动画
        if (mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
        }
        if (mRefresh.getVisibility() != View.VISIBLE) {
            mRefresh.setVisibility(View.VISIBLE);
        }
        if (mContent.getVisibility() != View.GONE) {
            mContent.setVisibility(View.GONE);
        }
    }


    /**
     * 加载失败后点击后的操作
     */
    protected void onRefresh() {

    }

    /**
     * 在这里实现Fragment数据的缓加载.
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            mIsVisible = true;
            onVisible();
        } else {
            mIsVisible = false;
            onInvisible();
        }
    }

    protected abstract void onVisible();

    protected abstract void onInvisible();

    @SuppressWarnings("unchecked")
    public final <E extends View> E getView(@IdRes int id) {
        try {
            return (E) super.getView().findViewById(id);
        } catch (ClassCastException ex) {
            XLog.w("视图元素类型强制失败！", ex);
            throw ex;
        }
    }

    public void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(s);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            this.mCompositeSubscription.unsubscribe();
        }
    }

    public void removeSubscription() {
        if (this.mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            this.mCompositeSubscription.unsubscribe();
        }
    }

}
