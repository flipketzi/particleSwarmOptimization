package entities;

import math.Vector2D;
import simulation.EntityController;
import simulation.Map2D;
import simulation.Simulation;

import java.awt.*;

public class Entity {

    private Vector2D position;
    private Vector2D direction;
    private int ticksSinceLastImprovement = 0;

    private double distance = 8;
    private Vector2D personalBest;


    public Entity(Vector2D startPos)
    {
        this.position = new Vector2D(startPos.getX(), startPos.getY());
        this.personalBest = startPos;
        this.direction = new Vector2D(Math.random() -0.5f, Math.random() -0.5f).mult(distance);

    }


    public void move(Map2D map){

        //Calculate direction for Vectors to move into
        Vector2D dir = new Vector2D(direction.getX(),direction.getY()).normalize().mult(distance);
        Vector2D pBest = new Vector2D(personalBest.getX() - position.getX(), personalBest.getY() - position.getY()).normalize().mult(distance);
        Vector2D gBest = new Vector2D(EntityController.GLOBAL_BEST.getX() - position.getX(), EntityController.GLOBAL_BEST.getY() - position.getY()).normalize().mult(distance);

        //Random distance
        dir.mult(3 * Math.random());
        pBest.mult(2 * Math.random());
        gBest.mult(2 * Math.random());

        if(ticksSinceLastImprovement > 100)
            gBest.mult(0);
        if(ticksSinceLastImprovement > 300)
            pBest.mult(0);


        //Update Position and direction Vector
        double oldX = position.getX();
        double oldY = position.getY();

        this.position.add(dir).add(pBest).add(gBest);
        direction.setX(position.getX() - oldX);
        direction.setY(position.getY() - oldY);

        if(map.getValueOf((int) position.getX()/ Simulation.BLOCKSIZE,(int) position.getY()/ Simulation.BLOCKSIZE) == Map2D.BORDER)
        {
            this.position.setX(oldX);
            this.position.setY(oldY);
            this.direction = new Vector2D(Math.random() -0.5f, Math.random() -0.5f).mult(distance);
        }

        ticksSinceLastImprovement++;

    }

    public void draw(Graphics g){
        Vector2D pos = this.getPosition();
        int x = (int) pos.getX();
        int y = (int) pos.getY();
        int r = 10;
        x = x-(r/2);
        y = y-(r/2);
        g.setColor(Color.BLUE);
        g.fillOval(x,y,r,r);
        g.setColor(Color.black);
        g.drawOval(x,y,r,r);
    }

    public Vector2D getPosition(){return position;}

    public void jump(){

    }

    public Vector2D getPersonalBest() {
        return personalBest;
    }

    public void setPersonalBest(Vector2D personalBest) {
        this.personalBest = personalBest;
        ticksSinceLastImprovement = 0;
    }
}
