package com.p4.musicquest

import android.content.Context
import android.opengl.GLES30 as gl
import android.opengl.GLSurfaceView
import androidx.compose.material3.Text
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

    override fun onDrawFrame(unused: GL10) {
        val curTime = System.currentTimeMillis()
        dt = (curTime - prevTime).toFloat() / 1000
        prevTime = curTime

        // set greyness targets

        shader.setTargetGreyness("village", 0f)
        shader.setTargetGreyness("forest", 0f)
        shader.setTargetGreyness("ice", if (world.state == World.WorldState.ICE_UNGREYED) 0f else 1f)

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

            world.player?.update(dt)

            camera.followPlayer(world.player!!, dt)

            world.discForest?.update(dt)
            world.discTest?.update(dt)
            world.discForest?.draw(shader, camera)
            world.discTest?.draw(shader, camera)

            for (villager in world.listVillager) {
                villager.update(dt)
                villager.draw(shader, camera)

                if (villager.showSignal) {
                    villager.drawEntity(shader, camera)
                }
            }

            if(world.iceBoss?.health!! > 0) {
                world.iceBoss?.update(dt)
                world.iceBoss?.draw(shader, camera)
            }

            world.player?.draw(shader, camera)

            for (monster in world.listMonster) {

                if (monster.health <= 0) {
                    continue
                }

                monster.update(dt)
                monster.draw(shader, camera)
            }

            for (shoot in world.listShoot) {
                shoot.update(dt)
                shoot.draw(shader, camera)
            }

        } else if (ui.uiState == UI.UIState.DIALOG) {
            world.player?.update(dt)
            world.player?.draw(shader, camera)

            for (villager in world.listVillager) {
                //TODO("afficher seulement le villageois qui interagit")
                villager.update(dt)
                villager.draw(shader, camera)
            }

        }

        // render UI

        ui.draw(shader, dt)
        
    }

    /*
    fun shoot() {
        if (world.player != null) {

            // if player moves
            if (abs(world.player!!.velocity[0]) >= 0.005f && abs(world.player!!.velocity[2]) >= 0.005f) {
                listShoot[numberShoot].directionEntity[0] = world.player!!.direction[0]
                listShoot[numberShoot].directionEntity[2] = world.player!!.direction[2]
            }

            listShoot[numberShoot].position[0] = world.player!!.position[0]
            listShoot[numberShoot].position[2] = world.player!!.position[2]

        }
        numberShoot = (numberShoot + 1) % 3
    }
    */
}