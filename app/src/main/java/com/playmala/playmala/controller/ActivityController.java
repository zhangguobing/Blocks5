package com.playmala.playmala.controller;

import android.text.TextUtils;

import com.playmala.playmala.api.ApiResponse;
import com.playmala.playmala.api.RequestCallback;
import com.playmala.playmala.api.ResponseError;
import com.playmala.playmala.base.BaseController;
import com.playmala.playmala.model.Activity;
import com.playmala.playmala.model.Comment;
import com.playmala.playmala.model.request.CreateActivityParams;
import com.playmala.playmala.model.request.MainActivityListParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * author：zhangguobing on 2017/7/1 16:21
 * email：bing901222@qq.com
 */

public class ActivityController extends BaseController<ActivityController.ActivityUi,ActivityController.ActivityUiCallbacks> {

    @Override
    protected ActivityUiCallbacks createUiCallbacks(ActivityUi ui) {
        return new ActivityUiCallbacks() {
            @Override
            public void createActivity(CreateActivityParams params) {
                doCreateActivity(getId(ui), params);
            }

            @Override
            public void getActivityList(MainActivityListParams params, int page_index) {
                doGetActivityList(getId(ui), params, page_index);
            }

            @Override
            public void getActivityList(int user_id, String activity_state, int page_index) {
                doGetActivityList(getId(ui), user_id,activity_state, page_index);
            }

            @Override
            public void getActivityListByUserId(int user_id, int page_index) {
                doGetActivityListByUserId(getId(ui), user_id, page_index);
            }

            @Override
            public void getActivityListByJoinId(int join_user_id, int page_index) {
                doGetActivityListByJoinId(getId(ui), join_user_id, page_index);
            }

            @Override
            public void getActivityListByJoinIdAndState(int join_user_id, int activity_state, int page_index) {
                doGetActivityListByJoinIdAndState(getId(ui), join_user_id,activity_state, page_index);
            }

            @Override
            public void getActivityById(int activity_id) {
                doGetActivityById(getId(ui), activity_id);
            }

            @Override
            public void collectActivity(int activity_id) {
                doCollectActivity(getId(ui), activity_id);
            }

            @Override
            public void cancelCollectActivity(int activity_id) {
                doCancelCollectActivity(getId(ui), activity_id);
            }

            @Override
            public void join(int activity_id) {
                doJoin(getId(ui), activity_id);
            }

            @Override
            public void cancelJoin(int activity_id) {
                doCancelJoin(getId(ui), activity_id);
            }

            @Override
            public void report(int rpt_type, int numeric,  String content) {
                doReportActivity(getId(ui), rpt_type, numeric, content);
            }

            @Override
            public void start(int activity_id) {
                doStartActivity(getId(ui), activity_id);
            }

            @Override
            public void getComments(String activity_id,Map<String, String> paramsMap) {
                doGetComments(getId(ui),activity_id,paramsMap);
            }

            @Override
            public void addComment(String activity_id, String content, int is_team) {
                doAddComment(getId(ui), activity_id, content, is_team);
            }

            @Override
            public void cancelActivity(String activity_id) {
                doCancelActivity(getId(ui), activity_id);
            }

            @Override
            public void updateNotice(String activity_id, int is_team, String content) {
                doUpdateNotice(getId(ui),activity_id, is_team, content);
            }
        };
    }

    private void doCreateActivity(final int callingId, CreateActivityParams params){
        mApiClient.activityService()
                .createActivity(mToken,params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse>() {
                    @Override
                    public void onResponse(ApiResponse response) {
                        ActivityUi ui = findUi(callingId);
                        if(ui instanceof CreateActivityUi){
                            ((CreateActivityUi)ui).createActivitySuccess(response.message);
                        }
                    }
                    @Override
                    public void onFailure(ResponseError error) {
                        findUi(callingId).onResponseError(error);
                    }
                });
    }

