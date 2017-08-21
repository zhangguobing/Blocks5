package com.bing.blocks5.ui.activity.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bing.blocks5.R;
import com.bing.blocks5.base.BaseController;
import com.bing.blocks5.base.BasePresenterFragment;
import com.bing.blocks5.base.ContentView;
import com.bing.blocks5.controller.ActivityUserController;
import com.bing.blocks5.model.Activity;
import com.bing.blocks5.model.ActivityUser;
import com.bing.blocks5.polling.PollingManager;
import com.bing.blocks5.ui.activity.adapter.SignInUserAdapter;
import com.bing.blocks5.ui.user.UserDetailActivity;
import com.bing.blocks5.util.ActivityDataConvert;
import com.google.zxing.client.android.encode.QREncode;

import java.util.List;

import butterknife.Bind;

/**
 * Created by tian on 2017/8/20.
 */
@ContentView(R.layout.fragment_sign_in)
public class SignInFragment extends BasePresenterFragment<ActivityUserController.ActivityUserUiCallbacks>
    implements ActivityUserController.SignUpList{

    public static final String KEY_ACTIVITY = "key_activity";
    public static final String KEY_TYPE = "key_type";

    public static final int TYPE_NO_CONFIRM = 0;
    public static final int TYPE_HAS_CONFIRMED = 1;

    @Bind(R.id.listView)
    ListView mListView;

    private SignInUserAdapter mAdapter;

    private int type;
    private int activity_id;

    public static SignInFragment newInstance(int type, Activity activity) {
        Bundle args = new Bundle();
        args.putInt(KEY_TYPE, type);
        args.putParcelable(KEY_ACTIVITY, activity);
        SignInFragment fragment = new SignInFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        type = getArguments().getInt(KEY_TYPE);
        Activity activity = getArguments().getParcelable(KEY_ACTIVITY);


        View headView = View.inflate(getContext(),R.layout.layout_sign_list_header, null);
        ImageView barcodeImg = (ImageView) headView.findViewById(R.id.iv_barcode);
        TextView timeTv = (TextView) headView.findViewById(R.id.time);
        TextView statusTv = (TextView) headView.findViewById(R.id.status);

        activity_id = activity.getId();
        final Bitmap bitmap = QREncode.encodeQRWithoutWhite(new QREncode.Builder(getContext())
                .setColor(getResources().getColor(R.color.black))
                .setContents("http://www.blocks5.com?activityId="+ activity.getId())
                .build());
        barcodeImg.setImageBitmap(bitmap);

        timeTv.setText("时间：" + activity.getBegin_at() +"-"+ activity.getEnd_at());
        String statusStr = ActivityDataConvert.getActivityStateById(activity.getState()+"");
        if(activity.getState() != 2){
            statusStr = statusStr + "，不可签到";
        }
        statusTv.setText(statusStr);


        mListView.addHeaderView(headView);

        mListView.setOnItemClickListener((adapterView, view, i, l) -> {
            if(i > 0) UserDetailActivity.create(getContext(),mAdapter.getItem(i-1).getUser_id());
        });

        mAdapter = new SignInUserAdapter(getContext());
        mListView.setAdapter(mAdapter);

        getCallbacks().getUsersByActivityId(type, activity_id, "", 1, 1, "99999");
    }

    @Override
    protected BaseController getPresenter() {
        return new ActivityUserController();
    }

    @Override
    public void onSignUpList(List<ActivityUser> users) {
        mAdapter.replace(users);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(isVisibleToUser){
            PollingManager.getInstance().addTask("task" + type, 0, 3, () -> getCallbacks().getUsersByActivityId(type, activity_id, "", 1, 1, "99999"));
        }else{
            PollingManager.getInstance().removeTask("task" + type);
        }
    }
}
