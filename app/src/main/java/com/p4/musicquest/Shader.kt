package com.p4.musicquest

import android.content.Context
import java.lang.Exception
import java.nio.IntBuffer
import android.opengl.GLES30 as gl

class Shader(private val context: Context, vertPath: String, fragPath: String) {
    private val program: Int = gl.glCreateProgram()
    private var mvpLoc: Int
    private var samplerLoc: Int

    private var rightMulLoc: Int
    private var leftMulLoc: Int
    private var topMulLoc: Int
    private var bottomMulLoc: Int

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

        // get locations

        mvpLoc = gl.glGetUniformLocation(program, "mvp")
        samplerLoc = gl.glGetUniformLocation(program, "sampler")

        rightMulLoc = gl.glGetUniformLocation(program, "rightMultiplier")
        leftMulLoc = gl.glGetUniformLocation(program, "leftMultiplier")
        topMulLoc = gl.glGetUniformLocation(program, "topMultiplier")
        bottomMulLoc = gl.glGetUniformLocation(program, "bottomMultiplier")
    }

    fun use() {
        gl.glUseProgram(program)
    }

    fun setMvp(mvp: Matrix) {
        gl.glUniformMatrix4fv(mvpLoc, 1, false, mvp.getBuf())
    }

    fun setSampler(sampler: Int) {
        gl.glUniform1i(samplerLoc, sampler)
    }

    fun setMultipliers(right: Float, left: Float, top: Float, bottom: Float) {
        gl.glUniform1f(rightMulLoc, right)
        gl.glUniform1f(leftMulLoc, left)
        gl.glUniform1f(topMulLoc, top)
        gl.glUniform1f(bottomMulLoc, bottom)
    }
}