    private void doGetComments(final int callingId,String activity_id, Map<String,String> map){
        mApiClient.activityService()
                .getActivityComments(activity_id,map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse<List<Comment>>>() {
                    @Override
                    public void onResponse(ApiResponse<List<Comment>> response) {
                        ActivityUi ui = findUi(callingId);
                        if(ui instanceof CommentUi){
                            ((CommentUi)ui).getCommentSuccess(response.data);
                        }
                    }
                    @Override
                    public void onFailure(ResponseError error) {
                        findUi(callingId).onResponseError(error);
                    }
                });
    }


    private void doAddComment(final int callingId,String activity_id, String content, int is_team){
        mApiClient.activityService()
                .addActivityComment(activity_id,mToken,content, is_team)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse<Comment>>() {
                    @Override
                    public void onResponse(ApiResponse<Comment> response) {
                        ActivityUi ui = findUi(callingId);
                        if(ui instanceof CommentUi){
                            ((CommentUi)ui).addCommentSuccess(response.data);
                        }
                    }
                    @Override
                    public void onFailure(ResponseError error) {
                        ActivityUi ui = findUi(callingId);
                        if(ui instanceof CommentUi){
                            ((CommentUi)ui).addCommentFail(error.getMessage());
                        }
                    }
                });
    }

    private void doGetActivityList(final int callingId, MainActivityListParams params, int page_index){
        Map<String,String> map = new HashMap<>();
        map.put("token",mToken);
        map.put("activity_type_id",params.activity_type_id +"");
        map.put("page_index",page_index+"");
        map.put("page_size","15");
        if(!TextUtils.isEmpty(params.city)){
            map.put("city",params.city);
        }
        if(!TextUtils.isEmpty(params.sort_type)){
            map.put("sort_type",params.sort_type);
        }
        if(!TextUtils.isEmpty(params.area) && !params.area.equals("所有地区")){
            map.put("area",params.area);
        }
        map.put("state",params.state);
        map.put("begin_at",params.begin_at);
        map.put("end_at",params.end_at);
        mApiClient.activityService()
                .getActivityList(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse<List<Activity>>>() {
                    @Override
                    public void onResponse(ApiResponse<List<Activity>> response) {
                        ActivityUi ui = findUi(callingId);
                        if(ui instanceof ActivityListUi){
                            ((ActivityListUi)ui).activityListCallback(response.data);
                        }
                    }
                    @Override
                    public void onFailure(ResponseError error) {
                        findUi(callingId).onResponseError(error);
                    }
                });
    }

    private void doGetActivityList(final int callingId, int user_id, String state, int page_index){
        mApiClient.activityService()
                .getActivityList(mToken,user_id,state,page_index, "15")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse<List<Activity>>>() {
                    @Override
                    public void onResponse(ApiResponse<List<Activity>> response) {
                        ActivityUi ui = findUi(callingId);
                        if(ui instanceof ActivityListUi){
                            ((ActivityListUi)ui).activityListCallback(response.data);
                        }
                    }
                    @Override
                    public void onFailure(ResponseError error) {
                        findUi(callingId).onResponseError(error);
                    }
                });
    }

    private void doGetActivityListByUserId(final int callingId, int user_id, int page_index){
        mApiClient.activityService()
                .getActivityListByUserId(mToken,user_id,page_index, "15")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse<List<Activity>>>() {
                    @Override
                    public void onResponse(ApiResponse<List<Activity>> response) {
                        ActivityUi ui = findUi(callingId);
                        if(ui instanceof ActivityListUi){
                            ((ActivityListUi)ui).activityListCallback(response.data);
                        }
                    }
                    @Override
                    public void onFailure(ResponseError error) {
                        findUi(callingId).onResponseError(error);
                    }
                });
    }

    private void doGetActivityListByJoinId(final int callingId, int join_user_id, int page_index){
        mApiClient.activityService()
                .getActivityListByJoinId(mToken,join_user_id,page_index, "15")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse<List<Activity>>>() {
                    @Override
                    public void onResponse(ApiResponse<List<Activity>> response) {
                        ActivityUi ui = findUi(callingId);
                        if(ui instanceof ActivityListUi){
                            ((ActivityListUi)ui).activityListCallback(response.data);
                        }
                    }
                    @Override
                    public void onFailure(ResponseError error) {
                        findUi(callingId).onResponseError(error);
                    }
                });
    }

    private void doGetActivityListByJoinIdAndState(final int callingId, int join_user_id, int state, int page_index){
        mApiClient.activityService()
                .getActivityListByJoinIdAndState(mToken,join_user_id,state,page_index, "15")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse<List<Activity>>>() {
                    @Override
                    public void onResponse(ApiResponse<List<Activity>> response) {
                        ActivityUi ui = findUi(callingId);
                        if(ui instanceof ActivityListUi){
                            ((ActivityListUi)ui).activityListCallback(response.data);
                        }
                    }
                    @Override
                    public void onFailure(ResponseError error) {
                        findUi(callingId).onResponseError(error);
                    }
                });
    }

    //收藏活动
    private void doCollectActivity(final int callingId, int activity_id){
        mApiClient.activityService()
                .collectActivity(activity_id,mToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse>() {
                    @Override
                    public void onResponse(ApiResponse response) {
                        ActivityUi ui = findUi(callingId);
                        if(ui instanceof ActivityDetailUi){
                            ((ActivityDetailUi)ui).onCollectSuccess(response.message);
                        }
                    }

                    @Override
                    public void onFailure(ResponseError error) {
                        findUi(callingId).onResponseError(error);
                    }
                });
    }


    //取消收藏活动
    private void doCancelCollectActivity(final int callingId, int activity_id){
        mApiClient.activityService()
                .cancelCollectActivity(mToken,activity_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse>() {
                    @Override
                    public void onResponse(ApiResponse response) {
                        ActivityUi ui = findUi(callingId);
                        if(ui instanceof ActivityDetailUi){
                            ((ActivityDetailUi)ui).cancelCollectSuccess(response.message);
                        }
                    }

                    @Override
                    public void onFailure(ResponseError error) {
                        findUi(callingId).onResponseError(error);
                    }
                });
    }

    //取消活动
    private void doCancelActivity(final int callingId, String activity_id){
        mApiClient.activityService()
                .cancelActivity(activity_id,mToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse<Activity>>() {
                    @Override
                    public void onResponse(ApiResponse<Activity> response) {
                        ActivityUi ui = findUi(callingId);
                        if(ui instanceof ActivityDetailUi){
                            ((ActivityDetailUi)ui).cancelActivitySuccess(response.data);
                        }
                    }

                    @Override
                    public void onFailure(ResponseError error) {
                        findUi(callingId).onResponseError(error);
                    }
                });
    }

    private void doUpdateNotice(final int callingId, String activity_id, int is_team, String content){
        mApiClient.activityService()
                .updateNotice(activity_id,mToken, is_team, content)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse>() {
                    @Override
                    public void onResponse(ApiResponse response) {
                        ActivityUi ui = findUi(callingId);
                        if(ui instanceof NoticeUi){
                            ((NoticeUi)ui).updateNoticeSuccess();
                        }
                    }

                    @Override
                    public void onFailure(ResponseError error) {
                        findUi(callingId).onResponseError(error);
                    }
                });
    }


    private void doReportActivity(final int callingId, int rpt_type, int numeric, String content){
        mApiClient.activityService()
                .reportActivity(mToken,rpt_type,numeric,content)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse>() {
                    @Override
                    public void onResponse(ApiResponse response) {
                        ActivityUi ui = findUi(callingId);
                        if(ui instanceof ActivityDetailUi){
                            ((ActivityDetailUi)ui).reportSuccess();
                        }else if(ui instanceof CommentUi){
                            ((CommentUi)ui).reportSuccess();
                        }
                    }

                    @Override
                    public void onFailure(ResponseError error) {
                        findUi(callingId).onResponseError(error);
                    }
                });
    }

    private void doStartActivity(final int callingId, int activity_id){
        mApiClient.activityService()
                .startActivity(activity_id,mToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse>() {
                    @Override
                    public void onResponse(ApiResponse response) {
                        ActivityUi ui = findUi(callingId);
                        if(ui instanceof SignUpUi) {
                            ((SignUpUi)ui).startActivitySuccess();
                        }
                    }

                    @Override
                    public void onFailure(ResponseError error) {
                        findUi(callingId).onResponseError(error);
                    }
                });
    }

    private void doGetActivityById(final int callingId, int activity_id){
        mApiClient.activityService()
                .getActivityById(activity_id,mToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse<Activity>>() {
                    @Override
                    public void onResponse(ApiResponse<Activity> response) {
                        ActivityUi ui = findUi(callingId);
                        if(ui instanceof ActivityDetailUi){
                            ((ActivityDetailUi)ui).getActivitySuccess(response.data);
                        }
                    }
                    @Override
                    public void onFailure(ResponseError error) {
                        findUi(callingId).onResponseError(error);
                    }
                });
    }

    //报名
    private void doJoin(final int callingId, int activity_id) {
        mApiClient.activityUserService()
                .joinActivity(activity_id, mToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse>() {
                    @Override
                    public void onResponse(ApiResponse response) {
                        ActivityController.ActivityUi ui = findUi(callingId);
                        if (ui instanceof ActivityController.ActivityDetailUi) {
                            ((ActivityController.ActivityDetailUi) ui).joinSuccess();
                        }
                    }

                    @Override
                    public void onFailure(ResponseError error) {
                        ActivityController.ActivityUi ui = findUi(callingId);
                        if (ui instanceof ActivityController.ActivityDetailUi) {
                            ((ActivityController.ActivityDetailUi) ui).joinFail(error);
                        }
                    }
                });
    }

    //取消报名
    private void doCancelJoin(final int callingId, int activity_id){
        mApiClient.activityUserService()
                .cancelJoinActivity(activity_id,mToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse>() {
                    @Override
                    public void onResponse(ApiResponse response) {
                        ActivityController.ActivityUi ui = findUi(callingId);
                        if(ui instanceof ActivityController.ActivityDetailUi){
                            ((ActivityController.ActivityDetailUi)ui).cancelJoinSuccess();
                        }
                    }
                    @Override
                    public void onFailure(ResponseError error) {
                        findUi(callingId).onResponseError(error);
                    }
                });
    }

    public interface ActivityUiCallbacks{
        void createActivity(CreateActivityParams params);
        void getActivityList(MainActivityListParams params, int page_index);
        void getActivityList(int user_id, String activity_state, int page_index);
        void getActivityListByUserId(int user_id, int page_index);
        void getActivityListByJoinId(int join_user_id, int page_index);
        void getActivityListByJoinIdAndState(int join_user_id, int activity_state, int page_index);
        void getActivityById(int activity_id);
        void collectActivity(int activity_id);
        void cancelCollectActivity(int activity_id);
        void join(int activity_id);
        void cancelJoin(int activity_id);
        void report(int rpt_type,int numeric,String content);
        void start(int activity_id);
        void getComments(String activity_id,Map<String,String> paramsMap);
        void addComment(String activity_id,String content, int is_team);
        void cancelActivity(String activity_id);
        void updateNotice(String activity_id, int is_team, String content);
    }

    public interface ActivityUi extends BaseController.Ui<ActivityUiCallbacks>{

    }

    public interface CreateActivityUi extends ActivityUi{
        void createActivitySuccess(String msg);
    }

    public interface ActivityListUi extends ActivityUi{
        void activityListCallback(List<Activity> activities);
    }

    public interface SignUpUi extends ActivityUi{
        void startActivitySuccess();
    }

    public interface ActivityDetailUi extends ActivityUi{
        void getActivitySuccess(Activity activity);
        void onCollectSuccess(String msg);
        void cancelCollectSuccess(String msg);
        void joinSuccess();
        void joinFail(ResponseError error);
        void cancelJoinSuccess();
        void reportSuccess();
        void cancelActivitySuccess(Activity activity);
    }

    public interface CommentUi extends ActivityUi{
        void getCommentSuccess(List<Comment> comments);
        void addCommentSuccess(Comment comment);
        void addCommentFail(String msg);
        void reportSuccess();
    }

    public interface NoticeUi extends ActivityUi{
        void updateNoticeSuccess();
    }
}
