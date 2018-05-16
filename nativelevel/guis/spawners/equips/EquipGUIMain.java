package nativelevel.guis.spawners.equips;

import me.dpohvar.powernbt.api.NBTCompound;
import me.dpohvar.powernbt.api.NBTList;
import nativelevel.KoM;
import nativelevel.guis.spawners.SpawnerGUIMain;
import nativelevel.utils.GUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class EquipGUIMain extends GUI {

    CreatureSpawner spawner;
    Chest chest;

    public EquipGUIMain(CreatureSpawner spawner) {
        super(Bukkit.createInventory(null, 54, "§3MobSPAWNAER, Equip's"), spawner);
        this.spawner = (CreatureSpawner) spawner.getBlock().getState();

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

        NBTCompound spawnerData = KoM.nbtManager.read(spawner.getBlock());
        NBTCompound spawnData = spawnerData.getCompound("SpawnData");

        if (spawnData.getList("ArmorDropChances") == null || spawnData.getList("HandDropChances") == null) {
            attProbSpawner(getProbs());
        }

        botaVidros();

        ItemStack[] items = mobItems();
        ItemStack[] probsItems = probabilyItems(getProbs());

        inventory.setItem(10, items[0]);
        inventory.setItem(11, items[1]);
        inventory.setItem(12, items[2]);
        inventory.setItem(13, items[3]);
        inventory.setItem(15, items[4]);
        inventory.setItem(16, items[5]);
        inventory.setItem(19, probsItems[3]);
        inventory.setItem(20, probsItems[2]);
        inventory.setItem(21, probsItems[1]);
        inventory.setItem(22, probsItems[0]);
        inventory.setItem(24, probsItems[5]);
        inventory.setItem(25, probsItems[4]);
        inventory.setItem(40, itemTroca());

    }

    private ItemStack[] mobItems() {

        ItemStack[] itemStacks = new ItemStack[]{
                chest.getBlockInventory().getItem(18),
                chest.getBlockInventory().getItem(19),
                chest.getBlockInventory().getItem(20),
                chest.getBlockInventory().getItem(21),
                chest.getBlockInventory().getItem(23),
                chest.getBlockInventory().getItem(24)
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

        itemMeta.addEnchant(Enchantment.MENDING, 1, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

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

    private float[] getProbs() {

        float[] probs = new float[]{0, 0, 0, 0, 0, 0};

        NBTCompound spawnerData = KoM.nbtManager.read(spawner.getBlock());
        NBTCompound spawnData = spawnerData.getCompound("SpawnData");

        int index = 0;

        if (spawnData.getList("ArmorDropChances") != null) {

            NBTList armorProbs = spawnData.getList("ArmorDropChances");

            for (int x = 0; x < armorProbs.size(); x++) {
                probs[index] = (float) armorProbs.get(x);
                index++;
            }

        }

        if (spawnData.getList("HandDropChances") != null) {

            NBTList handProbs = spawnData.getList("HandDropChances");

            index = 4;

            for (int x = 0; x < handProbs.size(); x++) {
                probs[index] = (float) handProbs.get(x);
                index++;
            }

        }

        spawnerData.put("SpawnData", spawnData);


        return probs;
    }

    private ItemStack[] probabilyItems(float[] probs) {

        ItemStack[] itemStacks = new ItemStack[6];

        for (int x = 0; x < probs.length; x++) {
            itemStacks[x] = new ItemStack(Material.SIGN);
            ItemMeta itemMeta = itemStacks[x].getItemMeta();
            itemMeta.setDisplayName("§eProbabilidade");

            List<String> itemLore = new ArrayList<>();

            itemLore.add("");
            itemLore.add("§7" + ((int) (probs[x] * 100)));
            itemLore.add("");

            itemMeta.setLore(itemLore);
            itemStacks[x].setItemMeta(itemMeta);

        }

        return itemStacks;
    }

    @Override
    public void interage(InventoryClickEvent event) {
        super.interage(event);

        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();
        InventoryAction inventoryAction = event.getAction();

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
            case 19:
                attProb(slot, inventoryAction);
                break;
            case 20:
                attProb(slot, inventoryAction);
                break;
            case 21:
                attProb(slot, inventoryAction);
                break;
            case 22:
                attProb(slot, inventoryAction);
                break;
            case 24:
                attProb(slot, inventoryAction);
                break;
            case 25:
                attProb(slot, inventoryAction);
                break;
            case 40:
                trocaItems();
                open(player, new SpawnerGUIMain(spawner));
                break;
        }

    }

    private void trocaItems() {

        ItemStack[] novosItems = new ItemStack[6];
        int index;

        int[] slots = new int[]{10, 11, 12, 13, 15, 16};
        index = 0;

        for (int slot : slots) {
            if (inventory.getItem(slot) != null && inventory.getItem(slot).equals(displayItems(index))) {
                inventory.setItem(slot, new ItemStack(Material.AIR));

            }
            novosItems[index] = inventory.getItem(slot);
            index++;
        }

//        int[] slotsProbs = new int[]{19, 20, 21, 22, 24, 25};
        int[] slotsProbs = new int[]{22, 21, 20, 19, 25, 24};
        float[] probs = new float[6];
        index = 0;

        for (int slot : slotsProbs) {

            ItemStack itemStack = inventory.getItem(slot);
            ItemMeta itemMeta = itemStack.getItemMeta();

            List<String> itemLore = itemMeta.getLore();

            probs[index] = (Float.valueOf(itemLore.get(1).replace("§7", "")) / 100);
            index++;

        }

        int[] slotsChest = new int[]{18, 19, 20, 21, 23, 24};
        index = 0;

        for (int x : slotsChest) {
            chest.getBlockInventory().setItem(x, novosItems[index]);
            index++;
        }

        attProbSpawner(probs);

    }

    private void attProb(int slot, InventoryAction inventoryAction) {

        ItemStack itemStack = inventory.getItem(slot);
        ItemMeta itemMeta = itemStack.getItemMeta();

        List<String> itemLore = itemMeta.getLore();

        double newValor = SpawnerGUIMain.calc((Double.valueOf(itemLore.get(1).replace("§7", ""))), inventoryAction, 1, 5);

        if (newValor < 0 || newValor > 100) return;

        itemLore.set(1, "§7" + ((int) newValor));

        itemMeta.setLore(itemLore);
        itemStack.setItemMeta(itemMeta);

        inventory.setItem(slot, itemStack);

    }

    private void attProbSpawner(float[] probs) {

        NBTList armorProbs = new NBTList();

        armorProbs.add(probs[0]);
        armorProbs.add(probs[1]);
        armorProbs.add(probs[2]);
        armorProbs.add(probs[3]);

        NBTList handProbs = new NBTList();

        handProbs.add(probs[4]);
        handProbs.add(probs[5]);

        NBTCompound spawnerData = KoM.nbtManager.read(spawner.getBlock());
        NBTCompound spawnData = spawnerData.getCompound("SpawnData");

        spawnData.put("ArmorDropChances", armorProbs);
        spawnData.put("HandDropChances", handProbs);
        spawnerData.put("SpawnData", spawnData);

        KoM.nbtManager.write(spawner.getBlock(), spawnerData);
        KoM.nbtManager.write(spawner.getBlock(), spawnData);

    }

}
