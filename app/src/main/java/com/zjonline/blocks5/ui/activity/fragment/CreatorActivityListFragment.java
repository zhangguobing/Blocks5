package com.zjonline.blocks5.ui.activity.fragment;

import android.os.Bundle;

import com.lcodecore.tkrefreshlayout.utils.DensityUtil;
import com.zjonline.blocks5.base.BaseAdapter;
import com.zjonline.blocks5.base.BaseListFragment;
import com.zjonline.blocks5.base.BasePresenter;
import com.zjonline.blocks5.model.Activity;
import com.zjonline.blocks5.presenter.ActivityPresenter;
import com.zjonline.blocks5.ui.activity.ActivityDetailActivity;
import com.zjonline.blocks5.ui.search.adapter.ActivityListAdapter;
import com.zjonline.blocks5.ui.search.adapter.holder.ActivityViewHolder;
import com.zjonline.blocks5.widget.BottomSpaceItemDecoration;

import java.util.List;

/**
 * author：zhangguobing on 2017/7/7 13:52
 * email：bing901222@qq.com
 */

public class CreatorActivityListFragment extends BaseListFragment<Activity,ActivityViewHolder,ActivityPresenter.ActivityUiCallbacks>
        implements ActivityPresenter.ActivityListUi{

    private static final String EXTRA_ACTIVITY_STATE = "extra_activity_state";
    //活动创建者ID
    private static final String EXTRA_USER_ID = "extra_user_id";

    private int mState;
    private int mUserId;

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mRecyclerView.addItemDecoration(new BottomSpaceItemDecoration(DensityUtil.dp2px(getContext(),20)));
        Bundle bundle = getArguments();
        mState = bundle.getInt(EXTRA_ACTIVITY_STATE);
        mUserId = bundle.getInt(EXTRA_USER_ID);
        getCallbacks().getActivityList(mState, mUserId,mPage);
    }

    /**
     * @param activity_state 活动状态
     * @param user_id 活动创建者ID
     */
    public static CreatorActivityListFragment newInstance(int activity_state, int user_id) {
        Bundle args = new Bundle();
        args.putInt(EXTRA_ACTIVITY_STATE, activity_state);
        args.putInt(EXTRA_USER_ID,user_id);
        CreatorActivityListFragment fragment = new CreatorActivityListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void refreshPage() {
        getCallbacks().getActivityList(mState, mUserId,mPage = 1);
    }

    @Override
    protected BasePresenter getPresenter() {
        return new ActivityPresenter();
    }

    @Override
    protected void onItemClick(int position, Activity item) {
        ActivityDetailActivity.create(getContext(),item.getId()+"");
    }

    @Override
    protected BaseAdapter<Activity, ActivityViewHolder> getAdapter() {
        return new ActivityListAdapter();
    }

    @Override
    public void activityListCallback(List<Activity> activities) {
        onFinishRequest(activities);
    }
}
