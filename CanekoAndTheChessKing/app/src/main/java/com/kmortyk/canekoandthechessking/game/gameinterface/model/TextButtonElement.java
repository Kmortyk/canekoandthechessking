package com.kmortyk.canekoandthechessking.game.gameinterface.model;

import android.graphics.RectF;
import android.util.Log;

import com.kmortyk.canekoandthechessking.resources.ResourceManager;

public class TextButtonElement extends ElementsGroup{

    private TextElement textElement;
    private Backing     backing;
    private Runnable    onTouch;

    private final RectF saveBounds;

    public TextButtonElement(String text, float left, float top) {
        textElement = new TextElement(text, left, top);
        textElement.setFontSize(23*ResourceManager.getInstance().density());

        backing = new Backing(textElement.bounds);
        backing.scale(1.1f, 1.5f);
        backing.setFlags(true, true);

        bounds = backing.bounds;
        saveBounds = new RectF(bounds);

        addElements(backing, textElement);

    }

    public void setOnTouch(Runnable onTouch) { this.onTouch = onTouch; }

    @Override
    public void centering() {
        //backing.centering();
        textElement.centering();
        backing.offsetTo(textElement.bounds.centerX() - backing.width()*0.5f,
                         textElement.bounds.centerY() - backing.height()*0.4f);
        saveBounds();
    }

    @Override
    public void onTouchDown(float x, float y) { backing.scale(0.95f, 0.95f); }

    @Override
    public boolean onTouch(float x, float y) {
        restoreSize();
        if(onTouch == null) { return false; }
        onTouch.run();
        return true;
    }

    private void restoreSize() { bounds.set(saveBounds); }

    public void saveBounds() { saveBounds.set(bounds); }

    @Override
    public float height() { return backing.height(); }

    @Override
    public float width() { return backing.width(); }

}
