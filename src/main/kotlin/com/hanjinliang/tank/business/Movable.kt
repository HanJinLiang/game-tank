package com.hanjinliang.tank.business

import com.hanjinliang.tank.Config
import com.hanjinliang.tank.enums.Direction
import com.hanjinliang.tank.mode.IView

/**
 * 移动的能力
 */
interface Movable :IView {
    /**
     * 当前方向
     */
    val currentDirection:Direction

    /**
     * 移动速度
     */
    val speed:Int

    /**
     * 判断移动物体和阻塞是否发生碰撞
     * Direction 即将要碰撞的方向  null表示没有
     */
    fun willCollision(blockable: Blockable):Direction?{
        //将要碰撞 先虚拟移动一次 在比较
        var x=this.x
        var y=this.y

        when (currentDirection) {
            Direction.UP -> y-=speed
            Direction.RIGHT -> x+=speed
            Direction.DOWN -> y+=speed
            Direction.LEFT -> x-=speed
        }

        //和边界检测  和边界碰撞
        if(x<0) return Direction.LEFT
        if(x> Config.gameWidth - width) return Direction.RIGHT
        if(y<0)  return Direction.UP
        if(y> Config.gameHeight - height) return Direction.DOWN


        //碰撞检测  下一步
        var collision=checkCollision(x,y,width,height,blockable.x,blockable.y,blockable.width,blockable.height)

        return if(collision) currentDirection else null
    }

    fun notifyCollision(direction: Direction?,blockable: Blockable?)

}