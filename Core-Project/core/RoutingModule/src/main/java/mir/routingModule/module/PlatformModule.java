package mir.routingModule.module;

import mir.routingModule.Constants;
import mir.routingModule.Module;

public class PlatformModule {
    public static void main(String[] args) {
        Module acquirerModule = new Module(Constants.Ports.PLATFORM_MODULE);
        acquirerModule.start();
    }
}
