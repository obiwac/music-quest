#version 300 es

precision mediump float;

layout(location = 0) in vec3 vertex_position; // vertex position attribute

out vec3 local_position; // interpolated vertex position

uniform mat4 mvp;

void main(void) {
	local_position = vertex_position;
	gl_Position = mvp * vec4(vertex_position, 1.0); // set vertex position
}