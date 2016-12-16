package com.app.generator.db;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

public class FileCreator
{
    private Properties props;
    private String javaSrcPath;

    public FileCreator(Properties props)
    {
        this.props = props;
        javaSrcPath = (String) props.get("project.path") + File.separator + "src/main/java";
    }

    public void createModel(String modelName, String modelContent)
    {
        String modelPath = props.getProperty("project.base.package").replace(".", File.separator);
        String javaFile = javaSrcPath + File.separator + modelPath + File.separator + "model" + File.separator
                + modelName + ".java";
        File modelFile = new File(javaFile);
        try
        {
            if (!modelFile.exists())
            {
                FileUtils.touch(modelFile);
            }
            FileWriter fileWriter = new FileWriter(modelFile);
            BufferedWriter bufferWritter = new BufferedWriter(fileWriter);
            bufferWritter.write(modelContent);
            bufferWritter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void createDAO(String modelName, String daoContent)
    {
        String daoPath = props.getProperty("project.base.package").replace(".", File.separator);
        String javaFile = javaSrcPath + File.separator + daoPath + File.separator + "dao" + File.separator + modelName
                + "DAO.java";
        File daoFile = new File(javaFile);
        try
        {
            if (!daoFile.exists())
            {
                FileUtils.touch(daoFile);
            }
            FileWriter fileWriter = new FileWriter(daoFile);
            BufferedWriter bufferWritter = new BufferedWriter(fileWriter);
            bufferWritter.write(daoContent);
            bufferWritter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void createDAOImpl(String modelName, String daoImplContent)
    {
        String daoImplPath = props.getProperty("project.base.package").replace(".", File.separator);
        String javaFile = javaSrcPath + File.separator + daoImplPath + File.separator + "dao" + File.separator + "impl"
                + File.separator + modelName + "DAOImpl.java";
        File daoImplFile = new File(javaFile);
        try
        {
            if (!daoImplFile.exists())
            {
                FileUtils.touch(daoImplFile);
            }
            FileWriter fileWriter = new FileWriter(daoImplFile);
            BufferedWriter bufferWritter = new BufferedWriter(fileWriter);
            bufferWritter.write(daoImplContent);
            bufferWritter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void createService(String modelName, String serviceContent)
    {
        String servicePath = props.getProperty("project.base.package").replace(".", File.separator);
        String javaFile = javaSrcPath + File.separator + servicePath + File.separator + "service" + File.separator
                + modelName + "Service.java";
        File serviceFile = new File(javaFile);
        try
        {
            if (!serviceFile.exists())
            {
                FileUtils.touch(serviceFile);
            }
            FileWriter fileWriter = new FileWriter(serviceFile);
            BufferedWriter bufferWritter = new BufferedWriter(fileWriter);
            bufferWritter.write(serviceContent);
            bufferWritter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void createServiceImpl(String modelName, String serviceImplContent)
    {
        String serviceImplPath = props.getProperty("project.base.package").replace(".", File.separator);
        String javaFile = javaSrcPath + File.separator + serviceImplPath + File.separator + "service" + File.separator
                + "impl" + File.separator + modelName + "ServiceImpl.java";
        File serviceImplFile = new File(javaFile);
        try
        {
            if (!serviceImplFile.exists())
            {
                FileUtils.touch(serviceImplFile);
            }
            FileWriter fileWriter = new FileWriter(serviceImplFile);
            BufferedWriter bufferWritter = new BufferedWriter(fileWriter);
            bufferWritter.write(serviceImplContent);
            bufferWritter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
