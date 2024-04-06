#version 300 es

precision highp float;
precision highp sampler2D;

out vec4 fragment_colour; // output of our shader

uniform sampler2D sampler;

uniform float rightMultiplier;
uniform float leftMultiplier;
uniform float topMultiplier;
uniform float bottomMultiplier;

in vec3 local_position;  // interpolated vertex position
in vec2 interp_tex_coords;

void main(void) {
    vec4 colour = texture(sampler, vec2(interp_tex_coords.x, 1.0 - interp_tex_coords.y));
    vec3 bw_colour = vec3(0.2126 * colour.r + 0.7152 * colour.g + 0.0722 * colour.b);

    float greyness = clamp(distance(local_position, vec3(0.0)) / 1.3 - 0.3, 0.0, 1.0);

    if (local_position.x - abs(local_position.y) > 0.0) {
        greyness *= rightMultiplier;
    }

    else if (-local_position.x - abs(local_position.y) > 0.0) {
        greyness *= leftMultiplier;
    }

    else if (local_position.y - abs(local_position.x) > 0.0) {
        greyness *= topMultiplier;
    }

    else if (-local_position.y - abs(local_position.x) > 0.0) {
        greyness *= bottomMultiplier;
    }

    if (colour.a < 0.1) {
        discard;
    }

	fragment_colour = vec4(mix(colour.rgb, bw_colour, greyness), colour.a);
}