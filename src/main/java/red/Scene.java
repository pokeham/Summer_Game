package red;

public abstract class Scene {
    public Scene(){

    }
    //update for that scene happens every frame
    public abstract void update(float dt);
    //init for scene
    public abstract void init();
}
