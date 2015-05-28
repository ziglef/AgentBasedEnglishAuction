package jadex;

import jadex.base.Starter;
import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IExternalAccess;
import jadex.bridge.service.RequiredServiceInfo;
import jadex.bridge.service.search.SServiceProvider;
import jadex.bridge.service.types.cms.CreationInfo;
import jadex.bridge.service.types.cms.IComponentManagementService;
import jadex.commons.future.ThreadSuspendable;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 28/05/15.
 *
 * @author André Silva
 * @author Rui Grandão
 */
public class Jadex {

    private static IExternalAccess platform;
    private static IComponentManagementService cms;
    private static IComponentIdentifier applicationIdentifier;

    private static List<IComponentIdentifier> bidders = new ArrayList<IComponentIdentifier>();

    public Jadex () {

        ThreadSuspendable sus = new ThreadSuspendable();

        String[] defargs = new String[] {
                "-gui", "true",
                "-welcome", "false",
                "-cli", "false",
                "-printpass", "false",
                "-platformname", "TNEL"
        };

        platform = Starter.createPlatform(defargs).get(sus);

        cms = SServiceProvider.getService(platform.getServiceProvider(),
                IComponentManagementService.class, RequiredServiceInfo.SCOPE_PLATFORM).get(sus);

        applicationIdentifier = cms.createComponent("application/EnglishAuction.application.xml", null).getFirstResult(sus);
    }

    public boolean addNewBidderToApplication(DefaultTableModel biddersTableModel) {

        if (applicationIdentifier != null) {
            ThreadSuspendable sus = new ThreadSuspendable();

            CreationInfo info = new CreationInfo(applicationIdentifier);
            IComponentIdentifier agentIdentifier = cms.createComponent("agents/BidderBDI.class", info).getFirstResult(sus);
            bidders.add(agentIdentifier);
            biddersTableModel.addRow(new Object[]{agentIdentifier, agentIdentifier.getLocalName()});

            return true;
        }

        return false;
    }
}
