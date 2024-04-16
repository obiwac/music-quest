package com.p4.musicquest

import android.content.Context
import java.lang.Exception
import java.nio.IntBuffer
import android.opengl.GLES30 as gl

class Shader(private val context: Context, vertPath: String, fragPath: String) {
    private val program: Int = gl.glCreateProgram()
    private var mvpLoc: Int
    private var samplerLoc: Int
    private var maskSamplerLoc: Int

    class Greyness(var loc: Int = 0, var greyness: Float = 1f, var targetGreyness: Float = 1f) {
    }

    private val greynesses: MutableMap<String, Greyness>

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
        maskSamplerLoc = gl.glGetUniformLocation(program, "maskSampler")

        // arrayOf(uniformLocation, greyness, targetGreyness)

        greynesses = mutableMapOf(
            Pair("village", Greyness()),
            Pair("forest", Greyness()),
            Pair("ice", Greyness()),
        )

        for ((k, _) in greynesses) {
            greynesses[k]?.loc = gl.glGetUniformLocation(program, "${k}Greyness")
        }
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

    fun setMaskSampler(sampler: Int, dt: Float) {
        gl.glUniform1i(maskSamplerLoc, sampler)

        for ((_, v) in greynesses) {
            v.greyness += (v.targetGreyness - v.greyness) * dt * 5
            gl.glUniform1f(v.loc, v.greyness)
        }
    }

    fun setTargetGreyness(key: String, greyness: Float) {
        greynesses[key]?.targetGreyness = greyness
    }
}