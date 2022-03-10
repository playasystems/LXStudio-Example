package heronarts.lx.app;

import heronarts.lx.LX;
import heronarts.lx.LXLoopTask;
import playasystems.gigglepixel.*;
import heronarts.lx.color.LXColor;
import heronarts.lx.color.LXSwatch;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;

public class GigglePixelListener implements LXLoopTask {
  private final LX lx;
  private GPListener gp;
  private InetAddress listenAddr;
  private final int myID; // Set to >= 0, and packets with this ID will be ignored as from ourselves
  public boolean enabled;

  public GigglePixelListener(LX lx, String listenIP, int myID) throws IOException {
    this.lx = lx;
    this.listenAddr = InetAddress.getByName(listenIP);
    this.myID = myID;
    this.enabled = false;
  }

  @Override
  public void loop(double deltaMs) {
    if (!this.enabled) {
      this.gp = null;
      return;
    }

    if (this.gp == null) try {
      this.gp = new GPListener(this.listenAddr);
    } catch (SocketException e) {
      LX.log("Failed to create GigglePixel listener: " + e.getMessage());
      this.enabled = false;
      return;
    }

    GPPacket packet;

    try {
      packet = this.gp.loop();
    } catch (IOException e) {
      LX.log("Got I/O error in GigglePixel code: " + e.getMessage());
      return;
    } catch (GPException e) {
      LX.log("Got invalid GigglePixel packet: " + e.getMessage());
      return;
    }

    if (packet == null) return;

    if (packet instanceof GPPalettePacket) {
      GPPalettePacket pp = (GPPalettePacket) packet;
      int numColors = pp.entries.size();
      if (numColors < 1) {
        LX.log("Got empty palette packet");
        return;
      } else if (numColors > 5) {
        numColors = 5;  // TODO: Be smarter when getting big palette packets
      }
      LXSwatch swatch = lx.engine.palette.swatch;
      while(swatch.colors.size() > numColors) {
        swatch.removeColor();
      }
      while(swatch.colors.size() < numColors) {
        swatch.addColor();
      }
      for (int i = 0; i < numColors; i++) {
        GPColor gpColor = pp.entries.get(i);
        int color = LXColor.rgb(gpColor.r, gpColor.g, gpColor.b);
        swatch.colors.get(i).color.setColor(color);
      }
    }
  }
}
