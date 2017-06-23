package br.pucpr.cg;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import br.pucpr.mage.Keyboard;
import br.pucpr.mage.Mesh;
import br.pucpr.mage.Scene;
import br.pucpr.mage.Shader;
import br.pucpr.mage.Window;
import br.pucpr.mage.phong.DirectionalLight;

import br.pucpr.cg.PerlinNoiseGen;

import br.pucpr.mage.phong.Material;

import br.pucpr.mage.Texture;


public class TerrainLoader implements Scene {

    private static final String PATH = "grass.png";
    private Keyboard keys = Keyboard.getInstance();
    
    //Dados da cena
    private Camera camera = new Camera();

    private PerlinNoiseGen perlin = new PerlinNoiseGen();

    float scaleMoutain=1;


    private DirectionalLight light = new DirectionalLight(
            new Vector3f( 1.0f, -1.0f, -1.0f), //direction
            new Vector3f( 0.5f,  0.5f,  0.5f),   //ambient
            new Vector3f( 1.0f,  1.0f,  0.8f),   //diffuse
            new Vector3f( 1.0f,  1.0f,  1.0f));  //specular

    //Dados da malha
    private Mesh mesh;
     Material material = new Material(
            new Vector3f(0.5f, 0.5f, 0.5f), //ambient
            new Vector3f(0.5f, 0.5f, 0.5f), //diffuse
            new Vector3f(0.5f, 0.5f, 0.5f), //specular
            100.0f);                         //specular power

    private float angleX = 0.0f;
    private float angleY = 0.5f;
    
    @Override
    public void init() {
        perlin.run();
        material.setTexture("uTexture", new Texture(PATH));

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        try {
            mesh = MeshFactory.loadTerrain(new File("perlin1.png"), 0.5f,3);
            System.out.println("Done!");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        camera.getPosition().y = 200.0f;
        camera.getPosition().z = 200.0f;

       /* try {
			mesh = MeshFactory
					.loadTerrain(new File("bricks_t.jpg"), 0.5f));
		} catch (IOException e) {
			e.printStackTrace();
		}
        camera.getPosition().y = 1.0f;*/
    }

    @Override
    public void update(float secs) {
        if(keys.isDown(GLFW_KEY_Z)){
            scaleMoutain += scaleMoutain > 3 ? 0 : 0.1f;
        }

        if(keys.isDown(GLFW_KEY_X)){
            scaleMoutain -= scaleMoutain < -1 ? 0 : 0.1f;
        }
        if (keys.isPressed(GLFW_KEY_ESCAPE)) {
            glfwSetWindowShouldClose(glfwGetCurrentContext(), GLFW_TRUE);
            return;
        }

        if (keys.isDown(GLFW_KEY_A)) {
            angleY +=  Math.toRadians(180) * secs;
        }

        if (keys.isDown(GLFW_KEY_D)) {
            angleY -= Math.toRadians(180) * secs;
        }
        
        if (keys.isDown(GLFW_KEY_W)) {
            angleX += angleX >Math.toRadians(30) ? 0 : Math.toRadians(180) * secs;
        }

        if (keys.isDown(GLFW_KEY_S)) {
            angleX -= angleX < Math.toRadians(-30) ? 0 : Math.toRadians(180) * secs;
        }

        if(keys.isDown(GLFW_KEY_L)){
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        }
    }

@Override
public void draw() {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    
    Shader shader = mesh.getShader();
    shader.bind()
        .setUniform("uProjection", camera.getProjectionMatrix())
        .setUniform("uView", camera.getViewMatrix())
        .setUniform("uCameraPosition", camera.getPosition());
    light.apply(shader);
    material.apply(shader);
    shader.unbind();

    mesh.setUniform("uWorld", new Matrix4f().rotateY(angleY).rotateX(angleX));
    mesh.setUniform("uScale",scaleMoutain);
    mesh.draw();
}

    @Override
    public void deinit() {
    }

    public static void main(String[] args) {
        new Window(new TerrainLoader(), "TerrainLoader", 800, 600).show();
    }
}
