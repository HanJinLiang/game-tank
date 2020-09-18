package com.hanjinliang.tank.mode

import com.hanjinliang.tank.Config
import com.hanjinliang.tank.business.Blockable
import org.itheima.kotlin.game.core.Painter

/**
 * 水
 */
class Water(override val x: Int, override val y: Int) :Blockable {
    //位置
//    override var x:Int =200
//    override var y:Int =200

    //宽高
    override var width:Int=Config.block
    override var height:Int=Config.block

    //显示
    override fun draw(){
        Painter.drawImage("img/water.gif",x,y)
    }
}