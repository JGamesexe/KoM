package nativelevel.utils;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import nativelevel.KoM;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.io.*;

public class StructureAPI {

    public String[][][] getStructure(Player p) {

        WorldEditPlugin worldedit = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");

        Selection selection = worldedit.getSelection(p);

        if (selection != null) {

            Block lmin = p.getWorld().getBlockAt(selection.getMinimumPoint());
            Block lmax = p.getWorld().getBlockAt(selection.getMaximumPoint());

            int minX = lmin.getX();
            int minY = lmin.getY();
            int minZ = lmin.getZ();

            int maxX = lmax.getX();
            int maxY = lmax.getY();
            int maxZ = lmax.getZ();

            String[][][] blocks = new String[maxX - minX + 1][maxY - minY + 1][maxZ - minZ + 1];

            for (int x = minX; x <= maxX; x++) {

                for (int y = minY; y <= maxY; y++) {

                    for (int z = minZ; z <= maxZ; z++) {

                        Block b = p.getWorld().getBlockAt(x, y, z);
                        blocks[x - minX][y - minY][z - minZ] = b.getType() + " " + b.getData();

                    }

                }

            }

            return blocks;

        }

        return null;
    }

    public void paste(String[][][] blocks, Location l, boolean override) {

        for (int x = 0; x < blocks.length; x++) {

            for (int y = 0; y < blocks[x].length; y++) {

                for (int z = 0; z < blocks[x][y].length; z++) {

                    Location neww = l.clone();
                    neww.add((x - (blocks.length / 2)), y, (z - (blocks[x][y].length / 2)));
                    Block b = neww.getBlock();

                    if (override) {

                        String[] bloco = blocks[x][y][z].split(" ");
                        b.setType(Material.getMaterial(bloco[0]));
                        b.setData(Byte.parseByte(bloco[1]));
                        b.getState().update(true);

                    } else {

                        if (b.getType() != Material.AIR &&
                                b.getType() != Material.SAPLING &&
                                b.getType() != Material.LONG_GRASS &&
                                b.getType() != Material.LEAVES &&
                                b.getType() != Material.LEAVES_2 &&
                                b.getType() != Material.YELLOW_FLOWER &&
                                b.getType() != Material.RED_ROSE &&
                                b.getType() != Material.DOUBLE_PLANT) continue;

                        String[] bloco = blocks[x][y][z].split(" ");

                        if (bloco[0].equalsIgnoreCase("AIR")) continue;

                        b.setType(Material.getMaterial(bloco[0]));
                        b.setData(Byte.parseByte(bloco[1]));
                        b.getState().update(true);

                    }

                }

            }

        }

    }

    public void save(String name, String[][][] b) {

        ObjectOutputStream oos = null;

        File f = new File(KoM._instance.getDataFolder() + "/schematikas/" + name + ".schem");
        File dir = new File(KoM._instance.getDataFolder() + "/schematikas");

        try {

            dir.mkdirs();
            f.createNewFile();

        } catch (IOException e1) {

            e1.printStackTrace();

        }

        try {

            FileOutputStream fout = new FileOutputStream(f);
            oos = new ObjectOutputStream(fout);
            oos.writeObject(b);

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            if (oos != null) {

                try {

                    oos.close();

                } catch (IOException e) {

                    e.printStackTrace();

                }

            }

        }

    }

    public String[][][] load(String name) {

        File f = new File(KoM._instance.getDataFolder() + "/schematikas/" + name + ".schem");

        String[][][] loaded = new String[0][0][0];

        try {

            FileInputStream streamIn = new FileInputStream(f);
            ObjectInputStream objectinputstream = new ObjectInputStream(streamIn);
            loaded = (String[][][]) objectinputstream.readObject();

            objectinputstream.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return loaded;

    }

}