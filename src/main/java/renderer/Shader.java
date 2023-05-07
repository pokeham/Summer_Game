package renderer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader {
    private int shaderProgramID;
    private String vertexSRC;
    private String fragmentSRC;
    private String filePath;
    public Shader(String filepath){
        this.filePath = filepath;
        try{

            String source = new String(Files.readAllBytes(Paths.get(filepath)));//get entrie file into a string
            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)"); // split the file at the #type ...
            int index = source.indexOf("#type")+6; //index of word after type
            int eol = source.indexOf("\r\n",index);//end of line index
            String firstpattern = source.substring(index,eol).trim(); //find the substring of the word after the first #type
            index = source.indexOf("#type",eol)+6;
            eol = source.indexOf("\r\n",index);
            String secondpattern = source.substring(index,eol).trim();//find the substring of the word after the second #type

            if(firstpattern.equals("vertex")){
                vertexSRC = splitString[1];
            }else if(firstpattern.equals("fragment")){
                fragmentSRC = splitString[1];
            }else{
                throw new IOException("Unexpected token '" + firstpattern +"'");

            }

            if(secondpattern.equals("vertex")){
                vertexSRC = splitString[2];
            }else if(secondpattern.equals("fragment")){
                fragmentSRC = splitString[2];
            }else{
                throw new IOException("Unexpected token '" + secondpattern +"'");

            }
        }catch (IOException e){
            e.printStackTrace();
            assert false : "Error: Could not open shader file at: '" + filepath+"'";
        }
    }
    public void compile(){
        int vertexID,fragmentID;

        //====================================
        //     Compile and Link Shaders
        //====================================

        //first load and compile vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        //pass shader src code to gpu
        glShaderSource(vertexID, vertexSRC);
        //compile the passed shader
        glCompileShader(vertexID);

        //Error Check is compilation
        int success = glGetShaderi(vertexID,GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int len = glGetShaderi(vertexID,GL_INFO_LOG_LENGTH);
            System.out.println("Error: vertex shader compilation failed "+ filePath);
            System.out.println(glGetShaderInfoLog(vertexID,len));
            assert false: "";
        }

        //second load and compile fragment shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        //pass shader src code to gpu
        glShaderSource(fragmentID , fragmentSRC);
        //compile the passed shader
        glCompileShader(fragmentID );

        //Error Check is compilation
        success = glGetShaderi(fragmentID ,GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int len = glGetShaderi(fragmentID ,GL_INFO_LOG_LENGTH);
            System.out.println("Error: fragment shader compilation failed " + filePath);
            System.out.println(glGetShaderInfoLog(fragmentID ,len));
            assert false: "";
        }

        //then link shaders and check for errors
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID,vertexID);
        glAttachShader(shaderProgramID,fragmentID);
        glLinkProgram(shaderProgramID);

        //check for linking errors
        success = glGetProgrami(shaderProgramID,GL_LINK_STATUS);
        if(success == GL_FALSE){
            int len = glGetProgrami(shaderProgramID,GL_INFO_LOG_LENGTH);
            System.out.println("Error: linking shaders failed " + filePath);
            System.out.println(glGetProgramInfoLog(shaderProgramID,len));
        }
    }
    public void use(){
        glUseProgram(shaderProgramID);
    }
    public void detatch(){
        glUseProgram(0);
    }
    public void uploadMat4f(String varName, Matrix4f mat4f){
        int varLoc = glGetUniformLocation(shaderProgramID,varName);
        FloatBuffer matBuf = BufferUtils.createFloatBuffer(16);
        mat4f.get(matBuf);
        glUniformMatrix4fv(varLoc,false,matBuf);
    }
}
