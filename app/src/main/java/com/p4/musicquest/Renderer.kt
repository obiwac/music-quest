package com.p4.musicquest

import android.content.Context
import android.graphics.Color
import android.opengl.GLSurfaceView
import com.p4.musicquest.entities.Monster
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLES30 as gl


open class Renderer(private val context: Context) : GLSurfaceView.Renderer {
    private lateinit var world: World
    lateinit var shader: Shader
    private lateinit var mask: Mask
    lateinit var ui: UI

    lateinit var camera: Camera

    private var prevTime: Long = 0

    open var dt = 0f
    object TimerSpawn {
        var spawnChance = 0f
    }


    override fun onSurfaceCreated(unused: GL10, config: EGLConfig?) {
        prevTime = System.currentTimeMillis()

        world = World(context, this)
        shader = Shader(context, "shaders/vert.glsl", "shaders/frag.glsl")
        mask = Mask(context)

        // Entities and others are created in world

        camera = Camera()
        ui = UI(context, world.player!!)
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        gl.glViewport(0, 0, width, height)

        camera.updateResolution(width, height)
        ui.updateResolution(width, height)
    }
    fun update(dt: Float){
        // Which part of the mask is the player in?

        val player = world.player

        if (player != null) {
            // TODO Put this in Mask but I'm too tired to do this now, mothafucka.

            val maskScale = .15f
            val maskRadius = 235.2f

            val maskLeft = -maskRadius * maskScale
            val maskRight = maskRadius * maskScale
            val maskBottom = -maskRadius * maskScale
            val maskTop = maskRadius * maskScale

            val maskX = 1f - (player.position[0] - maskLeft) / (maskRight - maskLeft)
            val maskY = (player.position[2] - maskBottom) / (maskTop - maskBottom)

            val colour = mask.query(maskX, maskY)

            val r = Color.red(colour) / 256f
            val g = Color.green(colour) / 256f
            val b = Color.blue(colour) / 256f

            val rLo = r < .33f
            val rMi = !rLo && r < .66f
            val rHi = r > .66f

            val gLo = g < .33f
            val gMi = !gLo && g < .66f
            val gHi = g > .66f

            val bLo = b < .33f
            val bMi = !bLo && b < .66f
            val bHi = b > .66f

            var greyness = 0f

            if (rHi && gLo && bLo) { // red
                greyness = shader.getGreyness("village")
            }

            if (rLo && gHi && bLo) { // green
                greyness = shader.getGreyness("forest")
            }

            if (
                (rHi && gHi && bLo) || // yellow
                (rLo && gLo && bHi) // blue
            ) {
                greyness = shader.getGreyness("ice")
            }

            if (
                (rLo && gHi && bHi) || // cyan
                (rLo && gLo && bLo) || // black
                (rHi && gMi && bLo) // orange
            ) {
                greyness = shader.getGreyness("beach")
            }

            if (
                (rHi && gHi && bHi) || // white
                (rLo && gHi && bMi) // turquoise
            ) {
                greyness = shader.getGreyness("magma")
            }

            if (
                (rHi && gLo && bHi) || // magenta
                (rMi && gMi && bMi) // grey
            ) {
                greyness = shader.getGreyness("candy")
            }

            if (greyness > .5f) {
                world.player!!.getHit(null)
                if (world.player!!.health <= 0) {
                    if (ui.uiState != UI.UIState.MENU) {
                        ui.uiState = UI.UIState.DEAD
                    }
                    TimerSpawn.spawnChance = 0f
                }

            }
        }

        // Procedural spawning of enemies around the player.

        TimerSpawn.spawnChance += kotlin.random.Random.nextFloat() * dt
        if (TimerSpawn.spawnChance >= (50f  -world.player?.health!!.toFloat())){
            val listCoordsMonster = arrayOf(arrayOf(world.player?.position!![0] + 2.2f, 0f, world.player?.position!![2]),
                arrayOf(world.player?.position!![0] - 2.2f, 0f, world.player?.position!![2]),
                arrayOf(world.player?.position!![0], 0f, world.player?.position!![2] + 2.2f),
                arrayOf(world.player?.position!![0], 0f, world.player?.position!![2] - 2.2f))
            // TODO(delete monsters when player died)
            for (i in listCoordsMonster.indices) {
                world.listMonster.add(Monster(context, world, listCoordsMonster[i], world.player))
            }
            TimerSpawn.spawnChance=0f
        }


        if (world.player!!.health <= 0) {
            ui.uiState = UI.UIState.DEAD
            world.player!!.resetPlayer()
            TimerSpawn.spawnChance = 0f

        } else if (ui.uiState == UI.UIState.PLAYING) {

            world.player?.update(dt)

            camera.followPlayer(world.player!!, dt)

            val iteratorItem = world.listItem.listIterator()
            while (iteratorItem.hasNext()) {
                val item = iteratorItem.next()

                item.update(dt)

                if (item.position[0] == 999f && item.position[2] == 999f) {
                    iteratorItem.remove()
                }
            }

            world.coin1?.update(dt)

            for (villager in world.listVillager) {
                villager.update(dt)
            }

            val iteratorBoss = world.listBoss.listIterator()
            while (iteratorBoss.hasNext()) {
                val boss = iteratorBoss.next()

                boss.update(dt)

                if (boss.health <= 0) {
                    iteratorBoss.remove()
                }
            }

            val iteratorMonster = world.listMonster.listIterator()
            while (iteratorMonster.hasNext()) {
                val monster = iteratorMonster.next()

                monster.update(dt)

                if (monster.health <= 0) {
                    iteratorMonster.remove()
                }
            }

            val iteratorCoin = world.listCoins.listIterator()
            while (iteratorCoin.hasNext()) {
                val coin = iteratorCoin.next()

                coin.update(dt)

                if (coin.position[0] == 999f && coin.position[2] == 999f) {
                    iteratorCoin.remove()
                }
            }

            val iteratorShoot = world.listShoot.listIterator()
            while (iteratorShoot.hasNext()) {
                val shoot = iteratorShoot.next()

                shoot.update(dt)

                if (shoot.position[0] == 999f && shoot.position[2] == 999f) {
                    iteratorShoot.remove()
                }
            }

        } else if (ui.uiState == UI.UIState.DIALOG || ui.uiState == UI.UIState.SHOP) {
            world.player?.update(dt)

            for (villager in world.listVillager) {
                //TODO("afficher seulement le villageois qui interagit")
                villager.update(dt)
            }

        }
    }

