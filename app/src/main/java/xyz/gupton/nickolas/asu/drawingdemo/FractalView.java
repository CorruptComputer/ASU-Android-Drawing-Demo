package xyz.gupton.nickolas.asu.drawingdemo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * A View that draws substitution fractals, one per swipe.
 * @author Rob LeGrand
 */
class FractalView extends View implements View.OnTouchListener {


    /**
     * Is true when the user is currently swiping for a new fractal.
     */
    private boolean isMoving;

    /**
     * The x coordinate of the starting point of the fractal.
     */
    private float fromX;

    /**
     * The y coordinate of the starting point of the fractal.
     */
    private float fromY;

    /**
     * The x coordinate of the ending point of the fractal.
     */
    private float toX;

    /**
     * The y coordinate of the ending point of the fractal.
     */
    private float toY;

    /**
     * The recursion depth for drawing the fractal.
     */
    private int depth;

    /**
     * The Paint object needed to draw on the Canvas.
     */
    private Paint paint;

    /**
     * The TextView object needed to update the instructions with the fractal depth.
     */
    private TextView instructionsTextView;

    /**
     * Sets up a new FractalView.
     * @param context   The Context (probably an Activity) the FractalView is running in.
     */
    public FractalView(Context context) {
        // Call the constructor of the View class.
        super(context);

        // Make it so that the onTouch method gets called when the FractalView is touched.
        setOnTouchListener(this);

        // Make sure nothing will be drawn until the first swipe.
        isMoving = false;
        depth = 0;

        // Create a new paintbrush to use to draw to the Canvas.
        paint = new Paint();
        // Make the lines wider.
        paint.setStrokeWidth(3.0f);

        // Find the instructions TextView and save it to use later.
        try {
            Activity activity = (Activity) context;
            instructionsTextView = activity.findViewById(R.id.instructionsTextView);
        } catch (ClassCastException ex) {
            // context must not have been an Activity object, so we can't use instructionsTextView.
        } catch (NullPointerException ex) {
            // context must have been a null reference, so we can't use instructionsTextView.
        }
    }

    /**
     * Recursively draw a substitution fractal on the given Canvas.
     * @param canvas   The Canvas to draw on.
     * @param fromX    The x coordinate of the point to draw from.
     * @param fromY    The y coordinate of the point to draw from.
     * @param toX      The x coordinate of the point to draw to.
     * @param toY      The y coordinate of the point to draw to.
     * @param depth    The depth of the recursion tree to use.
     */
    private void drawFractal(Canvas canvas, float fromX, float fromY, float toX, float toY, int depth) {
        // The lines array determines how each line is turned into several lines.
        // These values will simulate the regular paperfolding sequence:
        final float[][][] lines = {
                {{0.00f, 0.00f}, {0.30f, 0.60f}},
                {{0.25f, 0.25f}, {0.47f, 0.75f}},
                {{0.75f, 0.40f}, {0.30f, 0.60f}},
                {{0.47f, 0.75f}, {0.75f, 0.75f}}
        };
        if (depth <= 0) {
            // We've recursed enough, so just draw a line.
            canvas.drawLine(fromX, fromY, toX, toY, paint);
        } else {
            // We need to recurse some more.  Turn this line into several.
            float cosDistance = (toX - fromX + toY - fromY) / 2.0f;
            float sinDistance = (fromX - toX + toY - fromY) / 2.0f;
            int whichLine;
            for (whichLine = 0; whichLine < lines.length; whichLine += 1) {
                drawFractal(
                        canvas,
                        fromX + lines[whichLine][0][0] * cosDistance - lines[whichLine][0][1] * sinDistance,
                        fromY + lines[whichLine][0][0] * sinDistance + lines[whichLine][0][1] * cosDistance,
                        fromX + lines[whichLine][1][0] * cosDistance - lines[whichLine][1][1] * sinDistance,
                        fromY + lines[whichLine][1][0] * sinDistance + lines[whichLine][1][1] * cosDistance,
                        depth - 1
                );
            }
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (isMoving) {
            // Just draw a line to indicate the current selection.
            paint.setColor(Color.BLUE);
            canvas.drawLine(fromX, fromY, toX, toY, paint);
        } else if (depth > 0) {
            // Draw a fractal at the selected location and report the current depth.
            paint.setColor(Color.BLACK);
            drawFractal(canvas, fromX, fromY, toX, toY, depth);
            try {
                instructionsTextView.setText("Fractal depth: " + depth);
            } catch (NullPointerException ex) {
                // We couldn't get instructionsTextView, so we can't report the depth.
            }
        }
    }
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        // React to the touch event: down, move, cancel or up.
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Set the original point.
                isMoving = true;
                fromX = event.getX();
                fromY = event.getY();

                if (depth > 17) {
                    depth = 0;
                }

                break;
            case MotionEvent.ACTION_MOVE:
                // Set the terminal point and show the user the current line.
                toX = event.getX();
                toY = event.getY();
                invalidate(); // Force the Canvas to redraw.
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                // Set the terminal point and draw the fractal.
                isMoving = false;
                toX = event.getX();
                toY = event.getY();
                depth += 1; // Increase the recursion depth after each swipe.
                invalidate(); // Force the Canvas to redraw.
                break;
        }
        return true; // Indicate that the touch event has been handled.
    }
}
