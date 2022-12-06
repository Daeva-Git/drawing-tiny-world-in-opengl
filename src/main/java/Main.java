import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import renderer.Shader;

import java.nio.IntBuffer;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.system.MemoryStack.stackPush;

public class Main {
    private Scene scene;
    private Window window;
    private Camera camera;

    private int VBO;
    private Shader shader;

    public static void main(String[] args) {
        new Main().run();
    }

    public void run() {
        init();
        start();
        dispose();
    }

    private void init() {
        scene = new Scene();
        window = new Window(1920, 1080);
        camera = new Camera();
    }

    private void start() {
        GL.createCapabilities();
        glClearDepth(-1);
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_GREATER);
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);
            glfwGetFramebufferSize(window.getID(), pWidth, pHeight);
            glViewport(0, 0, pWidth.get(0), pHeight.get(0));
        }

        glfwSetFramebufferSizeCallback(window.getID(), (window, width, height) -> glViewport(0, 0, width, height));

        shader = new Shader("src/assets/shaders/vertex.shader", "src/assets/shaders/fragment.shader");

        // set up vertex data (and buffer(s)) and configure vertex attributes
        // ------------------------------------------------------------------
        float vertices[] = {
                -0.5f, -0.5f,  1.0f, 0.0f, 0.0f,
                0.0f,  0.5f,  0.0f, 0.0f, 1.0f,
                0.5f, -0.5f,  0.0f, 1.0f, 0.0f
        };

        int VAO = glGenVertexArrays();
        glBindVertexArray(VAO);

        VBO = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 2, GL_FLOAT, false, 4 * 5, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 3, GL_FLOAT, false, 4 * 5, 2*4);
        glEnableVertexAttribArray(1);

        // glBindBuffer(GL_ARRAY_BUFFER, 0);
        shader.use();

        // init mouse position
        glfwSetCursorPosCallback(window.getID(), MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(window.getID(), MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(window.getID(), MouseListener::mouseScrollCallback);

        // keep time for calculating delta time
        long lastFrame = System.nanoTime();

        // run the rendering loop until the user has attempted to close the window or has pressed the ESCAPE key.
        while (!glfwWindowShouldClose(window.getID())) {
            // calculate delta time
            final long currentFrame = System.nanoTime();
            final float deltaTime = (currentFrame - lastFrame) / 100000000.0f;
            lastFrame = currentFrame;

            // update and draw
            update(deltaTime);
            draw(deltaTime);

            // swap the color buffers
            glfwSwapBuffers(window.getID());
            // poll for window events
            glfwPollEvents();
        }
    }

    private void update (float deltaTime) {
        shader.use();

        // camera
        if (MouseListener.isDragging()) {
            // TODO: 12/7/2022 something not working with dragging view
            camera.mouseDragged(deltaTime, MouseListener.getDeltaX(), -MouseListener.getDeltaY());
        }
        camera.mouseScrolled(deltaTime, MouseListener.getScrollY());
        camera.input(window.getID(), deltaTime);
        camera.update(window.getWidth(), window.getHeight());

        // shaders
        shader.setMatrix("model", camera.getModel());
        shader.setMatrix("view", camera.getView());
        shader.setMatrix("projection", camera.getProjection());

        MouseListener.endFrame();

        shader.dispose();
    }

    private void draw (float deltaTime) {
        // set the clear color
        glClearColor(28 / 255f, 30 / 255f, 38 / 255f, 1f);
        // clear the framebuffer
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        scene.render();
    }

    private void dispose() {
        glDeleteBuffers(VBO);

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window.getID());
        glfwDestroyWindow(window.getID());

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}
