package com.zbxn.main.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pub.base.BaseFragment;
import com.pub.utils.FragmentAdapter;
import com.pub.widget.FloatingActionButton.FloatingActionButton;
import com.pub.widget.FloatingActionButton.FloatingActionMenu;
import com.pub.widget.smarttablayout.SmartTabLayout;
import com.zbxn.R;
import com.zbxn.main.activity.dynamic.DynamicFragment;
import com.zbxn.main.activity.dynamic.FriendsMessageFragment;
import com.zbxn.main.activity.dynamic.PublishMessageActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainMessageFragment extends BaseFragment {

    @BindView(R.id.mSmartTabLayout)
    SmartTabLayout mSmartTabLayout;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;
    @BindView(R.id.menu)
    FloatingActionMenu mMenu;
    @BindView(R.id.menu_collection)
    FloatingActionButton menuCollection;
    @BindView(R.id.menu_message)
    FloatingActionButton menuMessage;

    private FragmentAdapter mAdapter;

    private int mIndex = 0;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            if (mMenu.isOpened()) {
                mMenu.toggle(false);
            }
        } else {
        }
    }

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_main_message, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initialize(View root, @Nullable Bundle savedInstanceState) {
        initView();

        mIndex = getArguments().getInt("index");
        if (0 == mIndex) {
            mViewPager.setCurrentItem(0);
        } else {
            mViewPager.setCurrentItem(1);
        }
    }

    public void setRefresh(final int index) {
        try {
            mSmartTabLayout.post(new Runnable() {
                @Override
                public void run() {
                    if (0 == index) {
                        mViewPager.setCurrentItem(0);
                    } else {
                        mViewPager.setCurrentItem(1);
                    }
                }
            });
        } catch (Exception e) {

        }
    }

    private void initView() {
        mMenu.setVisibility(View.GONE);
        mMenu.setClosedOnTouchOutside(true);
        mMenu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if (opened) {//已打开
                    mMenu.setBackgroundColor(Color.parseColor("#ccffffff"));
                } else {//未打开
                    mMenu.setBackgroundColor(Color.parseColor("#00ffffff"));
                }
            }
        });

        mAdapter = new FragmentAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mAdapter);
        Bundle bundle = null;
        DynamicFragment fragment1 = new DynamicFragment();
        fragment1.setFragmentTitle("应用消息");
        bundle = new Bundle();
        fragment1.setArguments(bundle);
        FriendsMessageFragment fragment2 = new FriendsMessageFragment();
        fragment2.setFragmentTitle("好友消息");
        bundle = new Bundle();
        fragment2.setArguments(bundle);
        mAdapter.addFragment(fragment1);
        mSmartTabLayout.setViewPager(mViewPager);
    }

    @OnClick({R.id.menu_collection, R.id.menu_message})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu_collection://查看收藏
                break;
            case R.id.menu_message://发布消息
                startActivity(new Intent(getContext(), PublishMessageActivity.class));
                break;
        }
        setCancel();
    }

    public void setCancel() {
        if (mMenu.isOpened()) {
            mMenu.toggle(false);
        }
    }

}
