package com.p4.musicquest

import android.content.Context
import java.lang.Exception
import android.opengl.GLES30 as gl

class Shader(private val context: Context, vertPath: String, fragPath: String) {
    private val program: Int = gl.glCreateProgram()

    private fun createShader(target: Int, path: String) {
        val src = context.assets.open(path).reader().use { it.readText() }

        gl.glShaderSource(target, src)
        gl.glCompileShader(target)

        val log = gl.glGetShaderInfoLog(target)

        if (log.length > 1) {
            throw Exception(log)
        }
    }

    init {
        // create vertex shader

        val vertShader = gl.glCreateShader(gl.GL_VERTEX_SHADER)
        createShader(vertShader, vertPath)
        gl.glAttachShader(program, vertShader)

        // create fragment shader

        val fragShader = gl.glCreateShader(gl.GL_FRAGMENT_SHADER)
        createShader(fragShader, fragPath)
        gl.glAttachShader(program, fragShader)

        // link program and clean up

        gl.glLinkProgram(program)

        gl.glDeleteShader(vertShader)
        gl.glDeleteShader(fragShader)
    }

    fun use() {
        gl.glUseProgram(program)
    }
}