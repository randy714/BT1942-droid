package com.b1942.engine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class TouchControlsView extends View {

    private Paint pBase, pStick, pBtn, pFire, pTxt, pBdr;
    private float jx, jy, jr, sx, sy, sr;
    private boolean jAct = false;
    private int jPid = -1;
    private final float[][] B = new float[6][3];
    private final String[] BL = {"FIRE", "JUMP", "CRO", "RLD", "WPN", "MAP"};
    private final boolean[] BP = new boolean[6];
    private Vibrator vib;

    public TouchControlsView(Context c) {
        super(c);
        init(c);
    }

    public TouchControlsView(Context c, AttributeSet a) {
        super(c, a);
        init(c);
    }

    public TouchControlsView(Context c, AttributeSet a, int d) {
        super(c, a, d);
        init(c);
    }

    private void init(Context c) {
        vib = (Vibrator) c.getSystemService(Context.VIBRATOR_SERVICE);

        pBase = new Paint(Paint.ANTI_ALIAS_FLAG);
        pBase.setColor(Color.argb(80, 74, 122, 46));

        pStick = new Paint(Paint.ANTI_ALIAS_FLAG);
        pStick.setColor(Color.argb(180, 212, 160, 23));

        pBtn = new Paint(Paint.ANTI_ALIAS_FLAG);
        pBtn.setColor(Color.argb(100, 45, 74, 28));

        pFire = new Paint(Paint.ANTI_ALIAS_FLAG);
        pFire.setColor(Color.argb(140, 139, 26, 26));

        pTxt = new Paint(Paint.ANTI_ALIAS_FLAG);
        pTxt.setColor(0xDCE8E4D8);
        pTxt.setTextAlign(Paint.Align.CENTER);
        pTxt.setFakeBoldText(true);

        pBdr = new Paint(Paint.ANTI_ALIAS_FLAG);
        pBdr.setColor(0xA0D4A017);
        pBdr.setStyle(Paint.Style.STROKE);
        pBdr.setStrokeWidth(3);
    }

    @Override
    protected void onSizeChanged(int w, int h, int ow, int oh) {
        float s = Math.min(w, h) / 800f;

        jr = 80 * s;
        jx = 120 * s;
        jy = h - 140 * s;
        sr = 35 * s;
        sx = jx;
        sy = jy;

        float br = 36 * s;
        float rx = w - 60 * s;
        float by = h - 100 * s;

        setBtn(0, rx - 80 * s, by, 55 * s);
        setBtn(1, rx - 160 * s, by - 70 * s, br);
        setBtn(2, rx - 80 * s, by - 130 * s, br);
        setBtn(3, rx - 10 * s, by - 70 * s, br);
        setBtn(4, rx - 160 * s, by - 140 * s, br * 0.8f);
        setBtn(5, 80 * s, 60 * s, br * 0.7f);

        pTxt.setTextSize(22 * s);
    }

    private void setBtn(int i, float x, float y, float r) {
        B[i][0] = x;
        B[i][1] = y;
        B[i][2] = r;
    }

    private void drawOct(Canvas c, float cx, float cy, float r, Paint fill) {
        Path p = new Path();
        for (int i = 0; i < 8; i++) {
            double a = Math.PI * 2 * i / 8 - Math.PI / 8;
            float px = cx + r * (float) Math.cos(a);
            float py = cy + r * (float) Math.sin(a);
            if (i == 0) p.moveTo(px, py);
            else p.lineTo(px, py);
        }
        p.close();
        c.drawPath(p, fill);
        c.drawPath(p, pBdr);
    }

    @Override
    protected void onDraw(Canvas c) {
        c.drawCircle(jx, jy, jr, pBase);
        c.drawCircle(jx, jy, jr, pBdr);
        c.drawCircle(sx, sy, sr, pStick);

        for (int i = 0; i < 6; i++) {
            Paint f = BP[i] ? pStick : (i == 0 ? pFire : pBtn);
            drawOct(c, B[i][0], B[i][1], B[i][2], f);
            c.drawText(BL[i], B[i][0], B[i][1] + 8, pTxt);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        int a = e.getActionMasked();
        int i = e.getActionIndex();
        int pid = e.getPointerId(i);
        float x = e.getX(i);
        float y = e.getY(i);

        if (a == MotionEvent.ACTION_DOWN || a == MotionEvent.ACTION_POINTER_DOWN) {
            if (dist(x, y, jx, jy) < jr * 1.5f) {
                jAct = true;
                jPid = pid;
                moveStick(x, y);
                doVib();
            } else {
                for (int k = 0; k < 6; k++) {
                    if (dist(x, y, B[k][0], B[k][1]) < B[k][2] * 1.3f) {
                        BP[k] = true;
                        doVib();
                    }
                }
            }
        } else if (a == MotionEvent.ACTION_MOVE) {
            for (int k = 0; k < e.getPointerCount(); k++) {
                if (e.getPointerId(k) == jPid && jAct) {
                    moveStick(e.getX(k), e.getY(k));
                }
            }
        } else if (a == MotionEvent.ACTION_UP || a == MotionEvent.ACTION_POINTER_UP) {
            if (pid == jPid) {
                jAct = false;
                jPid = -1;
                sx = jx;
                sy = jy;
            }
            for (int k = 0; k < 6; k++) BP[k] = false;
        }

        invalidate();
        return true;
    }

    private void moveStick(float x, float y) {
        float dx = x - jx;
        float dy = y - jy;
        float d = (float) Math.sqrt(dx * dx + dy * dy);
        if (d > jr) {
            dx = dx / d * jr;
            dy = dy / d * jr;
        }
        sx = jx + dx;
        sy = jy + dy;
    }

    private float dist(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    private void doVib() {
        if (vib == null) return;
        if (Build.VERSION.SDK_INT >= 26) {
            vib.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vib.vibrate(30);
        }
    }
                                 }
