package com.kmortyk.canekoandthechessking.menu;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

public class MenuButton {

    private TextView textView;

    public MenuButton(final TextView textView, final MenuClickable menuClickable) {
        this.textView = textView;

        final AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.1f);
        fadeOut.setDuration(200);
        fadeOut.setFillAfter(true);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fadeOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) { }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        menuClickable.onAnimationEnd();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });
                textView.startAnimation(fadeOut);

            }
        });

    }

    public void fadeIn() {
        final AlphaAnimation fadeIn = new AlphaAnimation(1f, 1f);
        fadeIn.setDuration(10);
        fadeIn.setFillAfter(true);
        textView.startAnimation(fadeIn);
    }

}
