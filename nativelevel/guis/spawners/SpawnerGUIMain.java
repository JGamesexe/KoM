package nativelevel.guis.spawners;

import me.dpohvar.powernbt.api.NBTCompound;
import me.dpohvar.powernbt.api.NBTList;
import nativelevel.KoM;
import nativelevel.guis.spawners.equips.EquipGUIMain;
import nativelevel.guis.spawners.mobs.MobsGUIMain;
import nativelevel.utils.EntityHelp;
import nativelevel.utils.GUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.*;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SpawnerGUIMain extends GUI {

    private CreatureSpawner spawner;

    public SpawnerGUIMain(CreatureSpawner spawner) {
        super(Bukkit.createInventory(null, 45, "§3MobSPAWNER"), spawner);
        this.spawner = (CreatureSpawner) spawner.getBlock().getState();
        cria();
    }

    private void cria() {

        botaVidros();

        NBTCompound spawnerData = KoM.nbtManager.read(spawner.getBlock());
        NBTCompound spawnData = spawnerData.getCompound("SpawnData");

        inventory.setItem(10, typeItem(spawner.getSpawnedType()));
        inventory.setItem(11, isBabyItem(spawnData));
        inventory.setItem(13, simpleDataItem(spawnerData.getInt("RequiredPlayerRange"), "§eDistancia pra ativar", Material.SLIME_BALL));
        inventory.setItem(14, simpleDataItem(spawnerData.getInt("MaxNearbyEntities"), "§eMaximo de entidades proximas", Material.SLIME_BALL));
        inventory.setItem(15, simpleDataItem(spawnerData.getInt("SpawnCount"), "§eQuantia maxima de entidades por spawn", Material.SLIME_BALL));
        inventory.setItem(16, simpleDataItem(spawnerData.getInt("SpawnRange"), "§eDistancia max de spawn da entidade", Material.SLIME_BALL));
        inventory.setItem(28, simpleDataItem((spawnerData.getInt("MinSpawnDelay") / 20), "§eSegundos para spawnar §o[MIN]", Material.DIODE));
        inventory.setItem(29, simpleDataItem((spawnerData.getInt("MaxSpawnDelay") / 20), "§eSegundos para spawnar §o[MAX]", Material.DIODE));
        inventory.setItem(31, simpleDataItem(getAttr(spawnData.getList("Attributes"), "generic.maxHealth").getDouble("Base"), "§eVida da entidade", Material.REDSTONE));
        inventory.setItem(32, simpleDataItem(getAttr(spawnData.getList("Attributes"), "generic.movementSpeed").getDouble("Base"), "§eVelocidade da entidade", Material.REDSTONE));
        inventory.setItem(33, simpleDataItem(getAttr(spawnData.getList("Attributes"), "generic.attackDamage").getDouble("Base"), "§eDano da entidade", Material.REDSTONE));
        inventory.setItem(34, simpleDataItem("Armadura...", "§eEquip's", Material.CHEST));

    }

    private ItemStack typeItem(EntityType entity) {

        ItemStack itemStack = new ItemStack(Material.MONSTER_EGG);
        SpawnEggMeta spawnEggMeta = (SpawnEggMeta) itemStack.getItemMeta();
        spawnEggMeta.setSpawnedType(entity);
        spawnEggMeta.setDisplayName("§eTipo do entidade");
        ArrayList<String> itemLore = new ArrayList<>();
        itemLore.add("");
        itemLore.add("§7" + entity.toString());
        itemLore.add("");
        spawnEggMeta.setLore(itemLore);
        itemStack.setItemMeta(spawnEggMeta);

        return itemStack;
    }

    private ItemStack isBabyItem(NBTCompound spawnData) {


        Entity entityTeste = spawner.getWorld().spawnEntity(spawner.getLocation(), spawner.getSpawnedType());

        ItemStack itemStack;
        ItemMeta itemMeta;
        ArrayList<String> itemLore = new ArrayList<>();

        itemLore.add("");

        if (entityTeste instanceof Zombie) {

            if (spawnData.getBoolean("IsBaby")) {

                itemStack = new ItemStack(Material.MILK_BUCKET);
                itemLore.add("§7Sim");

            } else {

                itemStack = new ItemStack(Material.BUCKET);
                itemLore.add("§7Não");

            }

        } else if (entityTeste instanceof Ageable) {

            if (spawnData.getInt("Age") < 0) {

                itemStack = new ItemStack(Material.MILK_BUCKET);
                itemLore.add("§7Sim");

            } else {

                itemStack = new ItemStack(Material.BUCKET);
                itemLore.add("§7Não");

            }

        } else {

            itemStack = new ItemStack(Material.LAVA_BUCKET);
            itemLore.add("§cEntidade não pode ser bebe");

        }

        itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName("§eEntidade é bebê?");

        itemLore.add("");

        itemMeta.setLore(itemLore);
        itemStack.setItemMeta(itemMeta);

        entityTeste.remove();

        return itemStack;

    }

    private ItemStack simpleDataItem(Object valor, String nome, Material item) {

        ItemStack itemStack = new ItemStack(item);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(nome);
        ArrayList<String> itemLore = new ArrayList<>();
        itemLore.add("");
        itemLore.add("§7" + valor);
        itemLore.add("");
        itemMeta.setLore(itemLore);
        itemStack.setItemMeta(itemMeta);

        return itemStack;

    }

    private static NBTCompound getAttr(NBTList attributes, String name) {

        if (attributes != null) {

            for (int index = 0; index < attributes.size(); index++) {

                NBTCompound attr = (NBTCompound) attributes.get(index);

                if (attr.getString("Name").contains(name)) {
                    return attr;
                }

            }
        }
        Map<String, Object> nAcheiMap = new HashMap<>();
        nAcheiMap.put("Base", "-666");
        nAcheiMap.put("Name", "Ta foda");

        NBTCompound nAchei = new NBTCompound(nAcheiMap);

        return nAchei;
    }

    public static void removeAttr(CreatureSpawner spawner, String name) {

        NBTCompound spawnerData = KoM.nbtManager.read(spawner.getBlock());
        NBTCompound spawnData = spawnerData.getCompound("SpawnData");
        NBTList attributes = spawnerData.getList("Attributes");

        if (attributes != null) {

            for (int index = 0; index < attributes.size(); index++) {

                NBTCompound attr = (NBTCompound) attributes.get(index);

                if (attr.getString("Name").contains(name)) {
                    attributes.remove(index);
                }

            }
        }

        spawnData.put("Attributes", attributes);

        spawnerData.put("SpawnData", spawnData);
        KoM.nbtManager.write(spawner.getBlock(), spawnerData);
        KoM.nbtManager.write(spawner.getBlock(), spawnData);

    }

    public static void tiraBebe(CreatureSpawner spawner) {

        NBTCompound spawnerData = KoM.nbtManager.read(spawner.getBlock());
        NBTCompound spawnData = spawnerData.getCompound("SpawnData");

        spawnData.remove("IsBaby");
        spawnData.remove("Age");

        spawnerData.put("SpawnData", spawnData);
        KoM.nbtManager.write(spawner.getBlock(), spawnerData);
        KoM.nbtManager.write(spawner.getBlock(), spawnData);

    }

    private static NBTCompound criaAttr(String nome, Object valor) {

        Map<String, Object> attrMap = new HashMap<>();
        attrMap.put("Base", valor);
        attrMap.put("Name", nome);

        NBTCompound attr = new NBTCompound(attrMap);

        return attr;

    }

    private static NBTList attAttributes(NBTList attributes, NBTCompound attr) {

        if (attributes == null) {
            attributes = new NBTList();
            attributes.add(attr);
            return attributes;
        } else {
            for (int index = 0; index < attributes.size(); index++) {

                NBTCompound attrNew = (NBTCompound) attributes.get(index);

                if (attrNew.getString("Name").equals(attr.getString("Name"))) {
                    attributes.set(index, attr);
                    return attributes;
                }
            }

            attributes.add(attr);

        }
        return attributes;

    }

    @Override
    public void interage(InventoryClickEvent event) {
        super.interage(event);

        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();
        InventoryAction inventoryAction = event.getAction();

        if (inventory.getItem(slot) == null || inventory.getItem(slot).getType().equals(Material.STAINED_GLASS_PANE))
            return;

        switch (slot) {
            case 10:
                open(player, new MobsGUIMain(spawner));
                break;
            case 11:
                mudaBabe(inventoryAction, slot);
                break;
            case 13:
                mudaValorOnSpawnerData("RequiredPlayerRange", 1, 5, player, inventoryAction, slot, 5, 64, 1);
                break;
            case 14:
                mudaValorOnSpawnerData("MaxNearbyEntities", 1, 5, player, inventoryAction, slot, 1, 24, 1);
                break;
            case 15:
                mudaValorOnSpawnerData("SpawnCount", 1, 5, player, inventoryAction, slot, 1, 12, 1);
                break;
            case 16:
                mudaValorOnSpawnerData("SpawnRange", 1, 5, player, inventoryAction, slot, 2, 32, 1);
                break;
            case 28:
                mudaValorOnSpawnerData("MinSpawnDelay", 1, 5, player, inventoryAction, slot, 10, 500, 20);
                break;
            case 29:
                mudaValorOnSpawnerData("MaxSpawnDelay", 1, 5, player, inventoryAction, slot, 10, 500, 20);
                break;
            case 31:
                mudaAttrOnSpawnData("generic.maxHealth", 1d, 5d, player, inventoryAction, slot, 1d, 450d);
                break;
            case 32:
                mudaAttrOnSpawnData("generic.movementSpeed", 0.001d, 0.01d, player, inventoryAction, slot, 0.1d, 2.5d);
                break;
            case 33:
                mudaAttrOnSpawnData("generic.attackDamage", 1d, 5d, player, inventoryAction, slot, 1d, 100d);
                break;
            case 34:
                open(player, new EquipGUIMain(spawner));
                break;
        }

    }

    public static double calc(double valor, InventoryAction inventoryAction, double quantiaP, double quantiaM) {

        switch (inventoryAction) {
            default:
                valor += quantiaP;
                break;
            case PICKUP_ALL:
                valor += quantiaP;
                break;
            case PICKUP_HALF:
                valor -= quantiaP;
                break;
            case DROP_ONE_SLOT:
                valor += quantiaM;
                break;
            case DROP_ALL_SLOT:
                valor -= quantiaM;
                break;
        }

        return valor;
    }

    private void mudaValorOnSpawnerData(String data, int quantiaP, int quantiaM, Player player, InventoryAction inventoryAction, int slot, int valorMin, int valorMax, int multiplicadorDeValor) {

        NBTCompound spawnerData = KoM.nbtManager.read(spawner.getBlock());

        int valor = (spawnerData.getInt(data) / multiplicadorDeValor);

        int newValor = (int) calc(valor, inventoryAction, quantiaP, quantiaM);

        if (newValor < valorMin | newValor > valorMax) {

            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, SoundCategory.MASTER, 10f, 1.5f);
            return;

        } else if (newValor > valor) {

            player.playSound(player.getLocation(), Sound.UI_TOAST_IN, SoundCategory.MASTER, 0.65f, 1.6f);

        } else {

            player.playSound(player.getLocation(), Sound.UI_TOAST_IN, SoundCategory.MASTER, 0.65f, 1f);

        }

        spawnerData.put(data, (newValor * multiplicadorDeValor));

        KoM.nbtManager.write(spawner.getBlock(), spawnerData);
        inventory.setItem(slot, simpleDataItem(newValor, inventory.getItem(slot).getItemMeta().getDisplayName(), inventory.getItem(slot).getType()));

    }

    private void mudaAttrOnSpawnData(String data, double quantiaP, double quantiaM, Player player, InventoryAction inventoryAction, int slot, double valorMin, double valorMax) {

        if (data.equals("generic.attackDamage") && !EntityHelp.mobs[1].contains(spawner.getSpawnedType())) {

            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMEN_SCREAM, SoundCategory.MASTER, 1f, 2f);
            player.sendMessage("§c§oCara, se a entidade não for monstro procura o JG pra botar dano nesse bixo ai");
            return;

        }

        NBTCompound spawnerData = KoM.nbtManager.read(spawner.getBlock());
        NBTCompound spawnData = spawnerData.getCompound("SpawnData");
        NBTList attributes = spawnData.getList("Attributes");

        NBTCompound attr = getAttr(attributes, data);

        if (!attr.getString("Name").equals(data)) {
            attr = criaAttr(data, 0f);
        }

        double valor = attr.getDouble("Base");

        DecimalFormat format = new DecimalFormat("0.000");

        double newValor = Double.valueOf(format.format(calc(valor, inventoryAction, quantiaP, quantiaM)).replace(',', '.'));

        if (valor == 0) {

            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1f, 1.5f);
            newValor = valorMin;

        } else if (newValor < valorMin | newValor > valorMax) {

            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, SoundCategory.MASTER, 1f, 1.5f);
            player.sendMessage("§c§oTem que variar de " + format.format(valorMin) + "~" + format.format(valorMax));
            return;

        } else if (newValor > valor) {

            player.playSound(player.getLocation(), Sound.UI_TOAST_IN, SoundCategory.MASTER, 0.65f, 1.6f);

        } else {

            player.playSound(player.getLocation(), Sound.UI_TOAST_IN, SoundCategory.MASTER, 0.65f, 1f);

        }

        if (data.equals("generic.maxHealth")) {
            spawnData.put("Health", newValor);
        }

        spawnData.put("Attributes", attAttributes(attributes, criaAttr(data, newValor)));
        spawnerData.put("SpawnData", spawnData);
        KoM.nbtManager.write(spawner.getBlock(), spawnerData);
        KoM.nbtManager.write(spawner.getBlock(), spawnData);

        inventory.setItem(slot, simpleDataItem(newValor, inventory.getItem(slot).getItemMeta().getDisplayName(), inventory.getItem(slot).getType()));
    }

    private void mudaBabe(InventoryAction inventoryAction, int slot) {

        if (inventory.getItem(slot).getType().equals(Material.LAVA_BUCKET)) return;

        NBTCompound spawnerData = KoM.nbtManager.read(spawner.getBlock());
        NBTCompound spawnData = spawnerData.getCompound("SpawnData");

        Entity entityTeste = spawner.getWorld().spawnEntity(spawner.getLocation(), spawner.getSpawnedType());
        ;

        if (entityTeste instanceof Zombie) {

            if (spawnData.getBoolean("IsBaby")) {

                spawnData.remove("IsBaby");

            } else {

                spawnData.put("IsBaby", true);

            }

        } else if (entityTeste instanceof Ageable) {

            if (spawnData.getInt("Age") < 0) {

                spawnData.remove("Age");

            } else {

                spawnData.put("Age", -999999999);

            }
        }

        spawnerData.put("SpawnData", spawnData);
        KoM.nbtManager.write(spawner.getBlock(), spawnerData);
        KoM.nbtManager.write(spawner.getBlock(), spawnData);

        entityTeste.remove();

        inventory.setItem(slot, isBabyItem(spawnData));

    }

}
