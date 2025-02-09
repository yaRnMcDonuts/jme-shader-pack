/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.shader.demo.utils.blendlayers.effects;

import com.github.shader.demo.utils.blendlayers.ShaderBlendLayer;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector4f;

/**
 *
 * @author ryan
 */
public class HSVCycleEffect extends BlendLayerEffect{
    
    private float hueChangeVar, saturationChangeVar = 0.35f, brightnessChangeVar;
    private float hueCycleSpeed = 0.5f, saturationCycleSpeed, brightnessSaturationCycleSpeed;
  
    private Vector4f originalHsvValue;
    
    public HSVCycleEffect(String name) {
        super(name);
    }
    
    public HSVCycleEffect() {
        super("HSV_Effect");
        setDuration(8.0f);
    }
    
    @Override
    public void update(float tpf) {
        super.update(tpf); 
        
        if(affectedLayer != null){
            Vector4f currentHsv = affectedLayer.getHsvScalar();
            if(hueCycleSpeed != 0.0f){
                
                float hueChange = tpf * hueCycleSpeed;
                
                float newHue = currentHsv.getX() + hueChange;
                if(newHue > 1.0f){
                    newHue = 1.0f - newHue;
                }else if(newHue < 0.0f){
                    newHue = newHue + 1.0f;
                }
                currentHsv.setX(newHue);
            }
        }
        
        
    }
    
    

    @Override
    public void onFinish() {
        if(affectedLayer != null){
            affectedLayer.setHsvScalar(this.originalHsvValue);
        }
        super.onFinish(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }
    

    @Override
    public void setAffectedLayer(ShaderBlendLayer affectedLayer) {
        super.setAffectedLayer(affectedLayer); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        originalHsvValue = affectedLayer.getHsvScalar().clone();
        
        affectedLayer.getHsvScalar().setX(hueChangeVar);
        affectedLayer.getHsvScalar().setY(saturationChangeVar);
        affectedLayer.getHsvScalar().setZ(brightnessChangeVar);
    }
    
    public float getSaturationChangeVar() {
        return saturationChangeVar;
    }

    public float getSaturationCycleSpeed() {
        return saturationCycleSpeed;
    }

    public float getBrightnessChangeVar() {
        return brightnessChangeVar;
    }

    public float getBrightnessSaturationCycleSpeed() {
        return brightnessSaturationCycleSpeed;
    }

    public void setBrightnessChangeVar(float brightnessChangeVar) {
        this.brightnessChangeVar = brightnessChangeVar;
    }

    public void setSaturationChangeVar(float saturationChangeVar) {
        this.saturationChangeVar = saturationChangeVar;
    }

    public void setSaturationCycleSpeed(float saturationCycleSpeed) {
        this.saturationCycleSpeed = saturationCycleSpeed;
    }

    public void setBrightnessSaturationCycleSpeed(float brightnessSaturationCycleSpeed) {
        this.brightnessSaturationCycleSpeed = brightnessSaturationCycleSpeed;
    }

    public void setHueChangeVar(float hueChangeVar) {
        this.hueChangeVar = hueChangeVar;
    }

    public void setHueCycleSpeed(float hueCycleSpeed) {
        this.hueCycleSpeed = hueCycleSpeed;
    }

    public float getHueChangeVar() {
        return hueChangeVar;
    }

    public float getHueCycleSpeed() {
        return hueCycleSpeed;
    }

}
