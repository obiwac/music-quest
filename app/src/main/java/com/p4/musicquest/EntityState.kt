package com.p4.musicquest

import kotlin.math.abs

class EntityState (private val entity: Entity?) {
	// for the animation of sprite

	// Take animation of sprite in function of state
	enum class State {
		NOT_MOVING,
		NOT_MOVING_TWO,
		STARTED_MOVING,
		IS_MOVING
	}

	var state: State = State.NOT_MOVING

	fun update() {
		if (entity != null) {
			when (state) {
				State.NOT_MOVING ->
					if (abs(entity!!.velocity[0]) < 0.005f || abs(entity!!.velocity[2]) < 0.005f) {
						state = State.NOT_MOVING_TWO
					}
				State.NOT_MOVING_TWO ->
					if (abs(entity!!.velocity[0]) >= 0.005f || abs(entity!!.velocity[2]) >= 0.005f) {
						state = State.STARTED_MOVING
					}
				State.STARTED_MOVING ->
					if (abs(entity!!.velocity[0]) >= 0.005f || abs(entity!!.velocity[2]) >= 0.005f) {
						state = State.IS_MOVING
					}
				State.IS_MOVING ->
					if (abs(entity!!.velocity[0]) < 0.005f && abs(entity!!.velocity[2]) < 0.005f) {
						state = State.NOT_MOVING
					}
			}
		}

	}
}