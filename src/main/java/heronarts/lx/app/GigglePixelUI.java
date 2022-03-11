package heronarts.lx.app;

import heronarts.lx.LX;
import heronarts.lx.parameter.*;
import heronarts.lx.studio.LXStudio;
import heronarts.p4lx.ui.UI2dContainer;
import heronarts.p4lx.ui.component.*;

public class GigglePixelUI extends UICollapsibleSection {
  public enum GPMode {
    OFF("Off"),
    PEEK("Peek"),
    SUBSCRIBE("Subscribe"),
    BROADCAST("Broadcast");
    public final String label;

    GPMode(String label) {
      this.label = label;
    }

    @Override
    public String toString() {
      return this.label;
    }
  }

  public final EnumParameter<GPMode> gpMode =
          new EnumParameter<>("GP Mode", GPMode.BROADCAST)
                  .setDescription(
                          "Broadcast GigglePixel, peek at traffic without using it, " +
                                  "subscribe to received traffic, or do nothing?");

  public final DiscreteParameter channelIndex =
          new DiscreteParameter("Channel", 0, 8)
                  .setDescription("Channel number to watch for GP-aware patterns, " +
                          "or 0 to just broadcast the main palette");

  public GigglePixelUI(final LXStudio.UI ui, float width,
                       GigglePixelOutput output,
                       GigglePixelListener listener,
                       GigglePixelBroadcaster broadcaster) {
    super(ui, 0, 0, width, 16);
    setTitle("GigglePixel");
    setLayout(UI2dContainer.Layout.VERTICAL);
    UIDropMenu gpModeWidget = new UIDropMenu(0, 0, width, 16, gpMode);
    gpModeWidget.addToContainer(this);

    new UILabel(0, 0, width, 16, "Channel:").addToContainer(this);
    new UIIntegerBox(width, channelIndex).addToContainer(this);
    output.channelIndex = this.channelIndex;

    new UILabel(0, 0, width, 16, "Peers:").addToContainer(this);
    listener.peersTextbox = new UITextBox(0, 0, width, 16);
    listener.peersTextbox.addToContainer(this);

    final LXParameterListener updateMode = (p) -> {
      switch(gpMode.getEnum()) {
      case OFF:
        listener.peeking = false;
        listener.subscribing = false;
        broadcaster.enabled = false;
        break;
      case PEEK:
        listener.peeking = true;
        listener.subscribing = false;
        broadcaster.enabled = false;
        break;
      case SUBSCRIBE:
        listener.peeking = true;
        listener.subscribing = true;
        broadcaster.enabled = false;
        break;
      case BROADCAST:
        listener.peeking = false;
        listener.subscribing = false;
        broadcaster.enabled = true;
        break;
      default:
        throw new Error("Internal Error");
      }
    };
    gpMode.addListener(updateMode);
    updateMode.onParameterChanged(null);
  }
}
