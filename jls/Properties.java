package jls;

import java.io.IOException;
import java.util.Arrays;

public class Properties
{
    public static final int COLUMNS_MIN = 1;
    public static final int COLUMNS_MAX = 1000;
    public static final int ROWS_MIN = 1;
    public static final int ROWS_MAX = 1000;
    public static final int GENERATIONS_MIN = 1;
    public static final int GENERATIONS_MAX = 1000;
    public static final int PERIOD_MIN = 1;
    public static final int PERIOD_MAX = 1000;
    
    public static final int CELLS_MAX = 2000000;
    
    public static final int SYMM_NONE = 0;
    public static final int SYMM_MIRROR_HORZ = 1;
    public static final int SYMM_MIRROR_VERT = 2;
    public static final int SYMM_MIRROR_DIAG_FWD = 3;
    public static final int SYMM_MIRROR_DIAG_BWD = 4;
    public static final int SYMM_ROT_180 = 5;
    public static final int SYMM_4FOLD = 6;
    public static final int SYMM_4FOLD_DIAG = 7;
    public static final int SYMM_ROT_90 = 8;
    public static final int SYMM_8FOLD = 9;

    public static final String[] SYMMETRY_NAMES =
        { "None", "Mirror-Horizontal", "Mirror-Vertical", "Mirror-Diagonal",
                "Mirror-Diagonal-Backwards", "Rotate-180", "4-Fold",
                "4-Fold-Diagonal", "Rotate-90", "8-Fold" };

    public static final int XLAT_NONE = 0;
    public static final int XLAT_FLIP_HORZ = 1;
    public static final int XLAT_FLIP_VERT = 2;
    public static final int XLAT_FLIP_DIAG_FWD = 3;
    public static final int XLAT_FLIP_DIAG_BWD = 4;
    public static final int XLAT_ROT_90 = 5;
    public static final int XLAT_ROT_180 = 6;
    public static final int XLAT_ROT_270 = 7;
    public static final int XLAT_GLIDE_DIAG_FWD = 8;
    public static final int XLAT_GLIDE_DIAG_BWD = 9;

    public static final String[] TRANSLATION_NAMES =
        { "None", "Flip-Horizontal", "Flip-Vertical", "Flip-Diagonal",
                "Flip-Diagonal-Backwards", "Rotate-90", "Rotate-180",
                "Rotate-270", "Glide-Diagonal", "Glide-Diagonal-Backwards" };

    private boolean validated = false;
    
    private int columns = 35;
    private static final String COLUMNS_NAME = "columns";
    private int rows = 15;
    private static final String ROWS_NAME = "rows";
    private int generations = 4;
    private static final String GENERATIONS_NAME = "generations";

    private boolean outerSpaceUnset = false;
    private static final String OUTER_SPACE_UNSET_NAME = "outer_space_unset";

    private int period [] = {generations, 1, 2, 3, 4, 5, 6};
    private static final String PERIOD_NAME = "periods";

    private int symmetry = SYMM_NONE;
    private static final String SYMMETRY_NAME = "symmetry";

    private boolean tileGen = true;
    private static final String TILE_TEMPORAL_NAME = "tile_temporal";
    private int tileGenShiftRight = 0;
    private static final String TILE_TEMPORAL_SHIFT_RIGHT_NAME = "tile_temporal_shift_right";
    private int tileGenShiftDown = 0;
    private static final String TILE_TEMPORAL_SHIFT_DOWN_NAME = "tile_temporal_shift_down";
    private boolean tileHorz = false;
    private static final String TILE_HORIZONTAL_NAME = "tile_horizontal";
    private int tileHorzShiftDown = 0;
    private static final String TILE_HORIZONTAL_SHIFT_DOWN_NAME = "tile_horizontal_shift_down";
    private int tileHorzShiftFuture = 0;
    private static final String TILE_HORIZONTAL_SHIFT_FUTURE_NAME = "tile_horizontal_shift_future";
    private boolean tileVert = false;
    private static final String TILE_VERTICAL_NAME = "tile_vertical";
    private int tileVertShiftRight = 0;
    private static final String TILE_VERTICAL_SHIFT_RIGHT_NAME = "tile_vertical_shift_right";
    private int tileVertShiftFuture = 0;
    private static final String TILE_VERTICAL_SHIFT_FUTURE_NAME = "tile_vertical_shift_future";
    private int translation = XLAT_NONE;
    private static final String TRANSLATION_NAME = "translation";

