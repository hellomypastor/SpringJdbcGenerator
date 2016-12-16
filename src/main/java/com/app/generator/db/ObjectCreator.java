package com.app.generator.db;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.app.generator.Column;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class ObjectCreator
{
    private Properties props;
    private FileCreator fileCreator;

    public ObjectCreator(Properties props)
    {
        this.props = props;
        this.fileCreator = new FileCreator(props);
    }

    public void generateModel(final List<Column> cols, final String model)
    {
        String modelField = "\t\tprivate {colType} {colName};\n";
        String getter = "\t\tpublic {colType} {colGetter} {return colName;}\n";
        String setter = "\t\tpublic void {colSetter}({colType} {colName}) {this.{colName} = {colName};}\n";

        StringBuilder fieldBuilder = new StringBuilder();
        StringBuilder getterBuilder = new StringBuilder();
        StringBuilder setterBuilder = new StringBuilder();

        for (Column column : cols)
        {
            String tempModelField = modelField.replaceAll("\\{colType\\}", column.getType());
            tempModelField = tempModelField.replaceAll("\\{colName\\}", column.getName());
            fieldBuilder.append(tempModelField);
            String tempGetter = getter.replaceAll("\\{colType\\}", column.getType());
            tempGetter = tempGetter.replaceAll("\\{colGetter\\}", column.getGetter());
            tempGetter = tempGetter.replaceAll("colName", column.getName());
            getterBuilder.append(tempGetter);
            String tempSetter = setter.replaceAll("\\{colType\\}", column.getType());
            tempSetter = tempSetter.replaceAll("\\{colSetter\\}", column.getSetter());
            tempSetter = tempSetter.replaceAll("\\{colName\\}", column.getName());
            setterBuilder.append(tempSetter);
        }

        Map<String, Object> map = new HashMap<String, Object>();
        putNotNullInMap(map, "basePackage", props.getProperty("project.base.package"));
        putNotNullInMap(map, "modelClass", model);
        putNotNullInMap(map, "modelFields", fieldBuilder.toString());
        putNotNullInMap(map, "getter", getterBuilder.toString());
        putNotNullInMap(map, "setter", setterBuilder.toString());
        String resultString = processToString("template/model.tmpl", map);
        fileCreator.createModel(model, resultString);
    }

    public void generateDAO(final List<Column> cols, final String model)
    {
        List<String> primaryKeys = new ArrayList<String>();
        for (Column column : cols)
        {
            if (column.ispk)
            {
                primaryKeys.add(column.getType() + " " + column.getName());
            }
        }
        String primaryKey = "";
        for (String str : primaryKeys)
        {
            primaryKey += str + ", ";
        }
        primaryKey = primaryKey.substring(0, primaryKey.length() - 2);
        Map<String, Object> map = new HashMap<String, Object>();
        putNotNullInMap(map, "basePackage", props.getProperty("project.base.package"));
        putNotNullInMap(map, "modelClass", model);
        putNotNullInMap(map, "primaryKeyDef", primaryKey);
        String resultString = processToString("template/dao.tmpl", map);
        fileCreator.createDAO(model, resultString);
    }

    public void generateService(final List<Column> cols, final String model)
    {
        String modelPrefix = model;
        List<String> primaryKeys = new ArrayList<String>();
        for (Column column : cols)
        {
            if (column.ispk)
            {
                primaryKeys.add(column.getType() + " " + column.getName());
            }
        }
        String primaryKey = "";
        for (String str : primaryKeys)
        {
            primaryKey += str + ", ";
        }

        primaryKey = primaryKey.substring(0, primaryKey.length() - 2);
        Map<String, Object> map = new HashMap<String, Object>();
        putNotNullInMap(map, "basePackage", props.getProperty("project.base.package"));
        putNotNullInMap(map, "modelClass", model);
        putNotNullInMap(map, "modelPrefix", modelPrefix);
        putNotNullInMap(map, "primaryKeyDef", primaryKey);
        String resultString = processToString("template/service.tmpl", map);
        fileCreator.createService(model, resultString);
    }

    public void generateServiceimpl(final List<Column> cols, final String model)
    {
        String modelPrefix = model;
        List<String> primaryKeys = new ArrayList<String>();
        for (Column column : cols)
        {
            if (column.ispk)
            {
                primaryKeys.add(column.getType() + " " + column.getName());
            }
        }
        String primaryKey = "";
        for (String str : primaryKeys)
        {
            primaryKey += str + ", ";
        }

        primaryKey = primaryKey.substring(0, primaryKey.length() - 2);
        Map<String, Object> map = new HashMap<String, Object>();
        putNotNullInMap(map, "basePackage", props.getProperty("project.base.package"));
        putNotNullInMap(map, "modelClass", model);
        putNotNullInMap(map, "modelPrefix", modelPrefix);
        putNotNullInMap(map, "primaryKeyDef", primaryKey);
        String resultString = processToString("template/serviceimpl.tmpl", map);
        fileCreator.createServiceImpl(model, resultString);
    }

    private String updateQueryValueHolder(Column column)
    {
        if (column.isIspk())
        {
            return "valueArr.add(entity." + column.getGetter() + ");\n";
        }
        else
        {
            return "";
        }
    }

    private String whereClauseValueHolder(Column column)
    {
        if (StringUtils.equals("Timestamp", column.getType()))
        {
            return "if(entity." + column.getGetter() + "!=null){valueArr.add(entity." + column.getGetter() + ");}";
        }
        else if (StringUtils.equals("String", column.getType()))
        {
            return "if(StringUtils.isNotBlank(entity." + column.getGetter() + ")){valueArr.add(entity."
                    + column.getGetter() + ");}";
        }
        else
        {
            return "if(entity." + column.getGetter() + "!=0){valueArr.add(entity." + column.getGetter() + ");}";
        }
    }

    private String whereClausePreparator(Column column)
    {
        if (StringUtils.equals("Timestamp", column.getType()))
        {
            return "if(entity."
                    + column.getGetter()
                    + "!=null){if (isFirst) {query.append(\" WHERE \");isFirst = false;}else{query.append(\" AND \");}query.append(\""
                    + column.getDbname() + " = ?\");}";
        }
        else if (StringUtils.equals("String", column.getType()))
        {
            return "if(StringUtils.isNotBlank(entity."
                    + column.getGetter()
                    + ")){if (isFirst) {query.append(\" WHERE \");isFirst = false;}else{query.append(\" AND \");}query.append(\""
                    + column.getDbname() + " = ?\");}";
        }
        else
        {
            return "if(entity."
                    + column.getGetter()
                    + "!=0){if (isFirst) {query.append(\" WHERE \");isFirst = false;}else{query.append(\" AND \");}query.append(\""
                    + column.getDbname() + " = ?\");}";
        }
    }

    public void generateDAOImpl(final List<Column> cols, final String model, String tableName)
    {
        String modelPrefix = model.substring(0, 1).toLowerCase() + model.substring(1);

        List<String> insertQueryPlaceHolder = new ArrayList<String>();
        List<String> updateQueryPlaceHolder = new ArrayList<String>();
        StringBuilder whereClauseBuilder = new StringBuilder();
        StringBuilder likeWhereClauseBuilder = new StringBuilder();
        StringBuilder whereClauseValueHolderBuilder = new StringBuilder();
        StringBuilder updateQueryValueHolderBuilder = new StringBuilder();
        StringBuilder insertQueryValueHolderBuilder = new StringBuilder();
        List<String> primaryKeys = new ArrayList<String>();
        List<String> primaryKeysVal = new ArrayList<String>();
        List<Column> primaryKeyColumns = new ArrayList<Column>();
        List<String> columnNames = new ArrayList<String>();
        List<String> primaryKeyQueryPlaceHolder = new ArrayList<String>();
        StringBuilder fieldConstants = new StringBuilder();
        StringBuilder resultSetFetcherBuilder = new StringBuilder();

        for (Column column : cols)
        {
            columnNames.add(column.getDbname());

            if (StringUtils.equals("Timestamp", column.getType()))
            {
                insertQueryPlaceHolder.add("?");
                insertQueryValueHolderBuilder.append("\t\tvalueArr.add(entity." + column.getGetter() + ");\n");
                updateQueryPlaceHolder.add(column.getDbname() + " = ?");
                updateQueryValueHolderBuilder.append(updateQueryValueHolder(column));
            }
            else if (StringUtils.equals("String", column.getType()))
            {
                insertQueryPlaceHolder.add("?");
                insertQueryValueHolderBuilder.append("\t\tvalueArr.add(entity." + column.getGetter() + ");\n");

                if (column.isIspk())
                {
                    primaryKeyQueryPlaceHolder.add(column.getDbname() + " = ? ");
                    primaryKeyColumns.add(column);
                    primaryKeys.add(column.getType() + " " + column.getName());
                    primaryKeysVal.add(column.getName());
                }
                else
                {
                    updateQueryPlaceHolder.add(column.getDbname() + " = ? ");
                    updateQueryValueHolderBuilder.append(updateQueryValueHolder(column));
                }
            }
            else
            {
                insertQueryPlaceHolder.add("?");
                insertQueryValueHolderBuilder.append("\t\tvalueArr.add(entity." + column.getGetter() + ");\n");
                if (column.isIspk())
                {
                    primaryKeyQueryPlaceHolder.add(column.getDbname() + " = ? ");
                    primaryKeys.add(column.getType() + " " + column.getName());
                    primaryKeyColumns.add(column);
                    primaryKeysVal.add(column.getName());
                }
                else
                {
                    updateQueryPlaceHolder.add(column.getDbname() + " = ? ");
                    updateQueryValueHolderBuilder.append(updateQueryValueHolder(column));
                }
            }

            whereClauseValueHolderBuilder.append(whereClauseValueHolder(column));
            whereClauseBuilder.append(whereClausePreparator(column));
            //likeWhereClauseBuilder.append(likeWhereClausePreparator(column));

            //            fieldConstants.append("\t\t public static final String " + column.getDbname() + " = \""
            //                    + column.getDbname() + "\";\n".toString());

            resultSetFetcherBuilder.append("\t\t "
                    + modelPrefix
                    + ".set"
                    + com.app.generator.StringUtils.firstCaps(com.app.generator.StringUtils.replaceUnderscore(column
                            .getDbname())) + "(rs.get" + StringUtils.capitalise(column.getType()) + "(\""
                    + column.getDbname() + "\"));\n");
        }

        for (Column column : primaryKeyColumns)
        {
            updateQueryValueHolderBuilder.append("valueArr.add(entity." + column.getGetter() + ");\n");
        }

        String selectSql = "SELECT * FROM " + tableName;
        String insertSql = "INSERT INTO " + tableName + "(" + StringUtils.join(columnNames, ",") + ") VALUES ("
                + StringUtils.join(insertQueryPlaceHolder, ",") + ")";
        String deleteSql = "";
        String updateSql = "";
        String primaryKeyDef = StringUtils.join(primaryKeys, ",");
        String countSql = "SELECT count(0) FROM " + tableName;
        String existsSql = "SELECT 1 FROM " + tableName;
        String modelObject = modelPrefix;

        if (primaryKeyQueryPlaceHolder.isEmpty())
        {
            updateSql = "UPDATE " + tableName + " SET " + StringUtils.join(updateQueryPlaceHolder, ",");
        }
        else
        {
            updateSql = "UPDATE " + tableName + " SET " + StringUtils.join(updateQueryPlaceHolder, ",") + " WHERE "
                    + StringUtils.join(primaryKeyQueryPlaceHolder, ",");
            deleteSql = "DELETE FROM " + tableName + " WHERE " + StringUtils.join(primaryKeyQueryPlaceHolder, ",");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        putNotNullInMap(map, "basePackage", props.getProperty("project.base.package"));
        putNotNullInMap(map, "modelClass", model);
        putNotNullInMap(map, "modelNamePrefix", modelPrefix);
        putNotNullInMap(map, "existsSql", existsSql);
        putNotNullInMap(map, "selectSql", selectSql);
        putNotNullInMap(map, "insertSql", insertSql);
        putNotNullInMap(map, "updateSql", updateSql);
        putNotNullInMap(map, "deleteSql", deleteSql);
        putNotNullInMap(map, "countSql", countSql);
        putNotNullInMap(map, "whereClauseBlock", whereClauseBuilder.toString());
        putNotNullInMap(map, "whereClauseLikeBlock", likeWhereClauseBuilder.toString());
        putNotNullInMap(map, "selectValArrPrepBlock", whereClauseValueHolderBuilder.toString());
        putNotNullInMap(map, "insertValBlock", insertQueryValueHolderBuilder.toString());
        putNotNullInMap(map, "updateValBlock", updateQueryValueHolderBuilder.toString());
        //        putNotNullInMap(map, "mapperConstants", fieldConstants.toString());
        putNotNullInMap(map, "modelObject", modelObject);
        putNotNullInMap(map, "beanPopulation", resultSetFetcherBuilder);

        putNotNullInMap(map, "primaryKeyDef", primaryKeyDef);
        putNotNullInMap(map, "primaryKeyVal", StringUtils.join(primaryKeysVal, ","));
        String resultString = processToString("template/daoimpl.tmpl", map);
        fileCreator.createDAOImpl(model, resultString);
    }

    @SuppressWarnings("rawtypes")
    public String processToString(String templateFile, Map map)
    {
        try
        {
            Template template = new Configuration().getTemplate(templateFile);
            template.setEncoding("UTF-8");

            StringWriter sw = new StringWriter();

            template.process(map, sw);
            return sw.getBuffer().toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void putNotNullInMap(Map map, Object key, Object value)
    {
        if (map != null && key != null && value != null)
        {
            map.put(key, value.toString());
        }
    }

}
