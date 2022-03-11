/**
 * Copyright 2020- Mark C. Slee, Heron Arts LLC
 *
 * This file is part of the LX Studio software library. By using
 * LX, you agree to the terms of the LX Studio Software License
 * and Distribution Agreement, available at: http://lx.studio/license
 *
 * Please note that the LX license is not open-source. The license
 * allows for free, non-commercial use.
 *
 * HERON ARTS MAKES NO WARRANTY, EXPRESS, IMPLIED, STATUTORY, OR
 * OTHERWISE, AND SPECIFICALLY DISCLAIMS ANY WARRANTY OF
 * MERCHANTABILITY, NON-INFRINGEMENT, OR FITNESS FOR A PARTICULAR
 * PURPOSE, WITH RESPECT TO THE SOFTWARE.
 *
 * @author Mark C. Slee <mark@heronarts.com>
 */

package heronarts.lx.app.pattern;

import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXPoint;
import heronarts.lx.pattern.LXPattern;

import java.util.*;

@LXCategory("GigglePixel-Enabled")
public class RGBStripesPattern extends LXPattern implements GigglePixelPattern {

  public RGBStripesPattern(LX lx) {
    super(lx);
  }

  public List<LXPoint> getGigglePixelPoints() {
    List<LXPoint> gigglePixelPoints = new ArrayList<>();
    if(model.points.length >= 1) gigglePixelPoints.add(model.points[0]);
    if(model.points.length >= 2) gigglePixelPoints.add(model.points[1]);
    if(model.points.length >= 3) gigglePixelPoints.add(model.points[2]);
    return gigglePixelPoints;
  }

  @Override
  protected void run(double deltaMs) {
    for (int i = 0 ; i < model.points.length; i++) {
      LXPoint p = model.points[i];
      int color;
      if (i % 3 == 0) {
        color = LXColor.rgb(255, 0, 0);
      } else if (i % 3 == 1) {
        color = LXColor.rgb(0, 255, 0);
      } else {
        color = LXColor.rgb(0, 0, 255);
      }
      colors[p.index] = color;
    }
  }

}
