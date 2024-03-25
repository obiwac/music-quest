package com.p4.musicquest

import kotlin.math.min
import kotlin.math.max
class Collider(var x1: Float = 0f, var y1: Float = 0f, var z1: Float = 0f, var x2: Float = 0f, var y2: Float = 0f, var z2: Float = 0f) {
	class Collision(val entryTime: Float, val normal: Array<Float>? = null) {
	}

	companion object {
		val NO_COLLISION = Collision(1f)
	}

	private fun time(x: Float, y: Float): Float {
		if (y != 0f) {
			return x / y;
		}

		val inf = 9999999f;

		if (x > 0f) {
			return -inf;
		}

		return inf;
	}

	fun collide(other: Collider, vx: Float, vy: Float, vz: Float): Collision {
		val xEntry = time(if (vx > 0) other.x1 - x2 else other.x2 - x1, vx)
		val xExit  = time(if (vx > 0) other.x2 - x1 else other.x1 - x2, vx)
		val yEntry = time(if (vy > 0) other.y1 - y2 else other.y2 - y1, vy)
		val yExit  = time(if (vy > 0) other.y2 - y1 else other.y1 - y2, vy)
		val zEntry = time(if (vz > 0) other.z1 - z2 else other.z2 - z1, vz)
		val zExit  = time(if (vz > 0) other.z2 - z1 else other.z1 - z2, vz)

		if (xEntry < 0 && yEntry < 0 && zEntry < 0) {
			return NO_COLLISION;
		}

		if (xEntry > 1 || yEntry > 1 || zEntry > 1) {
			return NO_COLLISION;
		}

		val entry = maxOf(xEntry, yEntry, zEntry)
		val exit = minOf(xExit, yExit, zExit)

		if (entry > exit) {
			return NO_COLLISION;
		}

		val normal = arrayOf(0f, 0f, 0f);

		if (entry == xEntry) {
			normal[0] = if (vx > 0) -1f else 1f
		}

		if (entry == yEntry) {
			normal[1] = if (vy > 0) -1f else 1f
		}

		if (entry == zEntry) {
			normal[2] = if (vz > 0) -1f else 1f
		}

		return Collision(entry, normal);
	}
	fun intersection (other : Collider): Boolean {
		var touched = false
		var x = min(x2, other.x2) - max(x1, other.x1)
		var y = min(y2, other.y2) - max(y1, other.y1)
		var z = min(z2, other.z2) - max(z1, other.z1)
        if (x>0 && y>0 && z>0) touched = true
		return touched
	}
}
