package frontend.gui;

import java.awt.*;

/**
 * Utility wrapper class for simplified GridBagConstraints configuration in GUI layouts.
 * <p>
 * This class provides a convenient interface for configuring {@link GridBagConstraints}
 * objects through multiple overloaded methods that handle common layout scenarios
 * within the airport management system's GUI components. It encapsulates the complexity
 * of GridBagLayout positioning while providing flexible configuration options for
 * different layout requirements.
 * </p>
 * <p>
 * The Constraints class supports comprehensive layout configuration including:
 * </p>
 * <ul>
 *   <li><strong>Grid Positioning:</strong> Precise component placement through grid coordinates</li>
 *   <li><strong>Size Management:</strong> Component spanning across multiple grid cells</li>
 *   <li><strong>Fill Behavior:</strong> Component expansion control within allocated space</li>
 *   <li><strong>Padding Configuration:</strong> Internal and external spacing management</li>
 *   <li><strong>Anchor Positioning:</strong> Component alignment within grid cells</li>
 *   <li><strong>Weight Distribution:</strong> Space allocation priorities for resizing behavior</li>
 *   <li><strong>Margin Control:</strong> External spacing through Insets configuration</li>
 * </ul>
 *
 * @author Aeroporto Di Napoli
 * @version 1.0
 * @since 1.0
 * @see GridBagConstraints
 * @see GridBagLayout
 * @see Insets
 */
public class Constraints {

    /**
     * The encapsulated GridBagConstraints object that maintains layout configuration state.
     * <p>
     * This object holds all constraint parameters including grid positioning, size
     * specifications, fill behavior, padding values, anchor positioning, weight
     * distribution, and margin settings. The constraints are configured through
     * the various setConstraints methods and retrieved via getGridBagConstraints.
     * </p>
     */
    private static final GridBagConstraints gridBagConstraints;

    static {
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.weightx = 0.01;
        gridBagConstraints.weighty = 0.01;
    }

    /**
     * Configures comprehensive GridBagConstraints with full parameter specification.
     * <p>
     * This method provides complete control over all GridBagConstraints parameters,
     * serving as the primary configuration method for complex layout scenarios.
     * All other setConstraints methods delegate to this method while providing
     * default values for omitted parameters.
     * </p>
     * <p>
     * The comprehensive parameter set enables precise layout control including:
     * </p>
     * <ul>
     *   <li><strong>Grid Positioning:</strong> Exact placement through gridx and gridy coordinates</li>
     *   <li><strong>Cell Spanning:</strong> Multi-cell occupation through gridwidth and gridheight</li>
     *   <li><strong>Fill Behavior:</strong> Component expansion control within allocated grid space</li>
     *   <li><strong>Internal Padding:</strong> Component size adjustment through ipadx and ipady</li>
     *   <li><strong>Anchor Positioning:</strong> Component alignment within grid cells when smaller than allocated space</li>
     *   <li><strong>Weight Distribution:</strong> Proportional space allocation during container resizing</li>
     *   <li><strong>External Margins:</strong> Component spacing through Insets configuration</li>
     * </ul>
     *
     * @param gridx the horizontal grid position (zero-based indexing)
     * @param gridy the vertical grid position (zero-based indexing)
     * @param gridwidth the number of horizontal grid cells to span
     * @param gridheight the number of vertical grid cells to span
     * @param fill the fill behavior constant determining component expansion within allocated space
     * @param ipadx the internal horizontal padding added to component width
     * @param ipady the internal vertical padding added to component height
     * @param anchor the anchor constant determining component alignment within grid cells
     * @param weightx the horizontal weight for proportional space distribution during resizing
     * @param weighty the vertical weight for proportional space distribution during resizing
     * @param insets the external margins surrounding the component within its grid area
     */
    public static void setConstraints(int gridx, int gridy, int gridwidth, int gridheight, int fill,
                               int ipadx, int ipady, int anchor, float weightx, float weighty, Insets insets) {

        gridBagConstraints.gridx = gridx;
        gridBagConstraints.gridy = gridy;
        gridBagConstraints.gridwidth = gridwidth;
        gridBagConstraints.gridheight = gridheight;
        gridBagConstraints.fill = fill;
        gridBagConstraints.ipadx = ipadx;
        gridBagConstraints.ipady = ipady;
        gridBagConstraints.anchor = anchor;
        gridBagConstraints.weightx = weightx;
        gridBagConstraints.weighty = weighty;
        gridBagConstraints.insets = insets;
    }

