package com.p4.musicquest

import android.content.Context
import android.opengl.GLES30 as gl
import android.opengl.GLSurfaceView
import com.p4.musicquest.entities.Shoot
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.abs
import kotlin.math.sqrt

open class Renderer(private val context: Context) : GLSurfaceView.Renderer {
    private lateinit var world: World
    lateinit var shader: Shader
    lateinit var ui: UI

    lateinit var listShoot: ArrayList<Shoot>
    var numberShoot = 0

    lateinit var camera: Camera

    private var prevTime: Long = 0

    private var rightMul: Float = 0f
    private var leftMul: Float = 0f
    private var topMul: Float = 0f
    private var bottomMul: Float = 0f

    private var targetRightMul: Float = 1f
    private var targetLeftMul: Float = 1f
    private var targetTopMul: Float = 1f
    private var targetBottomMul: Float = 0f

    open var dt = 0f

    fun setRight() {
        targetRightMul = 0f
        targetLeftMul = 1f
        targetTopMul = 1f
        targetBottomMul = 1f
    }

    fun setLeft() {
        targetRightMul = 1f
        targetLeftMul = 0f
        targetTopMul = 1f
        targetBottomMul = 1f
    }

    fun setTop() {
        targetRightMul = 1f
        targetLeftMul = 1f
        targetTopMul = 0f
        targetBottomMul = 1f
    }

    fun setBottom() {
        targetRightMul = 1f
        targetLeftMul = 1f
        targetTopMul = 1f
        targetBottomMul = 0f
    }

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig?) {
        prevTime = System.currentTimeMillis()

        world = World(context, this)
        shader = Shader(context, "shaders/vert.glsl", "shaders/frag.glsl")

        // Entities and others are created in world

        //TODO("remettre le shoot")
        listShoot = ArrayList<Shoot>()
        for (i in 1..3) {
            listShoot.add(Shoot(context, world.player!!, world, arrayOf(999f, 0f, 999f))) // pour qu'on voit pas la fleche
        }

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

        // world section animation

        rightMul += (targetRightMul - rightMul) * dt * 3
        leftMul += (targetLeftMul - leftMul) * dt * 3
        topMul += (targetTopMul - topMul) * dt * 3
        bottomMul += (targetBottomMul - bottomMul) * dt * 3

        // check if player should die

        val (x, y, z) = world.player!!.position
        val dist = sqrt(x * x + z * z)

        /*
        if (dist > 1.5f) {
            if (x - abs(z) > 0 && rightMul > .9f) {
                player?.position = arrayOf(0f, 0f, -1f)
            }
            if (-x - abs(z) > 0 && leftMul > .9f) {
                player?.position = arrayOf(0f, 0f, -1f)
            }
            if (z - abs(x) > 0 && topMul > .9f) {
                player?.position = arrayOf(0f, 0f, -1f)
            }
            if (-z - abs(x) > 0 && bottomMul > .9f) {
                player?.position = arrayOf(0f, 0f, -1f)
            }
        }
        */
        //println(rendererState.toString())

        // rendering

        gl.glEnable(gl.GL_DEPTH_TEST)
        gl.glDisable(gl.GL_BLEND)

        shader.use()
        shader.setMvp(camera.mvp(0f, 0f, 0f))
        shader.setMultipliers(rightMul, leftMul, topMul, bottomMul)

        gl.glClearColor(0f, 1f, .5f, 1f)
        gl.glClear(gl.GL_COLOR_BUFFER_BIT or gl.GL_DEPTH_BUFFER_BIT)

        world.draw(shader)

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

            world.player?.draw(shader, camera)

            for (monster in world.listMonster) {

                if (monster.health <= 0) {
                    continue
                }

                monster.update(dt)
                monster.draw(shader, camera)
            }

            for (shoot in listShoot) {
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

    fun shoot() {
        if (world.player != null) {

            // if player moves
            if (abs(world.player!!.velocity[0]) >= 0.005f && abs(world.player!!.velocity[2]) >= 0.005f) {
                listShoot[numberShoot].directionPlayer[0] = world.player!!.direction[0]
                listShoot[numberShoot].directionPlayer[2] = world.player!!.direction[2]
            }

            listShoot[numberShoot].position[0] = world.player!!.position[0]
            listShoot[numberShoot].position[2] = world.player!!.position[2]

        }
        numberShoot = (numberShoot + 1) % 3
    }
}