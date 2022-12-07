import renderer.Utils;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL20C.glEnableVertexAttribArray;

public class Demo {
    private Scene scene;
    private Window window;

    public static Demo instance;
    public static void main(String[] args) {
        instance = new Demo();
        instance.run();
    }

    public void run() {
        init(500, 500);
        start();
        dispose();
    }

    private void init(int width, int height) {
        window = new Window(width, height);
        scene = new Scene();
    }

    private void start() {
        final int stride = (Utils.POSITION_DATA_SIZE_IN_ELEMENTS + Utils.TEXTURE_DATA_SIZE_IN_ELEMENTS) * 4;

        // position
        glVertexAttribPointer(0, 3, GL_FLOAT, false, stride, 0);
        glEnableVertexAttribArray(0);

        // texture
        glVertexAttribPointer(1, 2, GL_FLOAT, false, stride, Utils.POSITION_DATA_SIZE_IN_ELEMENTS * 4);
        glEnableVertexAttribArray(1);

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
            render();

            // swap the color buffers
            glfwSwapBuffers(window.getID());
            // poll for window events
            glfwPollEvents();
        }
    }

    private void update (float deltaTime) {
        scene.update(deltaTime);
        MouseListener.endFrame();
    }

    private void render () {
        // set the clear color
        glClearColor(28 / 255f, 30 / 255f, 38 / 255f, 1f);
        // clear the framebuffer
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        scene.render();
    }

    private void dispose() {
        scene.dispose();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window.getID());
        glfwDestroyWindow(window.getID());

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public Window getWindow () {
        return window;
    }

    public Scene getScene () {
        return scene;
    }
}