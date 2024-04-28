package com.p4.musicquest

import android.content.Context
import android.graphics.Color
import android.opengl.GLSurfaceView
import com.p4.musicquest.entities.Monster
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.pow
import kotlin.math.sqrt
import android.opengl.GLES30 as gl


open class Renderer(private val context: Context) : GLSurfaceView.Renderer {
    private lateinit var world: World
    lateinit var mapShader: Shader
    lateinit var shader: Shader
    private lateinit var mask: Mask
    private lateinit var water: Texture
    private lateinit var lava: Texture
    private lateinit var choco: Texture
    lateinit var ui: UI

    lateinit var camera: Camera

    private var prevTime: Long = 0

    open var dt = 0f
    object TimerSpawn {
        var spawnChance = 0f
    }

    // Distance to show entity relative to the distance from the player

    val renderDistance = 5f

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig?) {
        prevTime = System.currentTimeMillis()

        world = World(context, this)
        shader = Shader(context, "shaders/vert.glsl", "shaders/frag.glsl")
        mapShader = Shader(context, "shaders/map_vert.glsl", "shaders/map_frag.glsl")
        mask = Mask(context)
        water = Texture(context, "textures/water.png")
        lava = Texture(context, "textures/lava.png")
        choco = Texture(context, "textures/choco.png")

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

            val rLo = r < .1f
            val rMi = !rLo && r < .9f
            val rHi = r > .9f

            val gLo = g < .1f
            val gMi = !gLo && g < .9f
            val gHi = g > .9f

            val bLo = b < .1
            val bMi = !bLo && b < .9f
            val bHi = b > .9f

            val village = rHi && gLo && bLo; // red
            val forest = rLo && gHi && bLo; // green
            val ice = rHi && gHi && bLo; // yellow
            val icePath = rLo && gLo && bHi; // blue
            val sand = rLo && gHi && bHi; // cyan
            val water = rLo && gLo && bLo; // black
            val oil = rHi && gMi && bLo; // orange
            val lava = rHi && gHi && bHi; // white
            val magma = rLo && gHi && bMi; // turquoise
            val candy = rHi && gLo && bHi; // magenta
            val choco = rMi && gMi && bMi; // grey

            var greyness = 0f

            if (village) {
                greyness = mapShader.getGreyness("village")
            }

            if (forest) {
                greyness = mapShader.getGreyness("forest")
            }

            if (ice || icePath) {
                greyness = mapShader.getGreyness("ice")
            }

            if (sand || water || oil) {
                greyness = mapShader.getGreyness("beach")
            }

            if (lava || magma) {
                greyness = mapShader.getGreyness("magma")
            }

            if (candy || choco) {
                greyness = mapShader.getGreyness("candy")
            }

            if (choco) {
                player.velocity[0] -= .2f
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

                if (distanceToPlayer(item) <= renderDistance) {
                    item.update(dt)
                }

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

                if (distanceToPlayer(boss) <= renderDistance) {
                    boss.update(dt)
                }

                if (boss.health <= 0) {
                    iteratorBoss.remove()
                }
            }

            val iteratorMonster = world.listMonster.listIterator()
            while (iteratorMonster.hasNext()) {
                val monster = iteratorMonster.next()

                if (distanceToPlayer(monster) <= renderDistance) {
                    monster.update(dt)
                }

                if (monster.health <= 0) {
                    iteratorMonster.remove()
                }
            }

            val iteratorCoin = world.listCoins.listIterator()
            while (iteratorCoin.hasNext()) {
                val coin = iteratorCoin.next()

                if (distanceToPlayer(coin) <= renderDistance) {
                    coin.update(dt)
                }

                if (coin.position[0] == 999f && coin.position[2] == 999f) {
                    iteratorCoin.remove()
                }
            }

            val iteratorShoot = world.listShoot.listIterator()
            while (iteratorShoot.hasNext()) {
                val shoot = iteratorShoot.next()

                if (distanceToPlayer(shoot) <= renderDistance) {
                    shoot.update(dt)
                }

                if (shoot.position[0] == 999f && shoot.position[2] == 999f) {
                    iteratorShoot.remove()
                }
            }

        } else if (ui.uiState == UI.UIState.DIALOG || ui.uiState == UI.UIState.SHOP) {
            world.player?.update(dt)

            for (villager in world.listVillager) {
                if (distanceToPlayer(villager) <= 2f) {
                    villager.update(dt)
                }
            }

        }
    }

    override fun onDrawFrame(unused: GL10) {
        val curTime = System.currentTimeMillis()
        dt = (curTime - prevTime).toFloat() / 1000
        prevTime = curTime
        update(dt)

        // set greyness targets

        mapShader.setTargetGreyness("village", 0f)
        mapShader.setTargetGreyness("forest", 0f)
        mapShader.setTargetGreyness("ice", if (world.state == World.WorldState.ICE_UNGREYED) 0f else 1f)
        mapShader.setTargetGreyness("beach", if (world.state == World.WorldState.BEACH_UNGREYED) 0f else 1f)
        mapShader.setTargetGreyness("magma", if (world.state == World.WorldState.MAGMA_UNGREYED) 0f else 1f)
        mapShader.setTargetGreyness("candy", if (world.state == World.WorldState.CANDY_UNGREYED) 0f else 1f)

        // start rendering pass

        gl.glEnable(gl.GL_DEPTH_TEST)
        gl.glEnable(gl.GL_BLEND)
        gl.glBlendFunc(gl.GL_SRC_ALPHA, gl.GL_ONE_MINUS_SRC_ALPHA)

        gl.glClearColor(0f, 1f, .5f, 1f)
        gl.glClear(gl.GL_COLOR_BUFFER_BIT or gl.GL_DEPTH_BUFFER_BIT)

        // render map

        mapShader.use()
        mapShader.setMvp(camera.mvp(0f, 0f, 0f))
        mapShader.setTime(dt)

        gl.glActiveTexture(gl.GL_TEXTURE1)
        mapShader.setMaskSampler(1, dt)
        gl.glBindTexture(gl.GL_TEXTURE_2D, mask.tex.tex)

        gl.glActiveTexture(gl.GL_TEXTURE2)
        mapShader.setWaterSampler(2)
        gl.glBindTexture(gl.GL_TEXTURE_2D, water.tex)

        gl.glActiveTexture(gl.GL_TEXTURE3)
        mapShader.setLavaSampler(3)
        gl.glBindTexture(gl.GL_TEXTURE_2D, lava.tex)

        gl.glActiveTexture(gl.GL_TEXTURE4)
        mapShader.setChocoSampler(4)
        gl.glBindTexture(gl.GL_TEXTURE_2D, choco.tex)

        world.draw(mapShader)

        // render everything else

        shader.use()
        shader.setMvp(camera.mvp(0f, 0f, 0f))

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

                if (distanceToPlayer(item) <= renderDistance) {
                    item.draw(shader, camera)
                }
            }

            for (villager in world.listVillager) {
                if (distanceToPlayer(villager) <= renderDistance) {
                    villager.draw(shader, camera)

                    if (villager.showSignal) {
                        villager.drawEntity(shader, camera)
                    }
                }
            }

            for (boss in world.listBoss) {
                if (boss.health <= 0) {
                    continue
                }

                if (distanceToPlayer(boss) <= renderDistance) {
                    boss.draw(shader, camera)
                }
            }

            for (monster in world.listMonster) {

                if (monster.health <= 0) {
                    continue
                }

                if (distanceToPlayer(monster) <= renderDistance) {
                    monster.draw(shader, camera)
                }
            }

            for (shoot in world.listShoot) {

                if (shoot.position[0] == 999f && shoot.position[2] == 999f) {
                    continue
                }

                if (distanceToPlayer(shoot) <= renderDistance) {
                    shoot.draw(shader, camera)
                }
            }

            for (coin in world.listCoins) {

                if (coin.position[0] == 999f && coin.position[2] == 999f) {
                    continue
                }

                if (distanceToPlayer(coin) <= renderDistance) {
                    coin.draw(shader, camera)
                }
            }

        } else if (ui.uiState == UI.UIState.DIALOG || ui.uiState == UI.UIState.SHOP) {
            world.player?.draw(shader, camera)

            for (villager in world.listVillager) {
                if (distanceToPlayer(villager) <= 2f) {
                    villager.draw(shader, camera)
                }
            }

        }

        // render UI

        ui.draw(shader, dt)
        
    }

    fun distanceToPlayer(target: Entity): Float {
        if (world.player == null) {
            return 999f
        }
        val distanceToPlayerX = world.player!!.position[0] - target.position[0]
        val distanceToPlayerY = world.player!!.position[2] - target.position[2]

        // calculate distance between enemy to player
        val distanceToPlayer = sqrt((distanceToPlayerX).pow(2) + (distanceToPlayerY).pow(2))

        return distanceToPlayer
    }
}
