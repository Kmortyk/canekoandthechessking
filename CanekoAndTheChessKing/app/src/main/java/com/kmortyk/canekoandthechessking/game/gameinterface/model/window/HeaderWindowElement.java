package com.kmortyk.canekoandthechessking.game.gameinterface.model.window;

import android.graphics.Color;
import android.util.Log;

import com.kmortyk.canekoandthechessking.R;
import com.kmortyk.canekoandthechessking.game.gameinterface.model.Backing;
import com.kmortyk.canekoandthechessking.game.gameinterface.model.ButtonElement;
import com.kmortyk.canekoandthechessking.game.gameinterface.model.ElementsGroup;
import com.kmortyk.canekoandthechessking.game.gameinterface.model.TextElement;
import com.kmortyk.canekoandthechessking.resources.GameResources;

public class HeaderWindowElement extends ElementsGroup {

    private ButtonElement closeButton;
    private TextElement headerText;

    private Backing backing;
    private Backing header;

    public HeaderWindowElement(GameResources gameResources, float cx, float cy, float w, float h) {
        super(cx, cy, w, h);
        centering();

        backing = new Backing(bounds.left, bounds.top, w, h);

        header = new Backing(bounds.left, bounds.top, w,h*0.1f);
        header.setAlpha(255);
        header.setColor(Color.BLUE);
        header.setFlags(true, false);

        Log.d("Simple_window", "created");

        headerText = new TextElement("Simple window", header.bounds.left, header.bounds.centerY());
        headerText.setPadding(10f, 5f);

        closeButton = new ButtonElement(header.bounds.right, header.bounds.centerY(),
                                        gameResources.getDrawable(R.drawable.exitbutton, 1), null );
        closeButton.centering();
        closeButton.bounds.offset(-closeButton.bounds.width(), 0);

        addElements(backing, header, headerText, closeButton);
    }

    public void setHeaderText(String text) { headerText.setText(text); }

    public void setOnClose(Runnable onTouch) { closeButton.setOnTouch(onTouch); }

}
