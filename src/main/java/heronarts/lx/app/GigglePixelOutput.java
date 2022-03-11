package heronarts.lx.app;

import heronarts.lx.LX;
import heronarts.lx.app.pattern.GigglePixelPattern;
import heronarts.lx.color.LXDynamicColor;
import heronarts.lx.mixer.LXAbstractChannel;
import heronarts.lx.mixer.LXChannel;
import heronarts.lx.model.LXPoint;
import heronarts.lx.output.LXOutput;
import heronarts.lx.parameter.DiscreteParameter;
import heronarts.lx.pattern.LXPattern;

import java.util.*;

public class GigglePixelOutput extends LXOutput {
  private GigglePixelBroadcaster broadcaster;
  private LXPattern lastPattern;
  public DiscreteParameter channelIndex;

  public GigglePixelOutput(LX lx, GigglePixelBroadcaster broadcaster) {
    super(lx);
    this.broadcaster = broadcaster;
    this.lastPattern = null;
  }

  // Get the GigglePixel-specific colors for the selected pattern, if it
  // publishes such a list
  private List<Integer> patternColors(int[] colors) {
    int channelIndex = this.channelIndex.getValuei();

    // If it's 0, use the system palette instead.
    if (channelIndex == 0) return null;

    // Otherwise, move from its 1-based index to the 0-based index we need
    channelIndex--;

    List<LXAbstractChannel> abstractChannels = this.lx.engine.mixer.channels;
    List<LXChannel> channels = new ArrayList<>();
    for (LXAbstractChannel ac : abstractChannels) {
      if (ac instanceof LXChannel) {
        channels.add((LXChannel) ac);
      }
    }

    if (channelIndex >= channels.size()) return null;

    LXChannel channel = channels.get(channelIndex);
    LXPattern pattern = channel.getActivePattern();

    if (pattern != this.lastPattern) {
      this.lastPattern = pattern;
      this.broadcaster.sendImmediately();
    }
    if (!(pattern instanceof GigglePixelPattern)) return null;
    GigglePixelPattern gpPattern = (GigglePixelPattern) pattern;
    List<LXPoint> points = gpPattern.getGigglePixelPoints();
    if (points.isEmpty()) return null;

    List<Integer> gpColors = new ArrayList<>();
    for (LXPoint point : points) {
      gpColors.add(colors[point.index]);
    }
    return gpColors;
  }

  // If the pattern doesn't specify custom GigglePixel colors, use the system palette
  private List<Integer> paletteColors() {
    List<Integer> gpColors = new ArrayList<>();
    for (LXDynamicColor dc : this.lx.engine.palette.swatch.colors) {
      gpColors.add(dc.getColor());
    }
    return gpColors;
  }

  @Override
  protected void onSend(int[] colors, byte[][] glut, double brightness) {
    List<Integer> gpColors = patternColors(colors);
    if (gpColors == null) gpColors = paletteColors();
    this.broadcaster.setColors(gpColors);
  }
}
