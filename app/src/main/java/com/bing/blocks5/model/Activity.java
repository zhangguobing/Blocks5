package com.bing.blocks5.model;

import java.util.List;

/**
 * author：zhangguobing on 2017/7/2 13:30
 * email：bing901222@qq.com
 */

public class Activity {

    /**
     * id : 1
     * user_id : 1
     * activity_type_id : 1
     * title : 活动标题
     * city : 深圳
     * area : 南山
     * content :
     * man_num : 3
     * woman_num : 3
     * price_total : 200
     * price_type : 1
     * price_content : 0
     * begin_at : 2016-02-05 00:00:00
     * end_at : 2016-02-09 00:00:00
     * state : 0
     * need_identity : 0
     * cover_url : http://a.com/b.jpg
     * img_url_1 : http://a.com/b.jpg
     * img_url_2 :
     * img_url_3 :
     * created_at : 2017-04-12 09:47:33
     * creator : {"id":1,"nick_name":"不小不大","sex":"男","avatar":"","credit":100,"identity_state":0}
     * is_collect : 1
     * is_join : 0
     * man_left : 3
     * woman_left : 3
     */

    private int id;
    private int user_id;
    private int activity_type_id;
    private String title;
    private String city;
    private String area;
    private String content;
    private int man_num;
    private int woman_num;
    private int price_total;
    private int price_type;
    private String price_content;
    private String begin_at;
    private String end_at;
    private int state;
    private String need_identity;
    private String cover_url;
    private String img_url_1;
    private String img_url_2;
    private String img_url_3;
    private String created_at;
    private CreatorBean creator;
    private List<Comment> comments;
    private int is_collect;
    private int is_join;
    private int man_left;
    private int woman_left;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getActivity_type_id() {
        return activity_type_id;
    }

