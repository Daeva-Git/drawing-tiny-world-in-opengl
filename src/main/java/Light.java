import org.joml.Vector3f;

public class Light {
    private Vector3f color;
    private Vector3f direction;

    public Light () {
        color = new Vector3f(0.7f, 0.7f, 0.7f);
        direction = new Vector3f(4, 4, 4).normalize();
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public Vector3f getDirection() {
        return direction;
    }

    public void setDirection(Vector3f direction) {
        this.direction = direction;
    }
}
