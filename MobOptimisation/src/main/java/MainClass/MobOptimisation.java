package MainClass;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Beehive;
import org.bukkit.entity.*;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class MobOptimisation extends JavaPlugin {

    MobOptimisation mobOptimisation;

    @Override
    public void onEnable() {

        mobOptimisation = this;
        setScheduledTasks();

    }

    private void setScheduledTasks() {

        new BukkitRunnable() {
            @Override
            public void run() {
                fillBeeHives();
            }
        }.runTaskTimer(mobOptimisation, 1L, 20L * 600);


        new BukkitRunnable() {
            @Override
            public void run() {
                resetVillagerTrades();
            }
        }.runTaskTimer(mobOptimisation, 20L * 300, 20L * 1200);


        new BukkitRunnable() {
            @Override
            public void run() {
                resetWool();
            }
        }.runTaskTimer(mobOptimisation, 20L * 600, 20L * 1200);
    }

    //Give sheep their wool back

    private void resetWool() {

        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {

                if (entity instanceof Sheep) {

                    if (!(((Sheep) entity).hasAI())) {
                        continue;
                    }

                    if (((Sheep) entity).isSheared()) {

                        ((Sheep) entity).setSheared(false);

                    }
                }
            }
        }
    }

//Reset villager trades

    private void resetVillagerTrades() {

        for (World world : Bukkit.getWorlds()) {

            for (Entity entity : world.getEntities()) {

                if (entity instanceof Villager) {

                    if (!(((Villager) entity).hasAI())) {
                        continue;
                    }

                    List<MerchantRecipe> recipes = ((Villager) entity).getRecipes();

                    for (MerchantRecipe recipe : recipes) {
                        recipe.setUses(0);
                    }
                }
            }
        }
    }


    //Fill be hives
    private void fillBeeHives() {

        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {

                if (entity instanceof Bee) {

                    if (((Bee) entity).getHive() != null && !(((Bee) entity).hasAI())) {
                        Block block = ((Bee) entity).getHive().getBlock();

                        if (block.getBlockData() instanceof org.bukkit.block.data.type.Beehive) {

                            if (((Beehive) block.getBlockData()).getHoneyLevel() < ((Beehive) block.getBlockData()).getMaximumHoneyLevel()) {

                                BlockData blockData = block.getBlockData();

                                ((Beehive) blockData).setHoneyLevel(((Beehive) blockData).getHoneyLevel() + 1);

                                block.setBlockData(blockData);
                            }
                        }
                    }
                }

            }
        }
    }
}
