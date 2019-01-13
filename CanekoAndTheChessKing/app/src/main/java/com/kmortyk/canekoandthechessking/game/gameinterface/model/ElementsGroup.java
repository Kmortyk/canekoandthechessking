package com.kmortyk.canekoandthechessking.game.gameinterface.model;

import android.graphics.Canvas;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * You can work as if with one element.
 */
//TODO add elements stack
public class ElementsGroup extends InterfaceElement {

    protected List<InterfaceElement> elements;

    public ElementsGroup() {
        super();
        elements = new LinkedList<>();
    }

    public ElementsGroup(float cx, float cy, float w, float h) {
        super(cx, cy, w, h);
        elements = new LinkedList<>();
    }

    @Override
    public void draw(Canvas canvas) { for (InterfaceElement e: elements) e.draw(canvas); }

    @Override
    public boolean onTouch(float x, float y) {
        for(InterfaceElement e: elements) {
            if(e.contains(x, y) && e.onTouch(x, y)) {  return true; }
        }
        return false;
    }

    @Override
    public void onTouchDown(float x, float y) {
        for(InterfaceElement e: elements) {
            if(e.contains(x, y)) e.onTouchDown(x, y);
        }
    }

    @Override
    public void dispose() { for (InterfaceElement e: elements) e.dispose(); }

    public void addElements(InterfaceElement... es) { Collections.addAll(elements, es); }

    public List<InterfaceElement> getElements() { return elements; }

}
