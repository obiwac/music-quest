package com.p4.musicquest

import kotlin.math.abs

class Animator (private val listSprite: ArrayList<Sprite>){

	// Change sprite in function of state for the entity's animation

	companion object {
		const val MAX_UPDATES_BEFORE_NEXT_STAND_FRAME = 50
		const val MAX_UPDATES_BEFORE_NEXT_MOVE_FRAME = 10
	}

	private var updateBeforeNextMoveFrame = 0
	private var updateBeforeNextStandFrame = 0
	private var indexStandingFrame = 0
	private var indexMovingFrame = 2

	fun draw(shader: Shader, camera: Camera, x: Float, y: Float, z: Float, entity: Entity) {
		when(entity.entityState.state) {
			EntityState.State.NOT_MOVING -> {
				updateBeforeNextStandFrame = MAX_UPDATES_BEFORE_NEXT_STAND_FRAME
				drawFrame(shader, camera, x, y, z, listSprite[indexStandingFrame])
			}

			EntityState.State.NOT_MOVING_TWO -> {
				updateBeforeNextStandFrame--
				if (updateBeforeNextStandFrame == 0) {
					updateBeforeNextStandFrame = MAX_UPDATES_BEFORE_NEXT_STAND_FRAME
					changeIndexStandingFrame()
				}
				drawFrame(shader, camera, x, y, z, listSprite[indexStandingFrame])
			}

			EntityState.State.STARTED_MOVING -> {
				updateBeforeNextMoveFrame = MAX_UPDATES_BEFORE_NEXT_MOVE_FRAME
				drawFrame(shader, camera, x, y, z, listSprite[indexMovingFrame])
			}

			EntityState.State.IS_MOVING -> {
				updateBeforeNextMoveFrame--
				if (updateBeforeNextMoveFrame == 0) {
					updateBeforeNextMoveFrame = MAX_UPDATES_BEFORE_NEXT_MOVE_FRAME
					changeIndexMovingFrame()
				}
				drawFrame(shader, camera, x, y, z, listSprite[indexMovingFrame])
			}
		}
	}

	private fun changeIndexStandingFrame() {
		if (indexStandingFrame == 0) {
			indexStandingFrame = 1
		} else {
			indexStandingFrame = 0
		}
	}

	private fun changeIndexMovingFrame() {
		if (indexMovingFrame == 2) {
			indexMovingFrame = 3
		} else {
			indexMovingFrame = 2
		}
	}

	fun drawFrame(shader: Shader, camera: Camera, x: Float, y: Float, z: Float, sprite: Sprite) {
		sprite.draw(shader, camera, x, y, z)
	}
}