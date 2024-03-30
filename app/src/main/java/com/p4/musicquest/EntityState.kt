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
		IS_MOVING_RIGHT
	}

	var state: State = State.NOT_MOVING

	fun update() {
		if (entity != null) {

			if (abs(entity.velocity[0]) < 0.005f && abs(entity.velocity[2]) < 0.005f) {
				state = State.NOT_MOVING

			} else if (entity.direction[2] > 0 && abs(entity.direction[2]) > abs(entity.direction[0])) {
				state = State.IS_MOVING_UP

			} else if ((entity.direction[2] < 0 && abs(entity.direction[2]) > abs(entity.direction[0]))) {
				state = State.IS_MOVING_DOWN

			} else if (entity.direction[0] < 0 && abs(entity.direction[0]) > abs(entity.direction[2])) {
				state = State.IS_MOVING_LEFT

			} else {
				state = State.IS_MOVING_RIGHT
			}
		}
	}
}