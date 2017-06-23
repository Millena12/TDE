package br.pucpr.mage;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * Created by Zadooke on 22/05/2017.
 */
public class Texture {
    private int id;
    private TextureParameters parameters;

    public int getId() {
        return id;
    }

    public TextureParameters getParameters() {
        return parameters;
    }

    public Texture bind() {
        glBindTexture(GL_TEXTURE_2D, id);
        return this;
    }

    public Texture unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
        return this;
    }

    public Texture(Image img, TextureParameters parameters) {
        // Valida os parametros
        if (img.getChannels() < 3)
            throw new IllegalArgumentException("Invalid Image!!");

        if (parameters == null)
            throw new IllegalArgumentException("Invalid Params!!");

        int format = img.getChannels() == 3 ? GL_RGB : GL_RGBA;

        // Copia os dados
        id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);
        glTexImage2D(GL_TEXTURE_2D, 0, format,
                img.getWidth(), img.getHeight(), 0, format,
                GL_UNSIGNED_BYTE, img.getPixels());

        // Ajustando os parametros
        this.parameters = parameters;
        parameters.apply(GL_TEXTURE_2D);
        if (parameters.isMipMapped()) {
            glGenerateMipmap(GL_TEXTURE_2D);
        }

        // Limpeza
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public Texture(Image img) {
        this(img, new TextureParameters());
    }

    public Texture(String imagePath, TextureParameters params) {
        this(new Image(imagePath), params);
    }

    public Texture(String imagePath) {
        this(new Image(imagePath));
    }
}