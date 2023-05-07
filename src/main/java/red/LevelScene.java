package red;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import renderer.Shader;

import java.awt.event.KeyEvent;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class LevelScene extends Scene{
    private int vboID, vaoID, eboID;
    private float[] vertexArray = {
        //position                  //color
         100.5f, 0.5f,0.0f,           1.0f,0.0f,0.0f,1.0f,//bottom right(red)     0
         100.5f,100.5f,0.0f,           0.0f,0.0f,1.0f,1.0f,//top right(blue)       1
         0.5f,0.5f,0.0f,           1.0f,1.0f,0.0f,1.0f,//bottom left(yellow?)  2
         0.5f, 100.5f,0.0f,           0.0f,1.0f,0.0f,1.0f//top left(green)        3
    };
    //IMPORTANT: MUST BE IN CCW ORDER
    private int[] elementArray = {
        1,3,0, //top right triangle
        0,3,2 //bottom left triangle

    };
    private float theta = 0.0f;

    private float[] cubeVertexArray = {
            //position of each point           //color of eah point (all red for now)
            -0.5f,-0.5f,-0.5f,                 1.0f,0.0f,0.0f,1.0f, // 0   bottom left      front face
            -0.5f, 0.5f,-0.5f,                 1.0f,0.0f,0.0f,1.0f, // 1   top left
             0.5f, 0.5f,-0.5f,                 1.0f,0.0f,0.0f,1.0f, // 2   top right
             0.5f,-0.5f,-0.5f,                 1.0f,0.0f,0.0f,1.0f, // 3   bottom right

             0.5f,-0.5f, 0.5f,                 0.0f,0.0f,1.0f,1.0f, // 4    bottom right                back face (looking from front)
             0.5f, 0.5f, 0.5f,                 0.0f,0.0f,1.0f,1.0f, // 5    top right
            -0.5f, 0.5f, 0.5f,                 0.0f,0.0f,1.0f,1.0f, // 6    top left
            -0.5f,-0.5f, 0.5f,                 0.0f,0.0f,1.0f,1.0f, // 7    top right
    };
    private int[] cubeElementArray = {

            //left
            0,1,6,
            6,7,0,
            //right
            5,2,4,
            2,3,4,
            //back
            5,6,7,
            7,4,5,
            //front
            3,2,1,
            1,0,3,
            //top
            1,2,5,
            5,6,1,
            //bottom
            4,7,0,
            0,3,4
    };

    private float offsetX = 0.0f;

    private Shader defaultShader;
    public LevelScene() {

    }
    @Override
    public void update(float dt) {



        if(KeyListener.isKeyPressed(GLFW_KEY_D)){
            offsetX = offsetX + 0.1f;
            Camera.get().setPosition(new Vector3f(Camera.get().getPosition().x+10.0f,Camera.get().getPosition().y,Camera.get().getPosition().z));
        }
        if(KeyListener.isKeyPressed(GLFW_KEY_A)){
            offsetX = offsetX - 0.1f;
            Camera.get().setPosition(new Vector3f(Camera.get().getPosition().x+10.0f,Camera.get().getPosition().y,Camera.get().getPosition().z));
        }
        if(KeyListener.isKeyPressed(GLFW_KEY_W)){

        }
        if(KeyListener.isKeyPressed(GLFW_KEY_S)){

        }
        System.out.println(Camera.get().getPosition().z);
        Camera.get().setPosition(new Vector3f((MouseListener.getX())-1870+offsetX,-1*MouseListener.getY(),Camera.get().getPosition().z));

        defaultShader.use();
        defaultShader.uploadMat4f("uProj",Camera.get().getProjectionMatrix());
        defaultShader.uploadMat4f("uView",Camera.get().getViewMatrix());
        //bind VAO
        glBindVertexArray(vaoID);

        //enable vertex attrib pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        //draw          //how to       //how many          //type       //start@
        glDrawElements(GL_TRIANGLES,cubeElementArray.length,GL_UNSIGNED_INT,0);

        //unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        defaultShader.detatch();

    }

    @Override
    public void init() {
        defaultShader= new Shader("assets/shaders/default.glsl");
        defaultShader.compile();
        //===============================================================
        // Generate VAO, VBO and EBO buffer objects , and send to GPU
        // ==============================================================

        //generate and bind vao
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        //create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        //create vbo and upload vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,vboID);
        glBufferData(GL_ARRAY_BUFFER,vertexBuffer,GL_STATIC_DRAW);

        //create the indicies and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER,elementBuffer,GL_STATIC_DRAW);

        //add vertex attribute pointers (offset)
        int positionsSize =3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionsSize + colorSize) * floatSizeBytes;
        //pointer for position elements
        glVertexAttribPointer(0,positionsSize,GL_FLOAT,false,vertexSizeBytes,0);
        glEnableVertexAttribArray(0);
        //pointer for color elements
        glVertexAttribPointer(1,colorSize,GL_FLOAT,false,vertexSizeBytes,positionsSize*floatSizeBytes);
        glEnableVertexAttribArray(1);


    }
}
