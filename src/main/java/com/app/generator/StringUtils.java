package com.app.generator;

public class StringUtils
{
    public static String firstCaps(String in)
    {
        if ((in != null) && (in.length() > 1))
            return in.substring(0, 1).toUpperCase() + in.substring(1);
        return null;
    }

    public static String firstSmall(String in)
    {
        if ((in != null) && (in.length() > 1))
            return in.substring(0, 1).toLowerCase() + in.substring(1);
        return null;
    }

    public static String replaceUnderscore(String in)
    {

        if ((in != null) && (in.length() > 1))
        {
            char[] ch = in.toCharArray();
            char[] out = new char[ch.length];
            int cnt = 0;

            for (int i = 0; i < ch.length; i++)
            {
                if ((ch[i] == '_') && (i > 0))
                {
                    out[cnt] = Character.toUpperCase(ch[++i]);
                }
                else
                {
                    out[cnt] = ch[i];
                }
                cnt++;
            }
            return new String(out).trim();
        }
        return null;
    }

    public static void main(String[] args)
    {
        System.out.println(replaceUnderscore("abc_def"));
    }
}
