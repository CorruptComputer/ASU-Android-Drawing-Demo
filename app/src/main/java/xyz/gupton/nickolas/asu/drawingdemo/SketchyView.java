package xyz.gupton.nickolas.asu.drawingdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

/**
 * A View that illustrates some simple drawing using a Canvas.
 * @author Rob LeGrand
 */
class SketchyView extends View implements View.OnTouchListener {


    /**
     * Becomes true once the SketchyView object has been touched,
     * which keeps the A and B points from being reinitialized.
     */
    private boolean hasBeenTouched;

    /**
     * The x coordinate of point A.
     */
    private float pointAX;

    /**
     * The y coordinate of point A.
     */
    private float pointAY;

    /**
     * The x coordinate of point B.
     */
    private float pointBX;

    /**
     * The y coordinate of point B.
     */
    private float pointBY;

    private float[] pointC = new float[2];

    /**
     * The Paint object needed to draw on the Canvas.
     */
    private Paint paint;

    /**
     * Sets up a new SketchyView.
     * @param context   The Context (probably an Activity) the SketchyView is running in.
     */
    public SketchyView(Context context) {
        // Call the constructor of the View class.
        super(context);

        // Make it so that the onTouch method gets called when the SketchyView is touched.
        setOnTouchListener(this);

        // We'll set the beginning point locations only before the SketchyView is touched.
        hasBeenTouched = false;

        // Create a new paintbrush to use to draw to the Canvas.
        paint = new Paint();
        // Make the text size bigger.
        paint.setTextSize(40.0f);
        // Make the lines wider.
        paint.setStrokeWidth(5.0f);
    }

    @Override
    public void onDraw(Canvas canvas) {
        // Paint the background a very light blue.
        canvas.drawColor(Color.rgb(235, 245, 255));

        if (!hasBeenTouched) {
            // Set the beginning point locations.
            pointAX = getWidth() / 3.0f;
            pointBX = 2.0f * getWidth() / 3.0f;
            pointAY = 2.0f * getHeight() / 3.0f;
            pointBY = getHeight() / 3.0f;
        }

        // Draw lines among the points and corners of the Canvas.
        paint.setColor(Color.MAGENTA);
        canvas.drawLine(0.0f, getHeight(), pointAX, pointAY, paint);
        canvas.drawLine(pointAX, pointAY, pointBX, pointBY, paint);
        canvas.drawLine(pointBX, pointBY, getWidth(), 0.0f, paint);

        // Draw the points and label them.
        paint.setColor(Color.rgb(0, 170, 170));
        canvas.drawCircle(pointAX, pointAY, 27.0f, paint);
        paint.setColor(Color.rgb(170, 0, 170));
        canvas.drawRect(pointBX - 24.0f, pointBY - 24.0f, pointBX + 24.0f, pointBY + 24.0f, paint);
        paint.setColor(Color.WHITE);
        canvas.drawText("A", pointAX - 13.0f, pointAY + 14.0f, paint);
        canvas.drawText("B", pointBX - 13.0f, pointBY + 14.0f, paint);

        pointC[0] = (pointAX + pointBX)/2;
        pointC[1] = (pointAY + pointBY)/2;
        paint.setColor(Color.rgb(170, 25, 25));
        canvas.drawOval(pointC[0]-20f, pointC[1]-30f,pointC[0]+20f, pointC[1]+30f, paint);
        paint.setColor(Color.WHITE);
        canvas.drawText("C", pointC[0] - 13.0f, pointC[1] + 14.0f, paint);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        // Remember that we won't need to reset the beginning point locations now.
        hasBeenTouched = true;
        // React to the touch event: down, move, cancel or up.
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Set new point A and force the Canvas to redraw.
                pointAX = event.getX();
                pointAY = event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                // Do nothing.
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                // Set new point B and force the Canvas to redraw.
                pointBX = event.getX();
                pointBY = event.getY();
                invalidate();
                break;
        }
        return true; // Indicate that the touch event has been handled.
    }
}
