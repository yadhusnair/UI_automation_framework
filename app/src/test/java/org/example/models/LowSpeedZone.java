package org.example.models;

import org.example.pages.MapEditorV2;

public class LowSpeedZone extends BaseZone {

    private double velocityFactor;

    public LowSpeedZone(String name, int startX, int    startY,
                        int endX, int endY, int angle,
                        double velocityFactor) {

        super(name, startX, startY, endX, endY, angle);
        this.velocityFactor = velocityFactor;
    }

    @Override
    public void fillDynamicFields(MapEditorV2 page) {
        page.fillVelocityFactor(velocityFactor);
    }

    @Override
    public ZoneType getZoneType(){
        return ZoneType.LOW_SPEED;
    }
    
}