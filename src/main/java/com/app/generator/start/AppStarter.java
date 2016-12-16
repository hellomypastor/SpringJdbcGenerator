package com.app.generator.start;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import com.app.generator.Column;
import com.app.generator.db.DBManager;
import com.app.generator.db.ObjectCreator;

public class AppStarter
{
    public static void main(String[] args) throws FileNotFoundException, IOException
    {
        Properties props = new Properties();

        File file = new File("conf/app.properties");
        InputStream in = new FileInputStream(file);

        props.load(in);

        ObjectCreator objectCreator = new ObjectCreator(props);
        String javaDir = "src";
        new File("..", javaDir).mkdirs();
        DBManager manager = DBManager.getInstance(props);
        String table = props.getProperty("project.table.names");
        String tableName = table.toUpperCase();
        String modelName = "OemDbInstance";
        List<Column> columns = manager.getTableInfo(tableName);
        objectCreator.generateModel(columns, modelName);
        objectCreator.generateDAO(columns, modelName);
        objectCreator.generateDAOImpl(columns,modelName,tableName);
        objectCreator.generateService(columns, modelName);
        objectCreator.generateServiceimpl(columns, modelName);
        //objectCreator.createJUnit(modelName);
        //objectCreator.generateDAOBeansFile();
    }
}
