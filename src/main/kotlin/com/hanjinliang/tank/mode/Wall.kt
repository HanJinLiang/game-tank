package com.hanjinliang.tank.mode

import com.hanjinliang.tank.business.Attackable
import com.hanjinliang.tank.business.Blockable
import com.hanjinliang.tank.business.Destroyable
import com.hanjinliang.tank.business.Sufferable
import org.itheima.kotlin.game.core.Composer
import org.itheima.kotlin.game.core.Painter


class Wall(override val x: Int, override val y: Int):Blockable,Sufferable,Destroyable{


    //位置
//    override val x:Int =100
//    override val y:Int =100

    //宽高
    override val width:Int= com.hanjinliang.tank.Config.block
    override val height:Int=com.hanjinliang.tank.Config.block

    //总共三滴血
    override var blood: Int=3

    //显示
    override fun draw(){
        Painter.drawImage("img/walls.gif",x,y)
    }

    override fun isDestroyed(): Boolean {
       return blood<=0
    }

    override fun notifySuffer(attackable: Attackable):Array<IView>? {
        //遭受到攻击
        //println("遭受到攻击")
        //砖墙被销毁了
        blood-=attackable.attackPower//掉血

        //播放声音
        Composer.play("snd/hit.wav")

        return arrayOf(Blast(x,y))
    }
}