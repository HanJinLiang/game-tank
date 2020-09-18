package com.hanjinliang.tank.mode

import com.hanjinliang.tank.Config
import com.hanjinliang.tank.business.Destroyable
import org.itheima.kotlin.game.core.Painter

/**
 * 爆炸物
 */
class Blast(override val x: Int, override val y: Int) :IView,Destroyable {
    //宽高
    override var width:Int= Config.block/2
    override var height:Int= Config.block/2

    private val imagePaths= arrayListOf<String>()
    private var index:Int=0
    init {
        (1..32).forEach{
            imagePaths.add("img/blast_${it}.png")
        }
    }

    //显示
    override fun draw(){
        val i=index%imagePaths.size
        Painter.drawImage(imagePaths[i],x,y)
        index++
    }

    override fun isDestroyed(): Boolean {
         return index>=imagePaths.size
    }

}