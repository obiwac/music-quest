package com.p4.musicquest

class Animator (private val listSprite: ArrayList<Sprite>){

	// Change sprite in function of state for the entity's animation

	companion object {
		const val MAX_UPDATES_BEFORE_NEXT_STAND_FRAME = 50
		const val MAX_UPDATES_BEFORE_NEXT_MOVE_FRAME = 10
		const val MAX_UPDATES_BEFORE_NEXT_HIT_FRAME = 10
		const val MAX_UPDATES_BEFORE_NEXT_ATTACK_FRAME = 15
	}

	private var updateBeforeNextMoveFrame = MAX_UPDATES_BEFORE_NEXT_MOVE_FRAME
	private var updateBeforeNextStandFrame = MAX_UPDATES_BEFORE_NEXT_STAND_FRAME
	private var updateBeforeNextHitFrame =  MAX_UPDATES_BEFORE_NEXT_HIT_FRAME
	private var updateBeforeNextAttackFrame = MAX_UPDATES_BEFORE_NEXT_ATTACK_FRAME
	private var indexStandingFrame = 0
	private var indexMovingFrame = 0
	private var indexAttackFrame = 0

	fun draw(shader: Shader, camera: Camera, x: Float, y: Float, z: Float, entity: Entity) {
		if (entity.entityLife) {
			when (entity.entityState.state) {
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

				EntityState.State.IS_HIT_DOWN -> {
					changeHitFrame(entity)
					drawFrame(shader, camera, x, y, z, listSprite[10])
				}

				EntityState.State.IS_HIT_UP -> {
					changeHitFrame(entity)
					drawFrame(shader, camera, x, y, z, listSprite[13])
				}

				EntityState.State.IS_HIT_LEFT -> {
					changeHitFrame(entity)
					drawFrame(shader, camera, x, y, z, listSprite[11])
				}

				EntityState.State.IS_HIT_RIGHT -> {
					changeHitFrame(entity)
					drawFrame(shader, camera, x, y, z, listSprite[12])
				}

				EntityState.State.ATTACK_DOWN -> {
					indexAttackFrame = 1
					changeAttackFrame(entity)
					drawFrame(shader, camera, x, y, z, listSprite[14 + indexAttackFrame])
				}

				EntityState.State.ATTACK_UP -> {
					indexAttackFrame = 1
					changeAttackFrame(entity)
					drawFrame(shader, camera, x, y, z, listSprite[20 + indexAttackFrame])
				}

				EntityState.State.ATTACK_LEFT -> {
					indexAttackFrame = 1
					changeAttackFrame(entity)
					drawFrame(shader, camera, x, y, z, listSprite[16 + indexAttackFrame])
				}

				EntityState.State.ATTACK_RIGHT -> {
					indexAttackFrame = 1
					changeAttackFrame(entity)
					drawFrame(shader, camera, x, y, z, listSprite[18 + indexAttackFrame])
				}
			}
		} else if (entity.typeEntity == Entity.TYPE_ENTITY.SLIME_BOSS) {

			when(entity.entityState.stateSlimeBoss) {
				EntityState.StateSlimeBoss.NOT_MOVING -> {
					updateBeforeNextStandFrame--
					if (updateBeforeNextStandFrame == 0) {
						updateBeforeNextStandFrame = MAX_UPDATES_BEFORE_NEXT_STAND_FRAME
						changeIndexStandingFrame()
					}
					drawFrame(shader, camera, x, y, z, listSprite[indexStandingFrame])
				}

				EntityState.StateSlimeBoss.CHARGE -> {
					updateBeforeNextHitFrame--
					if (updateBeforeNextHitFrame == 0) {
						updateBeforeNextHitFrame = MAX_UPDATES_BEFORE_NEXT_HIT_FRAME
						changeIndexStandingFrame()
					}
					drawFrame(shader, camera, x, y, z, listSprite[indexStandingFrame])
				}

				EntityState.StateSlimeBoss.DASH -> {
					updateBeforeNextStandFrame--
					if (updateBeforeNextStandFrame == 0) {
						updateBeforeNextStandFrame = MAX_UPDATES_BEFORE_NEXT_STAND_FRAME
						changeIndexStandingFrame()
					}
					drawFrame(shader, camera, x, y, z, listSprite[2])
				}
			}
		} else {
			when(entity.entityState.stateDeco) {
				EntityState.StateMonkey.ONE -> {
					updateBeforeNextStandFrame--
					if (updateBeforeNextStandFrame == 0) {
						updateBeforeNextStandFrame = MAX_UPDATES_BEFORE_NEXT_STAND_FRAME
						changeIndexStandingFrame()
					}
					drawFrame(shader, camera, x, y, z, listSprite[indexStandingFrame])
				}
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

	private fun changeHitFrame(entity: Entity) {
		updateBeforeNextHitFrame--
		if (updateBeforeNextHitFrame <= 0) {
			updateBeforeNextHitFrame = MAX_UPDATES_BEFORE_NEXT_HIT_FRAME
			entity.isHit = false
		}
	}

	private fun changeAttackFrame(entity: Entity) {
		updateBeforeNextAttackFrame--
		if (updateBeforeNextAttackFrame < 8) {
			indexAttackFrame = 0
		}
		if (updateBeforeNextAttackFrame <= 0) {
			updateBeforeNextAttackFrame = MAX_UPDATES_BEFORE_NEXT_ATTACK_FRAME
			entity.isAttack = false
		}
	}

	private fun drawFrame(shader: Shader, camera: Camera, x: Float, y: Float, z: Float, sprite: Sprite) {
		sprite.draw(shader, camera, x, y, z)
	}
}