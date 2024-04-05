package com.p4.musicquest

import kotlin.math.abs

class EntityState (private val entity: Entity?) {
	// for the animation of sprite
	// Determine the state of the entity

	enum class State {
		NOT_MOVING,
		IS_MOVING_DOWN,
		IS_MOVING_UP,
		IS_MOVING_LEFT,
		IS_MOVING_RIGHT,
		IS_HIT_DOWN,
		IS_HIT_LEFT,
		IS_HIT_RIGHT,
		IS_HIT_UP,
		ATTACK_DOWN,
		ATTACK_UP,
		ATTACK_LEFT,
		ATTACK_RIGHT
	}

	var state: State = State.NOT_MOVING

	fun update() {
		if (entity != null) {

			if (abs(entity.velocity[0]) < 0.005f && abs(entity.velocity[2]) < 0.005f) {
				state = State.NOT_MOVING

			} else if (entity.direction[2] > 0 && abs(entity.direction[2]) > abs(entity.direction[0])) {
				if (entity.isHit) {
					state = State.IS_HIT_DOWN
				} else if (entity.isAttack) {
					state = State.ATTACK_UP
				} else {
					state = State.IS_MOVING_UP
				}

			} else if ((entity.direction[2] < 0 && abs(entity.direction[2]) > abs(entity.direction[0]))) {
				if (entity.isHit) {
					state = State.IS_HIT_UP
				} else if (entity.isAttack) {
					state = State.ATTACK_DOWN
				} else {
					state = State.IS_MOVING_DOWN
				}

			} else if (entity.direction[0] < 0 && abs(entity.direction[0]) > abs(entity.direction[2])) {
				if (entity.isHit) {
					state = State.IS_HIT_RIGHT
				} else if (entity.isAttack) {
						state = State.ATTACK_LEFT
				} else {
					state = State.IS_MOVING_LEFT
				}

			} else {
				if (entity.isHit && (entity.direction[0] > 0 && abs(entity.direction[0]) > abs(entity.direction[2]))) {
					state = State.IS_HIT_LEFT
				} else if (entity.isAttack) {
					state = State.ATTACK_RIGHT
				} else {
					state = State.IS_MOVING_RIGHT
				}
			}
		}
	}
}