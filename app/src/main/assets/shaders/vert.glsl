#version 300 es

precision mediump float;

layout(location = 0) in vec3 vertex_position; // vertex position attribute
layout(location = 1) in vec2 tex_coords;

out vec3 local_position; // interpolated vertex position
out vec2 interp_tex_coords;

uniform mat4 mvp;

void main(void) {
	local_position = vertex_position;
	interp_tex_coords = tex_coords;
	gl_Position = mvp * vec4(vertex_position, 1.0); // set vertex position
}