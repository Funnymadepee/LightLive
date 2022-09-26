package com.lzm.lightLive.video

import android.content.Context
import android.util.AttributeSet
import android.view.SurfaceView

class VideoView : SurfaceView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
}