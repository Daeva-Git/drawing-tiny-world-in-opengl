import renderer.Texture;

import static org.lwjgl.opengl.GL33.*;

public class FrameBuffer{
    private int id;
    private Texture texture;

    public FrameBuffer (int width, int height) {
        // generate framebuffer
        id = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, id);

        // create the texture to render the data to, and attach it to our framebuffer
        this.texture = new Texture(width, height, GL_TEXTURE0);

        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, this.texture.getID(), 0);

        // create renderbuffer store the depth info
        int rboID = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, rboID);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT32, width, height);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rboID);

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            assert false : "Error: Framebuffer is not complete";
        }
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public void bind() {
        glBindFramebuffer(GL_FRAMEBUFFER, id);
    }

    public void unbind() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public void bindTexture () {
        glActiveTexture(texture.getSlot());
        glBindTexture(GL_TEXTURE_2D, texture.getID());
    }
}