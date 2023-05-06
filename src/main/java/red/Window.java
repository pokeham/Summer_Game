package red;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int width;
    private int height;
    private String title;
    private long glfwWindow = NULL;

    public void setGlfwWindow() {this.glfwWindow = glfwCreateWindow(getWidth(),getHeight(),getTitle(),NULL,NULL);}
    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}
    public int getHeight() {return height;}
    public int getWidth() {return width;}
    public void setHeight(int height) {this.height = height;}
    public void setWidth(int width) {this.width = width;}

    //only one window object can be had at a time
    private static Window window = null;
    private Window(){
        setHeight(1000);
        setTitle("Test");
        setWidth(1920);
    }
    //ensures that only one window exists
    public static Window get(){
        if(Window.window == null){
            Window.window = new Window();
        }
        return Window.window;
    }
    public void run(){
        System.out.println("LWGJL" + Version.getVersion());
        init();
        loop();
        //free memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        //terminate glfw and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
    public void init(){
        //error callback
        GLFWErrorCallback.createPrint(System.err).set();
        //init GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }



        //configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE,GLFW_FALSE);//set to inivs before window is created
        glfwWindowHint(GLFW_RESIZABLE,GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED,GLFW_TRUE);

        //create window
        setGlfwWindow();
        if(glfwWindow == NULL){
            throw new IllegalStateException("Failed to Create GLFW window");
        }
        //mouse callbacks
        glfwSetCursorPosCallback(glfwWindow,MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow,MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow,MouseListener::mouseScrollCallback);
        //keyboard listeners
        glfwSetKeyCallback(glfwWindow,KeyListener::keyCallBack);

        //make OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        //Enable v-sync
        glfwSwapInterval(1);

        //now that window is created show window
        glfwShowWindow(glfwWindow);
        //make sure you use bindings
        GL.createCapabilities();
    }
    public void loop(){
        while(!glfwWindowShouldClose(glfwWindow)){
            //Poll Events
            glfwPollEvents();


            glClearColor(1.0f,0.0f,0.0f,1.0f);//but color into color buffer
            glClear(GL_COLOR_BUFFER_BIT); //clear buffer bit and flush

            if(KeyListener.isKeyPressed(GLFW_KEY_SPACE)){
                System.out.println("pressed!");
            }

            glfwSwapBuffers(glfwWindow);
        }

    }
}
