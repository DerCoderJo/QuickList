package dev.dercoderjo.quicklist.decoration;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeDecorator;
import dev.emi.emi.api.widget.WidgetHolder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class ToDoButtons implements EmiRecipeDecorator {
    Gson gson = new Gson();

    @Override
    public void decorateRecipe(EmiRecipe emiRecipe, WidgetHolder widgetHolder) {
        if (emiRecipe.getId() != null) {
            int middleHeight = emiRecipe.getDisplayHeight() / 2;
            widgetHolder.addButton(-17, middleHeight - 13, 12, 12, 0, 0, Identifier.of("quicklist", "textures/gui/buttons.png"), () -> true, (mouseX, mouseY, button) -> {
                File todo = new File(FabricLoader.getInstance().getConfigDir().toFile(), "quicklist/todo.json");
                File configDir = todo.getParentFile();
                if (!configDir.exists()) {
                    configDir.mkdirs();
                }

                if (!todo.exists()) {
                    JsonObject jsonObject = new JsonObject();
                    try (FileWriter writer = new FileWriter(todo)) {
                        gson.toJson(jsonObject, writer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                try (FileReader reader = new FileReader(todo)) {
                    JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
                    String worldPath;
                    try {
                        worldPath = Objects.requireNonNull(MinecraftClient.getInstance().getServer()).getSavePath(WorldSavePath.ROOT).toFile().getParent();
                    } catch (NullPointerException e) {
                        worldPath = Objects.requireNonNull(MinecraftClient.getInstance().getCurrentServerEntry()).address;
                    }
                    String worldName = worldPath.substring(worldPath.lastIndexOf("\\") + 1);
                    if (!jsonObject.has(worldName)) {
                        jsonObject.add(worldName, new JsonArray());
                    }

                    JsonArray worldArray = jsonObject.get(worldName).getAsJsonArray();
                    boolean found = false;
                    for (int i = 0; i < worldArray.size(); i++) {
                        JsonObject itemObject = worldArray.get(i).getAsJsonObject();
                        if (Objects.equals(itemObject.get("id").getAsString(), emiRecipe.getId().toString())) {
                            JsonElement amountElement = itemObject.get("amount");
                            int amount = amountElement.getAsInt();
                            amount += 1;
                            itemObject.addProperty("amount", amount);

                            found = true;
                        }
                    }
                    if (!found) {
                        JsonObject itemObject = new JsonObject();
                        itemObject.addProperty("id", emiRecipe.getId().toString());
                        itemObject.addProperty("amount", 1);
                        worldArray.add(itemObject);
                    }

                    try (FileWriter writer = new FileWriter(todo)) {
                        gson.toJson(jsonObject, writer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            widgetHolder.addButton(-17, middleHeight + 1, 12, 12, 12, 0, Identifier.of("quicklist", "textures/gui/buttons.png"), () -> {
                File todo = new File(FabricLoader.getInstance().getConfigDir().toFile(), "quicklist/todo.json");
                File configDir = todo.getParentFile();
                if (configDir.exists() && todo.exists()) {
                    try (FileReader reader = new FileReader(todo)) {
                        JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
                        String worldPath;
                        try {
                            worldPath = Objects.requireNonNull(MinecraftClient.getInstance().getServer()).getSavePath(WorldSavePath.ROOT).toFile().getParent();
                        } catch (NullPointerException e) {
                            worldPath = Objects.requireNonNull(MinecraftClient.getInstance().getCurrentServerEntry()).address;
                        }
                        String worldName = new File(worldPath).getName();
                        if (!jsonObject.has(worldName)) {
                            jsonObject.add(worldName, new JsonArray());
                        }

                        JsonArray worldArray = jsonObject.get(worldName).getAsJsonArray();
                        for (int i = 0; i < worldArray.size(); i++) {
                            JsonObject itemObject = worldArray.get(i).getAsJsonObject();
                            String itemId = itemObject.get("id").getAsString();

                            if (itemId.equals(emiRecipe.getId().toString())) {
                                return true;
                            }
                        }

                        try (FileWriter writer = new FileWriter(todo)) {
                            gson.toJson(jsonObject, writer);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                return false;
            }, (mouseX, mouseY, button) -> {
                File todo = new File(FabricLoader.getInstance().getConfigDir().toFile(), "quicklist/todo.json");
                File configDir = todo.getParentFile();
                if (!configDir.exists()) {
                    configDir.mkdirs();
                }

                if (!todo.exists()) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.add("singleplayer", new JsonObject());
                    jsonObject.add("multiplayer", new JsonObject());

                    try (FileWriter writer = new FileWriter(todo)) {
                        gson.toJson(jsonObject, writer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                try (FileReader reader = new FileReader(todo)) {
                    JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
                    String worldPath;
                    try {
                        worldPath = Objects.requireNonNull(MinecraftClient.getInstance().getServer()).getSavePath(WorldSavePath.ROOT).toFile().getParent();
                    } catch (NullPointerException e) {
                        worldPath = Objects.requireNonNull(MinecraftClient.getInstance().getCurrentServerEntry()).address;
                    }
                    String worldName = worldPath.substring(worldPath.lastIndexOf("\\") + 1);
                    if (!jsonObject.has(worldName)) {
                        jsonObject.add(worldName, new JsonArray());
                    }

                    JsonArray worldArray = jsonObject.get(worldName).getAsJsonArray();
                    for (int i = 0; i < worldArray.size(); i++) {
                        JsonObject itemObject = worldArray.get(i).getAsJsonObject();
                        if (Objects.equals(itemObject.get("id").getAsString(), emiRecipe.getId().toString())) {
                            JsonElement amountElement = itemObject.get("amount");
                            int amount = amountElement.getAsInt();
                            amount -= 1;
                            if (amount <= 0) {
                                worldArray.remove(i);
                            } else {
                                itemObject.addProperty("amount", amount);
                            }
                        }
                    }

                    try (FileWriter writer = new FileWriter(todo)) {
                        gson.toJson(jsonObject, writer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
