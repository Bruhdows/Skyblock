package me.bruhdows.skyblock.api.gui.content;

import lombok.Getter;

@Getter
public class SlotPos {

    private final int row;
    private final int column;

    public SlotPos(int row, int column) {
        this.row = row;
        this.column = column;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null || getClass() != obj.getClass())
            return false;

        SlotPos slotPos = (SlotPos) obj;

        return row == slotPos.row && column == slotPos.column;
    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + column;

        return result;
    }

    public static SlotPos of(int row, int column) {
        return new SlotPos(row, column);
    }

    public static SlotPos of(int slot) {
        int row = slot / 9;
        int column = slot - (row * 9);
        return new SlotPos(row, column);
    }

}
