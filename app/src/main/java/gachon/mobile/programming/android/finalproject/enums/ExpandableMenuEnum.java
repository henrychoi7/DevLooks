package gachon.mobile.programming.android.finalproject.enums;

/**
 * Created by JJSOFT-DESKTOP on 2017-05-25.
 */

public enum ExpandableMenuEnum {
    GROUP(0),
    CHILD(1);

    private final int typeValue;

    ExpandableMenuEnum(int viewType) {
        this.typeValue = viewType;
    }

    public int getTypeValue() {
        return typeValue;
    }
}
