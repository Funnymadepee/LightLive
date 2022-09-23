package com.lzm.lightLive.http.request.dy;

import com.lzm.lightLive.http.BaseResult;
import com.lzm.lightLive.http.bean.dy.DyCate;
import com.lzm.lightLive.http.bean.dy.DyRoom;
import com.lzm.lightLive.http.bean.dy.DySortRoom;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/*
https://www.douyu.com/japi/weblist/apinc/header/cate                    //获取斗鱼分类默认
https://www.douyu.com/japi/weblist/apinc/getUserCustomCategories        //获取斗鱼全部分类
https://msg.douyu.com/wbapi/web/emotion                                 //获取斗鱼emotion表情
https://www.douyu.com/gapi/rkc/directory/mixList/2_{cate_id}/{page}     //获取对应分类id (cate_id) 第page页的房间
 */

public interface DyHttpRequest{
    /*获取房间信息*/
    @GET("api/RoomApi/room/{roomId}")
    Observable<BaseResult<DyRoom>> queryRoomInfo(@Path("roomId")String roomId);

    @GET("japi/search/api/getHotList")
    Observable<BaseResult<String>> queryHotList();

    @GET("gapi/rkc/directory/mixList/0_0/{page}")
    Observable<BaseResult<DySortRoom>> queryLiveMix(@Path("page") int page);

    @GET("japi/weblist/apinc/allpage/6/{page}")
    Observable<BaseResult<DySortRoom>> queryLiveHeatSort(@Path("page") int page);

    @GET("japi/weblist/apinc/getUserCustomCategories")
    Observable<DyCate> queryAllCategories();

    @GET("gapi/rkc/directory/mixList/2_{cate_id}/{page}")
    Observable<BaseResult<DySortRoom>> queryCateHosts(@Path("cate_id") int cateId, @Path("page") int page);

}
