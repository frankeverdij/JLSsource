package jls;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Component;

public class ImageCache
{
    private static final int STATE_MULTIPLIER = 7;
    private static final int STATE_ON = 1 * STATE_MULTIPLIER;
    private static final int STATE_EMPTY = 2 * STATE_MULTIPLIER;
    
    private static final int PSTATE_MULTIPLIER = STATE_MULTIPLIER * 3;
    private static final int PSTATE_ON = 1 * PSTATE_MULTIPLIER;
    private static final int PSTATE_EMPTY = 2 * PSTATE_MULTIPLIER;
    
    private static final int TYPE_MULTIPLIER = PSTATE_MULTIPLIER * 3;
    private static final int TYPE_UNCHECKED_SOFT = TYPE_MULTIPLIER;
    private static final int TYPE_UNCHECKED_HARD = 2 * TYPE_MULTIPLIER;
    
    private static final int LAYER_MULTIPLIER = TYPE_MULTIPLIER * 3;

    private static final int FROZEN = LAYER_MULTIPLIER * 3;
    private static final int DIVERSE = FROZEN * 2;
    private static final int STACK_ERROR = DIVERSE * 2;
    private static final int CELL_ERROR = STACK_ERROR * 2;
    private static final int SELECTED = CELL_ERROR * 2;
    
    private static final int TOTAL_COUNT = SELECTED * 2; 
    
    public static final Color normalColors [] =
    {
        new Color(192, 192, 192),
        new Color(255, 255, 192),
        new Color(192, 255, 192),
        new Color(192, 255, 255),
        new Color(255, 192, 255),
        new Color(255, 192, 192),
        new Color(192, 192, 255)
    };

    private static final Color selectedColors [] =
    {
        new Color(153, 153, 153),
        new Color(204, 204, 153),
        new Color(153, 204, 153),
        new Color(153, 204, 204),
        new Color(204, 153, 204),
        new Color(204, 153, 153),
        new Color(153, 153, 204)
    };

    private Color normalCellColor = new Color(0, 0, 255);

    private Color errorCellColor = new Color(204, 0, 0);

    private Color errorUnknownCellColor = new Color(204, 0, 0);

    private Color selectedNormalCellColor = new Color(0, 0, 204);

    private Color selectedErrorCellColor = new Color(204, 0, 0);

    private Color selectedErrorUnknownCellColor = new Color(204, 0, 0);;

    private Color normalColumnColor [] = 
        { new Color(0, 0, 0), new Color(0, 0, 192), new Color(0, 96, 0) };

    private Color normalSelectedColumnColor [] =
        { new Color(0, 0, 0), new Color(0, 0, 154), new Color(0, 77, 0) };

    private Color errorColumnColor = new Color(192, 0, 0);

    private Color errorSelectedColumnColor = new Color(153, 0, 0);
    
    private Color unknownHardColor = Color.black;

    private Color unknownHardSelectedColor = Color.black;

    private Color unknownSoftColor = Color.black;

    private Color unknownSoftSelectedColor = Color.black;

    private Color frozenColor = new Color(0, 0, 0);

    private Color frozenSelectedColor = new Color(0, 0, 0);
    
    private Color potentialCellColor = normalCellColor;
    private Color selectedPotentialCellColor = selectedNormalCellColor;
    private Color errorPotentialCellColor = errorCellColor;
    private Color selectedErrorPotentialCellColor = selectedErrorCellColor;
    //private Color potentialCellColor = new Color(128, 0, 255);
    //private Color selectedPotentialCellColor = new Color(102, 0, 204);

    private Color normalPotentialCellColors [] =
    {
        blend(normalColors[0], potentialCellColor),
        blend(normalColors[1], potentialCellColor),
        blend(normalColors[2], potentialCellColor),
        blend(normalColors[3], potentialCellColor),
        blend(normalColors[4], potentialCellColor),
        blend(normalColors[5], potentialCellColor),
        blend(normalColors[6], potentialCellColor)
    };

    private Color selectedPotentialNormalCellColors [] =
    {
        blend(selectedColors[0], selectedPotentialCellColor),
        blend(selectedColors[1], selectedPotentialCellColor),
        blend(selectedColors[2], selectedPotentialCellColor),
        blend(selectedColors[3], selectedPotentialCellColor),
        blend(selectedColors[4], selectedPotentialCellColor),
        blend(selectedColors[5], selectedPotentialCellColor),
        blend(selectedColors[6], selectedPotentialCellColor)
    };

