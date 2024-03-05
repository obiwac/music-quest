#version 300 es

precision mediump float;
precision mediump sampler2D;

out vec4 fragment_colour; // output of our shader

uniform sampler2D sampler;

in vec3 local_position;  // interpolated vertex position
in vec2 interp_tex_coords;

void main(void) {
	fragment_colour = texture(sampler, vec2(interp_tex_coords.x, 1.0 - interp_tex_coords.y)); // set the output colour based on the vertex position
}