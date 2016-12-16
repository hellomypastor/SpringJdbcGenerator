package com.app.generator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileUtils
{
    public static final String BASE_DIR = "/home/amit/project_work/DaoGenerator";
    public static final String TEMPLATE_DIR = BASE_DIR + "/template/";
    public static final String OUTPUT_DIR_MAIN_CODE = BASE_DIR + "/src/main/java/";
    public static final String OUTPUT_DIR_TESTING_CODE = BASE_DIR + "/src/test/java/";

    public static String getTemplate(final String file) throws Exception
    {
        BufferedReader br = null;
        try
        {
            br = new BufferedReader(new FileReader(TEMPLATE_DIR + file));
            StringBuffer buff = new StringBuffer();
            String str = null;

            while ((str = br.readLine()) != null)
            {
                buff.append(str).append("\n");
            }

            return buff.toString();
        }
        finally
        {
            if (br != null)
            {
                br.close();
            }
        }
    }

    public static void writeMainCode(final String file, final String content) throws Exception
    {
        String parentDirName = file.substring(0, file.lastIndexOf(File.separator));
        File parentDir = new File(OUTPUT_DIR_MAIN_CODE + File.separator + parentDirName);
        parentDir.mkdir();

        PrintWriter pw = null;
        File fileTowrite = new File(OUTPUT_DIR_MAIN_CODE + File.separator + file);
        try
        {
            pw = new PrintWriter(new FileWriter(fileTowrite));
            pw.print(content);
            pw.flush();
        }
        catch (IOException ioex)
        {
            ioex.printStackTrace();
        }
        finally
        {
            if (pw != null)
            {
                pw.close();
            }
        }
    }

    public static void writeTestingCode(final String file, final String content) throws Exception
    {
        String parentDirName = file.substring(0, file.lastIndexOf(File.separator));
        File parentDir = new File(OUTPUT_DIR_TESTING_CODE + File.separator + parentDirName);
        parentDir.mkdir();

        PrintWriter pw = null;
        File fileTowrite = new File(OUTPUT_DIR_TESTING_CODE + File.separator + file);
        try
        {
            pw = new PrintWriter(new FileWriter(fileTowrite));
            pw.print(content);
            pw.flush();
        }
        catch (IOException ioex)
        {
            ioex.printStackTrace();
        }
        finally
        {
            if (pw != null)
            {
                pw.close();
            }
        }
    }
}
