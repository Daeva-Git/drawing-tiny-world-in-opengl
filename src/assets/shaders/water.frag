#version 330 core
out vec4 FragColor;

in vec2 TexCoords;

uniform sampler2D Texture;
uniform float timePassed;

void main()
{
    vec2 uv = TexCoords;
    uv.x = uv.x + timePassed;
    FragColor = texture(Texture, uv);
}
