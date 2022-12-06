import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
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
    private World world;
    private Window window;
    private Camera camera;

    private int VBO;
    private Shader shader;

    private float deltaTime;

    // input
    private double[] lastMouseX;
    private double[] lastMouseY;
    private boolean mouseDown;
    public static void main(String[] args) {
        new Main().run();
    }

    public void run() {
        init();
        start();
        dispose();
    }

    private void init() {
        world = new World();
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
            glfwGetFramebufferSize(window.getWindow(), pWidth, pHeight);
            glViewport(0, 0, pWidth.get(0), pHeight.get(0));
        }

        glfwSetFramebufferSizeCallback(window.getWindow(), (window, width, height) -> glViewport(0, 0, width, height));

        shader = new Shader("src/shaders/vertex.shader", "src/shaders/fragment.shader");

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

        // mouse scroll
        GLFW.glfwSetScrollCallback(window.getWindow(), new GLFWScrollCallback() {
            @Override
            public void invoke (long window, double dx, double dy) {
                camera.mouseScrolled(deltaTime, (float) dy);
            }
        });

        // init mouse position
        lastMouseX = new double[1];
        lastMouseY = new double[1];
        glfwGetCursorPos(window.getWindow(), lastMouseX, lastMouseY);
        GLFW.glfwSetMouseButtonCallback(window.getWindow(), new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                mouseDown = action == 1;
                glfwGetCursorPos(window, lastMouseX, lastMouseY);
            }
        });

        // keep time for calculating delta time
        long lastFrame = System.nanoTime();

        // run the rendering loop until the user has attempted to close the window or has pressed the ESCAPE key.
        while (!glfwWindowShouldClose(window.getWindow())) {
            // calculate delta time
            final long currentFrame = System.nanoTime();
            deltaTime = (currentFrame - lastFrame) / 100000000.0f;
            lastFrame = currentFrame;

            // update and draw
            update(deltaTime);
            draw(deltaTime);

            // swap the color buffers
            glfwSwapBuffers(window.getWindow());
            // poll for window events
            glfwPollEvents();
        }
    }

    private void update (float deltaTime) {
        input(deltaTime);

        // update camera
        camera.update(window.getWidth(), window.getHeight());

        // set the clear color
        glClearColor(0.2f, .5f, .8f, 0.5f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

        shader.setMatrix("model", camera.getModel());
        shader.setMatrix("view", camera.getView());
        shader.setMatrix("projection", camera.getProjection());
        shader.use();
    }

    private void draw (float deltaTime) {
        world.render();
    }

    private void input(float deltaTime) {
        camera.input(window.getWindow(), deltaTime);

        // hande drag
        if (mouseDown) {
            double[] currentMouseX = {window.getWidth() / 2.0};
            double[] currentMouseY = {window.getHeight() / 2.0};
            glfwGetCursorPos(window.getWindow(), currentMouseX, currentMouseY);

            final float xOffset = (float) (lastMouseX[0] - currentMouseX[0]);
            final float yOffset = (float) (lastMouseY[0] - currentMouseY[0]);

            camera.mouseDragged(deltaTime, xOffset, -yOffset);

            lastMouseX[0] = currentMouseX[0];
            lastMouseY[0] = currentMouseY[0];
        }
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
