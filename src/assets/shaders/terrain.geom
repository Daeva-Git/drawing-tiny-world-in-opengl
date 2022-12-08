#version 330 core
layout (triangles) in;
layout (triangle_strip, max_vertices = 3) out;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;
uniform vec3 lightDir;

in vec2[] TexCoord;
out vec2 TexCoords;
out vec3 Norm;
out vec3 LightDir;

vec3 getNormal(vec4 pos0, vec4 pos1, vec4 pos2){
    vec3 a = vec3(pos0) - vec3(pos1);
    vec3 b = vec3(pos2) - vec3(pos1);
    return normalize(cross(a, b));
}

void main() {
    vec4 pos0 = model * gl_in[0].gl_Position;
    vec4 pos1 = model * gl_in[1].gl_Position;
    vec4 pos2 = model * gl_in[2].gl_Position;

    Norm = getNormal(pos0, pos1, pos2);
    LightDir = normalize(mat3(view) * lightDir);

    mat4 VP = projection * view;
    pos0 = VP * pos0;
    pos1 = VP * pos1;
    pos2 = VP * pos2;

    gl_Position = pos0;
    TexCoords = TexCoord[0];
    EmitVertex();

    gl_Position = pos1;
    TexCoords = TexCoord[1];
    EmitVertex();

    gl_Position = pos2;
    TexCoords = TexCoord[2];
    EmitVertex();

    EndPrimitive();
}