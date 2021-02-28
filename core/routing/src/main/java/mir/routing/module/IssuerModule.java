package mir.routing.module;

import mir.routing.Constants;
import mir.routing.Module;

public class IssuerModule {
    public static void main(String[] args) {
        Module acquirerModule = new Module(Constants.Ports.ISSUER_MODULE);
        acquirerModule.start();
    }
}