    private boolean ruleBirth [] = {false, false, false, true, false, false, false, false, false};
    private static final String RULE_BIRTH_NAME = "rule_birth";
    private boolean ruleSurvival [] = {false, false, true, true, false, false, false, false, false};
    private static final String RULE_SURVIVAL_NAME = "rule_survival";

    public String validate()
    {
        if ((columns < COLUMNS_MIN) || (columns > COLUMNS_MAX))
        {
            return "Number of columns must be between " + COLUMNS_MIN + " and " + COLUMNS_MAX + ".";
        }
        if ((rows < ROWS_MIN) || (rows > ROWS_MAX))
        {
            return "Number of rows must be between " + ROWS_MIN + " and " + ROWS_MAX + ".";
        }
        if ((generations < GENERATIONS_MIN) || (generations > GENERATIONS_MAX))
        {
            return "Number of generations must be between " + GENERATIONS_MIN + " and " + GENERATIONS_MAX + ".";
        }
        if (columns * rows * generations > CELLS_MAX)
        {
            return "Number of cells must not exceed " + CELLS_MAX + ".";
        }
        for (int i = 1; i < 7; ++i)
        {
            if ((period[i] < PERIOD_MIN) || (period[i] > PERIOD_MAX))
            {
                return "Subperiod must be between " + PERIOD_MIN + " and " + PERIOD_MAX + ".";
            }
        }
        switch (symmetry)
        {
        case SYMM_NONE:
        case SYMM_MIRROR_HORZ:
        case SYMM_MIRROR_VERT:
        case SYMM_4FOLD:
        case SYMM_ROT_180:
            break;
        case SYMM_MIRROR_DIAG_FWD:
        case SYMM_MIRROR_DIAG_BWD:
        case SYMM_4FOLD_DIAG:
        case SYMM_8FOLD:
        case SYMM_ROT_90:
            if (columns != rows)
            {
                return "Selected symmetry can be used only on square field.";
            }
            break;
        default:
            return "Invalid symmetry.";
        }
        if (tileHorz && tileVert && tileGen
                && ((0 != tileHorzShiftDown) || (0 != tileHorzShiftFuture))
                && ((0 != tileVertShiftRight) || (0 != tileVertShiftFuture))
                && ((0 != tileGenShiftRight) || (0 != tileGenShiftDown)))
        {
            return "Tiling: You cannot combine shifting in all three planes.";
        }
        if (tileGen && tileHorz && (0 != tileHorzShiftFuture) && (0 != tileGenShiftRight))
        {
            return "Tiling: You cannot combine shifting 'after' field right and shifting 'right' field to future.";
        }
        if (tileGen && tileVert && (0 != tileVertShiftFuture) && (0 != tileGenShiftDown))
        {
            return "Tiling: You cannot combine shifting 'after' field down and shifting 'below' field to future.";
        }
        if (tileHorz && tileVert && (0 != tileVertShiftRight) && (0 != tileHorzShiftDown))
        {
            return "Tiling: You cannot combine shifting 'right' field down and shifting 'below' field right.";
        }
        switch (translation)
        {
        case XLAT_NONE:
        case XLAT_FLIP_HORZ:
        case XLAT_FLIP_VERT:
        case XLAT_ROT_180:
        case XLAT_GLIDE_DIAG_FWD:
        case XLAT_GLIDE_DIAG_BWD:
            break;
        case XLAT_FLIP_DIAG_FWD:
        case XLAT_FLIP_DIAG_BWD:
        case XLAT_ROT_90:
        case XLAT_ROT_270:
            if (columns != rows)
            {
                return "Selected translation can be used only on square field.";
            }
            break;
        default:
            return "Invalid translation.";
        }
        validated = true;
        return null;
    }
    
    private void checkValidated()
    {
        if (validated)
        {
            throw(new RuntimeException("Validated Properties cannot be changed."));
        }
    }
    