    override fun onDrawFrame(unused: GL10) {
        val curTime = System.currentTimeMillis()
        dt = (curTime - prevTime).toFloat() / 1000
        prevTime = curTime
        update(dt)

        // set greyness targets

        shader.setTargetGreyness("village", 0f)
        shader.setTargetGreyness("forest", 0f)
        shader.setTargetGreyness("ice", if (world.state == World.WorldState.ICE_UNGREYED) 0f else 1f)
        shader.setTargetGreyness("beach", if (world.state == World.WorldState.BEACH_UNGREYED) 0f else 1f)
        shader.setTargetGreyness("magma", if (world.state == World.WorldState.MAGMA_UNGREYED) 0f else 1f)
        shader.setTargetGreyness("candy", if (world.state == World.WorldState.CANDY_UNGREYED) 0f else 1f)

        // rendering

        gl.glEnable(gl.GL_DEPTH_TEST)
        gl.glDisable(gl.GL_BLEND)

        shader.use()
        shader.setMvp(camera.mvp(0f, 0f, 0f))

        gl.glClearColor(0f, 1f, .5f, 1f)
        gl.glClear(gl.GL_COLOR_BUFFER_BIT or gl.GL_DEPTH_BUFFER_BIT)

        gl.glActiveTexture(gl.GL_TEXTURE1)
        shader.setMaskSampler(1, dt)
        gl.glBindTexture(gl.GL_TEXTURE_2D, mask.tex.tex)

        world.draw(shader)

        gl.glActiveTexture(gl.GL_TEXTURE1)
        gl.glBindTexture(gl.GL_TEXTURE_2D, 0)

        // Choice of what drawing

        if (world.player!!.health <= 0) {
            ui.uiState = UI.UIState.DEAD
            world.player!!.resetPlayer()

        } else if (ui.uiState == UI.UIState.PLAYING) {

            world.player?.draw(shader, camera)

            for (item in world.listItem) {

                if (item.position[0] == 999f && item.position[2] == 999f) {
                    continue
                }
                item.draw(shader, camera)
            }

            for (villager in world.listVillager) {
                villager.draw(shader, camera)

                if (villager.showSignal) {
                    villager.drawEntity(shader, camera)
                }
            }

            for (boss in world.listBoss) {
                if (boss.health <= 0) {
                    continue
                }

                boss.draw(shader, camera)
            }

            for (monster in world.listMonster) {

                if (monster.health <= 0) {
                    continue
                }

                monster.draw(shader, camera)
            }

            for (shoot in world.listShoot) {

                if (shoot.position[0] == 999f && shoot.position[2] == 999f) {
                    continue
                }
                shoot.draw(shader, camera)
            }

            for (coin in world.listCoins) {

                if (coin.position[0] == 999f && coin.position[2] == 999f) {
                    continue
                }
                coin.draw(shader, camera)
            }

        } else if (ui.uiState == UI.UIState.DIALOG || ui.uiState == UI.UIState.SHOP) {
            world.player?.draw(shader, camera)

            for (villager in world.listVillager) {
                //TODO("afficher seulement le villageois qui interagit")
                villager.draw(shader, camera)
            }

        }

        // render UI

        ui.draw(shader, dt)
        
    }
}
