package com.builtbroken.atomic.lib;

import net.minecraft.util.IStringSerializable;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/23/2018.
 */
public enum MetaEnum implements IStringSerializable
{
    ZERO,
    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    NINE,
    TEN,
    ELEVEN,
    TWELVE,
    THIRTEEN,
    FOURTEEN,
    FIFTEEN;

    public static MetaEnum get(int meta)
    {
        if (meta >= 0 && meta < values().length)
        {
            return values()[meta];
        }
        return meta < 0 ? ZERO : FIFTEEN;
    }

    @Override
    public String getName()
    {
        return "" + ordinal();
    }
}
