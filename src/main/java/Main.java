import org.joml.Vector3f;
import renderer.Shader;
import renderer.Water;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL20C.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Main {
    private Scene scene;
    private Window window;
    private Camera camera;
    private FrameBuffer frameBuffer;

    private Water water;
    private int VAO, VBO, EBO;
    private Shader defaultShader;

    public static void main(String[] args) {
        new Main().run();
    }

    public void run() {
        init();
        start();
        dispose();
    }

    private void init() {
        int width = 500;
        int height = 500;

        scene = new Scene();
        window = new Window(width, height);
        camera = new Camera();
        camera.setPosition(new Vector3f(-9.964E-3f,  1.180E+0f,  1.908E+0f));
        camera.setFront(new Vector3f(-1.160E-2f, -5.478E-1f, -8.365E-1f));
        camera.setPitch(-27);
//        frameBuffer = new FrameBuffer(width, height);

        water = new Water();
    }

    private void start() {
        defaultShader = new Shader("src/assets/shaders/vertex.shader", "src/assets/shaders/fragment.shader");

        VBO = glGenVertexArrays();
        VAO = glGenBuffers();
        EBO = glGenBuffers();

        glBindVertexArray(VAO);

        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, water.getVertices(), GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, water.getIndices(), GL_STATIC_DRAW);

        // position
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * 4, 0);
        glEnableVertexAttribArray(0);

        // TODO: 07.12.22 change into normals (color for now)
        // normals
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * 4, 3 * 4);
        glEnableVertexAttribArray(1);

        // texture
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * 4, 6 * 4);
        glEnableVertexAttribArray(2);

        // TODO: 07.12.22 remove
        defaultShader.setTexture("texture", water.getTexture().getID());

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
            render(deltaTime);

            // swap the color buffers
            glfwSwapBuffers(window.getID());
            // poll for window events
            glfwPollEvents();
        }
    }

    private void update (float deltaTime) {
        defaultShader.use();

        // camera
        if (MouseListener.isDragging()) {
            // TODO: 12/7/2022 something not working with dragging view
            camera.mouseDragged(deltaTime, MouseListener.getDeltaX(), -MouseListener.getDeltaY());
        }
        camera.mouseScrolled(deltaTime, MouseListener.getScrollY());
        camera.input(window.getID(), deltaTime);
        camera.update(window.getWidth(), window.getHeight());

        // shaders
        defaultShader.setMatrix("model", camera.getModel());
        defaultShader.setMatrix("view", camera.getView());
        defaultShader.setMatrix("projection", camera.getProjection());

        MouseListener.endFrame();

        defaultShader.dispose();
    }

    private void render (float deltaTime) {
        // set the clear color
        glClearColor(28 / 255f, 30 / 255f, 38 / 255f, 1f);
        // clear the framebuffer
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glBindVertexArray(VAO);
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

        // TODO: 12/7/2022 test
//        frameBuffer.bind();
//
//        frameBuffer.unbind();
        // TODO: 12/7/2022 test

        scene.render();
    }

    private void dispose() {
        glDeleteBuffers(VBO);
        glDeleteBuffers(VAO);
        glDeleteBuffers(EBO);

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window.getID());
        glfwDestroyWindow(window.getID());

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}
