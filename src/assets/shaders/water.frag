#version 330 core
out vec4 FragColor;

in vec2 TexCoords;

uniform sampler2D waterTexture;
uniform float timePassed;

void main()
{
    vec2 uv = TexCoords;
    uv.y = uv.y + timePassed;
    FragColor = texture(waterTexture, uv);
}
