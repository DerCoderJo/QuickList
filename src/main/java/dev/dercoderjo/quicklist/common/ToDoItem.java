package dev.dercoderjo.quicklist.common;

import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ToDoItem {
    private final Identifier identifier;
    private long amount;
    private final EmiStack stack;
    private final int craftAmount;

    public ToDoItem(EmiStack stack, int craftAmount) {
        this.identifier = stack.getId();
        this.amount = stack.getAmount();
        this.stack = stack;
        this.craftAmount = craftAmount;
    }


    public Identifier getIdentifier() {
        return identifier;
    }

    public long getAmount() {
        return amount;
    }
    public long addAmount(long amount) {
        return this.amount += amount;
    }

    public void render(DrawContext draw, int x, int y, float delta) {
        stack.render(draw, x, y, delta);
    }

    public Text getName() {
        return stack.getName();
    }

    public ItemStack getItemStack() {
        return stack.getItemStack();
    }

    public int getCraftAmount() {
        return craftAmount;
    }
}
