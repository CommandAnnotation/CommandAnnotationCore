package skywolf46.commandannotation.util;

import org.bukkit.Bukkit;

import java.util.concurrent.atomic.AtomicBoolean;

public class MinecraftChecker {
    private static AtomicBoolean mcCheck = new AtomicBoolean(false);

    static {
        recheckMinecraft();
    }

    public static void recheckMinecraft() {
        try {
            Class.forName("org.bukkit.Bukkit");
            if (Bukkit.getConsoleSender() == null)
                throw new Exception();
            mcCheck.set(true);
        } catch (Exception ex) {
            mcCheck.set(false);
        }
    }

    public static boolean isMinecraft() {
        return mcCheck.get();
    }
}
