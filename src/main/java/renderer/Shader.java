package renderer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL32.*;

public class Shader {
    // program ID
    private int ID;
    private FloatBuffer fb = BufferUtils.createFloatBuffer(16);

    String ParseShader(String shaderPath) {
        StringBuilder vertexCode = new StringBuilder();
        try {
            BufferedReader vertexReader = new BufferedReader(new FileReader(shaderPath));
            String line;
            while ((line = vertexReader.readLine()) != null) {
                vertexCode.append(line).append("//\n");
            }
            vertexReader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return vertexCode.toString();
    }

    int CompileShader(int type, String source) {
        int id = glCreateShader(type);
        glShaderSource(id, source);
        glCompileShader(id);
        // check for shader compile errors
        int success[] = new int[1];
        glGetShaderiv(id, GL_COMPILE_STATUS, success);
        if (success[0] == 0) {
            String infoLog = glGetShaderInfoLog(id, 512);
            System.out.println("ERROR::SHADER::VERTEX::COMPILATION_FAILED\\n" + infoLog + "\n");
        }
        return id;
    }

    // constructor reads and builds the shader
    public Shader(String path, String name) {
        final String vertexPath = path + "/" + name + ".vert";
        final String fragmentPath = path + "/" + name + ".frag";
        final String geometryPath = path + "/" + name + ".geom";

        final String vertexSource = ParseShader(vertexPath);
        final String fragmentSource = ParseShader(fragmentPath);
        final String geometrySource = ParseShader(geometryPath);

        final int vertexID = CompileShader(GL_VERTEX_SHADER, vertexSource);
        final int fragmentID = CompileShader(GL_FRAGMENT_SHADER, fragmentSource);
        final int geometryID = CompileShader(GL_GEOMETRY_SHADER, geometrySource);

        ID = glCreateProgram();
        glAttachShader(ID, vertexID);
        glAttachShader(ID, fragmentID);
        glAttachShader(ID, geometryID);
        glLinkProgram(ID);

        int success[] = new int[1];
        glGetProgramiv(ID, GL_LINK_STATUS, success);
        if (success[0] == 0) {
            String infoLog = glGetProgramInfoLog(ID, 512);
            System.out.println("ERROR::SHADER::PROGRAM::LINKING_FAILED\n" + infoLog + "\n");
        }

        // delete the shaders as they're linked into our program now and no longer necessary
        glDeleteShader(vertexID);
        glDeleteShader(fragmentID);
        glDeleteShader(geometryID);
    }

    // use/activate the shader
    public void bind() {
        glUseProgram(ID);
    }

    // utility uniform functions
    public void setBool(String name, boolean value) {
        int val = value ? 1 : 0;
        glUniform1i(glGetUniformLocation(ID, name), val);
    }

    public void setInt(String name, int value) {
        glUniform1i(glGetUniformLocation(ID, name), value);
    }

    public void setFloat(String name, float value) {
        glUniform1f(glGetUniformLocation(ID, name), value);
    }

    public void setMatrix(String name, Matrix4f value) {
        glUniformMatrix4fv(glGetUniformLocation(ID, name), false, value.get(fb));
    }

    public void setVec3(String name, Vector3f value) {
        GL20.glUniform3f(GL20.glGetUniformLocation(ID, name), value.x, value.y, value.z);
    }

    public void setVec3(String name, float x, float y, float z) {
        GL20.glUniform3f(GL20.glGetUniformLocation(ID, name), x, y, z);
    }

    public void setTexture(String name, int slot) {
        glUniform1i(glGetUniformLocation(ID, name), slot);
    }

    public void unbind() {
        glDeleteProgram(ID);
    }
}
