package com.hanjinliang.tank

import com.hanjinliang.tank.business.*
import com.hanjinliang.tank.enums.Direction
import com.hanjinliang.tank.ext.checkCollision
import com.hanjinliang.tank.mode.*
import javafx.scene.input.KeyEvent
import org.itheima.kotlin.game.core.Window
import javafx.scene.input.KeyCode
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.CopyOnWriteArrayList

class GameWindow : Window("坦克大战1.0","img/grass.png",Config.gameWidth,Config.gameHeight) {
    //管理元素的集合
    //private val allView= arrayListOf<IView>()
    //线程安全的集合
    private val allView= CopyOnWriteArrayList<IView>()
    //延迟初始化
    private  lateinit var tank:Tank
    //延迟初始化 大本营
    private  lateinit var camp:Camp

    //游戏是否结束
    private var gameOver:Boolean=false

    //敌方坦克的数量
    private var enemyTotalSize=20
    //敌方屏幕显示的激活数量
    private var enemyActiveSize=6
    //敌方出生点
    private var enemyBornLoc:ArrayList<Pair<Int,Int>> = arrayListOf<Pair<Int,Int>>()
    //出生地点下标
    private var enemyBornIndex=0
    //已经死亡的坦克数量
    private var enemyDestroyedCount=0

    override fun onCreate() {
        //窗体创建回调
        //地图
        //通过地图文件获取地图 打包之后不能这样获取
        //val file= File(javaClass.getResource("/map/1.map").path)
        val resourceAsStream = javaClass.getResourceAsStream("/map/1.map")
        val reader= BufferedReader(InputStreamReader(resourceAsStream,"utf-8"))
        //读取文件的行
        val lines:List<String> = reader.readLines()
        //循环遍历
        var lineNum=0
        lines.forEach{
            var columnNum=0
            //it 每一行 空砖空空砖空空空空砖空空空
            it.toCharArray().forEach {
                when(it){
                    '砖' ->allView.add(Wall(columnNum*Config.block,lineNum*Config.block))
                    '水' ->allView.add(Water(columnNum*Config.block,lineNum*Config.block))
                    '草' ->allView.add(Grass(columnNum*Config.block,lineNum*Config.block))
                    '铁' ->allView.add(Steel(columnNum*Config.block,lineNum*Config.block))
                    '敌' ->enemyBornLoc.add(Pair(columnNum*Config.block,lineNum*Config.block))
                }
                columnNum++
            }
            lineNum++
        }
        tank= Tank(Config.block*10,Config.block*12)
        //添加我方坦克
        allView.add(tank)

        //添加大本营
        camp=Camp(Config.gameWidth/2 - Config.block,Config.gameHeight - 90)
        allView.add(camp)
    }

    override fun onDisplay() {
        //绘制地图的样式
        allView.forEach{
            it.draw()
        }

        println(allView.size)

    }

    override fun onKeyPressed(event: KeyEvent) {
        if(gameOver){
            return
        }
        // 按键响应回调
        when(event.code){
            KeyCode.W->{
                tank.move(Direction.UP)
            }
            KeyCode.A->{
                tank.move(Direction.LEFT)
            }
            KeyCode.S->{
                tank.move(Direction.DOWN)
            }
            KeyCode.D->{
                tank.move(Direction.RIGHT)
            }
            KeyCode.ENTER->{
                val bullet=tank.shot()
                //添加子弹视图
                allView.add(bullet)
            }
        }
    }

    override fun onRefresh() {
        //移除具备销毁能力的 是否已经销毁
        allView.filterIsInstance<Destroyable>().forEach { destroyable ->
            if (destroyable.isDestroyed()) {
                allView.remove(destroyable)
                if (destroyable is Enemy) {
                    enemyDestroyedCount++
                }
            }
        }

        if(gameOver){
            return
        }
        // 耗时操作回调  业务逻辑
        //碰撞检测
        //找到运动的物体
        allView.filterIsInstance<Movable>().forEach { move ->
                var badDirection:Direction?=null
                var badBlock:Blockable?=null
                //阻塞的物体  并且不是本身   敌方坦克 既是可以移动的  也是可以阻挡的  自己就不需要检测
                //
                allView.filter{it is Blockable && move!=it}.forEach blockTag@{ block ->

                    block as Blockable
                    //获取碰撞方向
                    val direction: Direction? = move.willCollision(block)

                    direction?.let{
                            //移动的发现碰撞
                            badDirection=direction
                            badBlock=block
                            return@blockTag
                        }
                }
                //找到和move碰撞的阻塞块 block 和方向
                //通知move物体
                move.notifyCollision(badDirection,badBlock)
        }

        //自动移动的View 自动移动
        allView.filterIsInstance<AutoMovable>().forEach{
            autoMove->autoMove.autoMove()
        }


        //检测具备攻击能力和具备被攻击的能力是否产生碰撞
        val attackables = allView.filterIsInstance<Attackable>()

        attackables.forEach { attack ->
            //阻塞的物体   攻击方的持有者  不能是发射方
            allView.filter { it is Sufferable && attack.owner != it && attack!=it}.forEach sufferableTag@{ suffer ->
                suffer as Sufferable
                //判断是否产生碰撞
                 if(attack.isCollision(suffer)){
                     //通知攻击者产生碰撞
                     attack.notifyAttack(suffer)
                     //通知被攻击者 被攻击  返回一个被打的View
                     val sufferView = suffer.notifySuffer(attack)
                     sufferView?.let{
                         //显示挨打的效果
                         allView.addAll(sufferView)
                     }
                     return@sufferableTag
                 }
            }

        }

        //检测自动射击
        allView.filter{it is AutoShot}.forEach{
            it as AutoShot
            val showView=it.autoShot()
            showView?.let { allView.add(showView) }
        }


        //检测游戏是否结束
        if(allView.filterIsInstance<Camp>().isEmpty() || enemyDestroyedCount==enemyTotalSize){
            //大本营没有了 GameOver 输了 沙雕
            gameOver=true
        }


        //检测敌方出生
        //判断敌方数量
        if((enemyBornIndex< enemyTotalSize) && allView.filterIsInstance<Enemy>().size < enemyActiveSize){
            val index=enemyBornIndex % enemyBornLoc.size
            val pair = enemyBornLoc[index]
            val enemy = Enemy(pair.first, pair.second)
            //当前位置是否已经有敌人
            var locHasEnemy=false
            allView.filterIsInstance<Enemy>().forEach enemyTag@{
                activeEnemy ->
                    //有碰撞 说明当前位置已经有了敌人 不能重复添加
                    if(enemy.checkCollision(activeEnemy)){
                        locHasEnemy=true
                        return@enemyTag
                    }

            }
            if(!locHasEnemy){
                allView.add(enemy)
                enemyBornIndex++
            }
        }


    }
}
