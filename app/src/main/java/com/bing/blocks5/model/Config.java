package com.bing.blocks5.model;

import com.bigkoo.pickerview.model.IPickerViewData;

import java.util.List;

/**
 * author：zhangguobing on 2017/6/24 21:26
 * email：bing901222@qq.com
 */

public class Config {

    private List<BannersBean> banners;
    private List<UserIdentityStatesBean> user_identity_states;
    private List<ActivityTypesBean> activity_types;
    private List<ActivityAreasBean> activity_areas;
    private List<ActivityPriceTypesBean> activity_price_types;
    private List<ActivityStatesBean> activity_states;
    private List<ActivityNeedIdentitiesBean> activity_need_identities;
    private List<ActivityCommentIsTeamsBean> activity_comment_is_teams;
    private List<ActivityUserStatesBean> activity_user_states;
    private List<ActivityUserIsSignsBean> activity_user_is_signs;
    private List<ReportRptTypesBean> report_rpt_types;
    private List<ReportStatesBean> report_states;
    private List<FeedbackIsRepliesBean> feedback_is_replies;
    private AndroidUpdateBean android_update;

    public List<BannersBean> getBanners() {
        return banners;
    }

    public void setBanners(List<BannersBean> banners) {
        this.banners = banners;
    }

    public List<UserIdentityStatesBean> getUser_identity_states() {
        return user_identity_states;
    }

    public void setUser_identity_states(List<UserIdentityStatesBean> user_identity_states) {
        this.user_identity_states = user_identity_states;
    }

    public List<ActivityTypesBean> getActivity_types() {
        return activity_types;
    }

    public void setActivity_types(List<ActivityTypesBean> activity_types) {
        this.activity_types = activity_types;
    }

    public List<ActivityAreasBean> getActivity_areas() {
        return activity_areas;
    }

    public void setActivity_areas(List<ActivityAreasBean> activity_areas) {
        this.activity_areas = activity_areas;
    }

    public List<ActivityPriceTypesBean> getActivity_price_types() {
        return activity_price_types;
    }

    public void setActivity_price_types(List<ActivityPriceTypesBean> activity_price_types) {
        this.activity_price_types = activity_price_types;
    }

    public List<ActivityStatesBean> getActivity_states() {
        return activity_states;
    }

    public void setActivity_states(List<ActivityStatesBean> activity_states) {
        this.activity_states = activity_states;
    }

    public List<ActivityNeedIdentitiesBean> getActivity_need_identities() {
        return activity_need_identities;
    }

    public void setActivity_need_identities(List<ActivityNeedIdentitiesBean> activity_need_identities) {
        this.activity_need_identities = activity_need_identities;
    }

    public List<ActivityCommentIsTeamsBean> getActivity_comment_is_teams() {
        return activity_comment_is_teams;
    }

    public void setActivity_comment_is_teams(List<ActivityCommentIsTeamsBean> activity_comment_is_teams) {
        this.activity_comment_is_teams = activity_comment_is_teams;
    }

    public List<ActivityUserStatesBean> getActivity_user_states() {
        return activity_user_states;
    }

    public void setActivity_user_states(List<ActivityUserStatesBean> activity_user_states) {
        this.activity_user_states = activity_user_states;
    }

    public List<ActivityUserIsSignsBean> getActivity_user_is_signs() {
        return activity_user_is_signs;
    }

    public void setActivity_user_is_signs(List<ActivityUserIsSignsBean> activity_user_is_signs) {
        this.activity_user_is_signs = activity_user_is_signs;
    }

    public List<ReportRptTypesBean> getReport_rpt_types() {
        return report_rpt_types;
    }

    public void setReport_rpt_types(List<ReportRptTypesBean> report_rpt_types) {
        this.report_rpt_types = report_rpt_types;
    }

    public List<ReportStatesBean> getReport_states() {
        return report_states;
    }

    public void setReport_states(List<ReportStatesBean> report_states) {
        this.report_states = report_states;
    }

    public List<FeedbackIsRepliesBean> getFeedback_is_replies() {
        return feedback_is_replies;
    }

    public void setFeedback_is_replies(List<FeedbackIsRepliesBean> feedback_is_replies) {
        this.feedback_is_replies = feedback_is_replies;
    }

    public AndroidUpdateBean getAndroid_update() {
        return android_update;
    }

    public void setAndroid_update(AndroidUpdateBean android_update) {
        this.android_update = android_update;
    }

    public static class BannersBean {
        /**
         * imgUrl : https://www.baidu.com/img/bd_logo1.png
         * linkUrl : https://www.baidu.com
         */

        private String imgUrl;
        private String linkUrl;

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getLinkUrl() {
            return linkUrl;
        }

        public void setLinkUrl(String linkUrl) {
            this.linkUrl = linkUrl;
        }
    }

    public static class UserIdentityStatesBean {
        /**
         * id : 0
         * name : 未认证
         */

        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class ActivityTypesBean implements IPickerViewData{
        /**
         * id : 1
         * name : 骑行
         * peoples : [[2,2],[3,3],[4,4],[5,5],[0,6],[6,0]]
         */

        private int id;
        private String name;
        private List<List<Integer>> peoples;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<List<Integer>> getPeoples() {
            return peoples;
        }

        public void setPeoples(List<List<Integer>> peoples) {
            this.peoples = peoples;
        }

        @Override
        public String getPickerViewText() {
            return name;
        }
    }

    public static class ActivityAreasBean {
        /**
         * city : 深圳市
         * areas : ["南山区","宝安区","罗湖区","福田区","盐田区","龙岗区","坪山区","龙华区","光明新区","大鹏新区","深汕特别合作区","前海经济特区"]
         * isHot : 1
         */

        private String city;
        private int isHot;
        private List<String> areas;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public int getIsHot() {
            return isHot;
        }

        public void setIsHot(int isHot) {
            this.isHot = isHot;
        }

        public List<String> getAreas() {
            return areas;
        }

        public void setAreas(List<String> areas) {
            this.areas = areas;
        }
    }

    public static class ActivityPriceTypesBean {
        /**
         * id : 0
         * name : 男女AA
         */

        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class ActivityStatesBean {
        /**
         * id : 0
         * name : 活动审核中
         */

        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class ActivityNeedIdentitiesBean {
        /**
         * id : 0
         * name : 不需要
         */

        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class ActivityCommentIsTeamsBean {
        /**
         * id : 0
         * name : 游客留言
         */

        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class ActivityUserStatesBean {
        /**
         * id : 0
         * name : 待选择
         */

        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class ActivityUserIsSignsBean {
        /**
         * id : 0
         * name : 未签到
         */

        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class ReportRptTypesBean {
        /**
         * id : 0
         * name : 举报活动
         */

        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class ReportStatesBean {
        /**
         * id : 0
         * name : 待处理
         */

        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class FeedbackIsRepliesBean {
        /**
         * id : 0
         * name : 自己的反馈
         */

        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class AndroidUpdateBean {
        private String version;
        private String content;
        private int is_force;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getIs_force() {
            return is_force;
        }

        public void setIs_force(int is_force) {
            this.is_force = is_force;
        }
    }
}
