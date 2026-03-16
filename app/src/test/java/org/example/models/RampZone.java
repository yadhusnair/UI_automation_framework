package org.example.models;

import org.example.tests.MapEditorTestV2;
import org.example.pages.MapEditorV2;

public class RampZone extends BaseZone{
    private int upRampHeading;
    private int gradientAngle;

    public RampZone(String name, int startX , int startY, int endX, int endY , int angle , int upRampHeading , int gradientAngle){
        super(name,startX,startY,endX,endY,angle);
        this.upRampHeading = upRampHeading;
        this.gradientAngle = gradientAngle;

    }
    @Override
    public void fillDynamicFields(MapEditorV2 page){
        page.fillUpRampHeading(upRampHeading);
        page.fillGradientAngle(gradientAngle);
    }

    @Override
    public ZoneType getZoneType(){
        return ZoneType.RAMP;
    }
}