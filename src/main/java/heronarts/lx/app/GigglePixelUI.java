package heronarts.lx.app;

import heronarts.lx.LX;
import heronarts.lx.parameter.*;
import heronarts.lx.studio.LXStudio;
import heronarts.p4lx.ui.UI2dContainer;
import heronarts.p4lx.ui.component.UICollapsibleSection;
import heronarts.p4lx.ui.component.UIDropMenu;

public class GigglePixelUI extends UICollapsibleSection {
  public enum GPMode {
    OFF("Off"),
    LISTEN("Listen"),
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
                  .setDescription("Should GigglePixel listen, broadcast, or neither?");

  public GigglePixelUI(final LXStudio.UI ui, float width,
                       GigglePixelListener listener,
                       GigglePixelBroadcaster broadcaster) {
    super(ui, 0, 0, width, 16);
    setTitle("GigglePixel");
    setLayout(UI2dContainer.Layout.VERTICAL);
    UIDropMenu dropmenu = new UIDropMenu(0, 0, width, 16, gpMode);
    dropmenu.addToContainer(this);

    final LXParameterListener update = (p) -> {
      switch(gpMode.getEnum()) {
      case OFF:
        listener.enabled = false;
        broadcaster.enabled = false;
        break;
      case LISTEN:
        listener.enabled = true;
        broadcaster.enabled = false;
        break;
      case BROADCAST:
        listener.enabled = false;
        broadcaster.enabled = true;
        break;
      default:
        throw new Error("Internal Error");
      }
    };
    gpMode.addListener(update);
    update.onParameterChanged(null);


  }
}