#version 330 core
uniform sampler2D Texture;
uniform vec3 lightColor;
uniform vec3 lightDir;
uniform mat4 view;

in GS_OUT {
    vec2 texCoord;
    vec3 norm;
} fs_in;

out vec4 FragColor;

void main() {
    // Kd diffuse-reflection coefficient
    float Kd = 0.8;

    // diffuse
    vec3 diffuse = Kd * max(dot(fs_in.norm, lightDir), 0.0) * lightColor;

    vec4 vertexColor = texture(Texture, fs_in.texCoord);

    // discard alpha
    if (vertexColor.a <= 0.1) discard;
    // clip from top
    if (vertexColor.y > 0.5) discard;

    FragColor = vec4(vec4(diffuse + lightColor, 1) * vertexColor);
}
