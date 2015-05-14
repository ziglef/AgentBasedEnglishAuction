package gui;

import jadex.base.Starter;
import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IExternalAccess;
import jadex.bridge.service.RequiredServiceInfo;
import jadex.bridge.service.search.SServiceProvider;
import jadex.bridge.service.types.cms.IComponentManagementService;
import jadex.commons.future.ThreadSuspendable;

public class UserInterface {

    public static void main(String [] args) {

        String[] defargs = new String[] {
                "-gui", "true",
                "-welcome", "false",
                "-cli", "false",
                "-printpass", "false",
                "-platformname", "TNEL"
        };

        ThreadSuspendable sus = new ThreadSuspendable();

        IExternalAccess platform = Starter.createPlatform(defargs).get(sus);
        System.out.println("Started platform: "+platform.getComponentIdentifier());

        IComponentManagementService cms = SServiceProvider.getService(platform.getServiceProvider(),
                IComponentManagementService.class, RequiredServiceInfo.SCOPE_PLATFORM).get(sus);

        IComponentIdentifier cid = cms.createComponent("application/EnglishAuction.application.xml", null).getFirstResult(sus);
        System.out.println("CID: "+cid);
    }
}
