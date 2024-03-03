package com.p4.musicquest

import java.nio.FloatBuffer
import android.opengl.GLES30 as gl
import java.nio.IntBuffer

class World {
    private val vertexPositions = floatArrayOf(
        -0.5f,  0.5f, 1.0f,
        -0.5f, -0.5f, 1.0f,
        0.5f, -0.5f, 1.0f,
        0.5f,  0.5f, 1.0f
    )

    private val indices = intArrayOf(
        0, 1, 2,
        0, 2, 3,
    )

    private val vao: Int

    init {
        val vertexPositionsBuf = FloatBuffer.allocate(vertexPositions.size)
        vertexPositionsBuf.put(vertexPositions)
        vertexPositionsBuf.position(0)

        val indicesBuf = IntBuffer.allocate(indices.size)
        indicesBuf.put(indices)
        indicesBuf.position(0)

        // create VAO

        val vaoBuf = IntBuffer.allocate(4)
        gl.glGenVertexArrays(1, vaoBuf)
        vao = vaoBuf[0]
        gl.glBindVertexArray(vao)

        // create VBO

        val vboBuf = IntBuffer.allocate(1)
        gl.glGenBuffers(1, vboBuf)
        val vbo = vboBuf[0]
        gl.glBindBuffer(gl.GL_ARRAY_BUFFER, vbo)

        gl.glBufferData(gl.GL_ARRAY_BUFFER, vertexPositions.size * 4, vertexPositionsBuf, gl.GL_STATIC_DRAW)

        gl.glVertexAttribPointer(0, 3, gl.GL_FLOAT, false, 0, 0)
        gl.glEnableVertexAttribArray(0)

        // create IBO

        val iboBuf = IntBuffer.allocate(1)
        gl.glGenBuffers(1, iboBuf)
        val ibo = iboBuf[0]
        gl.glBindBuffer(gl.GL_ELEMENT_ARRAY_BUFFER, ibo)

        gl.glBufferData(gl.GL_ELEMENT_ARRAY_BUFFER, indices.size * 4, indicesBuf, gl.GL_STATIC_DRAW)
    }

    fun draw() {
        gl.glBindVertexArray(vao)
        gl.glDrawElements(gl.GL_TRIANGLES, indices.size, gl.GL_UNSIGNED_INT, 0)
    }
}