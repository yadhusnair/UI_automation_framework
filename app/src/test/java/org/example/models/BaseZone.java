package org.example.models;

import org.example.pages.MapEditorV2;
import java.lang.Math;

public abstract class BaseZone{
    protected String name;
    protected int startX;
    protected int startY;
    protected int endX;
    protected int endY;
    protected int angle;


    public BaseZone(String name , int startX , int startY, int endX,int endY ,int angle ){
        this.name = name ;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.angle = angle;
    }
    public abstract void fillDynamicFields(MapEditorV2 page);

    public abstract ZoneType getZoneType();


    public String getName() {
        return name;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }

    public int getAngle() {
        return angle;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }
    public void setStartY(int startY) {
        this.startY = startY;
    }
    public void setEndX(int endX) {
        this.endX = endX;
    }
    public void setEndY(int endY) {
        this.endY = endY;
    }
    public void setAngle(int angle) {
        this.angle = angle;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getCenterY() {
    return Math.round((startY + endY) / 2.0f);
    }

    public int getCenterX() {
    return Math.round((startX + endX) / 2.0f);
    }



    

}