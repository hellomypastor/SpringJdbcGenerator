package com.app.generator;

public class Column
{
    public String dbname;
    public String name;
    public String type;
    public int length;
    public String getter;
    public String setter;
    public boolean ispk;

    public String getDbname()
    {
        return dbname;
    }

    public void setDbname(String dbname)
    {
        this.dbname = dbname;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public int getLength()
    {
        return length;
    }

    public void setLength(int length)
    {
        this.length = length;
    }

    public String getGetter()
    {
        return getter;
    }

    public void setGetter(String getter)
    {
        this.getter = getter;
    }

    public String getSetter()
    {
        return setter;
    }

    public void setSetter(String setter)
    {
        this.setter = setter;
    }

    public boolean isIspk()
    {
        return ispk;
    }

    public void setIspk(boolean ispk)
    {
        this.ispk = ispk;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Column [dbname=").append(dbname).append(", name=").append(name).append(", type=").append(type)
                .append(", length=").append(length).append(", getter=").append(getter).append(", setter=")
                .append(setter).append(", ispk=").append(ispk).append("]");
        return builder.toString();
    }

}
