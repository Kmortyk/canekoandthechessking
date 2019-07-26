package com.kmortyk.canekoandthechessking.gameinterface;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.kmortyk.canekoandthechessking.R;
import com.kmortyk.canekoandthechessking.gameinterface.model.ButtonElement;
import com.kmortyk.canekoandthechessking.gameinterface.model.ElementsGroup;
import com.kmortyk.canekoandthechessking.gameinterface.model.ImageElement;
import com.kmortyk.canekoandthechessking.gameinterface.model.InterfaceElement;
import com.kmortyk.canekoandthechessking.gameinterface.model.Ruler;
import com.kmortyk.canekoandthechessking.gameinterface.model.TextElement;
import com.kmortyk.canekoandthechessking.gameinterface.model.window.WindowElement;
import com.kmortyk.canekoandthechessking.resources.GameResources;
import com.kmortyk.canekoandthechessking.resources.ResourceManager;
import com.kmortyk.canekoandthechessking.thread.GameThread;

import java.util.List;
import java.util.Stack;

public class GameInterface extends ElementsGroup {

    private GameResources gameResources;
    private GameThread gameThread;

    private int width, height;

    private TextElement stepsText;
    private Ruler ruler;

    private WindowElement menuWindow;
    private WindowElement optionsWindow;

    private InventoryWindow inventoryWindow;
    private InventoryPanel inventoryPanel;

    private Stack<List<InterfaceElement>> viewsStack;

    private boolean isWindowOpen = false;

    public GameInterface(GameThread gameThread, int width, int height) {
        this.gameResources = gameThread.getResources();
        this.gameThread = gameThread;
        this.width = width;
        this.height = height;

        viewsStack = new Stack<>();

        Bitmap mbTexture = gameResources.getDrawable(R.drawable.ic_menubutton, 1);
        Bitmap hndTexture = gameResources.getDrawable(R.drawable.ic_hand, 1);
        Bitmap stpTexture = gameResources.getDrawable(R.drawable.ic_steps, 1);

        float padding = mbTexture.getWidth()*0.5f;

        // right
        ButtonElement menuButton = new ButtonElement(width - mbTexture.getWidth(), mbTexture.getHeight(), mbTexture, () -> setWindow(menuWindow));
        menuButton.centering();

        // bottom right
        ButtonElement handButton = new ButtonElement(width - mbTexture.getWidth(), height - mbTexture.getHeight(), hndTexture, this::openInventoryPanel);
        handButton.centering();

        // bottom center
        ruler = new Ruler(width * 0.5f, height - padding, ResourceManager.pxFromDp(3.333f), 5, 2);

        // left
        stepsText = new TextElement("0000", padding, padding*2);
        stepsText.setColor(Color.parseColor("#B1ADAC"));
        stepsText.setAlpha(138);
        stepsText.setFontSize(ResourceManager.pxFromDp(12));
        stepsText.offset(stpTexture.getWidth()*1.25f, -ResourceManager.pxFromDp(2));

        // to hard to get Y of text without bounds.top
        ImageElement stepsImage = new ImageElement(stpTexture, padding, stepsText.bounds.top + ResourceManager.pxFromDp(0.5f));

        createWindows();

        // active elements
        inventoryPanel = new InventoryPanel(gameThread.getGameWorld(), inventoryWindow, width, height);
        inventoryPanel.setVisible(false);

        addElements(menuButton, /*handButton,*/ ruler, stepsImage, stepsText, inventoryPanel);
    }

    /* --- steps --------------------------------------------------------------------------------- */

    private char[] stepsTextArr = {'0', '0', '0', '0'};
    private int curSteps = 0;

    public void increaseSteps() {
        curSteps++;
        if(curSteps > 9999) curSteps = 9999;

        int steps = curSteps; // copy

        for (int i = stepsTextArr.length - 1; i >= 0; i--) {
            stepsTextArr[i] = (char) (steps % 10 + '0'); // int to char array(4)
            steps /= 10;
        }

        stepsText.setText(stepsTextArr, false);
    }

    /* ------------------------------------------------------------------------------------------- */

    public void nextMap() { ruler.nextCur(); }

    private void createWindows() {

        Bitmap backButtonTexture = gameResources.getDrawable(R.drawable.ic_backbutton, 1);

        menuWindow = new WindowElement(gameResources, width*0.5f, height*0.5f, width*0.9f, height*0.9f);
        menuWindow.setOnClose(this::back);

        menuWindow.addButton("Inventory",     () -> setWindow(inventoryWindow));
        menuWindow.addButton("Options",       () -> setWindow(optionsWindow));
        menuWindow.addButton("Return to map", () -> gameThread.returnToMap());

        inventoryWindow = new InventoryWindow(gameThread, menuWindow);
        inventoryWindow.setCloseButtonTexture(backButtonTexture);
        inventoryWindow.setOnClose(this::back);

        optionsWindow = new WindowElement(menuWindow);
        optionsWindow.addButton("This is options!", null);
        optionsWindow.setCloseButtonTexture(backButtonTexture);
        optionsWindow.setOnClose(this::back);

    }

    public void openInventoryPanel() {
        inventoryPanel.swipeUp();
        ruler.setVisible(false);
    }

    public void closeInventoryPanel() {
        ruler.setVisible(true);
        inventoryPanel.swipeDown();
    }

    private void setWindow(WindowElement window) {
        viewsStack.add(elements);
        elements = window.getElements();
        window.onOpen();
        isWindowOpen = true;
    }

    private void back() {

        elements = viewsStack.pop(); // change cur view to previous

        if(viewsStack.size() == 0) { // allow to tap on the game world
            isWindowOpen = false;
        }

    }

    public boolean isActive() { return isWindowOpen; }

    public float getPanelOpenTopY() { return inventoryPanel.topY() + inventoryPanel.height()*0.5f; }

    public float getPanelCloseTopY() { return inventoryPanel.topY() - inventoryPanel.height()*1.5f; }

}
