package com.kmortyk.canekoandthechessking.menu;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

public class MenuButton {

    private TextView textView;

    public MenuButton(final TextView textView, final Runnable onTouch) {
        this.textView = textView;

        final AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.1f);
        fadeOut.setDuration(200);
        fadeOut.setFillAfter(true);

        textView.setOnClickListener(v -> {
            fadeOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) { }

                @Override
                public void onAnimationEnd(Animation animation) { onTouch.run(); }

                @Override
                public void onAnimationRepeat(Animation animation) { }
            });
            textView.startAnimation(fadeOut);
        });

    }

    public void fadeIn() {
        AlphaAnimation fadeIn = new AlphaAnimation(1f, 1f);
        fadeIn.setDuration(10);
        fadeIn.setFillAfter(true);
        textView.startAnimation(fadeIn);
    }

}
