package de.erdbeerbaerlp.log4jfix;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Mod( modid = Log4jfix.MOD_ID,name = Log4jfix.MOD_NAME,version = Log4jfix.VERSION, serverSideOnly = true, acceptableRemoteVersions = "*"
)
public class Log4jfix {

    public static final String MOD_ID = "log4jfix";
    public static final String MOD_NAME = "Log4J RCE Fix";
    public static final String VERSION = "1.0.0";
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    private final Pattern p = Pattern.compile("jndi:ldap");

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void chat(ServerChatEvent event) {
        final Matcher m = this.p.matcher(event.getMessage());
        if (m.find()) {
            LOGGER.warn(event.getPlayer().getName() + " just attempted to do something malicious!");
            event.setCanceled(true);
        }
    }
}
