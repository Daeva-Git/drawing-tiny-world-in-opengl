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

void generateGrassObj(vec4 position){
    gs_out.norm = getNormal(gl_in[0].gl_Position, gl_in[1].gl_Position, gl_in[2].gl_Position);

    mat4 MVP = projection * view * model;


    gl_Position = MVP * (position + vec4(size, size, 0.0, 0.0));
    gl_Position = vec4(gl_Position.xyz / gl_Position.w, 1.0);
    gs_out.texCoord = vec2(0, 0);
    EmitVertex();

    gl_Position = MVP * (position + vec4(size, 0.0, 0.0, 0.0));
    gl_Position = vec4(gl_Position.xyz / gl_Position.w, 1.0);
    gs_out.texCoord = vec2(0, 1);

    EmitVertex();

    gl_Position = MVP * (position + vec4(0.0, size, 0.0, 0.0));
    gl_Position = vec4(gl_Position.xyz / gl_Position.w, 1.0);
    gs_out.texCoord = vec2(1, 0);
    EmitVertex();


    gl_Position = MVP * (position);
    gl_Position = vec4(gl_Position.xyz / gl_Position.w, 1.0);
    gs_out.texCoord = vec2(1, 1);
    EmitVertex();
}

void main() {
    vec2 centerTexCoord = (gs_in[0].texCoord + gs_in[1].texCoord + gs_in[2].texCoord) / 3;

    if (texture(distribution, centerTexCoord).x > 0.5) {
        vec4 centerPos = (gl_in[0].gl_Position + gl_in[1].gl_Position + gl_in[2].gl_Position) / 3;
        generateGrassObj(centerPos);
    }

    EndPrimitive();
}