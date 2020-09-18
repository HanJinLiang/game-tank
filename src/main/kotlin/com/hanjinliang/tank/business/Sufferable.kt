package com.hanjinliang.tank.business

import com.hanjinliang.tank.mode.IView

/**
 * 遭受攻击的能力
 */
interface Sufferable:IView {
    //生命值
    val blood:Int
    fun notifySuffer(attackable: Attackable):Array<IView>?
}