package com.p4.musicquest

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES30 as gl
import java.nio.ByteBuffer
import java.nio.FloatBuffer
import java.nio.IntBuffer

abstract class TileMap(context: Context, texPath: String) {
	abstract val map: Array<Array<Int>>
	abstract val overlay: Array<Array<Int>> // the idea is that stuff like paths will go in the overlay map

	companion object {
		private const val TILE_SIZE = 1f
		private const val TILEMAP_STEP_WIDTH = 1f / 16
		private const val TILEMAP_STEP_HEIGHT = 1f / 16
	}

	private val mapTiles: Array<Tile> = arrayOf(
		Tile("GRASS", 0f, 0f),
	)

	private val overlayTiles: Array<Tile> = arrayOf(
		Tile("NONE", 0f, 0f),
		Tile("PATH_LR", 0f, 0f),
		Tile("PATH_TB", 0f, 0f),
		Tile("PATH_TR", 0f, 0f),
		Tile("PATH_TL", 0f, 0f),
		Tile("PATH_BR", 0f, 0f),
		Tile("PATH_BL", 0f, 0f),
	)

	private var tex: Int
	private val vao: Int
	private var indices: Array<Int>? = null

	init {
		// create VAO

		val vaoBuf = IntBuffer.allocate(4)
		gl.glGenVertexArrays(1, vaoBuf)
		vao = vaoBuf[0]
		gl.glBindVertexArray(vao)

		// load texture

		val texBuf = IntBuffer.allocate(1)
		gl.glGenTextures(1, texBuf)
		tex = texBuf[0]
		gl.glBindTexture(gl.GL_TEXTURE_2D, tex)

		val bitmap = context.assets.open(texPath).use { BitmapFactory.decodeStream(it) }
		val buf = ByteBuffer.allocate(bitmap.byteCount)
		bitmap.copyPixelsToBuffer(buf)
		buf.rewind()

		gl.glTexImage2D(gl.GL_TEXTURE_2D, 0, gl.GL_RGBA, bitmap.width, bitmap.height, 0, gl.GL_RGBA, gl.GL_UNSIGNED_BYTE, buf)

		gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MIN_FILTER, gl.GL_NEAREST_MIPMAP_LINEAR)
		gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MAG_FILTER, gl.GL_NEAREST)

		gl.glGenerateMipmap(gl.GL_TEXTURE_2D)
	}

	fun genMesh() {
		var vertices = arrayOf<Float>()
		var indices = arrayOf<Int>()

		for (y in map.indices) {
			for (x in map[y].indices) {
				val tileId = map[y][x]
				val tile = mapTiles[tileId]
				val prevVertices = vertices.size

				// indices

				indices += prevVertices + 0
				indices += prevVertices + 1
				indices += prevVertices + 2

				indices += prevVertices + 0
				indices += prevVertices + 2
				indices += prevVertices + 3

				// bottom left

				vertices += TILE_SIZE * x
				vertices += TILE_SIZE * y
				vertices += 0f

				vertices += TILEMAP_STEP_WIDTH * tile.u
				vertices += TILEMAP_STEP_HEIGHT * tile.v

				// bottom right

				vertices += TILE_SIZE * x + TILE_SIZE
				vertices += TILE_SIZE * y
				vertices += 0f

				vertices += TILEMAP_STEP_WIDTH * tile.u + TILEMAP_STEP_WIDTH
				vertices += TILEMAP_STEP_HEIGHT * tile.v

				// top right

				vertices += TILE_SIZE * x + TILE_SIZE
				vertices += TILE_SIZE * y + TILE_SIZE
				vertices += 0f

				vertices += TILEMAP_STEP_WIDTH * tile.u + TILEMAP_STEP_WIDTH
				vertices += TILEMAP_STEP_HEIGHT * tile.v + TILEMAP_STEP_HEIGHT

				// top left

				vertices += TILE_SIZE * x
				vertices += TILE_SIZE * y + TILE_SIZE
				vertices += 0f

				vertices += TILEMAP_STEP_WIDTH * tile.u
				vertices += TILEMAP_STEP_HEIGHT * tile.v + TILEMAP_STEP_HEIGHT
			}
		}

		// create buffers

		val verticesBuf = FloatBuffer.wrap(vertices.toFloatArray())
		verticesBuf.rewind()

		val indicesBuf = IntBuffer.wrap(indices.toIntArray())
		indicesBuf.rewind()

		// create VBO

		gl.glBindVertexArray(vao)

		val vboBuf = IntBuffer.allocate(1)
		gl.glGenBuffers(1, vboBuf)
		val vbo = vboBuf[0]
		gl.glBindBuffer(gl.GL_ARRAY_BUFFER, vbo)

		gl.glBufferData(gl.GL_ARRAY_BUFFER, vertices.size * 4, verticesBuf, gl.GL_STATIC_DRAW)

		gl.glVertexAttribPointer(0, 3, gl.GL_FLOAT, false, 4 * 5, 4 * 0)
		gl.glEnableVertexAttribArray(0)

		gl.glVertexAttribPointer(1, 2, gl.GL_FLOAT, false, 4 * 5, 4 * 3)
		gl.glEnableVertexAttribArray(1)

		// create IBO

		val iboBuf = IntBuffer.allocate(1)
		gl.glGenBuffers(1, iboBuf)
		val ibo = iboBuf[0]
		gl.glBindBuffer(gl.GL_ELEMENT_ARRAY_BUFFER, ibo)

		gl.glBufferData(gl.GL_ELEMENT_ARRAY_BUFFER, indices.size * 4, indicesBuf, gl.GL_STATIC_DRAW)
	}

	fun draw(shader: Shader) {
		if (indices == null) {
			return
		}

		gl.glActiveTexture(gl.GL_TEXTURE0)
		shader.setSampler(0)
		gl.glBindTexture(gl.GL_TEXTURE_2D, tex)

		gl.glBindVertexArray(vao)
		gl.glDrawElements(gl.GL_TRIANGLES, indices!!.size, gl.GL_UNSIGNED_INT, 0)
	}
}