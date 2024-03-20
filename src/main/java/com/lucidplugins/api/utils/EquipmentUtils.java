package com.lucidplugins.api.utils;

import com.example.Packets.MousePackets;
import com.example.Packets.WidgetPackets;
import com.lucidplugins.api.item.SlottedItem;
import net.runelite.api.Item;
import net.runelite.api.widgets.Widget;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.List;
import java.util.stream.Collectors;

public class EquipmentUtils
{

    public static List<SlottedItem> getAll()
    {
        return Equipment.search().result().stream().map(equipmentItemWidget -> new SlottedItem(equipmentItemWidget.getEquipmentItemId(), equipmentItemWidget.getItemQuantity(), equipmentItemWidget.getEquipmentIndex())).collect(Collectors.toList());
    }

    public static List<SlottedItem> getAll(Predicate<SlottedItem> filter)
    {
        return Equipment.search().result().stream().map(equipmentItemWidget -> new SlottedItem(equipmentItemWidget.getEquipmentItemId(), equipmentItemWidget.getItemQuantity(), equipmentItemWidget.getEquipmentIndex())).filter(filter).collect(Collectors.toList());
    }

    public static Optional<EquipmentItemWidget> getItemInSlot() {
        return Equipment.search().filter(item -> {
            EquipmentItemWidget iw = (EquipmentItemWidget) item;
            return iw.getEquipmentIndex() == 3;
        }).first();
    }

    public static Item getWepSlotItem()
    {
        Optional<EquipmentItemWidget> weaponWidget = Equipment.search().filter(item -> {
            EquipmentItemWidget iw = (EquipmentItemWidget) item;
            return iw.getEquipmentIndex() == 3;
        }).first();

        return weaponWidget.map(equipmentItemWidget -> new Item(equipmentItemWidget.getEquipmentItemId(), equipmentItemWidget.getItemQuantity())).orElse(null);
    }

    public static boolean contains(int id)
    {
        return !Equipment.search().withId(id).result().isEmpty();
    }

    public static boolean contains(int[] ids)
    {
        List<Integer> intIdList = Arrays.stream(ids).boxed().collect(Collectors.toList());

        return !Equipment.search().idInList(intIdList).result().isEmpty();
    }

    public static boolean contains(String name)
    {
        return !Equipment.search().nameContains(name).result().isEmpty();
    }

    public static void removeWepSlotItem()
    {
        Widget itemWidget = Equipment.search().indexIs(3).first().orElse(null);

        if (itemWidget != null)
        {
            MousePackets.queueClickPacket();
            WidgetPackets.queueWidgetAction(itemWidget, "Remove");
        }
    }
}
