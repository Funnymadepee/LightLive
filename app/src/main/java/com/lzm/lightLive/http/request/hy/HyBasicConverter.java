package com.lzm.lightLive.http.request.hy;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.lzm.lightLive.http.CustomGsonConvertFactory;
import com.lzm.lightLive.http.bean.hy.HyRoom;
import com.lzm.lightLive.util.CommonUtil;
import java.io.IOException;
import okhttp3.ResponseBody;

public class HyBasicConverter extends CustomGsonConvertFactory.ResponseConverter {

    @Override
    protected String convertResponse(ResponseBody value) throws IOException {
        String response = value.string();
        JsonObject obj = new Gson().fromJson(response, JsonObject.class);
        Log.e("convertResponse: ", obj.toString() );


        if(obj.has("data") && obj.get("data") != null) {
            HyRoom roomInfo = new Gson().fromJson(obj.get("data"), HyRoom.class);
            String roomName = roomInfo.getHyLiveData().getRoomName();
            long userCount = roomInfo.getHyLiveData().getUserCount();
            long profileRoom = roomInfo.getHyLiveData().getProfileRoom();
            String screenshot = roomInfo.getHyLiveData().getScreenshot();
            String fullName = roomInfo.getHyLiveData().getGameFullName();

            if(roomInfo.getHyStream() != null
                    && roomInfo.getHyStream().getFlv() != null
                    && roomInfo.getHyStream().getFlv().getMultiLine().get(0) != null) {
                String url = roomInfo.getHyStream().getFlv().getMultiLine().get(0).getUrl();
                roomInfo.setLiveStreamUri(url);
            }

            String nick = roomInfo.getHyProfileInfo().getNick();
            String avatar = roomInfo.getHyProfileInfo().getAvatar();
            int activityCount = roomInfo.getHyProfileInfo().getActivityCount();

            roomInfo.setHostName(nick);
            roomInfo.setRoomName(roomName);
            roomInfo.setCateName(fullName);
            roomInfo.setHostAvatar(avatar);
            roomInfo.setHeatNum(userCount);
            roomInfo.setThumbUrl(screenshot);
            roomInfo.setFansNum(activityCount);
            roomInfo.setStreamStatus(CommonUtil.generateLiveStatus(roomInfo.getHyRealLiveStatus()));
            roomInfo.setRoomId(String.valueOf(profileRoom));
            obj.remove("data");
            obj.add("data", new Gson().toJsonTree(roomInfo, new TypeToken<HyRoom>() {}.getType() ));
            response = obj.toString();
        }
        return response;
    }
}
