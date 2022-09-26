package com.lzm.lightLive.http.danmu.dy

import android.content.Context
import android.graphics.Color
import android.util.Log
import com.lzm.lightLive.R
import com.lzm.lightLive.http.danmu.basic.DanMu
import com.lzm.lightLive.http.danmu.basic.DanMuMessageType
import com.lzm.lightLive.http.danmu.basic.DanMuUserInfo
import com.lzm.lightLive.http.request.hy.DanMuFormat
import com.lzm.lightLive.util.ResourceUtil

object DanMuParserDY {
    fun parse(context: Context, message: String): DanMu? {
        if (!message.contains("type@=")) return null
        val type = message.substring(message.indexOf("type@="), message.indexOf("/rid@="))
            .replace("type@=".toRegex(), "")
        return when (type) {
            "chatmsg" -> parseChatMsg(context, message)
            else -> null
        }
    }

    /*
        String type;                //表示为“弹幕”消息，固定为 chatmsg
        String rid;                 //房间 id
        long uid;                   //发送者 uid
        String nn;	                //发送者昵称
        String txt;         	    //弹幕文本内容
        int level;	                //用户等级
        int gt;     	            //礼物头衔：默认值 0（表示没有头衔）
        int col;    	            //颜色：默认值 0（表示默认颜色弹幕）
        int ct;         	        //客户端类型：默认值 0
        int rg;	                    //房间权限组：默认值 1（表示普通权限用户）
        int pg;     	            //平台权限组：默认值 1（表示普通权限用户）
        int dlv;	                //酬勤等级：默认值 0（表示没有酬勤）
        int dc;	                    //酬勤数量：默认值 0（表示没有酬勤数量）
        int bdlv;	                //最高酬勤等级：默认值 0（表示全站都没有酬勤）
        int cmt;            	    //弹幕具体类型: 默认值 0（普通弹幕）
        int sahf;	                //扩展字段，一般不使用，可忽略
        String ic;	                //用户头像
        int nl;	                    //贵族等级
        int nc;	                    //贵族弹幕标识,0-非贵族弹幕,1-贵族弹幕,默认值 0
        long gatin; 	            //进入网关服务时间戳
        long chtin;	                //进入房间服务时间戳
        long repin;	                //进入发送服务时间戳
        String bnn;	                //徽章昵称
        int bl;	                    //徽章等级
        int brid;	                //徽章房间 id
        int hc;	                    //徽章信息校验码
        int ol; 	                //主播等级
        int rev;	                //是否反向弹幕标记: 0-普通弹幕，1-反向弹幕, 默认值 0
        int hl;	                    //是否高亮弹幕标记: 0-普通，1-高亮, 默认值 0
        int ifs;	                //是否粉丝弹幕标记: 0-非粉丝弹幕，1-粉丝弹幕, 默认值 0
        int p2p;                	//服务功能字段
        String[] el;    	        //用户获得的连击特效：数组类型，数组包含 eid（特效 id）,etp（特效类型）,sc（特效次数）信息，ef（特效标志）。
*/
    //    chatmsg/rid@=3168536
    //    /uid@=445964561/nn@=Yourk628
    //    /txt@=军事基地/cid@=a9e9ded627244c92ca774b0000000000
    //    /ic@=avatar_v3@S202204@Sb835c43626ef4086aee5005d112e70d5
    //    /level@=12/sahf@=0/col@=3/cst@=1663860486603
    //    /bnn@=XDD/bl@=9/brid@=3168536
    //    /hc@=5a604a8e5b9357c9c2ebd8a128eff4f4
    //    /ifs@=1/el@=/lk@=/fl@=9/dms@=5/pdg@=35/pdk@=84
    private fun parseChatMsg(context: Context, msg: String): DanMu? {
        val message = subString(msg, "type@=", "/ext@=")
        return try {
            val uid = subString(message, "/uid@=", "/nn@=")
            val nickName = subString(message, "/nn@=", "/txt@=")
            val content = subString(message, "txt@=", "/cid@=")
            val avatar = subString(message, "/ic@=", "/level@=")
            val level = subInteger(message, "/level@=", "/sahf@=")
            val badge = subString(message, "/bnn@=", "/bl@=")
            val badgeLevel = subString(message, "/bl@=", "/brid@=")
            val rg = subInteger(message, "/rg@=", "/cst@=")
            val col = generateDouYuColor(
                context,
                subInteger(message, "/col@=", if (rg == 0) "/cst@=" else "/rg@=")
            )
            val userInfo = DanMuUserInfo(level, uid, badge, avatar, nickName, badgeLevel)
            val danMuFormat = DanMuFormat()
            danMuFormat.fontColor = col
            val dyDanMu = DanMu(
                userInfo,
                content,
                danMuFormat,
                System.currentTimeMillis(),
                DanMuMessageType.DAN_MU
            )

            //TODO 根据uid / nickName 查找黑名单 / vip重点关注用户
            if (nickName == "realzmzzZ") {
                Log.e("TAG", "parse: " + userInfo.uid)
                userInfo.isVip = true
            }
            dyDanMu
        } catch (e: Exception) {
            Log.e("parseChatMsg: ", """${e.message} $message """.trimIndent())
            null
        }
    }

    private fun subString(message: String, form: String, to: String): String {
        return if (!message.contains(form)) "" else message.substring(
            message.indexOf(form),
            message.indexOf(to)
        )
            .replace(form.toRegex(), "")
    }

    private fun subInteger(message: String, form: String, to: String): Int {
        return if (!message.contains(form)) 0 else subString(message, form, to)
            .toInt()
    }

    private fun generateDouYuColor(context: Context, col: Int): Int {
        return when (col) { //col 颜色等级
            1 -> Color.RED
            2 -> Color.parseColor("#1e87f0")
            3 -> Color.parseColor("#7ac84b")
            4 -> Color.parseColor("#ff7f00")
            5 -> Color.parseColor("#9b39f4")
            6 -> Color.parseColor("#ff69b4")
            else -> ResourceUtil.attrColor(context, R.attr.basic_dm_text_color)
        }
    }

    /* case "dgb": */
    private fun parseDgb(message: String): DanMu {
        val uid = message.substring(message.indexOf("/uid@="), message.indexOf("/nn@="))
            .replace("/uid@=".toRegex(), "")
        val nickName = message.substring(message.indexOf("/nn@="), message.indexOf("/ic@="))
            .replace("/nn@=".toRegex(), "")
        val danMu = DanMu()
        val userInfo = DanMuUserInfo(nickName)
        userInfo.uid = uid
        userInfo.isVip = false
        danMu.userIfo = userInfo
        danMu.msgType = DanMuMessageType.GIFT
        return danMu
    }
}