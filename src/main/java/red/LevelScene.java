package red;

import org.lwjgl.BufferUtils;

import java.awt.event.KeyEvent;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class LevelScene extends Scene{
    private int vboID, vaoID, eboID;
    private float[] vertexArray = {
        //position                  //color
         0.5f,-0.5f,0.0f,           1.0f,0.0f,0.0f,1.0f,//bottom right(red)     0
         0.5f, 0.5f,0.0f,           0.0f,0.0f,1.0f,1.0f,//top right(blue)       1
        -0.5f,-0.5f,0.0f,           1.0f,1.0f,0.0f,1.0f,//bottom left(yellow?)  2
        -0.5f, 0.5f,0.0f,           0.0f,1.0f,0.0f,1.0f//top left(green)        3
    };
    //IMPORTANT: MUST BE IN CCW ORDER
    private int[] elementArray = {
        1,3,0, //top right triangle
        0,3,2 //bottom left triangle

    };
    private String vertexShaderSRC = "" +
            "#version 330 core\n" +
            "layout (location = 0) in vec3 aPos;\n" +
            "layout (location = 1) in vec4 aColor;\n" +
            "out vec4 fColor;\n" +
            "void main(){\n" +
            "    fColor = aColor;\n" +
            "    gl_Position = vec4(aPos, 1.0);\n" +
            "}";
    private String fragmentShaderSRC = "" +
            "#version 330 core\n" +
            "in vec4 fColor;\n" +
            "out vec4 color;\n" +
            "void main(){\n" +
            "    color = fColor;\n" +
            "}";

    private int vertexID,fragmentID,shaderProgram;
    public LevelScene(){

    }

    @Override
    public void update(float dt) {
        //binder shader program
        glUseProgram(shaderProgram);
        //bind VAO
        glBindVertexArray(vaoID);

        //enable vertex attrib pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        //draw          //how to       //how many          //type       //start@
        glDrawElements(GL_TRIANGLES,elementArray.length,GL_UNSIGNED_INT,0);

        //unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        glUseProgram(0);

    }

    @Override
    public void init() {
        //====================================
        //     Compile and Link Shaders
        //====================================

        //first load and compile vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        //pass shader src code to gpu
        glShaderSource(vertexID, vertexShaderSRC);
        //compile the passed shader
        glCompileShader(vertexID);

        //Error Check is compilation
        int success = glGetShaderi(vertexID,GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int len = glGetShaderi(vertexID,GL_INFO_LOG_LENGTH);
            System.out.println("Error: vertex shader compilation failed");
            System.out.println(glGetShaderInfoLog(vertexID,len));
            assert false: "";
        }

        //second load and compile fragment shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        //pass shader src code to gpu
        glShaderSource(fragmentID , fragmentShaderSRC);
        //compile the passed shader
        glCompileShader(fragmentID );

        //Error Check is compilation
        success = glGetShaderi(fragmentID ,GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int len = glGetShaderi(fragmentID ,GL_INFO_LOG_LENGTH);
            System.out.println("Error: fragment shader compilation failed");
            System.out.println(glGetShaderInfoLog(fragmentID ,len));
            assert false: "";
        }

        //then link shaders and check for errors
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram,vertexID);
        glAttachShader(shaderProgram,fragmentID);
        glLinkProgram(shaderProgram);

        //check for linking errors
        success = glGetProgrami(shaderProgram,GL_LINK_STATUS);
        if(success == GL_FALSE){
            int len = glGetProgrami(shaderProgram,GL_INFO_LOG_LENGTH);
            System.out.println("Error: linking shaders failed");
            System.out.println(glGetProgramInfoLog(shaderProgram,len));
        }

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
