#version 330 core
out vec4 FragColor;

in vec2 TexCoord;

uniform sampler2D terrainTexture;
uniform sampler2D heightMapTexture;

void main()
{
    FragColor = texture(terrainTexture, TexCoord);
}