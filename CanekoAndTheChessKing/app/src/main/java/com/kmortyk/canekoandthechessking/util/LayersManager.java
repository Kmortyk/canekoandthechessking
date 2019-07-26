package com.kmortyk.canekoandthechessking.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.LinkedList;
import java.util.List;

/**
 * Date class for storing a bitmap of a layer.
 */
class Layer{

    float x, y;
    Bitmap btm;

    Layer(Bitmap btm, float x, float y) {
        this.x = x;
        this.y = y;
        this.btm = btm;
    }

}

/**
 * A class that allows you to store multiple layers and draw them in some order.
 */
public class LayersManager {

    private List<Layer> layers;

    public LayersManager() { layers = new LinkedList<>(); }

    public void addLayer(Bitmap btm, float x, float y) {
        layers.add(new Layer(btm, x, y));
    }

    public void drawLayers(Canvas canvas, Vector2 viewOffset) {
        for (Layer l : layers) canvas.drawBitmap(l.btm, l.x + viewOffset.x, l.y + viewOffset.y, null);
    }

    public void clear() {
        for(Layer l: layers) { if(!l.btm.isRecycled()) l.btm.recycle(); }
        layers.clear();
    }

}
