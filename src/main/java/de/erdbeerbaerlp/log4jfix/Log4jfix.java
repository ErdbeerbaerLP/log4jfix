package de.erdbeerbaerlp.log4jfix;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fmllegacy.network.FMLNetworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("log4jfix")
public class Log4jfix {

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    private final Pattern p = Pattern.compile("jndi:ldap");

    public Log4jfix() {
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void chat(final ServerChatEvent event) {
        final Matcher m = this.p.matcher(event.getMessage());
        if (m.find()) {
            LOGGER.warn(event.getPlayer().getName().getContents() + " just attempted to do something malicious!");
            event.setCanceled(true);
        }
    }
}
