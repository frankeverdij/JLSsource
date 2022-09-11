package jls;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.LineNumberReader;


public class StatusFileHandler
{
    private static final String PROPERTIES_SECTION_NAME = "[Properties]";
    private static final String OPTIONS_SECTION_NAME = "[SearchOptions]";
    private static final String ARRAY_SECTION_NAME = "[CellArray]";
    private static final String SEARCH_SECTION_NAME = "[Search]";

    private BufferedWriter writer;
    private LineNumberReader reader;
    private String fullParamName;
    private String paramName;
    private int [] paramDetails;
    private String paramValue;
    
    public void startSection(String sectionName) throws IOException
    {
        newLine();
        write(sectionName);
        newLine();
        newLine();
    }
    
    public void putComment(String comment) throws IOException
    {
        if (comment.length() > 0)
        {
            write("# ");
            write(comment);
        }
        else
        {
            write("#");
        }
        newLine();
    }
    
    public void putParameter(String paramName, long paramValue) throws IOException
    {
        write(paramName);
        write("=");
        write(paramValue);
        newLine();
    }
    
    public void putParameter(String paramName, int paramValue) throws IOException
    {
        write(paramName);
        write("=");
        write(paramValue);
        newLine();
    }
    
    public void putParameter(String paramName, int [] paramArray, int paramCount) throws IOException
    {
        write(paramName);
        write("={");
        if (paramCount > 0)
        {
            write(paramArray[0]);
            for (int i = 1; i < paramCount; ++i)
            {
                write(",");
                write(paramArray[i]);
            }
        }
        write("}");
        newLine();
    }
    
    
    public void putParameter(String paramName, boolean paramValue) throws IOException
    {
        write(paramName);
        write("=");
        write(paramValue);
        newLine();
    }
    
    public void putParameter(String paramName, boolean [] paramArray, int paramCount) throws IOException
    {
        write(paramName);
        write("={");
        if (paramCount > 0)
        {
            write(paramArray[0]);
            for (int i = 1; i < paramCount; ++i)
            {
                write(",");
                write(paramArray[i]);
            }
        }
        write("}");
        newLine();
    }
    
    public void putParameter(String paramName, String paramValue) throws IOException
    {
        write(paramName);
        write("=");
        write(paramValue);
        newLine();
    }
    
    public void newLine() throws IOException
    {
        writer.newLine();
    }
    
    private void write(String value) throws IOException
    {
        writer.write(value);
    }
    
    private void write(boolean value) throws IOException
    {
        writer.write(value ? "Yes" : "No");
    }
    
    private void write(int value) throws IOException
    {
        writer.write(Integer.toString(value));
    }

    private void write(long value) throws IOException
    {
        writer.write(Long.toString(value));
    }

    public void saveStatus(Properties properties, SearchOptions searchOptions,
            EditCellArray cellArray, SearchCellArray searchCellArray,
            BufferedWriter writer) throws IOException
    {
        this.writer = writer;
        putComment("JavaLifeSearch status file, automatically generated");
        putComment("");
        putComment("Any changes to it, including changing order of lines, may cause");
        putComment("any kinds of strange behaviour after loading it to JLS");
        putComment("including errors, deadlocks, or crashes.");
        startSection(PROPERTIES_SECTION_NAME);
        properties.writeStatus(this);
        startSection(OPTIONS_SECTION_NAME);
        searchOptions.writeStatus(this);
        startSection(ARRAY_SECTION_NAME);
        cellArray.writeStatus(this);
        if (cellArray.isReadOnly())
        {
            startSection(SEARCH_SECTION_NAME);
            searchCellArray.writeStatus(this);
        }
    }
    
    private String getLine() throws IOException
    {
        while (true)
        {
            String line = reader.readLine();
            if (null == line)
            {
                return null;
            }
            if ((line.trim().length() > 0) && (!line.startsWith("#")))
            {
                return line;
            }
        }
    }
    
    private boolean prepareParameter(String line) throws IOException
    {
        if (null == line)
        {
            return false;
        }
        int i = line.indexOf('=');
        if (-1 == i)
        {
            return false;
        }
        fullParamName = line.substring(0, i);
        paramValue = line.substring(i + 1);
        i = fullParamName.indexOf('{');
        if (i >= 0)
        {
            paramName = fullParamName.substring(0, i);
            paramDetails = parseIntegerList(fullParamName.substring(i));
        }
        else
        {
            paramName = fullParamName;
            paramDetails = new int[0];
        }
        return true;
    }
    
    public long parseLong(String value) throws IOException
    {
        try
        {
            long result = Long.parseLong(value);
            return result;
        }
        catch (NumberFormatException e)
        {
            throw (new IOException("Value is not a long integer in parameter " + fullParamName + " at line " + reader.getLineNumber()));
        }
    }
    
    public int parseInteger(String value) throws IOException
    {
        try
        {
            int result = Integer.parseInt(value);
            return result;
        }
        catch (NumberFormatException e)
        {
            throw (new IOException("Value is not an integer in parameter " + fullParamName + " at line " + reader.getLineNumber()));
        }
    }
    
    public int[] parseIntegerList(String value) throws IOException
    {
        if ((value.length() < 2) || (value.charAt(0) != '{') || (value.charAt(value.length() - 1) != '}'))
        {
            throw (new IOException("Value is not a list in parameter " + fullParamName + " at line " + reader.getLineNumber()));
        }
        if (value.length() == 2)
        {
            return new int [0];
        }
        value = value.substring(1, value.length() - 1);
        int commas = 0;
        int i;
        for (i = 0; i < value.length(); ++i)
        {
            if (value.charAt(i) == ',')
            {
                ++commas;
            }
        }
        int [] result = new int [commas + 1];
        i = 0;
        do
        {
            commas = value.indexOf(',');
            if (commas > 0)
            {
                result[i] = parseInteger(value.substring(0, commas));
                value = value.substring(commas + 1);
                ++i;
            }
        } while (commas > 0);
        result[i] = parseInteger(value);
        return result;
    }
    
