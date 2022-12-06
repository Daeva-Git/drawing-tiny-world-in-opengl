import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {
    private static MouseListener instance;

    private final boolean[] mouseButtonPressed = new boolean[3];
    private double scrollX, scrollY;
    private double xPos, yPos, lastY, lastX;
    private boolean isDragging;

    private MouseListener() {
        this.scrollX = this.scrollY= 0.0;
        this.xPos = this.yPos = 0.0;
        this.lastX = this.lastY= 0.0;
    }

    public static MouseListener instance() {
        if (MouseListener.instance == null) {
            MouseListener.instance = new MouseListener();
        }

        return MouseListener.instance;
    }

    public static void mousePosCallback(long window, double xPos, double yPos) {
        instance().lastX = instance().xPos;
        instance().lastY = instance().yPos;
        instance().xPos = xPos;
        instance().yPos = yPos;
        instance().isDragging = instance().mouseButtonPressed[0] || instance().mouseButtonPressed[1] || instance().mouseButtonPressed[2];
    }

    public static void mouseButtonCallback(long window, int button, int action, int mode) {
        if (action == GLFW_PRESS) {
            if (button < instance().mouseButtonPressed.length) {
                instance().mouseButtonPressed[button] = true;
            }
        } else if (action == GLFW_RELEASE) {
            if (button < instance().mouseButtonPressed.length) {
                instance().mouseButtonPressed[button] = false;
                instance().isDragging = false;
            }
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        instance().scrollX = xOffset;
        instance().scrollY = yOffset;
    }

    public static void endFrame() {
        instance().scrollX = 0;
        instance().scrollY = 0;
        instance().lastX = instance().xPos;
        instance().lastY = instance().yPos;
    }

    public static float getX() {
        return (float) instance().xPos;
    }

    public static float getY() {
        return (float) instance().yPos;
    }

    public static float getDeltaX() {
        return (float) (instance().lastX - instance().xPos);
    }

    public static float getDeltaY() {
        return (float) (instance().lastY - instance().yPos);
    }

    public static float getScrollX() {
        return (float) instance().scrollX;
    }

    public static float getScrollY() {
        return (float) instance().scrollY;
    }

    public static boolean isDragging() {
        return instance().isDragging;
    }

    public static boolean mouseButtonDown(int button) {
        if (button < instance().mouseButtonPressed.length) {
            return instance().mouseButtonPressed[button];
        }
        return false;
    }
}