package com.p4.musicquest

import android.content.Context
import android.opengl.GLES30 as gl
import android.opengl.GLSurfaceView
import androidx.compose.material3.Text
import com.p4.musicquest.entities.Monster
import com.p4.musicquest.entities.Shoot
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.abs
import kotlin.math.sqrt

open class Renderer(private val context: Context) : GLSurfaceView.Renderer {
    private lateinit var world: World
    lateinit var shader: Shader
    private lateinit var mask: Texture
    lateinit var ui: UI

    lateinit var camera: Camera

    private var prevTime: Long = 0

    open var dt = 0f
    object Timer_spawn {
        var spawn_chance = 0f
    }


    override fun onSurfaceCreated(unused: GL10, config: EGLConfig?) {
        prevTime = System.currentTimeMillis()

        world = World(context, this)
        shader = Shader(context, "shaders/vert.glsl", "shaders/frag.glsl")
        mask = Texture(context, "textures/mask.png")

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

        Timer_spawn.spawn_chance += kotlin.random.Random.nextFloat() * dt
        if (Timer_spawn.spawn_chance>= (50f  -world.player?.health!!.toFloat())){
            val listCoordsMonster = arrayOf(arrayOf(world.player?.position!![0] + 2.2f, 0f, world.player?.position!![2]),
                arrayOf(world.player?.position!![0] - 2.2f, 0f, world.player?.position!![2]),
                arrayOf(world.player?.position!![0], 0f, world.player?.position!![2] + 2.2f),
                arrayOf(world.player?.position!![0], 0f, world.player?.position!![2] - 2.2f))
            for (i in listCoordsMonster.indices) {
                world.listMonster.add(Monster(context, world, listCoordsMonster[i], world.player))
            }
            Timer_spawn.spawn_chance=0f
        }


        if (world.player!!.health <= 0) {
            ui.uiState = UI.UIState.DEAD
            world.player!!.resetPlayer()
            Timer_spawn.spawn_chance = 0f

        } else if (ui.uiState == UI.UIState.PLAYING) {

            world.player?.update(dt)

            camera.followPlayer(world.player!!, dt)

            world.discForest?.update(dt)
            world.discTest?.update(dt)

            world.coin1?.update(dt)
            world.iceDisc?.update(dt)
            world.beachDisc?.update(dt)
            //world.mountainDisc?.update(dt)



            for (villager in world.listVillager) {
                villager.update(dt)
            }
            if (world.iceBoss != null) {
                world.iceBoss!!.update(dt)

            }
            if (world.iceBoss != null) {
                if(world.iceBoss?.health!! <= 0) {
                    world.iceBoss = null}
            }

            if (world.slimeBoss != null){
                world.slimeBoss!!.update(dt)
            }
            if (world.slimeBoss != null){
                if (world.slimeBoss?.health!! <= 0) {
                    world.slimeBoss = null
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

            for (coin in world.listCoins) {
                coin.update(dt)
            }

            for (shoot in world.listShoot) {
                shoot.update(dt)
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
        gl.glBindTexture(gl.GL_TEXTURE_2D, mask.tex)

        world.draw(shader)

        gl.glActiveTexture(gl.GL_TEXTURE1)
        gl.glBindTexture(gl.GL_TEXTURE_2D, 0)

        // Choice of what drawing

        if (world.player!!.health <= 0) {
            ui.uiState = UI.UIState.DEAD
            world.player!!.resetPlayer()

        } else if (ui.uiState == UI.UIState.PLAYING) {

            world.player?.draw(shader, camera)

            world.discForest?.draw(shader, camera)
            world.discTest?.draw(shader, camera)

            world.coin1?.draw(shader, camera)
            world.iceDisc?.draw(shader,camera)
            world.beachDisc?.draw(shader,camera)
            //world.mountainDisc?.draw(shader,camera)

            for (villager in world.listVillager) {
                villager.draw(shader, camera)

                if (villager.showSignal) {
                    villager.drawEntity(shader, camera)
                }
            }

            if (world.iceBoss != null){
                if(world.iceBoss?.health!! > 0  ) {
                    world.iceBoss?.draw(shader, camera)
                }
            }
            if (world.slimeBoss != null){
                if(world.slimeBoss?.health!! > 0) {
                    world.slimeBoss?.draw(shader, camera)
                }
            }

            for (monster in world.listMonster) {

                if (monster.health <= 0) {
                    continue
                }

                monster.draw(shader, camera)
            }

            for (shoot in world.listShoot) {
                shoot.draw(shader, camera)
            }

            for (coin in world.listCoins) {
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