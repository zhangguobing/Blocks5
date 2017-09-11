package com.playmala.playmala.ui.activity.fragment;

import android.os.Bundle;

import com.playmala.playmala.base.BaseController;
import com.lcodecore.tkrefreshlayout.utils.DensityUtil;
import com.playmala.playmala.base.BaseAdapter;
import com.playmala.playmala.base.BaseListFragment;
import com.playmala.playmala.model.Activity;
import com.playmala.playmala.controller.ActivityController;
import com.playmala.playmala.ui.activity.ActivityDetailActivity;
import com.playmala.playmala.ui.search.adapter.ActivityListAdapter;
import com.playmala.playmala.ui.search.adapter.holder.ActivityViewHolder;
import com.playmala.playmala.widget.BottomSpaceItemDecoration;

import java.util.List;

/**
 * author：zhangguobing on 2017/7/7 13:52
 * email：bing901222@qq.com
 */

public class JoinerActivityListFragment extends BaseListFragment<Activity,ActivityViewHolder,ActivityController.ActivityUiCallbacks>
        implements ActivityController.ActivityListUi{

    private static final String EXTRA_ACTIVITY_STATE = "extra_activity_state";
    //活动参与者ID
    private static final String EXTRA_JOIN_USER_ID = "extra_join_user_id";

    private int mState;
    private int mJoinUserId;

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mRecyclerView.addItemDecoration(new BottomSpaceItemDecoration(DensityUtil.dp2px(getContext(),20)));
        Bundle bundle = getArguments();
        mState = bundle.getInt(EXTRA_ACTIVITY_STATE);
        mJoinUserId = bundle.getInt(EXTRA_JOIN_USER_ID);
        getCallbacks().getActivityListByJoinIdAndState(mJoinUserId,mState,mPage);
    }

    @Override
    protected String getEmptyTitle() {
        return "暂无活动";
    }

    /**
     * @param activity_state 活动状态
     * @param join_user_id 活动参与者ID
     */
    public static JoinerActivityListFragment newInstance(int activity_state, int join_user_id) {
        Bundle args = new Bundle();
        args.putInt(EXTRA_ACTIVITY_STATE, activity_state);
        args.putInt(EXTRA_JOIN_USER_ID,join_user_id);
        JoinerActivityListFragment fragment = new JoinerActivityListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void refreshPage() {
        getCallbacks().getActivityListByJoinIdAndState(mJoinUserId,mState,mPage = 1);
    }

    @Override
    protected void nextPage() {
        getCallbacks().getActivityListByJoinIdAndState(mJoinUserId,mState,mPage);
    }

    @Override
    protected BaseController getPresenter() {
        return new ActivityController();
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
