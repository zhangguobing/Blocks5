package com.bing.blocks5.ui.activity.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bing.blocks5.AppCookie;
import com.bing.blocks5.Constants;
import com.bing.blocks5.R;
import com.bing.blocks5.base.BaseAdapter;
import com.bing.blocks5.model.Comment;
import com.bing.blocks5.util.ImageLoadUtil;
import com.bing.blocks5.util.Objects;
import com.bing.blocks5.util.TimeUtil;

import org.w3c.dom.Text;

/**
 * author：zhangguobing on 2017/7/20 17:00
 * email：bing901222@qq.com
 */

public class CommentAdapter extends BaseAdapter<Comment,RecyclerView.ViewHolder> {

    private static final int TYPE_TEXT_LEFT = 0;
    private static final int TYPE_TEXT_RIGHT = 1;

    /**传到外层的点击监听器，用来处理头像点击等需要外层处理的事件*/
    private View.OnClickListener outsideClickListener = null;
    private View.OnLongClickListener outsideLongClickListener = null;

    public CommentAdapter(View.OnClickListener outsideClickListener, View.OnLongClickListener outsideLongClickListener) {
        this.outsideClickListener = outsideClickListener;
        this.outsideLongClickListener = outsideLongClickListener;
    }

    @Override
    public int getViewLayoutId(int viewType) {
        return viewType == TYPE_TEXT_LEFT ? R.layout.item_chat_text_left : R.layout.item_chat_text_right;
    }

    @Override
    public int getItemViewType(int position) {
        Comment comment = getItem(position);
        return comment.getUser_id() == AppCookie.getUserInfo().getId() ? TYPE_TEXT_RIGHT: TYPE_TEXT_LEFT;
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(View view, int viewType) {
        return new TextHolder(view);
    }

    @Override
    public void bindViewHolder(RecyclerView.ViewHolder holder, Comment comment, int position) {
        if(holder instanceof BaseMessageHolder){
            BaseMessageHolder baseMessageHolder = (BaseMessageHolder) holder;
            setBase(comment, baseMessageHolder, position);
        }
        if(holder instanceof TextHolder){
            TextHolder textHolder = (TextHolder) holder;
            String text = comment.getContent();
            textHolder.text_content.setText(text);
            textHolder.text_content.setOnLongClickListener(outsideLongClickListener);
            textHolder.text_content.setTag(R.id.tag_click_content,position);
        }
    }

    /**显示基本参数*/
    private void setBase(Comment comment ,BaseMessageHolder holder,int pos){
        setTime(comment,holder.text_time, pos);
        setPhoto(comment, holder, pos);
        setSendState(comment, holder,pos);
    }

    private void setTime(Comment comment, TextView text_time, int pos) {

        if(pos == 0){
            //第一条显示时间
            text_time.setVisibility(View.VISIBLE);
            TimeUtil.displayTime(comment.getCreated_at(), text_time,0);
        }else{
            Comment prevComment = getItem(pos-1);
            if(prevComment != null && prevComment.getCreated_at() != null &&
                    TimeUtil.getTimeStamp(comment.getCreated_at())
                            -  TimeUtil.getTimeStamp(prevComment.getCreated_at()) < 60 * 1000){
                text_time.setVisibility(View.GONE);
            }else{
                text_time.setVisibility(View.VISIBLE);
                TimeUtil.displayTime(comment.getCreated_at(), text_time, 0);
            }
        }
    }

    /**显示头像*/
    private void setPhoto(Comment comment, BaseMessageHolder holder, int pos) {
        if(TextUtils.isEmpty(comment.getCreator().getAvatar())){
            holder.image_photo.setImageResource(R.mipmap.ic_user_avatar_black);
        }else {
            ImageLoadUtil.loadAvatar(holder.image_photo,comment.getCreator().getAvatar(),holder.itemView.getContext());
        }
        holder.image_photo.setOnClickListener(outsideClickListener);
        holder.image_photo.setTag(R.id.tag_click_content,pos);
    }

    /**显示息发送状态*/
    public void setSendState(Comment comment, BaseMessageHolder holder, int pos) {
        holder.icon_progress_failed.setOnClickListener(null);
        switch (comment.getSend_state()){
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

    @Override
    protected boolean areItemsTheSame(Comment oldItem, Comment newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    protected boolean areContentsTheSame(Comment oldItem, Comment newItem) {
        return Objects.equals(oldItem,newItem);
    }
}