    private Color errorPotentialCellColors [] =
    {
        blend(normalColors[0], errorPotentialCellColor),
        blend(normalColors[1], errorPotentialCellColor),
        blend(normalColors[2], errorPotentialCellColor),
        blend(normalColors[3], errorPotentialCellColor),
        blend(normalColors[4], errorPotentialCellColor),
        blend(normalColors[5], errorPotentialCellColor),
        blend(normalColors[6], errorPotentialCellColor)
    };

    private Color selectedPotentialErrorCellColors [] =
    {
        blend(selectedColors[0], selectedErrorPotentialCellColor),
        blend(selectedColors[1], selectedErrorPotentialCellColor),
        blend(selectedColors[2], selectedErrorPotentialCellColor),
        blend(selectedColors[3], selectedErrorPotentialCellColor),
        blend(selectedColors[4], selectedErrorPotentialCellColor),
        blend(selectedColors[5], selectedErrorPotentialCellColor),
        blend(selectedColors[6], selectedErrorPotentialCellColor)
    };

    private static Color blend(Color color1, Color color2)
    {
        int a = 100;
        int b = 60;
        return new Color((color1.getRed() * a + color2.getRed() * b) / (a + b),
                (color1.getGreen() * a + color2.getGreen() * b) / (a + b),
                (color1.getBlue() * a + color2.getBlue() * b) / (a + b));
    }

    private int cellSize;
    private Image[] cache;
    private Component owner;
    
    public ImageCache(Component owner, int cellSize)
    {
        this.owner = owner;
        this.cellSize = cellSize;
        cache = new Image[TOTAL_COUNT];
    }
    
