package com.hanjinliang.tank.mode

import com.hanjinliang.tank.Config
import com.hanjinliang.tank.business.*
import com.hanjinliang.tank.enums.Direction
import org.itheima.kotlin.game.core.Painter
import kotlin.random.Random

/**
 * 敌方坦克
 */
class Enemy(override var x: Int, override var y: Int) :Movable,AutoMovable,Blockable, AutoShot ,Sufferable,Destroyable {
    override var blood: Int = 4

    override var currentDirection: Direction = Direction.DOWN
    override val speed: Int=4

    //宽高
    override val width:Int=Config.block
    override val height:Int=Config.block

    //上次射击时间 射击频率 200毫秒
    private var lastShotTime=0L
    private val shotFrequency=500

    override fun draw() {
        val imagePath = when (currentDirection) {
            Direction.UP -> "img/enemy1U.gif"
            Direction.RIGHT -> "img/enemy1R.gif"
            Direction.DOWN -> "img/enemy1D.gif"
            Direction.LEFT -> "img/enemy1L.gif"
        }
        Painter.drawImage(imagePath,x,y)
    }
    private var badDirection:Direction?=null

    override fun notifyCollision(direction: Direction?, blockable: Blockable?) {
        //接收到碰撞信息
        this.badDirection=direction
    }

    override fun autoMove() {
        if(currentDirection==badDirection){
            //碰到障碍物不能前进 改变自己防线
            currentDirection=randomDirection(badDirection)
            return
        }

        //坦克坐标发生变化
        when (currentDirection) {
            Direction.UP -> y-=speed
            Direction.RIGHT -> x+=speed
            Direction.DOWN -> y+=speed
            Direction.LEFT -> x-=speed
        }

        //边界处理
        if(x<0) x=0
        if(x> Config.gameWidth - width) x= Config.gameWidth - width

        if(y<0) y=0
        if(y> Config.gameHeight - height) y= Config.gameHeight - height
    }

    /**
     * 随机生成一个方向
     */
    private fun randomDirection(badDirection:Direction?):Direction{
        val direction=when(Random.nextInt(4)){
            0->Direction.UP
            1->Direction.RIGHT
            2->Direction.DOWN
            3->Direction.LEFT
            else -> Direction.UP
        }
        if(direction==badDirection){
            return randomDirection(badDirection);
        }
        return direction
    }

    /**
     * 自动射击
     */
    override fun autoShot(): IView? {
        val currentTimeMillis = System.currentTimeMillis()
        if(currentTimeMillis-lastShotTime<shotFrequency)return null
        lastShotTime=currentTimeMillis

        return Bullet(this,currentDirection) { bulletWidth, bulletHeight ->
            val tankX=this.x
            val tankY=this.y
            val tankWidth=this.width
            val tankHeight=this.height


            var bulletX=0
            var bulletY=0
            //计算子弹的 x y
            when (currentDirection) {
                Direction.UP -> {
                    //如果坦克方向朝上
                    bulletX=tankX+(tankWidth-bulletWidth)/2
                    bulletY=tankY - bulletHeight/2
                }
                Direction.RIGHT ->{
                    //如果坦克方向朝右
                    bulletX=tankX+ tankWidth-bulletWidth/2
                    bulletY=tankY + (tankHeight-bulletHeight)/2
                }
                Direction.DOWN -> {
                    //如果坦克方向朝下
                    bulletX=tankX + (tankWidth-bulletWidth)/2
                    bulletY=tankY + (tankHeight- bulletHeight/2)
                }
                Direction.LEFT ->{
                    //如果坦克方向朝坐
                    bulletX=tankX - bulletWidth/2
                    bulletY=tankY + (tankHeight-bulletHeight)/2
                }
            }
            Pair(bulletX,bulletY)
        }
    }

    override fun notifySuffer(attackable: Attackable): Array<IView>? {
        if(attackable.owner is Enemy){
            //敌人互相打的时候不掉血
            return null
        }

        blood-=attackable.attackPower
        return arrayOf(Blast(x,y))
    }

    override fun isDestroyed(): Boolean {
        return blood<=0
    }

}