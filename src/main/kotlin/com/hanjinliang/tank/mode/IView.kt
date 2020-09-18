package com.hanjinliang.tank.mode

/**
 * 显示的视图规范
 */
interface IView {
    //位置
    val x:Int
    val  y:Int

    //宽高
    val  width:Int
    val  height:Int

    //显示
    fun draw()

    fun checkCollision(x1:Int,y1:Int,w1:Int,h1:Int,
                       x2:Int,y2:Int,w2:Int,h2:Int):Boolean{
        //两个物体的 x,y,w,h的比较
        var collision= if(y2+h2<=y1){
            false
        }else if(y1+h1<=y2){//阻挡物在运动物体下方
            false
        }else if(x2+w2<=x1){//阻挡物在运动物体左方
            false
        }else if(x1 + w1<=x2){//阻挡物在运动物体下方
            false
        }else{
            //碰撞了
            true
        }
        return collision
    }
}