    public Image getImage(
            int state,
            int potentialState,
            boolean frozen,
            int type,
            int period,
            boolean stackDiverse,
            boolean stackError,
            boolean cellError,
            boolean selected, 
            int layerIndex)
    {
        int index = period % 7;
        switch (state)
        {
        case Cell.STATE_ON:
            index += STATE_ON;
            break;
        case Cell.STATE_OFF:
            break;
        default: // STATE_EMPTY
            index += STATE_EMPTY;
        break;
        }
        switch (potentialState)
        {
        case Cell.STATE_ON:
            index += PSTATE_ON;
            break;
        case Cell.STATE_OFF:
            break;
        default: // STATE_EMPTY
            index += PSTATE_EMPTY;
        }
        switch (type)
        {
        case Cell.TYPE_NORMAL:
            break;
        case Cell.TYPE_UNCHECKED:
            index += TYPE_UNCHECKED_SOFT;
            break;
        default: // TYPE_UNECHECKED_HARD
            index += TYPE_UNCHECKED_HARD;
            break;
        }
        
        index += LAYER_MULTIPLIER * layerIndex;

        if (frozen)
        {
            index += FROZEN;
        }
        if (stackDiverse)
        {
            index += DIVERSE;
        }
        if (stackError)
        {
            index += STACK_ERROR;
        }
        if (cellError)
        {
            index += CELL_ERROR;
        }
        if (selected)
        {
            index += SELECTED;
        }
        Image result = cache[index];
        if (null == result)
        {
            result = owner.createImage(cellSize - 1, cellSize - 1);
            cache[index] = result;
            Graphics g = result.getGraphics();
            
            int csm3 = cellSize - 3;
            int csm4 = cellSize - 4;
            int csm5 = cellSize - 5;
            int csm6 = cellSize - 6;
            int csm10 = cellSize - 10;
            int csm5d2 = csm5 / 2;
            int csm5d4 = csm5 / 4;
            int csm6d3 = csm6 / 3;
            int csm4mcsm5d4 = csm4 - csm5d4;
            int csm4mcsm6d3 = csm4 - csm6d3;

            // first draw background
            if (selected)
            {
                g.setColor(selectedColors[period]);
            }
            else
            {
                g.setColor(normalColors[period]);
            }
            g.fillRect(1, 1, csm3, csm3);

            // then the cell
            if (type == Cell.TYPE_UNSET)
            {
                // hard unknown, just one picture
                g.setColor(selected ? unknownHardSelectedColor : unknownHardColor);
                g.drawLine(2 + csm6d3, 2, 2 + csm6d3,  csm4);
                g.drawLine(csm4mcsm6d3, 2, csm4mcsm6d3,  csm4);
                g.drawLine(2, 2 + csm6d3, csm4, 2 + csm6d3);
                g.drawLine(2, csm4mcsm6d3, csm4, csm4mcsm6d3);
            }
            else
            {
                // if empty and potential state selected, draw the potential state first
                if (state == Cell.STATE_EMPTY)
                {
                    if (potentialState == Cell.STATE_OFF)
                    {
                        g.setColor(selected ? (cellError ? selectedPotentialErrorCellColors[period] : selectedPotentialNormalCellColors[period]) : (cellError ? errorPotentialCellColors[period] : normalPotentialCellColors[period]));
                        g.fillOval(2, 2, csm6, csm6);
                        g.drawOval(2, 2, csm6, csm6);
                        if (selected)
                        {
                            g.setColor(selectedColors[period]);
                        }
                        else
                        {
                            g.setColor(normalColors[period]);
                        }
                        g.fillOval(4, 4, csm10, csm10);
                        g.drawOval(4, 4, csm10, csm10);
                    }
                    else if (potentialState == Cell.STATE_ON)
                    {
                        g.setColor(selected ? (cellError ? selectedPotentialErrorCellColors[period] : selectedPotentialNormalCellColors[period]) : (cellError ? errorPotentialCellColors[period] : normalPotentialCellColors[period]));
                        g.fillOval(2, 2, csm6, csm6);
                        g.drawOval(2, 2, csm6, csm6);
                    }
                    else if (cellError && (type == Cell.TYPE_NORMAL))
                    {
                        g.setColor(selected ? selectedErrorCellColor : errorCellColor);
                        g.drawLine(2 + csm5d4, 2 + csm5d4, csm4mcsm5d4, 2 + csm5d4);
                        g.drawLine(2 + csm5d4, 2 + csm5d4, 2 + csm5d2, csm4mcsm5d4);
                        g.drawLine(csm4mcsm5d4, 2 + csm5d4, 2 + csm5d2, csm4mcsm5d4);
                    }
                }
                else if (state == Cell.STATE_OFF)
                {
                    g.setColor(selected ? (cellError ? selectedErrorCellColor : selectedNormalCellColor) : (cellError ? errorCellColor : normalCellColor));
                    g.fillOval(2, 2, csm6, csm6);
                    g.drawOval(2, 2, csm6, csm6);
                    if (selected)
                    {
                        g.setColor(selectedColors[period]);
                    }
                    else
                    {
                        g.setColor(normalColors[period]);
                    }
                    g.fillOval(4, 4, csm10, csm10);
                    g.drawOval(4, 4, csm10, csm10);
                }
                else if (state == Cell.STATE_ON)
                {
                    g.setColor(selected ? (cellError ? selectedErrorCellColor : selectedNormalCellColor) : (cellError ? errorCellColor : normalCellColor));
                    g.fillOval(2, 2, csm6, csm6);
                    g.drawOval(2, 2, csm6, csm6);
                }
                if (type == Cell.TYPE_UNCHECKED)
                {
                    // soft unknown
                    g.setColor(cellError ? (selected ? selectedErrorUnknownCellColor : errorUnknownCellColor) : (selected ? unknownSoftSelectedColor : unknownSoftColor));
                    g.drawLine(2, 2, csm4, csm4);
                    g.drawLine(csm4, 2, 2, csm4);
                }
                if (frozen)
                {
                    g.setColor(selected ? frozenSelectedColor : frozenColor);
                    g.drawLine(2 + csm5d4, 2 + csm5d4, csm4mcsm5d4, 2 + csm5d4);
                    g.drawLine(2 + csm5d4, 2 + csm5d4, 2 + csm5d4, csm4mcsm5d4);
                    g.drawLine(2 + csm5d4, 2 + csm5d2, 2 + csm5d2, 2 + csm5d2);
                }
            }

            // and the surrounding rectangle last
            g.setColor(stackError ? (selected ? errorSelectedColumnColor : errorColumnColor) : (selected ? normalSelectedColumnColor : normalColumnColor)[layerIndex]);
            g.drawRect(0, 0, cellSize - 2, cellSize - 2);
            if (stackDiverse || stackError)
            {
                g.drawRect(1, 1, csm4, csm4);
            }
        }
        return result;
    }
}
