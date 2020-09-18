package com.hanjinliang.tank.ext

import com.hanjinliang.tank.mode.IView

/**
 * IView的扩展方法  直接比较两个IView 碰撞
 */
fun IView.checkCollision(view:IView):Boolean{
    return checkCollision(x,y,width,height,view.x,view.y,view.width,view.height)
}