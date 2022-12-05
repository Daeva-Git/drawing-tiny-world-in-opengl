import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import java.nio.IntBuffer;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.system.MemoryStack.stackPush;

public class Main {
    private int VBO;
    private Shader shader;

    public static void main(String[] args) {
        new Main().run();
    }

    public void run() {
        start();
        update();
        dispose();
    }

    private World world;
    private Window window;

    private void start() {
        world = new World();
        window = new Window(1920, 1080);
    }


    private void update() {
        GL.createCapabilities();
        glClearDepth(-1);
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_GREATER);
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);
            glfwGetFramebufferSize(window.getWindow(), pWidth, pHeight);
            glViewport(0, 0, pWidth.get(0), pHeight.get(0));
        }

        glfwSetFramebufferSizeCallback(window.getWindow(), (window, width, height) -> glViewport(0, 0, width, height));
    }

    private void input() {

    }

    private void dispose() {
        glDeleteBuffers(VBO);
        shader.dispose();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window.getWindow());
        glfwDestroyWindow(window.getWindow());

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}
