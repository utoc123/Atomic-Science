package com.builtbroken.atomic.content.machines.sensors.thermal;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.api.map.DataMapType;
import com.builtbroken.atomic.content.ASItems;
import com.builtbroken.atomic.content.prefab.BlockPrefab;
import com.builtbroken.atomic.lib.MetaEnum;
import com.builtbroken.atomic.map.events.MapSystemEvent;
import com.google.common.collect.Lists;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/23/2018.
 */
@Mod.EventBusSubscriber
public class BlockThermalRedstone extends BlockPrefab
{
    public static final PropertyEnum<MetaEnum> REDSTONE_PROPERTY = PropertyEnum.create("redstone", MetaEnum.class, Lists.newArrayList(MetaEnum.values()));

    public BlockThermalRedstone()
    {
        super(Material.IRON);
        setRegistryName(AtomicScience.PREFIX + "sensor_thermal_redstone");
        setTranslationKey(AtomicScience.PREFIX + "sensor.thermal.redstone");
        setDefaultState(getDefaultState().withProperty(REDSTONE_PROPERTY, MetaEnum.ZERO));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if(playerIn.getHeldItem(hand).getItem() == ASItems.itemHeatProbe)
        {
            return false;
        }
        if (!worldIn.isRemote)
        {
            playerIn.openGui(AtomicScience.INSTANCE, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side)
    {
        return true;
    }

    @Override
    public boolean canProvidePower(IBlockState state)
    {
        return true;
    }

    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return getRedstoneValue(blockState);
    }

    @Override
    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return getRedstoneValue(blockState);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(REDSTONE_PROPERTY, MetaEnum.get(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(REDSTONE_PROPERTY).ordinal();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, REDSTONE_PROPERTY);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityThermalRedstone();
    }

    protected static int getRedstoneValue(IBlockState blockState)
    {
        if (blockState.getPropertyKeys().contains(REDSTONE_PROPERTY))
        {
            return blockState.getValue(REDSTONE_PROPERTY).ordinal();
        }
        return 0;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onHeatChanged(MapSystemEvent.OnValueChanged event)
    {
        final World world = event.world();
        if (world != null && !world.isRemote && event.type == DataMapType.THERMAL && world.isBlockLoaded(event.getPos()))
        {
            final IBlockState blockState = world.getBlockState(event.getPos());
            if (blockState.getBlock() instanceof BlockThermalRedstone)
            {
                final TileEntity tile = world.getTileEntity(event.getPos());
                if (tile instanceof TileEntityThermalRedstone)
                {
                    ((TileEntityThermalRedstone) tile).updateRedstoneState(blockState, event.getNewValue());
                }
            }
        }
    }
}
