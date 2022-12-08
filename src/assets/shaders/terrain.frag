#version 330 core
out vec4 FragColor;

in vec2 TexCoords;
in vec3 LightDir;
in vec3 Norm;

uniform sampler2D Texture;

void main()
{
    vec3 lightColor = vec3(1, 1, 1);

    // Kd diffuse-reflection coefficient
    float Kd = 0.6;

    // diffuse
    vec3 diffuse = Kd * max(dot(Norm, LightDir), 0.0) * lightColor;

    vec4 vertexColor = texture(Texture, TexCoords);
    FragColor = vec4((diffuse + lightColor) * vec3(vertexColor), 1);
}
