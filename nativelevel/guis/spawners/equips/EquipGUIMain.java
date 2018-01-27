package nativelevel.guis.spawners.equips;

import nativelevel.utils.GUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class EquipGUIMain extends GUI {

    CreatureSpawner spawner;
    Chest chest;

    public EquipGUIMain(CreatureSpawner spawner) {
        super(Bukkit.createInventory(null, 45, "§3MobSPAWNAER, Equip's"), spawner);
        this.spawner = (CreatureSpawner) spawner.getBlock().getState();

        //TODO NÃO DEIXAR NEGO BOTAR MOBSPAWNER EM Y MENOR QUE 6

        if (!spawner.getWorld().getBlockAt(spawner.getLocation().add(0, -3, 0)).getType().equals(Material.SPONGE)) {
            spawner.getWorld().getBlockAt(spawner.getLocation().add(0, -3, 0)).setType(Material.SPONGE);
        }

        if (!spawner.getWorld().getBlockAt(spawner.getLocation().add(0, -2, 0)).getType().equals(Material.CHEST)) {
            spawner.getWorld().getBlockAt(spawner.getLocation().add(0, -2, 0)).setType(Material.CHEST);
        }

        if (!spawner.getWorld().getBlockAt(spawner.getLocation().add(0, -3, 0)).getType().equals(Material.SPONGE) || !spawner.getWorld().getBlockAt(spawner.getLocation().add(0, -2, 0)).getType().equals(Material.CHEST)) {
            botaVidros();
            return;
        }

        this.chest = (Chest) spawner.getWorld().getBlockAt(spawner.getLocation().add(0, -2, 0)).getState();
        cria();
    }

    private void cria() {

        botaVidros();

        ItemStack[] items = mobItems();

        inventory.setItem(10, items[0]);
        inventory.setItem(11, items[1]);
        inventory.setItem(12, items[2]);
        inventory.setItem(13, items[3]);
        inventory.setItem(15, items[4]);
        inventory.setItem(16, items[5]);
        inventory.setItem(31, itemTroca());

    }

    private ItemStack[] mobItems() {

        ItemStack[] itemStacks = new ItemStack[]{
                chest.getBlockInventory().getItem(19),
                chest.getBlockInventory().getItem(20),
                chest.getBlockInventory().getItem(21),
                chest.getBlockInventory().getItem(22),
                chest.getBlockInventory().getItem(24),
                chest.getBlockInventory().getItem(25)
        };

        for (int x = 0; x < itemStacks.length; x++) {
            if (itemStacks[x] == null || itemStacks[x].getType().equals(Material.AIR)) {
                itemStacks[x] = displayItems(x);
            }
        }

        return itemStacks;
    }

    private ItemStack itemTroca() {

        ItemStack itemStack = new ItemStack(Material.FIREWORK);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName("§eTroca equip's");

        ArrayList<String> itemLore = new ArrayList<>();
        itemLore.add("");
        itemLore.add("§7Clique aqui para fazer a troca");
        itemLore.add("§7dos Equip's da entidade");
        itemLore.add("");

        itemMeta.setLore(itemLore);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private ItemStack displayItems(int x) {

        ItemStack itemStack = new ItemStack(Material.LEASH);
        ItemMeta itemMeta = itemStack.getItemMeta();

        switch (x) {
            case 0:
                itemStack.setType(Material.LEATHER_HELMET);
                itemMeta.setDisplayName("§7Capacete");
                break;
            case 1:
                itemStack.setType(Material.LEATHER_CHESTPLATE);
                itemMeta.setDisplayName("§7Peitoral");
                break;
            case 2:
                itemStack.setType(Material.LEATHER_LEGGINGS);
                itemMeta.setDisplayName("§7Calça");
                break;
            case 3:
                itemStack.setType(Material.LEATHER_BOOTS);
                itemMeta.setDisplayName("§7Botas");
                break;
            case 4:
                itemStack.setType(Material.WOOD_SWORD);
                itemMeta.setDisplayName("§7Mão primaria");
                break;
            case 5:
                itemStack.setType(Material.SHIELD);
                itemMeta.setDisplayName("§7Mão secundaria");
                break;
        }

        ArrayList<String> itemLore = new ArrayList<>();
        itemLore.add("");
        itemLore.add("Coloque um item aqui!");
        itemLore.add("");

        itemMeta.setLore(itemLore);
        itemStack.setItemMeta(itemMeta);

        return itemStack;

    }

    @Override
    public void interage(Player player, int slot, InventoryAction inventoryAction) {
        super.interage(player, slot, inventoryAction);

        if (inventory.getItem(slot) != null && inventory.getItem(slot).getType().equals(Material.STAINED_GLASS_PANE)) {
            cancelaInteract = true;
            return;
        }

        switch (slot) {
            default:
                cancelaInteract = true;
                break;
            case 10:
                cancelaInteract = false;
                break;
            case 11:
                cancelaInteract = false;
                break;
            case 12:
                cancelaInteract = false;
                break;
            case 13:
                cancelaInteract = false;
                break;
            case 15:
                cancelaInteract = false;
                break;
            case 16:
                cancelaInteract = false;
                break;
            case 31:
                trocaItems();
                open(player, new EquipGUIMain(spawner));
                break;
        }

    }

    private void trocaItems() {

        ItemStack[] items = mobItems();
        ItemStack[] novosItems = new ItemStack[6];
        int index;

        int[] slots = new int[]{10, 11, 12, 13, 15, 16};
        index = 0;

        for (int x : slots) {
            for (ItemStack itemStack : items) {
                if (inventory.getItem(x) != null && itemStack.equals(inventory.getItem(x))) {
                    inventory.setItem(x, new ItemStack(Material.AIR));
                }
            }
            novosItems[index] = inventory.getItem(x);
            index++;
        }

        int[] slotsChest = new int[]{19, 20, 21, 22, 24, 25};
        index = 0;

        for (int x : slotsChest) {
            chest.getBlockInventory().setItem(x, novosItems[index]);
            index++;
        }

    }

}
