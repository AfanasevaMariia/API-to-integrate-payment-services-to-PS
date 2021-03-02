package mir.routing.module;

import mir.routing.constants.Constants;
import mir.routing.Module;

public class PlatformModule {
    public static void main(String[] args) {
        Module acquirerModule = new Module(Constants.Ports.PLATFORM_MODULE);
        acquirerModule.start();
    }
}
