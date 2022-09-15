package net.powerscale.logic;

import net.minecraft.world.World;
import net.powerscale.PowerScale;
import net.powerscale.config.Config;

import java.util.ArrayList;
import java.util.List;

public class PerPlayerDifficulty {
    public static List<Config.AttributeModifier> getAttributeModifiers(PatternMatching.EntityData entityData, World world) {
        var playerCount = world.getServer().getPlayerManager().getPlayerList().size(); // world.getPlayers().size();
        if (playerCount < 2) {
            return List.of();
        }
        var perPlayer = PowerScale.configManager.currentConfig.perPlayerDifficulty;
        if (perPlayer == null || perPlayer.entities == null || perPlayer.entities.length == 0) {
            return List.of();
        }

        float multiplier = playerCount - 1;
        var attributeModifiers = new ArrayList<Config.AttributeModifier>();
        for(var entityBaseModifier: perPlayer.entities) {
            if (entityData.matches(entityBaseModifier.entity_matches)) {
                for(var baseAttributeModifier: entityBaseModifier.attributes) {
                    var attributeModifier = new Config.AttributeModifier();
                    attributeModifier.attribute = baseAttributeModifier.attribute;
                    attributeModifier.value = 1.0F + (multiplier * baseAttributeModifier.value);
                    attributeModifier.operation = Config.Operation.MULTIPLY;
                    attributeModifiers.add(attributeModifier);
                }
            }
        }
        return attributeModifiers;
    }
}
