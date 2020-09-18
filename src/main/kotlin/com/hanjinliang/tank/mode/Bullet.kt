package com.hanjinliang.tank.mode

import com.hanjinliang.tank.Config
import com.hanjinliang.tank.business.Attackable
import com.hanjinliang.tank.business.AutoMovable
import com.hanjinliang.tank.business.Destroyable
import com.hanjinliang.tank.business.Sufferable
import com.hanjinliang.tank.enums.Direction
import com.hanjinliang.tank.ext.checkCollision
import org.itheima.kotlin.game.core.Painter

/**
 * 子弹
 */
class Bullet(override val owner: IView,override val currentDirection: Direction, create:(width:Int,height:Int) -> Pair<Int,Int>)
    :AutoMovable,Destroyable,Attackable,Sufferable{
    override val speed: Int = 8
    override val blood: Int=1//子弹对打消失

    override var x: Int = 0
    override var y: Int = 0
    //攻击力的1
    override val attackPower: Int= 1

    private var isDestroyed:Boolean=false

    private var imagePath:String = when(currentDirection){
        Direction.UP -> "img/bullet_u.gif"
        Direction.RIGHT -> "img/bullet_r.gif"
        Direction.DOWN -> "img/bullet_d.gif"
        Direction.LEFT -> "img/bullet_l.gif"
    }

    override val width: Int
    override val height: Int

    init {
        val size=Painter.size(imagePath)
        width=size[0]
        height=size[1]
        val pair=create.invoke(width,height)
        x=pair.first
        y=pair.second
    }

    override fun draw() {
        Painter.drawImage(imagePath,x,y)
    }

    override fun autoMove() {
        //自动移动子弹
        when(currentDirection){
            Direction.UP -> y-=speed
            Direction.RIGHT -> x+=speed
            Direction.DOWN -> y+=speed
            Direction.LEFT -> x-=speed
        }
    }

    /**
     * 超出屏幕  移除子弹
     */
    override fun isDestroyed(): Boolean {
        if(isDestroyed){
            return true
        }
        if(x<-width) return true
        if(x>Config.gameWidth) return true
        if(y<-height) return true
        if(y>Config.gameHeight) return true
        return false
    }



    override fun isCollision(sufferable: Sufferable): Boolean {
        return checkCollision(sufferable)
    }

    override fun notifyAttack(sufferable: Sufferable) {
        //println("子弹打到了障碍物")
        //子弹打到障碍物  需要销毁掉
        this.isDestroyed=true
    }

    override fun notifySuffer(attackable: Attackable): Array<IView>? {
        return arrayOf(Blast(x,y))
    }
}