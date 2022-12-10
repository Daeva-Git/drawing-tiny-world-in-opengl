#version 330 core
layout (triangles) in;
layout (triangle_strip, max_vertices = 4) out;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;
uniform sampler2D distribution;
uniform float size;

in VS_OUT {
    vec2 texCoord;
} gs_in[];

out GS_OUT {
    vec2 texCoord;
    vec3 norm;
} gs_out;

vec3 getNormal(vec4 pos0, vec4 pos1, vec4 pos2){
    vec3 a = vec3(pos0) - vec3(pos1);
    vec3 b = vec3(pos2) - vec3(pos1);
    return normalize(cross(a, b));
}

void generateGrassObj(vec4 position, float size){
    gs_out.norm = getNormal(gl_in[0].gl_Position, gl_in[1].gl_Position, gl_in[2].gl_Position);

    mat4 MVP = projection * view * model;

    gl_Position = MVP * (position + vec4(-size / 2, 0.0, 0.0, 0.0));
    gs_out.texCoord = vec2(1, 1);
    EmitVertex();

    gl_Position = MVP * (position + vec4(-size / 2, size, 0.0, 0.0));
    gs_out.texCoord = vec2(1, 0);
    EmitVertex();

    gl_Position = MVP * (position + vec4(size / 2, 0.0, 0.0, 0.0));
    gs_out.texCoord = vec2(0, 1);
    EmitVertex();

    gl_Position = MVP * (position + vec4(size / 2, size, 0.0, 0.0));
    gs_out.texCoord = vec2(0, 0);
    EmitVertex();
}

void main() {
    // heihgtmap
    vec2 centerTexCoord = (gs_in[0].texCoord + gs_in[1].texCoord + gs_in[2].texCoord) / 3;

    if (texture(distribution, centerTexCoord).x >= 0.1) {
        vec4 sum = gl_in[0].gl_Position + gl_in[1].gl_Position + gl_in[2].gl_Position;
        vec4 centerPos = sum / 3;
        generateGrassObj(centerPos, size);
    }

    EndPrimitive();
}