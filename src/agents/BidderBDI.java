package agents;

import jadex.bdiv3.BDIAgent;
import jadex.bdiv3.annotation.Belief;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;
import jadex.micro.annotation.Description;

import java.util.List;

/**
 * Created on 19/04/15.
 *
 * @author André Silva
 * @author Rui Grandão
 */

@Agent
@Description("This agent represents a bidder")
public class BidderBDI {

    @Agent
    protected BDIAgent bidder;

    // List with the auctions the bidder is in
    @Belief
    protected List<String> auctions;

    @AgentBody
    public void body() {
        bidder.waitForDelay(1000).get();

        bidder.getComponentIdentifier().getLocalName();
    }
}