    /**
     * Configures GridBagConstraints with custom weight values and default margins.
     * <p>
     * This convenience method provides weight specification while using default
     * zero margins through an empty {@link Insets} object. This method is suitable
     * for layouts requiring specific weight distribution without external spacing
     * requirements.
     * </p>
     * <p>
     * The method delegates to the comprehensive setConstraints method while
     * providing default Insets(0,0,0,0) for margin configuration. This approach
     * enables weight customization without margin complexity for common layout
     * scenarios.
     * </p>
     *
     * @param gridx the horizontal grid position (zero-based indexing)
     * @param gridy the vertical grid position (zero-based indexing)
     * @param gridwidth the number of horizontal grid cells to span
     * @param gridheight the number of vertical grid cells to span
     * @param fill the fill behavior constant determining component expansion within allocated space
     * @param ipadx the internal horizontal padding added to component width
     * @param ipady the internal vertical padding added to component height
     * @param anchor the anchor constant determining component alignment within grid cells
     * @param weightx the horizontal weight for proportional space distribution during resizing
     * @param weighty the vertical weight for proportional space distribution during resizing
     */
    public static void setConstraints(int gridx, int gridy, int gridwidth, int gridheight, int fill,
                               int ipadx, int ipady, int anchor, float weightx, float weighty) {

        Constraints.setConstraints(gridx, gridy, gridwidth, gridheight, fill,
                ipadx, ipady, anchor, weightx, weighty, new Insets(0, 0, 0, 0));
    }

    /**
     * Configures GridBagConstraints with custom margins and default weight values.
     * <p>
     * This convenience method enables margin specification while using the default
     * weight values (0.01) established during construction. This method is suitable
     * for layouts requiring specific spacing without complex weight distribution
     * requirements.
     * </p>
     *
     * @param gridx the horizontal grid position (zero-based indexing)
     * @param gridy the vertical grid position (zero-based indexing)
     * @param gridwidth the number of horizontal grid cells to span
     * @param gridheight the number of vertical grid cells to span
     * @param fill the fill behavior constant determining component expansion within allocated space
     * @param ipadx the internal horizontal padding added to component width
     * @param ipady the internal vertical padding added to component height
     * @param anchor the anchor constant determining component alignment within grid cells
     * @param insets the external margins surrounding the component within its grid area
     */
    public static void setConstraints(int gridx, int gridy, int gridwidth, int gridheight, int fill,
                               int ipadx, int ipady, int anchor, Insets insets) {

        Constraints.setConstraints(gridx, gridy, gridwidth, gridheight, fill,
                ipadx, ipady, anchor, 0.01f, 0.01f, insets);

    }

    /**
     * Configures GridBagConstraints with default weight values and margins.
     * <p>
     * This simplified convenience method provides basic layout configuration using
     * default values for weight distribution and margin spacing. This method is
     * suitable for straightforward layouts where precise weight and margin control
     * are not required.
     * </p>
     *
     * @param gridx the horizontal grid position (zero-based indexing)
     * @param gridy the vertical grid position (zero-based indexing)
     * @param gridwidth the number of horizontal grid cells to span
     * @param gridheight the number of vertical grid cells to span
     * @param fill the fill behavior constant determining component expansion within allocated space
     * @param ipadx the internal horizontal padding added to component width
     * @param ipady the internal vertical padding added to component height
     * @param anchor the anchor constant determining component alignment within grid cells
     */
    public static void setConstraints(int gridx, int gridy, int gridwidth, int gridheight, int fill,
                               int ipadx, int ipady, int anchor) {

        Constraints.setConstraints(gridx, gridy, gridwidth, gridheight, fill,
                ipadx, ipady, anchor, 0.01f, 0.01f, new Insets(0, 0, 0, 0));
    }

    /**
     * Retrieves the configured GridBagConstraints object for layout manager use.
     * <p>
     * This method provides access to the underlying {@link GridBagConstraints}
     * object that contains all configuration parameters set through the various
     * setConstraints methods. The returned object can be used directly with
     * {@link GridBagLayout} for component positioning.
     * </p>
     *
     * @return the configured GridBagConstraints object containing all layout parameters
     */
    public static GridBagConstraints getGridBagConstraints() {
        return gridBagConstraints;
    }
}