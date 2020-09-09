package xyz.gupton.nickolas.asu.drawingdemo;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * An Activity that allows the user to choose among different drawing modes.
 * It listens for changes to the Spinner to change the mode.
 * @author Rob LeGrand
 */
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // Constants that indicate drawing mode.
    private static final int MODE_SKETCHY = 0;
    private static final int MODE_FRACTAL = 1;
    private static final int MODE_POINTS = 2;
    private static final int MODE_AVERAGING = 3;
    private static final int MODE_GEOMETRY = 4;
    private static final int MODE_BEZIER = 5;
    private static final int MODE_VORONOI = 6;

    /**
     * Instructions specific to each mode to be put in a TextView.
     */
    private final String[] instructionsStrings = new String[] {
            "Swipe the screen to make the points move.",
            "Swipe the screen to draw a pretty fractal.",
            "Tap the screen to plot a point.",
            "Tap the screen to register a point to include in the average",
            "Move the green point around to make shapes.",
            "Drag the points around to change the Bezier curve.",
            "Tap to add points to the Voronoi diagram."
    };

    /**
     * Keeps track of the last View added so it can be found when it needs to be replaced.
     */
    private int indexOfAddedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the Spinner that selects the mode.
        Spinner spinner = findViewById(R.id.modeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.mode_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        // Add a generic new View and then replace it with a SketchyView.
        LinearLayout layout = findViewById(R.id.artsyLayout);
        View newView = new View(this);
        layout.addView(newView);
        indexOfAddedView = layout.indexOfChild(newView);
        changeMode(MODE_SKETCHY);
    }

    /**
     * Changes the instructions and replaces the View for the given mode.
     * @param mode   The number of the mode to change to.
     */
    private void changeMode(int mode) {
        // Change the instructions for the new mode.
        TextView instructionsTextView = findViewById(R.id.instructionsTextView);
        try {
            instructionsTextView.setText(instructionsStrings[mode]);
        } catch (ArrayIndexOutOfBoundsException ex) {
            // Then it must not be one of the valid modes.
            instructionsTextView.setText("Please choose a mode above.");
        }

        // Remove the old added View and add a new one according to the new mode.
        LinearLayout layout = findViewById(R.id.artsyLayout);
        layout.removeViewAt(indexOfAddedView);
        View newView;
        switch (mode) {
            case MODE_SKETCHY:
                newView = new SketchyView(this);
                break;
            case MODE_FRACTAL:
                newView = new FractalView(this);
                break;
            case MODE_POINTS:
                newView = new PointsView(this);
                break;
            case MODE_AVERAGING:
                newView = new AveragingView(this);
                break;
            case MODE_GEOMETRY:
                newView = new GeometryView(this);
                break;
            case MODE_BEZIER:
                newView = new BezierView(this);
                break;
            case MODE_VORONOI:
                newView = new VoronoiView(this);
                break;
            default: // It must not be one of the valid modes.
                newView = new View(this); // Generic and useless, but removable.
                break;
        }
        layout.addView(newView);
        indexOfAddedView = layout.indexOfChild(newView);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // When a different mode is selected, change to that mode.
        changeMode(pos);
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing.
    }

}





// Create new classes below.





