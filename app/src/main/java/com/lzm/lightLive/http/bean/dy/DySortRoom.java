package com.lzm.lightLive.http.bean.dy;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class DySortRoom {

    @SerializedName("pgcnt")
    private int pageCount;              //数据量

    @SerializedName("rl")
    private List<Related> relatedList;  //数据

    public static class Related {
        @SerializedName("rid")
        long roomId;                    //房间号
        @SerializedName("uid")
        long uid;                       //uid
        @SerializedName("rn")
        String roomName;                //房间名
        @SerializedName("nn")
        String nickName;                //昵称
        @SerializedName("ol")
        long online;                    //在线
        @SerializedName("rs16")
        String thumb;                   //预览图
        @SerializedName("av")
        String avatar;                  //头像 https://apic.douyucdn.cn/upload/ + "av" + .jpg 如果是avatar_v3 + _middle.jpg
        @SerializedName("c2name")
        String cateName;                //分类名
        @SerializedName("desc")
        String desc;                    //描述

        @Override
        public String toString() {
            return "Related{" +
                    "roomId=" + roomId +
                    ", uid=" + uid +
                    ", roomName='" + roomName + '\'' +
                    ", nickName='" + nickName + '\'' +
                    ", online=" + online +
                    ", thumb='" + thumb + '\'' +
                    ", avatar='" + avatar + '\'' +
                    ", cateName='" + cateName + '\'' +
                    ", desc='" + desc + '\'' +
                    '}';
        }

        public long getRoomId() {
            return roomId;
        }

        public void setRoomId(long roomId) {
            this.roomId = roomId;
        }

        public long getUid() {
            return uid;
        }

        public void setUid(long uid) {
            this.uid = uid;
        }

        public String getRoomName() {
            return roomName;
        }

        public void setRoomName(String roomName) {
            this.roomName = roomName;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public long getOnline() {
            return online;
        }

        public void setOnline(long online) {
            this.online = online;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getCateName() {
            return cateName;
        }

        public void setCateName(String cateName) {
            this.cateName = cateName;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public List<Related> getRelatedList() {
        return relatedList;
    }

    public void setRelatedList(List<Related> relatedList) {
        this.relatedList = relatedList;
    }

    @Override
    public String toString() {
        return "HeatSortRoom{" +
                "pageCount=" + pageCount +
                ", relatedList=" + relatedList +
                '}';
    }
}
