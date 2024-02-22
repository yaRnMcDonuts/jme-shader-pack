package com.github.shader.demo.utils;

import com.jme3.material.MatParam;
import com.jme3.material.Material;
import com.jme3.math.Vector4f;
import com.jme3.scene.Geometry;
import com.jme3.scene.SceneGraphVisitorAdapter;
import com.jme3.scene.Spatial;
import com.jme3.shader.VarType;
import com.jme3.texture.Texture;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ryan
 */
public class BlendLayerEffect {

    private String name;
    private int layerIndex = -1;
    private String blendLayerPrefixedName;
    private float blendValue = 0;
    private final Vector4f blendVec = new Vector4f();
    private final List<Material> materials = new ArrayList<>();
    private Texture baseColorMap;
    private Texture normalMap;
    private Texture metallicRoughnessAoMap;
    private Texture emissiveMap;
    private boolean triplanar = true;

    public BlendLayerEffect(String name, int layerIndex, Spatial spatial) {
        this.name = name;
        setLayerIndex(layerIndex);
        addMaterialsFromSpatial(spatial);
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getBlendValue() {
        return blendValue;
    }

    public void setBlendValue(float blendValue) {
        this.blendValue = blendValue;
        blendVec.x = blendValue;
    }

    private void clearLayer() {
        for (Material mat : materials) {
            for (MatParam matParam : mat.getParams()) {
                if (matParam.getName().startsWith(blendLayerPrefixedName)) {
                    mat.clearParam(matParam.getName());
                }
            }
        }
    }

    private void setLayerIndex(int layerIndex) {
        if (layerIndex != -1) {
            clearLayer();
        }

        this.layerIndex = layerIndex;
        this.blendLayerPrefixedName = "BlendLayer_" + layerIndex;

        for (Material mat : materials) {
            mat.setVector4(blendLayerPrefixedName + "_BlendVec", blendVec);
        }
    }

    private void addMaterialsFromSpatial(Spatial spatial) {
        spatial.breadthFirstTraversal(new SceneGraphVisitorAdapter() {
            @Override
            public void visit(Geometry geom) {
                registerMaterial(geom.getMaterial());
            }
        });
    }
    
    private void registerMaterial(Material mat) {
        String paramName = blendLayerPrefixedName + "_BlendVec";
        // detect if the material's matDef is valid and has support for the blend layer
        if (mat.getMaterialDef().getMaterialParam(paramName) != null) {
            if (!materials.contains(mat)) {
                materials.add(mat);
                mat.setVector4(paramName, blendVec);
            }
        }
    }
    
    public boolean isTriplanar() {
        return triplanar;
    }

    public void setTriplanar(boolean triplanar) {
        this.triplanar = triplanar;
        if (baseColorMap != null) {
            baseColorMap.setWrap(Texture.WrapMode.Repeat);
        }
        if (normalMap != null) {
            normalMap.setWrap(Texture.WrapMode.Repeat);
        }
        if (metallicRoughnessAoMap != null) {
            metallicRoughnessAoMap.setWrap(Texture.WrapMode.Repeat);
        }
        if (emissiveMap != null) {
            emissiveMap.setWrap(Texture.WrapMode.Repeat);
        }
    }

    public void setParam(String name, VarType varType, Object value) {
        for (Material mat : materials) {
            
            if (value == null) {
                mat.clearParam(name);
                continue;
            }
            
            switch (varType) {
                case Boolean:
                    Boolean bool = (Boolean) value;
                    if (!bool) {
                        mat.clearParam(name);
                    } else {
                        mat.setParam(name, varType, value);
                    }
                    break;
                    
                case Texture2D:
                    if (triplanar) {
                        Texture texture = (Texture) value;
                        texture.setWrap(Texture.WrapMode.Repeat);
                    }
                    mat.setParam(name, varType, value);
                    break;
                    
                default:
                    mat.setParam(name, varType, value);
            }
        }
    }
    
    /**
     * The specified value must be between -1 and 5
     * @param debugMode
     */
    public void setDebugValuesMode(int debugMode) {
        setParam("DebugValuesMode", VarType.Int, debugMode);
    }

    public void setBaseColorMap(Texture texture) {
        baseColorMap = texture;
        setParam(blendLayerPrefixedName + "_BaseColorMap", VarType.Texture2D, texture);
    }

    public void setNormalMap(Texture texture) {
        normalMap = texture;
        setParam(blendLayerPrefixedName + "_NormalMap", VarType.Texture2D, texture);
    }

    public void setMetallicRoughnessAoMap(Texture texture) {
        metallicRoughnessAoMap = texture;
        setParam(blendLayerPrefixedName + "_MetallicRoughnessAoMap", VarType.Texture2D, texture);
    }

    public void setEmissiveMap(Texture texture) {
        emissiveMap = texture;
        setParam(blendLayerPrefixedName + "_EmissiveMap", VarType.Texture2D, texture);
    }

    public void setBlendAlpha(boolean alpha) {
        setParam(blendLayerPrefixedName + "_BlendAlpha", VarType.Boolean, alpha);
    }
}
