#version 330 core
layout (location = 0) in vec3 vertex;
layout (location = 1) in vec2 aTexCoord;

uniform sampler2D heightmap;

out VS_OUT {
    vec2 texCoord;
    vec3 fragPos;
} vs_out;

void main() {
    vs_out.texCoord = aTexCoord;

    float size = 0.2;
    float y = (texture(heightmap, vs_out.texCoord).x - 0.5) * size;

    gl_Position = vec4(vertex.x, vertex.y + y, vertex.z, 1.0);
}
