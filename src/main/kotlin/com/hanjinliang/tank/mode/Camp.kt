package com.hanjinliang.tank.mode

import com.hanjinliang.tank.Config
import com.hanjinliang.tank.business.Attackable
import com.hanjinliang.tank.business.Blockable
import com.hanjinliang.tank.business.Destroyable
import com.hanjinliang.tank.business.Sufferable
import org.itheima.kotlin.game.core.Painter

/**
 * 大本营
 */
class Camp(override val x: Int, override val y: Int) :IView,Blockable,Sufferable,Destroyable {
    override var blood: Int=20

    //宽高
    override var width:Int= Config.block*2
    override var height:Int= Config.block + 30

    //显示
    override fun draw(){

        when {
            blood in 4..10 -> {
                //血量不足  画砖
                //绘制外围铁抢
                Painter.drawImage("img/wall.gif",x,y)
                Painter.drawImage("img/wall.gif",x+15,y)
                Painter.drawImage("img/wall.gif",x+30,y)
                Painter.drawImage("img/wall.gif",x+45,y)
                Painter.drawImage("img/wall.gif",x+60,y)
                Painter.drawImage("img/wall.gif",x+75,y)
                Painter.drawImage("img/wall.gif",x+90,y)
                Painter.drawImage("img/wall.gif",x+105,y)
                Painter.drawImage("img/wall.gif",x,y+15)
                Painter.drawImage("img/wall.gif",x+15,y+15)
                Painter.drawImage("img/wall.gif",x+30,y+15)
                Painter.drawImage("img/wall.gif",x+45,y+15)
                Painter.drawImage("img/wall.gif",x+60,y+15)
                Painter.drawImage("img/wall.gif",x+75,y+15)
                Painter.drawImage("img/wall.gif",x+90,y+15)
                Painter.drawImage("img/wall.gif",x+105,y+15)

                Painter.drawImage("img/wall.gif",x,y+30)
                Painter.drawImage("img/wall.gif",x,y+45)
                Painter.drawImage("img/wall.gif",x,y+60)
                Painter.drawImage("img/wall.gif",x,y+75)
                Painter.drawImage("img/wall.gif",x+15,y+30)
                Painter.drawImage("img/wall.gif",x+15,y+45)
                Painter.drawImage("img/wall.gif",x+15,y+60)
                Painter.drawImage("img/wall.gif",x+15,y+75)

                Painter.drawImage("img/wall.gif",x+90,y+30)
                Painter.drawImage("img/wall.gif",x+90,y+45)
                Painter.drawImage("img/wall.gif",x+90,y+60)
                Painter.drawImage("img/wall.gif",x+90,y+75)
                Painter.drawImage("img/wall.gif",x+105,y+30)
                Painter.drawImage("img/wall.gif",x+105,y+45)
                Painter.drawImage("img/wall.gif",x+105,y+60)
                Painter.drawImage("img/wall.gif",x+105,y+75)
            }
            blood<=3 -> {
                //血量不足 没有墙
            }
            else -> {
                //绘制外围砖块
                Painter.drawImage("img/steel.gif",x,y)
                Painter.drawImage("img/steel.gif",x+30,y)
                Painter.drawImage("img/steel.gif",x+60,y)
                Painter.drawImage("img/steel.gif",x+90,y)

                Painter.drawImage("img/steel.gif",x,y+30)
                Painter.drawImage("img/steel.gif",x,y+60)

                Painter.drawImage("img/steel.gif",x+90,y+30)
                Painter.drawImage("img/steel.gif",x+90,y+60)
            }
        }
        Painter.drawImage("img/camp.gif",x+30,y+30)

    }

    override fun notifySuffer(attackable: Attackable): Array<IView>? {
        //遭受攻击
        blood-=attackable.attackPower

        if(blood== 10 || blood== 3){
            return arrayOf(Blast(x,y), Blast(x+30,y),Blast(x+60,y),Blast(x+90,y)
                ,Blast(x,y+30),Blast(x+90,y+30)
                ,Blast(x,y+30),Blast(x+90,y+60)
            )
        }
        if(blood==0){
            return arrayOf(Blast(x+45,y+45))
        }
        return null
    }

    override fun isDestroyed(): Boolean {
        return blood<=0
    }
}