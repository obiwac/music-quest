#version 300 es

precision highp float;

layout(location = 0) in vec3 vertex_position;
layout(location = 1) in vec2 tex_coords;

out vec3 local_position;
out vec2 interp_tex_coords;

uniform mat4 mvp;

void main(void) {
    local_position = vertex_position;
    interp_tex_coords = tex_coords;

    // TODO ideally, MVP would be split in the shader into M * VP, so that we can use the model matrix to get a global position we can use to colour in sprites
    //      (though I don't know if the model matrix for sprites is set correctly in the first place)

    gl_Position = mvp * vec4(local_position, 1.0);
}