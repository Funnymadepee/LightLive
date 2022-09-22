package com.lzm.lib_danmu.control.dispatcher;


import com.lzm.lib_danmu.model.DanMuModel;
import com.lzm.lib_danmu.model.channel.DanMuChannel;


public interface IDanMuDispatcher {

    void dispatch(DanMuModel iDanMuView, DanMuChannel[] danMuChannels);

}