    public boolean readStatusParameter(StatusFileHandler handler, String name,
            int[] details, String value) throws IOException
    {
        if (name.equals(COLUMNS_NAME))
        {
            handler.checkDetailsLength(0);
            columns = handler.parseInteger(value);
        }
        else if (name.equals(ROWS_NAME))
        {
            handler.checkDetailsLength(0);
            rows = handler.parseInteger(value);
        }
        else if (name.equals(GENERATIONS_NAME))
        {
            handler.checkDetailsLength(0);
            generations = handler.parseInteger(value);
        }
        else if (name.equals(OUTER_SPACE_UNSET_NAME))
        {
            handler.checkDetailsLength(0);
            outerSpaceUnset = handler.parseBoolean(value);
        }
        else if (name.equals(PERIOD_NAME))
        {
            handler.checkDetailsLength(0);
            period = handler.parseIntegerList(value);
            return period.length == 7;
        }
        else if (name.equals(SYMMETRY_NAME))
        {
            handler.checkDetailsLength(0);
            symmetry = handler.parseEnum(value, SYMMETRY_NAMES);
        }
        else if (name.equals(TILE_HORIZONTAL_NAME))
        {
            handler.checkDetailsLength(0);
            tileHorz = handler.parseBoolean(value);
        }
        else if (name.equals(TILE_HORIZONTAL_SHIFT_DOWN_NAME))
        {
            handler.checkDetailsLength(0);
            tileHorzShiftDown = handler.parseInteger(value);
        }
        else if (name.equals(TILE_HORIZONTAL_SHIFT_FUTURE_NAME))
        {
            handler.checkDetailsLength(0);
            tileHorzShiftFuture = handler.parseInteger(value);
        }
        else if (name.equals(TILE_VERTICAL_NAME))
        {
            handler.checkDetailsLength(0);
            tileVert = handler.parseBoolean(value);
        }
        else if (name.equals(TILE_VERTICAL_SHIFT_RIGHT_NAME))
        {
            handler.checkDetailsLength(0);
            tileVertShiftRight = handler.parseInteger(value);
        }
        else if (name.equals(TILE_VERTICAL_SHIFT_FUTURE_NAME))
        {
            handler.checkDetailsLength(0);
            tileVertShiftFuture = handler.parseInteger(value);
        }
        else if (name.equals(TILE_TEMPORAL_NAME))
        {
            handler.checkDetailsLength(0);
            tileGen = handler.parseBoolean(value);
        }
        else if (name.equals(TILE_TEMPORAL_SHIFT_RIGHT_NAME))
        {
            handler.checkDetailsLength(0);
            tileGenShiftRight = handler.parseInteger(value);
        }
        else if (name.equals(TILE_TEMPORAL_SHIFT_DOWN_NAME))
        {
            handler.checkDetailsLength(0);
            tileGenShiftDown = handler.parseInteger(value);
        }
        else if (name.equals(TRANSLATION_NAME))
        {
            handler.checkDetailsLength(0);
            translation = handler.parseEnum(value, TRANSLATION_NAMES);
        }
        else if (name.equals(RULE_BIRTH_NAME))
        {
            handler.checkDetailsLength(0);
            ruleBirth = handler.parseBooleanList(value);
            return ruleBirth.length == 9;
        }
        else if (name.equals(RULE_SURVIVAL_NAME))
        {
            handler.checkDetailsLength(0);
            ruleSurvival = handler.parseBooleanList(value);
            return ruleSurvival.length == 9;
        }
        return true;
    }
    
    public String finishReadStatus()
    {
        return validate();
    }
    
    public void writeStatus(StatusFileHandler handler) throws IOException
    {
        handler.putParameter(COLUMNS_NAME, columns);
        handler.putParameter(ROWS_NAME, rows);
        handler.putParameter(GENERATIONS_NAME, generations);
        handler.putParameter(PERIOD_NAME, period, 7);
        handler.putParameter(OUTER_SPACE_UNSET_NAME, outerSpaceUnset);
        handler.putParameter(SYMMETRY_NAME, SYMMETRY_NAMES[symmetry]);
        handler.putParameter(TILE_HORIZONTAL_NAME, tileHorz);
        handler.putParameter(TILE_HORIZONTAL_SHIFT_DOWN_NAME, tileHorzShiftDown);
        handler.putParameter(TILE_HORIZONTAL_SHIFT_FUTURE_NAME, tileHorzShiftFuture);
        handler.putParameter(TILE_VERTICAL_NAME, tileVert);
        handler.putParameter(TILE_VERTICAL_SHIFT_RIGHT_NAME, tileVertShiftRight);
        handler.putParameter(TILE_VERTICAL_SHIFT_FUTURE_NAME, tileVertShiftFuture);
        handler.putParameter(TILE_TEMPORAL_NAME, tileGen);
        handler.putParameter(TILE_TEMPORAL_SHIFT_RIGHT_NAME, tileGenShiftRight);
        handler.putParameter(TILE_TEMPORAL_SHIFT_DOWN_NAME, tileGenShiftDown);
        handler.putParameter(TRANSLATION_NAME, TRANSLATION_NAMES[translation]);
        handler.putParameter(RULE_BIRTH_NAME, ruleBirth, 9);
        handler.putParameter(RULE_SURVIVAL_NAME, ruleSurvival, 9);
    }
    
