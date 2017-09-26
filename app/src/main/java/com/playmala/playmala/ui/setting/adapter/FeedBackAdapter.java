package com.playmala.playmala.ui.setting.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.playmala.playmala.AppCookie;
import com.playmala.playmala.Constants;
import com.playmala.playmala.R;
import com.playmala.playmala.base.BaseAdapter;
import com.playmala.playmala.model.FeedBack;
import com.playmala.playmala.util.ImageLoadUtil;
import com.playmala.playmala.util.Objects;
import com.playmala.playmala.util.TimeUtil;

/**
 * author：zhangguobing on 2017/7/20 17:00
 * email：bing901222@qq.com
 */

public class FeedBackAdapter extends BaseAdapter<FeedBack,RecyclerView.ViewHolder> {

    private static final int TYPE_TEXT_LEFT = 0;
    private static final int TYPE_TEXT_RIGHT = 1;

    /**传到外层的点击监听器，用来处理头像点击等需要外层处理的事件*/
    private View.OnClickListener outsideClickListener = null;

    public FeedBackAdapter(View.OnClickListener outsideClickListener) {
        this.outsideClickListener = outsideClickListener;
    }

    @Override
    public int getViewLayoutId(int viewType) {
        return viewType == TYPE_TEXT_LEFT ? R.layout.item_chat_text_left : R.layout.item_chat_text_right;
    }

    @Override
    public int getItemViewType(int position) {
        FeedBack feedBack = getItem(position);
        return feedBack.getIs_reply() == 0 ? TYPE_TEXT_RIGHT: TYPE_TEXT_LEFT;
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(View view, int viewType) {
        return new TextHolder(view);
    }

    @Override
    public void bindViewHolder(RecyclerView.ViewHolder holder, FeedBack feedBack, int position) {
        if(holder instanceof BaseMessageHolder){
            BaseMessageHolder baseMessageHolder = (BaseMessageHolder) holder;
            setBase(feedBack, baseMessageHolder, position);
        }
        if(holder instanceof TextHolder){
            TextHolder textHolder = (TextHolder) holder;
            String text = feedBack.getContent();
            textHolder.text_content.setText(text);
        }
    }

    /**显示基本参数*/
    private void setBase(FeedBack feedBack ,BaseMessageHolder holder,int pos){
        setTime(feedBack,holder.text_time, pos);
        setPhoto(feedBack, holder, pos);
        setSendState(feedBack, holder,pos);
    }

    private void setTime(FeedBack feedBack, TextView text_time, int pos) {
        if(pos == 0){
            //第一条显示时间
            text_time.setVisibility(View.VISIBLE);
            TimeUtil.displayTime(feedBack.getCreated_at(), text_time,0);
        }else{
            FeedBack prevFeedBack = getItem(pos-1);
            if(prevFeedBack != null && prevFeedBack.getCreated_at() != null && TimeUtil.getTimeStamp(feedBack.getCreated_at())
                    -  TimeUtil.getTimeStamp(prevFeedBack.getCreated_at()) < 60 * 1000){
                text_time.setVisibility(View.GONE);
            }else{
                text_time.setVisibility(View.VISIBLE);
                TimeUtil.displayTime(feedBack.getCreated_at(), text_time, 0);
            }
        }
    }

    /**显示头像*/
    private void setPhoto(FeedBack feedBack, BaseMessageHolder holder, int pos) {
        if(feedBack.getIs_reply() == 1){
            holder.image_photo.setImageResource(R.mipmap.ic_customer_service);
        }else{
            ImageLoadUtil.loadAvatar(holder.image_photo,AppCookie.getUserInfo().getAvatar() ,holder.itemView.getContext());
        }
        holder.image_photo.setOnClickListener(outsideClickListener);
        holder.image_photo.setTag(R.id.tag_click_content,pos);
    }

    /**显示息发送状态*/
    public void setSendState(FeedBack feedBack, BaseMessageHolder holder, int pos) {
        holder.icon_progress_failed.setOnClickListener(null);
        switch (feedBack.getSend_state()){
            case Constants.SendState.SUCCESS:    /*发送成功*/
                holder.icon_progress_failed.setVisibility(View.GONE);
                break;

            case Constants.SendState.FAILED:     /*发送失败*/
                holder.icon_progress_failed.setImageResource(R.drawable.chat_icon_send_failed);
                holder.icon_progress_failed.setVisibility(View.VISIBLE);
                holder.icon_progress_failed.setOnClickListener(outsideClickListener);
                holder.icon_progress_failed.setTag(R.id.tag_click_content,pos);
                break;

            case Constants.SendState.SENDING:    /*发送中*/
                Context context = holder.itemView.getContext();
                holder.icon_progress_failed.setImageDrawable(context.getResources().getDrawable(R.drawable.anim_loading_view));
                ((AnimationDrawable) holder.icon_progress_failed.getDrawable()).start();
                holder.icon_progress_failed.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**消息型Holder基类*/
    public abstract static class BaseMessageHolder extends RecyclerView.ViewHolder{
        /**时间*/
        TextView text_time;
        /**头像*/
        ImageView image_photo;
        /**"发送中"或是"发送失败"图标*/
        ImageView icon_progress_failed;

        public BaseMessageHolder(View itemView) {
            super(itemView);
            text_time = (TextView) itemView.findViewById(R.id.text_time);
            image_photo = (ImageView) itemView.findViewById(R.id.image_photo);
            icon_progress_failed = (ImageView) itemView.findViewById(R.id.icon_progress_failed);
        }
    }

    /**文字型Holder*/
    private static class TextHolder extends BaseMessageHolder{

        /**文字内容*/
        TextView text_content;

        private TextHolder(View view) {
            super(view);
            text_content = (TextView) view.findViewById(R.id.text_content);
        }
    }
}
