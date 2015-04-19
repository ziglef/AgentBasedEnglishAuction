package gui;

import jadex.base.Starter;
import jadex.bridge.IExternalAccess;
import jadex.commons.future.IFuture;
import jadex.commons.future.ThreadSuspendable;

public class UserInterface {

    public static void main(String [] args) {

        IExternalAccess platform;

        String[] defargs = new String[] {
                "-gui", "true",
                "-welcome", "false",
                "-cli", "false",
                "-printpass", "false",
                "-platformname", "TNEL - English Auction"
        };
        IFuture<IExternalAccess> platfut = Starter.createPlatform(defargs);

        ThreadSuspendable sus = new ThreadSuspendable();
        platform = platfut.get(sus);
        System.out.println("Started platform: "+platform.getComponentIdentifier());
    }
}
