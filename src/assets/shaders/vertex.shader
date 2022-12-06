#version 330 core
layout (location = 0) in vec3 aPos;
layout(location = 1) in vec4 color;
out vec4 var_color;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main()
{
   var_color = color;
   gl_Position = projection * view * model * vec4(aPos, 1.0);
}
