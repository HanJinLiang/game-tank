package com.hanjinliang.tank.business

import com.hanjinliang.tank.mode.IView

/**
 * 具备攻击的能力
 */
interface Attackable:IView {
    /**
     * 所有者
     */
    val owner:IView

    //攻击力
    val attackPower:Int
    /**
     * 攻击物是否碰撞
     */
    fun isCollision(sufferable: Sufferable):Boolean

    fun notifyAttack(sufferable: Sufferable)
}