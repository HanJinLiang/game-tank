package com.hanjinliang.tank.business

import com.hanjinliang.tank.enums.Direction
import com.hanjinliang.tank.mode.IView

/**
 * 可以自己移动
 */
interface AutoMovable:IView {
    val speed:Int
    val currentDirection:Direction
    fun autoMove()
}