    public void setActivity_type_id(int activity_type_id) {
        this.activity_type_id = activity_type_id;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMan_num() {
        return man_num;
    }

    public void setMan_num(int man_num) {
        this.man_num = man_num;
    }

    public int getWoman_num() {
        return woman_num;
    }

    public void setWoman_num(int woman_num) {
        this.woman_num = woman_num;
    }

    public int getPrice_total() {
        return price_total;
    }

    public void setPrice_total(int price_total) {
        this.price_total = price_total;
    }

    public int getPrice_type() {
        return price_type;
    }

    public void setPrice_type(int price_type) {
        this.price_type = price_type;
    }

    public String getPrice_content() {
        return price_content;
    }

    public void setPrice_content(String price_content) {
        this.price_content = price_content;
    }

    public String getBegin_at() {
        return begin_at;
    }

    public void setBegin_at(String begin_at) {
        this.begin_at = begin_at;
    }

    public String getEnd_at() {
        return end_at;
    }

    public void setEnd_at(String end_at) {
        this.end_at = end_at;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getNeed_identity() {
        return need_identity;
    }

    public void setNeed_identity(String need_identity) {
        this.need_identity = need_identity;
    }

    public String getCover_url() {
        return cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }

    public String getImg_url_1() {
        return img_url_1;
    }

    public void setImg_url_1(String img_url_1) {
        this.img_url_1 = img_url_1;
    }

    public String getImg_url_2() {
        return img_url_2;
    }

    public void setImg_url_2(String img_url_2) {
        this.img_url_2 = img_url_2;
    }

    public String getImg_url_3() {
        return img_url_3;
    }

    public void setImg_url_3(String img_url_3) {
        this.img_url_3 = img_url_3;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public CreatorBean getCreator() {
        return creator;
    }

    public void setCreator(CreatorBean creator) {
        this.creator = creator;
    }

    public int getIs_collect() {
        return is_collect;
    }

    public void setIs_collect(int is_collect) {
        this.is_collect = is_collect;
    }

    public int getIs_join() {
        return is_join;
    }

    public void setIs_join(int is_join) {
        this.is_join = is_join;
    }

    public int getMan_left() {
        return man_left;
    }

    public void setMan_left(int man_left) {
        this.man_left = man_left;
    }

    public int getWoman_left() {
        return woman_left;
    }

    public void setWoman_left(int woman_left) {
        this.woman_left = woman_left;
    }

    public static class CreatorBean {
        /**
         * id : 1
         * nick_name : 不小不大
         * sex : 男
         * avatar :
         * credit : 100
         * identity_state : 0
         */

        private int id;
        private String nick_name;
        private String sex;
        private String avatar;
        private int credit;
        private int identity_state;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNick_name() {
            return nick_name;
        }

        public void setNick_name(String nick_name) {
            this.nick_name = nick_name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getCredit() {
            return credit;
        }

        public void setCredit(int credit) {
            this.credit = credit;
        }

        public int getIdentity_state() {
            return identity_state;
        }

        public void setIdentity_state(int identity_state) {
            this.identity_state = identity_state;
        }
    }

    public static class Comment {

        /**
         * id : 16
         * activity_id : 1
         * user_id : 4
         * content : 二分法 v
         * is_team : 0
         * created_at : 2017-04-22 17:42:26
         */

        private int id;
        private int activity_id;
        private int user_id;
        private String content;
        private int is_team;
        private String created_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getActivity_id() {
            return activity_id;
        }

        public void setActivity_id(int activity_id) {
            this.activity_id = activity_id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getIs_team() {
            return is_team;
        }

        public void setIs_team(int is_team) {
            this.is_team = is_team;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Activity activity = (Activity) o;

        if (id != activity.id) return false;
        if (user_id != activity.user_id) return false;
        if (activity_type_id != activity.activity_type_id) return false;
        if (man_num != activity.man_num) return false;
        if (woman_num != activity.woman_num) return false;
        if (price_total != activity.price_total) return false;
        if (price_type != activity.price_type) return false;
        if (state != activity.state) return false;
        if (is_collect != activity.is_collect) return false;
        if (is_join != activity.is_join) return false;
        if (man_left != activity.man_left) return false;
        if (woman_left != activity.woman_left) return false;
        if (title != null ? !title.equals(activity.title) : activity.title != null) return false;
        if (city != null ? !city.equals(activity.city) : activity.city != null) return false;
        if (area != null ? !area.equals(activity.area) : activity.area != null) return false;
        if (content != null ? !content.equals(activity.content) : activity.content != null)
            return false;
        if (price_content != null ? !price_content.equals(activity.price_content) : activity.price_content != null)
            return false;
        if (begin_at != null ? !begin_at.equals(activity.begin_at) : activity.begin_at != null)
            return false;
        if (end_at != null ? !end_at.equals(activity.end_at) : activity.end_at != null)
            return false;
        if (need_identity != null ? !need_identity.equals(activity.need_identity) : activity.need_identity != null)
            return false;
        if (cover_url != null ? !cover_url.equals(activity.cover_url) : activity.cover_url != null)
            return false;
        if (img_url_1 != null ? !img_url_1.equals(activity.img_url_1) : activity.img_url_1 != null)
            return false;
        if (img_url_2 != null ? !img_url_2.equals(activity.img_url_2) : activity.img_url_2 != null)
            return false;
        if (img_url_3 != null ? !img_url_3.equals(activity.img_url_3) : activity.img_url_3 != null)
            return false;
        if (created_at != null ? !created_at.equals(activity.created_at) : activity.created_at != null)
            return false;
        if (creator != null ? !creator.equals(activity.creator) : activity.creator != null)
            return false;
        return comments != null ? comments.equals(activity.comments) : activity.comments == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + user_id;
        result = 31 * result + activity_type_id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (area != null ? area.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + man_num;
        result = 31 * result + woman_num;
        result = 31 * result + price_total;
        result = 31 * result + price_type;
        result = 31 * result + (price_content != null ? price_content.hashCode() : 0);
        result = 31 * result + (begin_at != null ? begin_at.hashCode() : 0);
        result = 31 * result + (end_at != null ? end_at.hashCode() : 0);
        result = 31 * result + state;
        result = 31 * result + (need_identity != null ? need_identity.hashCode() : 0);
        result = 31 * result + (cover_url != null ? cover_url.hashCode() : 0);
        result = 31 * result + (img_url_1 != null ? img_url_1.hashCode() : 0);
        result = 31 * result + (img_url_2 != null ? img_url_2.hashCode() : 0);
        result = 31 * result + (img_url_3 != null ? img_url_3.hashCode() : 0);
        result = 31 * result + (created_at != null ? created_at.hashCode() : 0);
        result = 31 * result + (creator != null ? creator.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        result = 31 * result + is_collect;
        result = 31 * result + is_join;
        result = 31 * result + man_left;
        result = 31 * result + woman_left;
        return result;
    }
}
