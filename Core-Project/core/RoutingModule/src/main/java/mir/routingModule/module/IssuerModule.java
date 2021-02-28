package mir.routingModule.module;

import mir.routingModule.Constants;
import mir.routingModule.Module;

public class IssuerModule {
    public static void main(String[] args) {
        Module acquirerModule = new Module(Constants.Ports.ISSUER_MODULE);
        acquirerModule.start();
    }
}
