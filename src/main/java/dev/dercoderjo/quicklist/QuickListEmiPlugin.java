package dev.dercoderjo.quicklist;

import dev.dercoderjo.quicklist.decoration.ToDoButtons;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;

public final class QuickListEmiPlugin implements EmiPlugin {
    @Override
    public void register(EmiRegistry emiRegistry) {
        emiRegistry.addRecipeDecorator(new ToDoButtons());
        System.out.println("Added To Do Button");
    }
}
