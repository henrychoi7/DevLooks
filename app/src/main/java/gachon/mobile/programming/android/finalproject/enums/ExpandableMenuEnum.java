package gachon.mobile.programming.android.finalproject.enums;

public enum ExpandableMenuEnum {
    GROUP(0);
    //CHILD(1);

    private final int typeValue;

    ExpandableMenuEnum(final int viewType) {
        this.typeValue = viewType;
    }

    public int getTypeValue() {
        return typeValue;
    }
}
