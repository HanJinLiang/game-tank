package com.hanjinliang.tank.mode

import com.hanjinliang.tank.Config
import com.hanjinliang.tank.business.Attackable
import com.hanjinliang.tank.business.Blockable
import com.hanjinliang.tank.business.Sufferable
import org.itheima.kotlin.game.core.Painter

/**
 * 贴墙
 */
class Steel(override val x: Int, override val y: Int) :Blockable,Sufferable {
    override val blood: Int=6


    //宽高
    override var width:Int=Config.block
    override var height:Int=Config.block

    //显示
    override fun draw(){
        Painter.drawImage("img/steels.gif",x,y)
    }

    override fun notifySuffer(attackable: Attackable): Array<IView>? {
        return null
    }
}