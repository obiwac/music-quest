#version 300 es

precision highp float;
precision highp sampler2D;

out vec4 fragment_colour; // output of our shader

uniform sampler2D sampler;
uniform sampler2D maskSampler;

uniform float villageGreyness; // red
uniform float forestGreyness; // green
uniform float iceGreyness; // yellow, blue
uniform float beachGreyness; // cyan, black, orange
uniform float magmaGreyness; // white, turquoise
uniform float candyGreyness; // magenta, grey

in vec3 local_position;  // interpolated vertex position
in vec2 interp_tex_coords;

void main(void) {
    vec4 colour = texture(sampler, interp_tex_coords.yx);

    if (colour.a < 0.1) {
        discard;
    }

    vec3 bw_colour = vec3(0.2126 * colour.r + 0.7152 * colour.g + 0.0722 * colour.b);

    float mask_scale = 0.15;
    float mask_radius = 235.2;

    float mask_left = -mask_radius * mask_scale;
    float mask_right = mask_radius * mask_scale;
    float mask_bottom = -mask_radius * mask_scale;
    float mask_top = mask_radius * mask_scale;

    float mask_x = -(local_position.x - mask_left) / (mask_right - mask_left);
    float mask_z = (local_position.y - mask_bottom) / (mask_top - mask_bottom);

    vec4 mask_colour = texture(maskSampler, vec2(mask_x, mask_z));

    bool r_lo = mask_colour.r < 0.1;
    bool r_mi = !r_lo && mask_colour.r < 0.9;
    bool r_hi = mask_colour.r > 0.9;

    bool g_lo = mask_colour.g < 0.1;
    bool g_mi = !g_lo && mask_colour.g < 0.9;
    bool g_hi = mask_colour.g > 0.9;

    bool b_lo = mask_colour.b < 0.1;
    bool b_mi = !b_lo && mask_colour.b < 0.9;
    bool b_hi = mask_colour.b > 0.9;

    float greyness = 0.0;

    if (r_hi && g_lo && b_lo) { // red
        greyness = villageGreyness;
    }

    if (r_lo && g_hi && b_lo) { // green
        greyness = forestGreyness;
    }

    if (
        (r_hi && g_hi && b_lo) || // yellow
        (r_lo && g_lo && b_hi) // blue
    ) {
        greyness = iceGreyness;
    }

    if (
        (r_lo && g_hi && b_hi) || // cyan
        (r_lo && g_lo && b_lo) || // black
        (r_hi && g_mi && b_lo) // orange
    ) {
        greyness = beachGreyness;
    }

    if (
        (r_hi && g_hi && b_hi) || // white
        (r_lo && g_hi && b_mi) // turquoise
    ) {
        greyness = magmaGreyness;
    }

    if (
        (r_hi && g_lo && b_hi) || // magenta
        (r_mi && g_mi && b_mi) // grey
    ) {
        greyness = candyGreyness;
    }

    // fragment_colour = vec4(mix(mask_colour.rgb, vec3(r_mi, g_mi, b_mi), 0.5), colour.a);
	fragment_colour = vec4(mix(colour.rgb, bw_colour, greyness), colour.a);
}