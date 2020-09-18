package com.hanjinliang.tank.business

import com.hanjinliang.tank.mode.IView

/**
 * 带有销毁的能力
 */
interface Destroyable:IView {

    fun isDestroyed():Boolean
}