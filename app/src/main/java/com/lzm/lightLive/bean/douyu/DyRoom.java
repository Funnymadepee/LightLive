package com.lzm.lightLive.bean.douyu;

import android.os.Parcel;
import com.lzm.lightLive.bean.Room;

public class DyRoom extends Room {

    public DyRoom() {

    }

    public DyRoom(String roomId, String roomThumb, String cateId,
                  String cateName, String roomName, int roomStatus,
                  String startTime, String ownerName, String avatar,
                  long online, int hn, String ownerWeight, String fansNum) {
        super(roomId, roomThumb, cateId, cateName, roomName, roomStatus, startTime, ownerName, avatar, online, hn, ownerWeight, fansNum);
    }

    protected DyRoom(Parcel in) {
        super(in);
    }

    public static final Creator<DyRoom> CREATOR = new Creator<DyRoom>() {
        @Override
        public DyRoom createFromParcel(Parcel in) {
            return new DyRoom(in);
        }

        @Override
        public DyRoom[] newArray(int size) {
            return new DyRoom[size];
        }
    };

//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        super.writeToParcel(dest, flags);
//    }
}
