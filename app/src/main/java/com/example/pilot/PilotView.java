package com.example.pilot;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class PilotView extends View {
    //Declarando el piloto
    private int plainX = 10;
    private int plainY;
    private int plainSpeed;
    private int verdeX, verdeY, verdeSpeed = 10;
    private int score, lifeCoutPlain;
    private Bitmap barril_rojo;
    private Bitmap barril_verde;

    private int rojoX, rojoY, rojoSpeed = 20;

    private int blackX, blackY, blackSpeed = 20;
    private Bitmap cierra;

    private int canvasWidth;
    private int canvasHeight;
    private Boolean touch = false;


    private Bitmap[] plain = new Bitmap[2];
    private Bitmap backGroundImage;
    private Paint scorePaint = new Paint();
    private Bitmap life[] = new Bitmap[2];
    private int frameWidth = 230, frameHeight = 150;
    private int objectWidth = 100, objectHeight = 100;


    public PilotView(Context context) {
        super(context);
        plain[0] = BitmapFactory.decodeResource(getResources(), R.drawable.fly1);
        plain[1] = BitmapFactory.decodeResource(getResources(), R.drawable.fly2);
        plain[0] = Bitmap.createScaledBitmap(plain[0], frameWidth, frameHeight, false);
        plain[1] = Bitmap.createScaledBitmap(plain[1], frameWidth, frameHeight, false);

        backGroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.bgdesert);
        //Dibujando el varril rojo
        barril_rojo = BitmapFactory.decodeResource(getResources(), R.drawable.barril_rojo);
        barril_rojo = Bitmap.createScaledBitmap(barril_rojo, objectWidth, objectHeight, false);

        //Dibujando el varril verde
        barril_verde = BitmapFactory.decodeResource(getResources(), R.drawable.barril_verde);
        barril_verde = Bitmap.createScaledBitmap(barril_verde, objectWidth, objectHeight, false);
        //Dibujando una cierra
        cierra = BitmapFactory.decodeResource(getResources(), R.drawable.cierra);
        cierra = Bitmap.createScaledBitmap(cierra, objectWidth, objectHeight, false);


        scorePaint.setColor(Color.WHITE);
        scorePaint.setTextSize(70);
        scorePaint.setTypeface(Typeface.DEFAULT_BOLD);
        scorePaint.setAntiAlias(true);

        life[0] = BitmapFactory.decodeResource(getResources(), R.drawable.hearts);
        life[1] = BitmapFactory.decodeResource(getResources(), R.drawable.heart_grey);
        plainY = 550;
        score = 0;
        lifeCoutPlain = 3;

    }

    //Dibujando la imagen del piloto
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();


        canvas.drawBitmap(backGroundImage, 0, 0, null);
        int minPlainY = plain[0].getHeight();
        int maxPlainY = canvasHeight - plain[0].getHeight() * 3;
        plainY = plainY + plainSpeed;
        if (plainY < minPlainY) {
            plainY = minPlainY;
        }
        if (plainY > maxPlainY) {
            plainY = maxPlainY;
        }

        plainSpeed = plainSpeed + 2;

        if (touch) {
            canvas.drawBitmap(plain[1], plainX, plainY, null);
            touch = false;
        } else {
            canvas.drawBitmap(plain[0], plainX, plainY, null);

        }
        //Barril Verde (Combustible)
        if (hitBarrilChecker(verdeX, verdeY)) {
            score = score + 10;
            verdeX = -100;
        }


        verdeX = verdeX - verdeSpeed;

        if (verdeX < 0) {
            verdeX = canvasWidth + 21;
            verdeY = (int) Math.floor(Math.random() * (maxPlainY - minPlainY)) + minPlainY;
        } //Barril Verde (Combustible)
        if (hitBarrilChecker(verdeX, verdeY)) {
            score = score + 10;
            verdeX = -100;
        }

        canvas.drawBitmap(barril_verde, verdeX, verdeY, null);

        verdeX = verdeX - verdeSpeed;

        if (verdeX < 0) {
            verdeX = canvasWidth + 21;
            verdeY = (int) Math.floor(Math.random() * (maxPlainY - minPlainY)) + minPlainY;
        }

        //barril Rojo (quita 1 Corazon)
        if (hitBarrilChecker(rojoX, rojoY) || hitBarrilChecker(blackX,blackY)) {
            score = score + 20;
            rojoX = -100;
            blackX = -100;
            lifeCoutPlain--;
            if (lifeCoutPlain == 0) {
                Toast.makeText(getContext(), "Game Over", Toast.LENGTH_SHORT).show();

                Intent gameOverIntent = new Intent(getContext(), GameOverActivity.class);
                gameOverIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                gameOverIntent.putExtra("score", score);
                getContext().startActivity(gameOverIntent);

            }

        }
        canvas.drawBitmap(barril_rojo, rojoX, rojoY, null);

        rojoX = rojoX - rojoSpeed;

        if (rojoX < 0) {
            rojoX = canvasWidth + 20;
            rojoY = (int) Math.floor(Math.random() * (maxPlainY - minPlainY)) + minPlainY;
        }

        canvas.drawBitmap(cierra, blackX, blackY, null);

        blackX = blackX - blackSpeed;

        if (blackX < 0) {
            blackX = canvasWidth + 19;
            blackY = (int) Math.floor(Math.random() * (maxPlainY - minPlainY)) + minPlainY;
        }
        canvas.drawText("Score :" + score, 20, 60, scorePaint);

        for (int i = 0; i < 3; i++) {
            int x = (int) (580 + life[0].getHeight() * 1.5 * i);
            int y = 30;
            if (i < lifeCoutPlain) {
                canvas.drawBitmap(life[0], x, y, null);
            } else {
                canvas.drawBitmap(life[1], x, y, null);

            }

        }
    }

    public Boolean hitBarrilChecker(int x, int y) {
        if (plainX < x && x < (plainX + plain[0].getWidth()) && plainY < y && y < (plainY + plain[0].getHeight())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            touch = true;
            plainSpeed = -22;
        }
        return true;
    }
}

