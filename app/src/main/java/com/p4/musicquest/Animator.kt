package com.p4.musicquest

class Animator (private val listSprite: ArrayList<Sprite>){

	// Change sprite in function of state for the entity's animation

	companion object {
		const val MAX_UPDATES_BEFORE_NEXT_STAND_FRAME = 50
		const val MAX_UPDATES_BEFORE_NEXT_MOVE_FRAME = 10
	}

	private var updateBeforeNextMoveFrame = MAX_UPDATES_BEFORE_NEXT_MOVE_FRAME
	private var updateBeforeNextStandFrame = MAX_UPDATES_BEFORE_NEXT_STAND_FRAME
	private var indexStandingFrame = 0
	private var indexMovingFrame = 2

	fun draw(shader: Shader, camera: Camera, x: Float, y: Float, z: Float, entity: Entity) {

		when(entity.entityState.state) {
			EntityState.State.NOT_MOVING -> {
				updateBeforeNextStandFrame--
				if (updateBeforeNextStandFrame == 0) {
					updateBeforeNextStandFrame = MAX_UPDATES_BEFORE_NEXT_STAND_FRAME
					changeIndexStandingFrame()
				}
				drawFrame(shader, camera, x, y, z, listSprite[indexStandingFrame])
			}

			EntityState.State.IS_MOVING_DOWN -> {
				updateBeforeNextMoveFrame--
				if (updateBeforeNextMoveFrame == 0) {
					updateBeforeNextMoveFrame = MAX_UPDATES_BEFORE_NEXT_MOVE_FRAME
					changeIndexMovingFrame()
				}
				drawFrame(shader, camera, x, y, z, listSprite[2 + indexMovingFrame])
			}

			EntityState.State.IS_MOVING_UP -> {
				updateBeforeNextMoveFrame--
				if (updateBeforeNextMoveFrame == 0) {
					updateBeforeNextMoveFrame = MAX_UPDATES_BEFORE_NEXT_MOVE_FRAME
					changeIndexMovingFrame()
				}
				drawFrame(shader, camera, x, y, z, listSprite[8 + indexMovingFrame])
			}

			EntityState.State.IS_MOVING_LEFT -> {
				updateBeforeNextMoveFrame--
				if (updateBeforeNextMoveFrame == 0) {
					updateBeforeNextMoveFrame = MAX_UPDATES_BEFORE_NEXT_MOVE_FRAME
					changeIndexMovingFrame()
				}
				drawFrame(shader, camera, x, y, z, listSprite[4 + indexMovingFrame])
			}

			EntityState.State.IS_MOVING_RIGHT -> {
				updateBeforeNextMoveFrame--
				if (updateBeforeNextMoveFrame == 0) {
					updateBeforeNextMoveFrame = MAX_UPDATES_BEFORE_NEXT_MOVE_FRAME
					changeIndexMovingFrame()
				}
				drawFrame(shader, camera, x, y, z, listSprite[6 + indexMovingFrame])
			}
		}
	}

	// Change frame of the animation

	private fun changeIndexStandingFrame() {
		if (indexStandingFrame == 0) {
			indexStandingFrame = 1
		} else {
			indexStandingFrame = 0
		}
	}

	private fun changeIndexMovingFrame() {
		if (indexMovingFrame == 0) {
			indexMovingFrame = 1
		} else {
			indexMovingFrame = 0
		}
	}

	private fun drawFrame(shader: Shader, camera: Camera, x: Float, y: Float, z: Float, sprite: Sprite) {
		sprite.draw(shader, camera, x, y, z)
	}
}