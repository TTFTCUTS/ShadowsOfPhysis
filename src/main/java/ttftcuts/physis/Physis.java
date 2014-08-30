package ttftcuts.physis;

import cpw.mods.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Physis.MOD_ID, name = "Shadows Of Physis", version = "$version", dependencies = "")
public class Physis {

    public static final String MOD_ID = "physis";
    public static final Logger logger = LogManager.getLogger(MOD_ID);
    //public static final SimpleNetworkWrapper networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID);

    @Mod.Instance
    public static Physis instance;
}
