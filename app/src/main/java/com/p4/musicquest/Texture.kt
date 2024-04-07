package com.p4.musicquest

import android.content.Context
import android.graphics.BitmapFactory
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.IntBuffer
import java.nio.channels.Channels
import kotlin.math.ceil
import android.opengl.GLES30 as gl

class Texture(private val context: Context, path: String) {
	val texBuf = IntBuffer.allocate(1)
	val tex: Int

	companion object {
		const val KTX_SIGNATURE_SIZE = 12
		const val KTX_HEADER_SIZE = 64
	}

	init {
		gl.glGenTextures(1, texBuf)
		tex = texBuf[0]
		gl.glBindTexture(gl.GL_TEXTURE_2D, tex)

		val width: Int
		val height: Int

		if (path.endsWith("ktx")) {
			val stream = context.assets.open(path)
			val channel = Channels.newChannel(stream)

			val header = ByteBuffer.allocate(KTX_HEADER_SIZE).order(ByteOrder.LITTLE_ENDIAN)
			channel.read(header)
			header.flip()

			val identifier = ByteArray(KTX_SIGNATURE_SIZE)
			header.get(identifier)

			val expectedIdentifier = byteArrayOf(
				0xAB.toByte(), 0x4B.toByte(), 0x54.toByte(), 0x58.toByte(),
				0x20.toByte(), 0x31.toByte(), 0x31.toByte(), 0xBB.toByte(),
				0x0D.toByte(), 0x0A.toByte(), 0x1A.toByte(), 0x0A.toByte(),
			)

			if (!identifier.contentEquals(expectedIdentifier)) {
				throw Exception("Invalid KTX identifier. This may not be a valid KTX file.")
			}

			val endianness = header.int
			val type = header.int
			val glTypeSize = header.int
			val format = header.int
			val internalFormat = header.int
			val glBaseInternalFormat = header.int
			width = header.int
			height = header.int
			val pixelDepth = header.int
			val numberOfArrayElements = header.int
			val numberOfFaces = header.int
			val numberOfMipmapLevels = header.int
			val bytesOfKeyValueData = header.int
			header.position(header.position() + bytesOfKeyValueData)
			stream.skip(4) // XXX I don't know why this is necessary...

			val dataSize = stream.available()
			val buf = ByteBuffer.allocate(dataSize).order(ByteOrder.LITTLE_ENDIAN)
			channel.read(buf)
			stream.close()
			buf.flip()

			gl.glCompressedTexImage2D(
				gl.GL_TEXTURE_2D,
				0,
				internalFormat,
				width,
				height,
				0,
				dataSize,
				buf
			)
		}

		else {
			val bitmap = context.assets.open(path).use { BitmapFactory.decodeStream(it) }
			width = bitmap.width
			height = bitmap.height
			val buf = ByteBuffer.allocate(bitmap.byteCount)
			bitmap.copyPixelsToBuffer(buf)
			buf.rewind()

			gl.glTexImage2D(
				gl.GL_TEXTURE_2D,
				0,
				gl.GL_RGBA, // TODO based on bitmap?
				width,
				height,
				0,
				gl.GL_RGBA,
				gl.GL_UNSIGNED_BYTE,
				buf
			)
		}

		gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MIN_FILTER, gl.GL_NEAREST)
		gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MAG_FILTER, gl.GL_NEAREST)

		gl.glGenerateMipmap(gl.GL_TEXTURE_2D)
	}
}
