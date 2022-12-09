#version 330 core
uniform sampler2D Texture;
uniform float timePassed;
uniform vec3 lightColor;
uniform vec3 lightPos;
uniform vec3 viewDir;
uniform mat4 view;

in GS_OUT {
    vec2 texCoord;
    vec3 fragPos;
    vec3 norm;
} fs_in;

out vec4 FragColor;

void main() {
    // Ka ambient reflection coefficient
    float Ka = 0.5;
    // Kd diffuse-reflection coefficient
    float Kd = 1;
    // Ks specular reflection coefficient
    float Ks = 2;
    // k shininess factor
    float k = 32;

    vec3 lightDir = normalize(lightPos - fs_in.fragPos);
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

    vec4 vertexColor = texture(Texture, uv);
//    FragColor = vec4((ambient + diffuse + specular) * vec3(vertexColor), 1.0);
    FragColor = vertexColor;
}
