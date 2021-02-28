package mir.routingModule.module;

import mir.routingModule.Constants;
import mir.routingModule.Module;

public class AcquirerModule {
    public static void main(String[] args) {
        Module acquirerModule = new Module(Constants.Ports.ACQUIRER_MODULE);
        acquirerModule.start();
    }
}
