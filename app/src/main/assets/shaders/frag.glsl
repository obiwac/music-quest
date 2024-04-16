#version 300 es

precision highp float;
precision highp sampler2D;

out vec4 fragment_colour; // output of our shader

uniform sampler2D sampler;
uniform sampler2D maskSampler;

uniform float villageGreyness; // red
uniform float forestGreyness; // green
uniform float iceGreyness; // yellow, blue

in vec3 local_position;  // interpolated vertex position
in vec2 interp_tex_coords;

void main(void) {
    vec4 colour = texture(sampler, interp_tex_coords.yx);

    if (colour.a < 0.1) {
        discard;
    }

    vec3 bw_colour = vec3(0.2126 * colour.r + 0.7152 * colour.g + 0.0722 * colour.b);

    float mask_scale = 0.15;

    float mask_left = -244.749 * mask_scale;
    float mask_right = 225.651 * mask_scale;
    float mask_bottom = -228.312 * mask_scale;
    float mask_top = 242.088 * mask_scale;

    float mask_x = -(local_position.x - mask_left) / (mask_right - mask_left);
    float mask_z = (local_position.y - mask_bottom) / (mask_top - mask_bottom);

    vec4 mask_colour = texture(maskSampler, vec2(mask_x, mask_z));

    bool has_r = mask_colour.r > 0.5;
    bool has_g = mask_colour.g > 0.5;
    bool has_b = mask_colour.b > 0.5;

    float greyness = 0.0;

    if (has_r && !has_g && !has_g) { // red
        greyness = villageGreyness;
    }

    if (!has_r && has_g && has_b) { // green
        greyness = forestGreyness;
    }

    if (
        (has_r && has_g && !has_b) ||
        (!has_r && !has_g && has_b)
    ) {
        greyness = iceGreyness;
    }

	fragment_colour = vec4(mix(colour.rgb, bw_colour, greyness), colour.a);
}