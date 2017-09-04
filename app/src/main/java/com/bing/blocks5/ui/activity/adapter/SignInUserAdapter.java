package com.bing.blocks5.ui.activity.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bing.blocks5.R;
import com.bing.blocks5.model.ActivityUser;
import com.bing.blocks5.util.ImageLoadUtil;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by tian on 2017/8/21.
 * 签到用户Adapter
 */

public class SignInUserAdapter extends BaseAdapter{

    private Context mContext;
    private LayoutInflater mInflater;

    public SignInUserAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    private List<ActivityUser> mList = new ArrayList<>();

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public ActivityUser getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void replace(List<ActivityUser> items){
        mList.clear();
        mList.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ActivityUser user = getItem(position);
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_sign_in_user, null);
            holder.mAvatarImg = (ImageView) convertView.findViewById(R.id.avatar);
            holder.mNameTv = (TextView) convertView.findViewById(R.id.name);
            holder.mStatus = (TextView) convertView.findViewById(R.id.status);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        if(TextUtils.isEmpty(user.getCreator().getAvatar())) {
            holder.mAvatarImg.setImageResource(R.mipmap.ic_user_avatar_black);
        }else{
            ImageLoadUtil.loadAvatar(holder.mAvatarImg,user.getCreator().getAvatar(),mContext);
        }
        holder.mNameTv.setText(user.getCreator().getNick_name());
        holder.mStatus.setText("0".equals(user.getIs_sign()) ? "未签到" : "已签到");

        return convertView;
    }

    static class ViewHolder {
        ImageView mAvatarImg;
        TextView mNameTv;
        TextView mStatus;
    }

}
