#version 330 core
uniform sampler2D Texture;
uniform sampler2D RelfectedTexture;
uniform float timePassed;
uniform vec3 lightColor;
uniform vec3 lightDir;
uniform vec3 viewDir;
uniform mat4 view;

in GS_OUT {
    vec2 texCoord;
    vec3 vertPos;
    vec3 norm;
    vec4 clipSpace;
} fs_in;

out vec4 FragColor;

void main() {
    // Ka ambient reflection coefficient
    float Ka = 0.7;
    // Kd diffuse-reflection coefficient
    float Kd = 0.6;
    // Ks specular reflection coefficient
    float Ks = 0.8;
    // k shininess factor
    float k = 35;

    vec3 reflectDir = reflect(lightDir, fs_in.norm);

    // ambient
    vec3 ambient = Ka * lightColor;

    // diffuse
    float diff = max(dot(fs_in.norm, lightDir), 0.0);
    vec3 diffuse = Kd * diff * lightColor;

    // specular
    float spec = pow(max(dot(fs_in.norm, normalize(viewDir + lightDir)), 0.0), k);
    vec3 specular = Ks * spec * lightColor;

    vec2 uv = fs_in.texCoord;
    uv.x = uv.x + timePassed;

    vec2 ss = (fs_in.clipSpace.xy / fs_in.clipSpace.w) * 0.5 + 0.5;
    vec2 reflection = vec2(ss.x, -ss.y);
    vec4 reflectionVertexColor = texture(RelfectedTexture, reflection);

    vec4 vertexColor = texture(Texture, uv);

    FragColor = mix(vec4((ambient + diffuse + specular) * vec3(vertexColor), 1.0), reflectionVertexColor, 0.5);
}
