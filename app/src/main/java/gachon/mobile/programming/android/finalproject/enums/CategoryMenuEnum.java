package gachon.mobile.programming.android.finalproject.enums;

import android.content.Context;

import gachon.mobile.programming.android.finalproject.R;

/**
 * Created by JJSoft on 2017-05-29.
 */

public enum CategoryMenuEnum {
    ANDROID("001", R.string.android),
    JAVA("002", R.string.java),
    PYTHON("003", R.string.python),
    PHP("004", R.string.php),
    JAVASCRIPT("005", R.string.javascript),
    ETC("006", R.string.etc);

    private final String id;
    private final int name;

    CategoryMenuEnum(final String id, final int name) {
        this.id = id;
        this.name = name;
    }

    public String getValue() { return id; }
    public String getName(final Context context) {return context.getString(name); }

    public static String findNameByValue(final String id, final Context context) {
        for (CategoryMenuEnum CategoryMenuEnum : values()) {
            if (CategoryMenuEnum.getValue().equals(id)) {
                return CategoryMenuEnum.getName(context);
            }
        }
        return null;
    }

    public static String findValueByName(String name, Context context) {
        for (CategoryMenuEnum CategoryMenuEnum : values()) {
            if (CategoryMenuEnum.getName(context).equals(name)) {
                return CategoryMenuEnum.getValue();
            }
        }
        return null;
    }
}
