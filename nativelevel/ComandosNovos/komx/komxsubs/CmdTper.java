package nativelevel.ComandosNovos.komx.komxsubs;

import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import nativelevel.ComandosNovos.Comando;
import nativelevel.ComandosNovos.SubCmd;
import nativelevel.KoM;
import nativelevel.Lang.L;
import nativelevel.sisteminhas.BookPortal;
import nativelevel.utils.BungLocation;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;

public class CmdTper extends SubCmd {

    public CmdTper() {
        super("tper", Comando.CommandType.OP);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {

        Player p = (Player) cs;

        if (p.getItemInHand().getType() == Material.BOOK_AND_QUILL) {
            BungLocation local = BookPortal.getLocationFromBook(p.getItemInHand());
            if (local == null) {
                p.sendMessage(ChatColor.RED + L.m("ou ce fica cum livro de portal ou cas mao vazia mero mortal !"));
            } else {

                ApplicableRegionSet set = KoM.worldGuard.getRegionManager(p.getWorld()).getApplicableRegions(p.getLocation());
                if (set.size() > 0) {

                    Iterator<ProtectedRegion> i = set.iterator();
                    while (i.hasNext()) {
                        ProtectedRegion regiao = i.next();
                        if (regiao.getId().contains("barco-") || regiao.getId().contains("balao-")) {

                            com.sk89q.worldedit.Location localWE = BukkitUtil.toLocation(local.toLocation());
                            regiao.setFlag(DefaultFlag.SPAWN_LOC, localWE);

                            p.sendMessage("Local destino do transporte setado !");
                            return;
                        }
                        break;
                    }
                }

                Block ondeTaONego = p.getLocation().getBlock();
                ondeTaONego.setType(Material.WOOD_PLATE);
                Block bau = ondeTaONego.getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN);
                bau.setType(Material.CHEST);
                Chest c = (Chest) bau.getState();
                c.getBlockInventory().addItem(p.getItemInHand());
                p.sendMessage(ChatColor.GREEN + L.m("Teleporter criado !"));
            }
        } else if (p.getInventory().getItemInMainHand().getType() != null && p.getInventory().getItemInMainHand().getType() != Material.AIR) {
            p.sendMessage(ChatColor.RED + L.m("ou ce fica cum livro de portal ou cas mao vazia mero mortal !"));
        } else {

            ItemStack livro = BookPortal.criaLivroPortal(p);

            if (args.length == 2) {
                livro = BookPortal.criaLivroPortal(p, args[1]);
            } else if (args.length == 3){
                livro = BookPortal.criaLivroPortal(p, args[1], args[2]);
            }
            p.setItemInHand(livro);
            p.sendMessage(ChatColor.GREEN + L.m("O poderoso livro do portal foi conjurado ! Use o poder do comando novamente para criar o teleporter !"));
        }
    }
}
