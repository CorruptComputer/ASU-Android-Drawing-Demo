package xyz.gupton.nickolas.asu.drawingdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

/**
 * Draws a Bezier curve based off of 4 points.
 * @author Nickolas Gupton
 */
class BezierView extends View implements View.OnTouchListener {

    /**
     * points | float[][]: points[point number][0 for X, 1 for Y].
     */
    private float[][] points = {
            {0f, 0f},
            {0f, 0f},
            {0f, 0f},
            {0f, 0f}
    };

    /**
     * selectedPoint | int: Currently selected point, if none set to -1.
     */
    private int selectedPoint = -1;

    /**
     * hasBeenTouched | boolean: Checks if the screen has been touched yet.
     */
    private boolean hasBeenTouched = false;

    /**
     * The Paint object needed to draw on the Canvas.
     */
    private Paint paint;
    private Path path;

    /**
     * Sets up a new PointsView.
     * @param context   The Context (probably an Activity) the PointsView is running in.
     */
    public BezierView(Context context) {
        // Call the constructor of the View class.
        super(context);

        // Make it so that the onTouch method gets called when the SketchyView is touched.
        setOnTouchListener(this);

        // Create a new paintbrush to use to draw to the Canvas.
        paint = new Paint();
        path = new Path();

        paint.setStrokeWidth(5f);
        paint.setTextSize(40.0f);
    }

    @Override
    public void onDraw(Canvas canvas) {
        // Paint the background a very light blue.
        canvas.drawColor(Color.WHITE);

        if (!hasBeenTouched) {
            // Set the beginning point locations.
            points[0][0] = getWidth() / 3.0f;
            points[0][1] = getHeight() / 3.0f;
            points[1][0] = 2 * getWidth() / 3.0f;
            points[1][1] = getHeight() / 3.0f;
            points[2][0] = 2 * getWidth() / 3.0f;
            points[2][1] = 2 * getHeight() / 3.0f;
            points[3][0] = getWidth() / 3.0f;
            points[3][1] = 2 * getHeight() / 3.0f;
        }

        // Draw lines among the points
        paint.setColor(Color.GRAY);
        canvas.drawLine(points[0][0], points[0][1], points[1][0], points[1][1], paint);
        canvas.drawLine(points[1][0], points[1][1], points[2][0], points[2][1], paint);
        canvas.drawLine(points[2][0], points[2][1], points[3][0], points[3][1], paint);

        path.reset();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        path.moveTo(points[0][0], points[0][1]);
        path.cubicTo(points[1][0], points[1][1], points[2][0], points[2][1], points[3][0], points[3][1]);
        canvas.drawPath(path, paint);
        paint.setStyle(Paint.Style.FILL);

        paint.setColor(Color.RED);
        canvas.drawCircle(points[0][0], points[0][1], 25, paint);
        canvas.drawCircle(points[1][0], points[1][1], 25, paint);
        canvas.drawCircle(points[2][0], points[2][1], 25, paint);
        canvas.drawCircle(points[3][0], points[3][1], 25, paint);

        if (selectedPoint != -1) {
            paint.setColor(Color.GREEN);
            canvas.drawCircle(points[selectedPoint][0], points[selectedPoint][1], 25, paint);
        }


        paint.setColor(Color.WHITE);
        canvas.drawText("0", points[0][0]-13f, points[0][1]+14f, paint);
        canvas.drawText("1", points[1][0]-13f, points[1][1]+14f, paint);
        canvas.drawText("2", points[2][0]-13f, points[2][1]+14f, paint);
        canvas.drawText("3", points[3][0]-13f, points[3][1]+14f, paint);

    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        hasBeenTouched = true;
        // React to the touch event: down, move, cancel or up.
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float[] thisTouch = new float[]{event.getX(), event.getY()};
                float closeDist = getDistance(points[0], thisTouch);
                int indexOfClosest = 0;
                for (int i = 1; i < points.length; i++) {
                    float currentDist = getDistance(points[i], thisTouch);
                    if (currentDist < closeDist) {
                        closeDist = currentDist;
                        indexOfClosest = i;
                    }
                }

                if (closeDist <= 50f) {
                    selectedPoint = indexOfClosest;
                }

            case MotionEvent.ACTION_MOVE:
                if (selectedPoint != -1) {
                    points[selectedPoint] = new float[]{event.getX(), event.getY()};
                    invalidate();
                }

                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                selectedPoint = -1;
                invalidate();
                break;
        }
        return true; // Indicate that the touch event has been handled.
    }

    private static float getDistance(float[] pointA, float[] pointB) {
        return (float)Math.sqrt(Math.pow(pointA[0] - pointB[0], 2) + Math.pow(pointA[1] - pointB[1], 2));
    }

}
