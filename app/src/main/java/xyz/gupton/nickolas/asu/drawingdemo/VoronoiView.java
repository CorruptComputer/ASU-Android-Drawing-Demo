package xyz.gupton.nickolas.asu.drawingdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;
import java.util.Vector;

/**
 * Draws a Voronoi diagram based off of where the user taps.
 * @author Nickolas Gupton
 */
class VoronoiView extends View implements View.OnTouchListener {

    /**
     * points | Vector<float[]>: A vector which contains all of the points which have been clicked.
     */
    private Vector<float[]> points = new Vector<>();

    /**
     * colors | Vector<Integer>: A vector which contains all of the colors for the points.
     */
    private Vector<Integer> colors = new Vector<>();

    /**
     * rdm | Random: A random number generator.
     */
    private Random rdm = new Random();

    /**
     * The Paint object needed to draw on the Canvas.
     */
    private Paint paint;

    /**
     * Sets up a new PointsView.
     * @param context   The Context (probably an Activity) the PointsView is running in.
     */
    public VoronoiView(Context context) {
        // Call the constructor of the View class.
        super(context);

        // Make it so that the onTouch method gets called when the SketchyView is touched.
        setOnTouchListener(this);

        // Create a new paintbrush to use to draw to the Canvas.
        paint = new Paint();
    }

    @Override
    public void onDraw(Canvas canvas) {
        // Paint the background a very light blue.
        canvas.drawColor(Color.BLACK);

        if (points.size() > 0) {
            for (int x = 0; x < getWidth(); x+=3) {
                for (int y = 0; y < getHeight(); y+=3) {
                    int closest = 0;
                    float closestDist = ((x-points.elementAt(0)[0]) * (x-points.elementAt(0)[0]))
                            + ((y-points.elementAt(0)[1]) * (y-points.elementAt(0)[1]));

                    for (int i = 1; i < points.size(); i++) {
                        float dist = ((x-points.elementAt(i)[0]) * (x-points.elementAt(i)[0]))
                                + ((y-points.elementAt(i)[1]) * (y-points.elementAt(i)[1]));
                        if (closestDist > dist) {
                            closest = i;
                            closestDist = dist;
                        }
                    }

                    paint.setColor(colors.elementAt(closest));
                    canvas.drawRect(x, y, x+3, y+3, paint);
                }
            }
        }


        for (float[] point : points) {
            paint.setColor(Color.BLACK);
            canvas.drawCircle(point[0], point[1], 14f, paint);
            paint.setColor(Color.WHITE);
            canvas.drawCircle(point[0], point[1], 7f, paint);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        // React to the touch event: down, move, cancel or up.
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                // Do nothing.
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                points.add(new float[]{event.getX(), event.getY()});
                colors.add(Color.rgb(rdm.nextInt(255), rdm.nextInt(255), rdm.nextInt(255)));

                invalidate();
                break;
        }
        return true; // Indicate that the touch event has been handled.
    }
}
