package com.kmortyk.canekoandthechessking.game.gameinterface;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.kmortyk.canekoandthechessking.R;
import com.kmortyk.canekoandthechessking.game.gameinterface.model.Backing;
import com.kmortyk.canekoandthechessking.game.gameinterface.model.ButtonElement;
import com.kmortyk.canekoandthechessking.game.gameinterface.model.ElementsGroup;
import com.kmortyk.canekoandthechessking.game.gameinterface.model.ImageElement;
import com.kmortyk.canekoandthechessking.game.gameinterface.model.InterfaceElement;
import com.kmortyk.canekoandthechessking.game.gameinterface.model.Ruler;
import com.kmortyk.canekoandthechessking.game.gameinterface.model.TextElement;
import com.kmortyk.canekoandthechessking.game.gameinterface.model.window.WindowElement;
import com.kmortyk.canekoandthechessking.resources.GameResources;
import com.kmortyk.canekoandthechessking.resources.ResourceManager;
import com.kmortyk.canekoandthechessking.thread.GameThread;

import java.util.List;
import java.util.Locale;
import java.util.Stack;

public class GameInterface extends ElementsGroup {

    private GameResources gameResources;
    private GameThread gameThread;

    private int width, height;

    private ImageElement stepsImage;
    private TextElement stepsText;
    private Ruler ruler;

    private WindowElement menuWindow;
    private WindowElement inventoryWindow;
    private WindowElement optionsWindow;

    private Stack<List<InterfaceElement>> viewsStack;

    public GameInterface(GameThread gameThread, int width, int height) {
        this.gameResources = gameThread.getResources();
        this.gameThread = gameThread;
        this.width = width;
        this.height = height;

        viewsStack = new Stack<>();

        Bitmap mbTexture = gameResources.getDrawable(R.drawable.menubutton, 1);

        // right
        ButtonElement menuButton = new ButtonElement(width - mbTexture.getWidth(),
                                                          mbTexture.getHeight(),
                                                          mbTexture,
                                                          () -> setWindow(menuWindow));
        menuButton.centering();

        float padding = mbTexture.getWidth()*0.5f;

        // bottom
        float radius = 5f;
        ruler = new Ruler(width/2, height - padding, radius, 5, 2);

        // left
        stepsText = new TextElement(/* 0 + */"0000", padding, 2f*padding);
        stepsText.setColor(Color.parseColor("#B1ADAC"));
        stepsText.setAlpha(138);
        stepsText.setFontSize(ResourceManager.getInstance().pxFromDp(12));
        stepsText.offset(0, -ResourceManager.getInstance().pxFromDp(2));

        stepsImage = new ImageElement(gameResources.getDrawable(R.drawable.steps, 1),
                                      stepsText.bounds.left,
                                      stepsText.bounds.top);
        stepsImage.offset(0, ResourceManager.getInstance().pxFromDp(0.5f));

        stepsText.offset(stepsImage.width()*1.25f,0);

        addElements(menuButton, ruler, stepsImage, stepsText);

        createWindows();
    }

    private int curSteps = 0;

    public void increaseSteps() {
        curSteps++;
        stepsText.setText(String.format(Locale.ENGLISH, "%04d", curSteps), false);
    }

    public void nextMap() { ruler.nextCur(); }

    private void createWindows() {
        menuWindow = new WindowElement(gameResources, width/2, height/2, width*0.9f, height*0.9f);
        menuWindow.setOnClose(this::back);

        menuWindow.addButton("Inventory", () -> setWindow(inventoryWindow));
        menuWindow.addButton("Options", () -> setWindow(optionsWindow));
        menuWindow.addButton("Return to map", () -> gameThread.returnToMap());

        Bitmap backButtonTexture = gameResources.getDrawable(R.drawable.backbutton, 1);

        inventoryWindow = new InventoryWindow(gameThread, menuWindow);
        inventoryWindow.setCloseButtonTexture(backButtonTexture);
        inventoryWindow.setOnClose(this::back);

        optionsWindow = new WindowElement(menuWindow);
        optionsWindow.addButton("This is options!", null);
        optionsWindow.setCloseButtonTexture(backButtonTexture);
        optionsWindow.setOnClose(this::back);
    }

    private void setWindow(WindowElement window) {
        viewsStack.add(elements);
        elements = window.getElements();
    }

    private void back() { elements = viewsStack.pop(); }

}
