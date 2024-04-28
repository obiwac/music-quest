#version 300 es

precision highp float;
precision highp sampler2D;

out vec4 fragment_colour; // output of our shader

uniform sampler2D sampler;
uniform sampler2D maskSampler;
uniform sampler2D waterSampler;
uniform sampler2D lavaSampler;
uniform sampler2D chocoSampler;

uniform float time;
uniform float villageGreyness; // red
uniform float forestGreyness; // green
uniform float iceGreyness; // yellow, blue
uniform float beachGreyness; // cyan, black, orange
uniform float magmaGreyness; // white, turquoise
uniform float candyGreyness; // magenta, grey

in vec3 local_position;  // interpolated vertex position
in vec2 interp_tex_coords;

void main(void) {
    float mask_scale = 0.15;
    float mask_radius = 235.2;

    float mask_left = -mask_radius * mask_scale;
    float mask_right = mask_radius * mask_scale;
    float mask_bottom = -mask_radius * mask_scale;
    float mask_top = mask_radius * mask_scale;

    float mask_x = -(local_position.x - mask_left) / (mask_right - mask_left);
    float mask_z = (local_position.y - mask_bottom) / (mask_top - mask_bottom);

    vec2 mask_coord = vec2(mask_x, mask_z);
    vec4 mask_colour = texture(maskSampler, mask_coord);

    bool r_lo = mask_colour.r < 0.1;
    bool r_mi = !r_lo && mask_colour.r < 0.9;
    bool r_hi = mask_colour.r > 0.9;

    bool g_lo = mask_colour.g < 0.1;
    bool g_mi = !g_lo && mask_colour.g < 0.9;
    bool g_hi = mask_colour.g > 0.9;

    bool b_lo = mask_colour.b < 0.1;
    bool b_mi = !b_lo && mask_colour.b < 0.9;
    bool b_hi = mask_colour.b > 0.9;

    bool village = r_hi && g_lo && b_lo; // red
    bool forest = r_lo && g_hi && b_lo; // green
    bool ice = r_hi && g_hi && b_lo; // yellow
    bool ice_path = r_lo && g_lo && b_hi; // blue
    bool sand = r_lo && g_hi && b_hi; // cyan
    bool water = r_lo && g_lo && b_lo; // black
    bool oil = r_hi && g_mi && b_lo; // orange
    bool lava = r_hi && g_hi && b_hi; // white
    bool magma = r_lo && g_hi && b_mi; // turquoise
    bool candy = r_hi && g_lo && b_hi; // magenta
    bool choco = r_mi && g_mi && b_mi; // grey

    float greyness = 0.0;

    if (village) {
        greyness = villageGreyness;
    }

    if (forest) {
        greyness = forestGreyness;
    }

    if (ice || ice_path) {
        greyness = iceGreyness;
    }

    if (sand || water || oil) {
        greyness = beachGreyness;
    }

    if (lava || magma) {
        greyness = magmaGreyness;
    }

    if (candy || choco) {
        greyness = candyGreyness;
    }

    vec4 colour = texture(sampler, interp_tex_coords.yx);

    if (colour.a < 0.1) {
        discard;
    }

    if (local_position.z < 0.01) { // Liquids are all under y = 0.01 (swizzled with z because I don't know and I don't care).
        float MASK_TO_TEX_RATIO = 81.92;

        if (water) {
            vec4 layer_1 = texture(waterSampler, mask_coord * MASK_TO_TEX_RATIO + time * vec2(.3, .2));
            vec4 layer_2 = texture(waterSampler, mask_coord * MASK_TO_TEX_RATIO + time * vec2(-.1, .15));
            colour *= (layer_1 * layer_2) * 1.5;
        }

        else if (lava) {
            vec4 layer_1 = texture(lavaSampler, mask_coord * MASK_TO_TEX_RATIO + time * vec2(.15, .1));
            vec4 layer_2 = texture(lavaSampler, mask_coord * MASK_TO_TEX_RATIO + time * vec2(-.05, .07));
            colour = (layer_1 + layer_2) * 1.5;
        }

        else if (choco) {
            // TODO Shadows are multiplied by .3 for now because I can't seem to get a proper cleanplate unfortunately... What I should do is just make this a solid colour in Blender to simplify this.

            vec3 clean_plate = texture(chocoSampler, mask_coord * MASK_TO_TEX_RATIO).rgb;
            vec3 shadow = colour.rgb - clean_plate;
            vec3 layer = texture(chocoSampler, vec2(mask_z, mask_x) * MASK_TO_TEX_RATIO + time * vec2(0.0, -.07)).rgb;
            colour = vec4(layer + shadow * .3, 1.0);
        }
    }

    vec3 bw_colour = vec3(0.2126 * colour.r + 0.7152 * colour.g + 0.0722 * colour.b) * .4 - 0.2;

    fragment_colour = vec4(mix(colour.rgb, bw_colour, greyness), colour.a);
}