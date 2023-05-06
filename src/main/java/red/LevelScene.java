package red;

import java.awt.event.KeyEvent;

public class LevelScene extends Scene{
    private boolean changingScene = false;
    private float ttChangeScene = 2.0f;
    public LevelScene(){

    }

    @Override
    public void update(float dt) {
        System.out.println(""+ (1.0f/dt)+" FPS");
        if(!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE)){
            changingScene = true;
        }
        if(changingScene && ttChangeScene > 0){
            ttChangeScene -= dt;
            Window.get().r -= dt * 2.0f;
            Window.get().b -= dt * 2.0f;
            Window.get().g -= dt * 2.0f;
        }else if(changingScene){
            System.out.println("scene changed");
            //Window.changeScene(1);
        }
    }
}