    public boolean isValidated()
    {
        return validated;
    }

    public int getColumns()
    {
        return columns;
    }

    public void setColumns(int columns)
    {
        checkValidated();
        this.columns = columns;
    }

    public int getGenerations()
    {
        return generations;
    }

    public void setGenerations(int generations)
    {
        checkValidated();
        this.generations = generations;
        period [0] = generations;
    }

    public int getRows()
    {
        return rows;
    }

    public void setRows(int rows)
    {
        checkValidated();
        this.rows = rows;
    }

    public int getPeriod(int index)
    {
        if (index == 0)
        {
            return generations;
        }
        return period [index];
    }

    public void setPeriod(int index, int period)
    {
        checkValidated();
        this.period [index] = period;
    }

    public int getSymmetry()
    {
        return symmetry;
    }

    public void setSymmetry(int symm)
    {
        checkValidated();
        symmetry = symm;
    }

    public boolean isTileGen()
    {
        return tileGen;
    }

    public void setTileGen(boolean tileGen)
    {
        checkValidated();
        this.tileGen = tileGen;
    }

    public int getTileGenShiftRight()
    {
        return tileGenShiftRight;
    }

    public void setTileGenShiftRight(int tileGenShiftRight)
    {
        checkValidated();
        this.tileGenShiftRight = tileGenShiftRight;
    }

    public int getTileGenShiftDown()
    {
        return tileGenShiftDown;
    }

    public void setTileGenShiftDown(int tileGenShiftDown)
    {
        checkValidated();
        this.tileGenShiftDown = tileGenShiftDown;
    }

    public boolean isTileHorz()
    {
        return tileHorz;
    }

    public void setTileHorz(boolean tileHorz)
    {
        checkValidated();
        this.tileHorz = tileHorz;
    }

    public int getTileHorzShiftFuture()
    {
        return tileHorzShiftFuture;
    }

    public void setTileHorzShiftFuture(int tileHorzShiftFuture)
    {
        checkValidated();
        this.tileHorzShiftFuture = tileHorzShiftFuture;
    }

    public int getTileHorzShiftDown()
    {
        return tileHorzShiftDown;
    }

    public void setTileHorzShiftDown(int tileHorzShiftDown)
    {
        checkValidated();
        this.tileHorzShiftDown = tileHorzShiftDown;
    }

    public boolean isTileVert()
    {
        return tileVert;
    }

    public void setTileVert(boolean tileVert)
    {
        checkValidated();
        this.tileVert = tileVert;
    }

    public int getTileVertShiftFuture()
    {
        return tileVertShiftFuture;
    }

    public void setTileVertShiftFuture(int tileVertShiftFuture)
    {
        checkValidated();
        this.tileVertShiftFuture = tileVertShiftFuture;
    }

    public int getTileVertShiftRight()
    {
        return tileVertShiftRight;
    }

    public void setTileVertShiftRight(int tileVertShiftRight)
    {
        checkValidated();
        this.tileVertShiftRight = tileVertShiftRight;
    }

    public int getTranslation()
    {
        return translation;
    }

    public void setTranslation(int translation)
    {
        checkValidated();
        this.translation = translation;
    }

    public boolean getRuleBirth(int index)
    {
        return ruleBirth [index];
    }
    
    public boolean[] getRuleBirth()
    {
        return Arrays.copyOf(ruleBirth, ruleBirth.length);
    }

    public boolean getRuleSurvival(int index)
    {
        return ruleSurvival [index];
    }

    public boolean[] getRuleSurvival()
    {
        return Arrays.copyOf(ruleSurvival, ruleSurvival.length);
    }

    public void setRuleBirth(int index, boolean birth)
    {
        checkValidated();
        ruleBirth [index] = birth;
    }

    public void setRuleSurvival(int index, boolean survival)
    {
        checkValidated();
        ruleSurvival [index] = survival;
    }

    public boolean isOuterSpaceUnset()
    {
        return outerSpaceUnset;
    }
    
    public boolean isOuterSpaceOff()
    {
        return !outerSpaceUnset;
    }

    public void setOuterSpaceUnset(boolean outerSpaceUnset)
    {
        this.outerSpaceUnset = outerSpaceUnset;
    }
}
