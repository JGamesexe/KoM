package nativelevel.guis;

import nativelevel.Jobs;
import nativelevel.KoM;
import nativelevel.utils.GUI;
import nativelevel.utils.TitleAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class BancoGUI extends GUI {

    private static Map<UUID, BancoGUI> bancoAberto = new HashMap<>();
    private static Map<UUID, UUID> howSee = new HashMap<>();
    private static Set<UUID> cantOpen = new HashSet<>();

    private UUID holder;
    private int slots;
    private ItemStack[] containts;

    private BancoGUI(UUID howSee, UUID uuid) {
        super(bancoAberto.containsKey(uuid) ? bancoAberto.get(uuid).inventory : Bukkit.createInventory(null, 27, "§8Banco"));
        if (bancoAberto.containsKey(uuid)) return;
        this.holder = uuid;
        slots = (5 + KoM.database.getSlotsBanco(uuid));
        containts = KoM.database.getBanco(uuid);
        cancelaInteract = false;
        cria();
        bancoAberto.put(uuid, this);
        BancoGUI.howSee.put(howSee, uuid);
    }

    private void cria() {
        blockSlots();
        if (containts != null && containts.length != 0) inventory.addItem(containts);
    }

    private void blockSlots() {
        int slot = (slots);

        ItemStack itemStack;
        ItemMeta itemMeta;

        while (slot < inventory.getSize()) {
            int rnd = Jobs.dado(3);
            if (rnd == 1) itemStack = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
            else if (rnd == 2) itemStack = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
            else itemStack = new ItemStack(Material.IRON_FENCE);
            itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("§7Slot Bloqueado");
            itemMeta.setLore(Collections.singletonList("§0" + Math.random()));
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(slot, itemStack);
            slot++;
        }
    }

    @Override
    public void interage(InventoryClickEvent event) {
        super.interage(event);

        ItemStack itemStack = inventory.getItem(event.getSlot());

        cancelaInteract = itemStack != null && itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName() != null && itemStack.getItemMeta().getDisplayName().equals("§7Slot Bloqueado");
    }

    private static void sendToSQL(UUID holder, Inventory inventory) {
        List<ItemStack> items = new ArrayList<>();
        if (inventory != null)
            for (ItemStack ss : inventory.getContents()) {
                if (ss != null && !ss.getType().equals(Material.AIR) && !(ss.getItemMeta() != null && ss.getItemMeta().getDisplayName() != null && ss.getItemMeta().getDisplayName().equals("§7Slot Bloqueado")))
                    items.add(ss);
            }

        KoM.database.setBanco(holder, items.toArray(new ItemStack[items.size()]));
    }

    public static void openBanco(Player player, UUID who) {
        if (cantOpen.contains(who)) {
            TitleAPI.sendActionBar(player, "§cAguarde um pouco...");
            return;
        }

        if (howSee.containsKey(player.getUniqueId())) return;

        BancoGUI banco = new BancoGUI(player.getUniqueId(), who);
        GUI.open(player, banco);
    }

    public static void closeBanco(InventoryCloseEvent ev) {
        Player closer = (Player) ev.getPlayer();
        UUID bancoOf = howSee.get(ev.getPlayer().getUniqueId());

        if (ev.getInventory().getViewers().size() <= 1) {

            cantOpen.add(bancoOf);
            sendToSQL(bancoOf, bancoAberto.get(bancoOf).inventory);
            bancoAberto.remove(bancoOf);
            Bukkit.getScheduler().runTaskLater(KoM._instance, () -> cantOpen.remove(bancoOf), 5);

        }

        howSee.remove(closer.getUniqueId());
    }

    public static void onDisable() {
        for (BancoGUI banco : bancoAberto.values())
            sendToSQL(banco.holder, banco.inventory);
    }

}