    public boolean parseBoolean(String value) throws IOException
    {
        if ("Yes".equals(value))
        {
            return true;
        }
        if ("No".equals(value))
        {
            return false;
        }
        throw (new IOException("Value is not boolean in parameter " + fullParamName + " at line " + reader.getLineNumber()));
    }
    
    public boolean[] parseBooleanList(String value) throws IOException
    {
        if ((value.length() < 2) || (value.charAt(0) != '{') || (value.charAt(value.length() - 1) != '}'))
        {
            throw (new IOException("Value is not a list in parameter " + fullParamName + " at line " + reader.getLineNumber()));
        }
        if (value.length() == 2)
        {
            return new boolean [0];
        }
        value = value.substring(1, value.length() - 1);
        int commas = 0;
        int i;
        for (i = 0; i < value.length(); ++i)
        {
            if (value.charAt(i) == ',')
            {
                ++commas;
            }
        }
        boolean [] result = new boolean [commas + 1];
        i = 0;
        do
        {
            commas = value.indexOf(',');
            if (commas > 0)
            {
                result[i] = parseBoolean(value.substring(0, commas));
                value = value.substring(commas + 1);
                ++i;
            }
        } while (commas > 0);
        result[i] = parseBoolean(value);
        return result;
    }
    
    public int parseEnum(String value, String [] valueList) throws IOException
    {
        int i = valueList.length;
        while (i > 0)
        {
            --i;
            if (valueList[i].equals(value))
            {
                return i;
            }
        }
        throw (new IOException("Unknown enumeration value in parameter " + fullParamName + " at line " + reader.getLineNumber()));
    }
    
    public void checkDetailsLength(int len) throws IOException
    {
        if (paramDetails.length != len)
        {
            throw (new IOException("Wrong details in parameter name: " + fullParamName + " at line " + reader.getLineNumber()));
        }
    }
    
    public void readStatus(Properties properties, SearchOptions searchOptions,
            EditCellArray cellArray, SearchCellArray searchCellArray,
            LineNumberReader reader) throws IOException
    {
        this.reader = reader;
        // find first section name
        String line = getLine();
        if (!PROPERTIES_SECTION_NAME.equals(line))
        {
            throw new IOException("Syntax error, section " + PROPERTIES_SECTION_NAME + " expected at line " + reader.getLineNumber());
        }
        line = getLine();
        while (prepareParameter(line))
        {
            if (!properties.readStatusParameter(this, paramName, paramDetails, paramValue))
            {
                throw (new IOException("Syntax error in parameter " + fullParamName + " at line " + reader.getLineNumber()));
            }
            line = getLine();
        }
        // not a parameter, probably next section
        String message = properties.finishReadStatus();
        if (null != message)
        {
            throw new IOException("Error found in section " + PROPERTIES_SECTION_NAME + ": " + message);
        }

        if (!OPTIONS_SECTION_NAME.equals(line))
        {
            throw new IOException("Syntax error, valid parameter or section " + OPTIONS_SECTION_NAME + " expected at line " + reader.getLineNumber());
        }
        line = getLine();
        while (prepareParameter(line))
        {
            if (!searchOptions.readStatusParameter(this, paramName, paramDetails, paramValue))
            {
                throw (new IOException("Syntax error in parameter " + fullParamName + " at line " + reader.getLineNumber()));
            }
            line = getLine();
        }
        // not a parameter, probably next section
        message = searchOptions.finishReadStatus();
        if (null != message)
        {
            throw new IOException("Error found in section " + OPTIONS_SECTION_NAME + ": " + message);
        }
        // up to now, everything is OK
        // let's start load contents
        cellArray.reconfigure(properties);
        searchCellArray.setOptions(searchOptions);
        if (!ARRAY_SECTION_NAME.equals(line))
        {
            throw new IOException("Syntax error, valid parameter or section " + ARRAY_SECTION_NAME + " expected at line " + reader.getLineNumber());
        }
        cellArray.incrementVersion();
        line = getLine();
        while (prepareParameter(line))
        {
            if (!cellArray.readStatusParameter(this, paramName, paramDetails, paramValue))
            {
                throw (new IOException("Syntax error in parameter " + fullParamName + " at line " + reader.getLineNumber()));
            }
            line = getLine();
        }
        // not a parameter, probably next section
        message = cellArray.finishReadStatus();
        if (null != message)
        {
            throw new IOException("Error found in section " + ARRAY_SECTION_NAME + ": " + message);
        }
        if (SEARCH_SECTION_NAME.equals(line))
        {
            searchCellArray.setArray(cellArray);
            line = getLine();
            while (prepareParameter(line))
            {
                if (!searchCellArray.readStatusParameter(this, paramName, paramDetails, paramValue))
                {
                    throw (new IOException("Syntax error in parameter " + fullParamName + " at line " + reader.getLineNumber()));
                }
                line = getLine();
            }
            // not a parameter, probably next section
            message = searchCellArray.finishReadStatus();
            if (null != message)
            {
                throw new IOException("Error found in section " + ARRAY_SECTION_NAME + ": " + message);
            }
        }
    }
}
