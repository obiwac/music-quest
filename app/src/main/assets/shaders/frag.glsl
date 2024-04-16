#version 300 es

precision highp float;
precision highp sampler2D;

out vec4 fragment_colour; // output of our shader

uniform sampler2D sampler;
uniform sampler2D maskSampler;

uniform float rightMultiplier;
uniform float leftMultiplier;
uniform float topMultiplier;
uniform float bottomMultiplier;

in vec3 local_position;  // interpolated vertex position
in vec2 interp_tex_coords;

void main(void) {
    vec4 colour = texture(sampler, interp_tex_coords.yx);
    vec3 bw_colour = vec3(0.2126 * colour.r + 0.7152 * colour.g + 0.0722 * colour.b);

    float mask_scale = 0.15;

    float mask_left = -244.749 * mask_scale;
    float mask_right = 225.651 * mask_scale;
    float mask_bottom = -228.312 * mask_scale;
    float mask_top = 242.088 * mask_scale;

    float mask_x = -(local_position.x - mask_left) / (mask_right - mask_left);
    float mask_z = (local_position.y - mask_bottom) / (mask_top - mask_bottom);

    vec4 mask_colour = texture(maskSampler, vec2(mask_x, mask_z));

    float greyness = mask_colour.b; // clamp(distance(local_position, vec3(0.0)) / 1.3 - 0.3, 0.0, 1.0);

    /*
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
    */

    if (colour.a < 0.1) {
        discard;
    }

	fragment_colour = vec4(mix(colour.rgb, bw_colour, greyness), colour.a);
}