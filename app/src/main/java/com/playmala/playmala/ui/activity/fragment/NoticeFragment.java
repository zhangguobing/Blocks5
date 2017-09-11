package com.playmala.playmala.ui.activity.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.playmala.playmala.R;
import com.playmala.playmala.base.BaseController;
import com.playmala.playmala.base.BasePresenterFragment;
import com.playmala.playmala.base.ContentView;
import com.playmala.playmala.controller.ActivityController;
import com.playmala.playmala.model.event.ActivityNoticeUpdateEvent;
import com.playmala.playmala.util.EventUtil;
import com.playmala.playmala.util.ToastUtil;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by tian on 2017/8/20.
 */
@ContentView(R.layout.fragment_notice)
public class NoticeFragment extends BasePresenterFragment<ActivityController.ActivityUiCallbacks>
 implements ActivityController.NoticeUi{

    public static final String KEY_TYPE = "key_type";
    public static final String KEY_ACTIVITY_Id = "key_activity_id";
    public static final String KEY_NOTICE_CONTENT = "key_notice_content";

    public static final int TYPE_GUEST = 0;
    public static final int TYPE_TEAM = 1;

    @Bind(R.id.et_content)
    EditText mContentEt;
    @Bind(R.id.btn_publish)
    Button mPublishBtn;

    private String mActivityId;
    private int type;

    public static NoticeFragment newInstance(int type,String activity_id,String noticeContent) {
        Bundle args = new Bundle();
        args.putInt(KEY_TYPE, type);
        args.putString(KEY_ACTIVITY_Id, activity_id);
        args.putString(KEY_NOTICE_CONTENT, noticeContent);
        NoticeFragment fragment = new NoticeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        type = getArguments().getInt(KEY_TYPE);
        mActivityId = getArguments().getString(KEY_ACTIVITY_Id);
        String noticeContent = getArguments().getString(KEY_NOTICE_CONTENT);
        if(type == TYPE_GUEST){
            mContentEt.setHint("请输入公告（所有人可见）");
        }else{
            mContentEt.setHint("请输入公告（仅限团队成员可见）");
        }
        if(!TextUtils.isEmpty(noticeContent)){
            mContentEt.setText(noticeContent);
            mContentEt.setSelection(noticeContent.length());
        }
    }

    @OnClick(R.id.btn_publish)
    public void onClick(View view){
        if(view.getId() == R.id.btn_publish){
            publishClick();
        }
    }

    @Override
    protected BaseController getPresenter() {
        return new ActivityController();
    }

    private void publishClick(){
        String content = mContentEt.getText().toString().trim();
        if(TextUtils.isEmpty(content)){
            ToastUtil.showText("内容不能为空");
            return;
        }
        showLoading(R.string.label_being_something);
        getCallbacks().updateNotice(mActivityId, type, content);
    }

    @Override
    public void updateNoticeSuccess() {
        ToastUtil.showText("发布成功");
        getActivity().finish();
        EventUtil.sendEvent(new ActivityNoticeUpdateEvent(type+"",mContentEt.getText().toString().trim()));
    }
}
