package dev.tomefix.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Quark (Ancient Tomes) hooks EnchantmentHelper.getComponentType and reads the
 * stack's components, which Enchiridion hooks to call getComponentType again,
 * which Quark hooks again... forever -> StackOverflowError.
 *
 * This wraps the whole method with a re-entrancy guard. A nested call on the
 * same thread skips the other mods' hooks and returns the vanilla answer,
 * which breaks the cycle.
 */
@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {

    private static final ThreadLocal<Boolean> TOMEFIX_ACTIVE = ThreadLocal.withInitial(() -> false);

    @WrapMethod(method = "getComponentType")
    private static DataComponentType<ItemEnchantments> tomefix$guard(
            ItemStack stack, Operation<DataComponentType<ItemEnchantments>> original) {
        if (TOMEFIX_ACTIVE.get()) {
            return stack.is(Items.ENCHANTED_BOOK)
                    ? DataComponents.STORED_ENCHANTMENTS
                    : DataComponents.ENCHANTMENTS;
        }
        TOMEFIX_ACTIVE.set(true);
        try {
            return original.call(stack);
        } finally {
            TOMEFIX_ACTIVE.set(false);
        }
    }
}
