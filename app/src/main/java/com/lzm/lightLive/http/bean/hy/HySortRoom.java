package com.lzm.lightLive.http.bean.hy;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class HySortRoom {

    private int status;
    private String message;
    private RecommendData data;

    public static class RecommendData {
        private int page;
        private int pageSize;
        private int totalPage;
        private int totalCount;

        @SerializedName("datas")
        private List<HyRecommendRoom> datas;

        @Override
        public String toString() {
            return "HyHeatSortRoom{" +
                    "page=" + page +
                    ", pageSize=" + pageSize +
                    ", totalPage=" + totalPage +
                    ", totalCount=" + totalCount +
                    ", datas=" + datas +
                    '}';
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public List<HyRecommendRoom> getDatas() {
            return datas;
        }

        public void setDatas(List<HyRecommendRoom> datas) {
            this.datas = datas;
        }
    }

    public static class HyRecommendRoom {
        @SerializedName("gameFullName")
        private String cateName;
        @SerializedName("totalCount")
        private String heatNum;
        @SerializedName("roomName")
        private String roomName;
        @SerializedName("nick")
        private String hostName;
        @SerializedName("screenshot")
        private String thumbUrl;
        @SerializedName("avatar180")
        private String avatar;
        @SerializedName("introduction")
        private String desc;
        @SerializedName("recommendTagName")
        private String tagName;
        @SerializedName("profileRoom")
        private String roomId;
        @SerializedName("isRoomPay")
        private String liveStatus;

        @Override
        public String toString() {
            return "HyRecommendRoom{" +
                    "cateName='" + cateName + '\'' +
                    ", heatNum='" + heatNum + '\'' +
                    ", roomName='" + roomName + '\'' +
                    ", hostName='" + hostName + '\'' +
                    ", thumbUrl='" + thumbUrl + '\'' +
                    ", avatar='" + avatar + '\'' +
                    ", desc='" + desc + '\'' +
                    ", tagName='" + tagName + '\'' +
                    ", roomId='" + roomId + '\'' +
                    ", liveStatus='" + liveStatus + '\'' +
                    '}';
        }

        public String getCateName() {
            return cateName;
        }

        public void setCateName(String cateName) {
            this.cateName = cateName;
        }

        public String getHeatNum() {
            return heatNum;
        }

        public void setHeatNum(String heatNum) {
            this.heatNum = heatNum;
        }

        public String getRoomName() {
            return roomName;
        }

        public void setRoomName(String roomName) {
            this.roomName = roomName;
        }

        public String getHostName() {
            return hostName;
        }

        public void setHostName(String hostName) {
            this.hostName = hostName;
        }

        public String getThumbUrl() {
            return thumbUrl;
        }

        public void setThumbUrl(String thumbUrl) {
            this.thumbUrl = thumbUrl;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getTagName() {
            return tagName;
        }

        public void setTagName(String tagName) {
            this.tagName = tagName;
        }

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public String getLiveStatus() {
            return liveStatus;
        }

        public void setLiveStatus(String liveStatus) {
            this.liveStatus = liveStatus;
        }
    }

    @Override
    public String toString() {
        return "HyHeatSortRoom{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public RecommendData getData() {
        return data;
    }

    public void setData(RecommendData data) {
        this.data = data;
    }
}
