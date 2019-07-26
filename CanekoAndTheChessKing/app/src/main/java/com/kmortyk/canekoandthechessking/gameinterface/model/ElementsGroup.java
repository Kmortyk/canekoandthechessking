package com.kmortyk.canekoandthechessking.gameinterface.model;

import android.graphics.Canvas;
import android.widget.ListView;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * You can work as if with one element.
 */
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
    public void onDraw(Canvas canvas) {
        for (InterfaceElement e: elements) { e.draw(canvas); }
    }

    @Override
    public boolean onTouch(float x, float y) {

        for (int i = elements.size() - 1; i >= 0; i--) {
            InterfaceElement e = elements.get(i);
            if(e.contains(x, y) && e.onTouch(x, y)) { return true; }
        }

        return false;

    }

    @Override
    public void onTouchDown(float x, float y) {

        for (int i = elements.size() - 1; i >= 0; i--) {
            InterfaceElement e = elements.get(i);
            if(e.contains(x, y)) { e.onTouchDown(x, y); }
        }

    }

    public void addElements(InterfaceElement ... es) { Collections.addAll(elements, es); }

    public List<InterfaceElement> getElements() { return elements; }

    @Override
    public void offset(float dx, float dy) {
        super.offset(dx, dy);
        for(InterfaceElement e: elements) {
            e.offset(dx, dy);
        }
    }

    @Override
    public void offsetTo(float newX, float newY) {
        float dx = newX - bounds.left;
        float dy = newY - bounds.top;
        offset(dx, dy);
    }

}
