#version 300 es

precision highp float;
precision highp sampler2D;

out vec4 fragment_colour;

uniform sampler2D sampler;
uniform sampler2D maskSampler;

in vec3 local_position;
in vec2 interp_tex_coords;

void main(void) {
    vec4 colour = texture(sampler, interp_tex_coords.yx);

    if (colour.a < 0.1) {
        discard;
    }

    fragment_colour = colour;
}