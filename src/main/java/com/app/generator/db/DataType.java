package com.app.generator.db;

public enum DataType
{
    BIGDECIMAL("long"), TIMESTAMP("String"), STRING("String"), INTEGER("int"), FLOAT("float");

    private String javaType;

    private DataType(final String javType)
    {
        this.javaType = javType;
    }

    @Override
    public String toString()
    {
        return javaType;
    }
}
