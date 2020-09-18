package com.hanjinliang.tank.mode

import com.hanjinliang.tank.Config
import com.hanjinliang.tank.business.Attackable
import com.hanjinliang.tank.business.Blockable
import com.hanjinliang.tank.business.Movable
import com.hanjinliang.tank.business.Sufferable
import com.hanjinliang.tank.enums.Direction
import org.itheima.kotlin.game.core.Painter

class Tank(override var x: Int, override var y: Int) :Movable,Blockable,Sufferable{
    override var blood: Int=20

    //宽高
    override val width:Int= Config.block
    override val height:Int= Config.block

    override val speed:Int=10

    private var badDirection:Direction?=null



    override fun notifyCollision(direction: Direction?, blockable: Blockable?) {
        //接收到碰撞信息
        this.badDirection=direction
    }

    //方向
    override var currentDirection:Direction=Direction.UP

    //显示
    override fun draw(){
        val imagePath = when (currentDirection) {
            Direction.UP -> "img/p1tankU.gif"
            Direction.RIGHT -> "img/p1tankR.gif"
            Direction.DOWN -> "img/p1tankD.gif"
            Direction.LEFT -> "img/p1tankL.gif"
        }
        Painter.drawImage(imagePath,x,y)
    }

    fun move(direction: Direction){
        if(direction==badDirection){//有碰撞了
            return
        }

        if(this.currentDirection!=direction){//当前方向和希望移动方向不一致的时候  只改变方向
            this.currentDirection=direction
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
        if(x>Config.gameWidth - width) x=Config.gameWidth - width

        if(y<0) y=0
        if(y>Config.gameHeight - height) y=Config.gameHeight - height

        //

    }

    fun shot():Bullet {
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
        blood-=attackable.attackPower
        return arrayOf(Blast(x,y))
    }
}