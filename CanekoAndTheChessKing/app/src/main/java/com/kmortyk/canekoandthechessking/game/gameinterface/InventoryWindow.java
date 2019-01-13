package com.kmortyk.canekoandthechessking.game.gameinterface;

import android.content.res.Configuration;
import android.graphics.RectF;

import com.kmortyk.canekoandthechessking.game.gameinterface.model.InventoryCell;
import com.kmortyk.canekoandthechessking.game.gameinterface.model.TextElement;
import com.kmortyk.canekoandthechessking.game.gameinterface.model.window.WindowElement;

import com.kmortyk.canekoandthechessking.game.item.Inventory;
import com.kmortyk.canekoandthechessking.resources.ResourceManager;
import com.kmortyk.canekoandthechessking.thread.GameThread;

public class InventoryWindow extends WindowElement {

    public float cellPaddingX = ResourceManager.getInstance().pxFromDp(10);
    public float cellPaddingY = ResourceManager.getInstance().pxFromDp(10);
    public float cellWidth = ResourceManager.getInstance().pxFromDp(60);
    public float cellHeight = ResourceManager.getInstance().pxFromDp(60);

    private RectF cellsBounds;
    private int cols;
    private int rows;

    private Inventory inventory;

    public InventoryWindow(GameThread gameThread, WindowElement window) { this(gameThread, window.bounds.centerX(), window.bounds.centerY(), window.width(), window.height()); }

    public InventoryWindow(GameThread gameThread, float cx, float cy, float w, float h) {
        super(gameThread.getResources(), cx, cy, w, h);

        TextElement textElement = new TextElement("Inventory", cx, bounds.top);
        textElement.offset(-textElement.width()*0.5f, textElement.height());

        if(gameThread.getOrientation() == Configuration.ORIENTATION_LANDSCAPE) {
            cols = 5;
            rows = 4;
            cellPaddingX *= 0.75; // TODO not always look at different devices
            cellPaddingY *= 0.75;
            cellWidth *= 0.75;
            cellHeight *= 0.75;
        }else{
            cols = 4;
            rows = 5;
        }

        cellsBounds = new RectF(
                     0,
                     0,
                    (cellHeight + cellPaddingX)*cols + cellPaddingX,
                  (cellWidth + cellPaddingY)*rows + cellPaddingY
        );
        cellsBounds.offsetTo(cx, cy);
        cellsBounds.offset(-cellsBounds.width()*0.5f, -cellsBounds.height()*0.5f);
        cellsBounds.offset(0, textElement.height());

        for (float x = cellsBounds.left + cellPaddingX; x < cellsBounds.right; x += cellWidth + cellPaddingX) {
            for (float y = cellsBounds.top + cellPaddingY; y < cellsBounds.bottom; y += cellHeight + cellPaddingY) {
               addElements(new InventoryCell(x, y, cellWidth, cellHeight));
            }
        }

        addElements(textElement);
    }

    public void setInventory(Inventory inventory) { this.inventory = inventory; }

}
