#version 330 core
layout (location = 0) in vec3 vertex;
layout (location = 1) in vec2 aTexCoord;

uniform sampler2D heightmap;

out vec2 TexCoord;
out float height;

void main()
{
    float size = 0.2;
    TexCoord = aTexCoord;

    float y = (texture(heightmap, TexCoord).x - 0.5) * size;

    gl_Position = vec4(vertex.x, vertex.y + y, vertex.z, 1.0);
}
