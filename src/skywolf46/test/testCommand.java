package skywolf46.test;

import org.bukkit.entity.Player;
import skywolf46.CommandAnnotation.v1_4R1.Data.CommandArgument;

import java.util.HashMap;

//@$_("/tc130")
public class testCommand {
//    @$_("damage")
    public static void test(Player p, CommandArgument cArg,HashMap<String, Integer> data) {
        p.sendMessage("§c데미지!");
        p.damage(4);

    }

//    @$_("heal")
    public static void test2(Player p){
        p.setHealth(Math.min(p.getMaxHealth(),p.getHealth() + 2));
        p.sendMessage("§a회복!");
    }
}
