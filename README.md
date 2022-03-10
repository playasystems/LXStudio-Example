GigglePixel LXStudio example by Mike Schiraldi

A demonstration of how to integrate [the GigglePixel library](https://github.com/playasystems/gigglepixel) into LXStudio

To use, run the project and look for the GigglePixel section of the sidebar. Set it to "Broadcast" to make it broadcast GigglePixel updates
over all available UDP interfaces, or set it to "Listen" to have it listen for GigglePixel traffic arriving on any UDP interface.

If you'd like to add GigglePixel support to your LX project, just plagiarize the following lines:

* [Code to add to initialize()](https://github.com/playasystems/LXStudio-Example/blob/master/src/main/java/heronarts/lx/app/LXStudioApp.java#L91-L110)
* [Code to add to onUIReady()](https://github.com/playasystems/LXStudio-Example/blob/master/src/main/java/heronarts/lx/app/LXStudioApp.java#L122-L124)
* [GigglePixelUI.java](https://github.com/playasystems/LXStudio-Example/blob/master/src/main/java/heronarts/lx/app/GigglePixelUI.java)
* [GigglePixelListener.java](https://github.com/playasystems/LXStudio-Example/blob/master/src/main/java/heronarts/lx/app/GigglePixelListener.java)
* [GigglePixelBroadcaster.java](https://github.com/playasystems/LXStudio-Example/blob/master/src/main/java/heronarts/lx/app/GigglePixelBroadcaster.java)


[More on LXStudio here](https://github.com/heronarts/LXStudio-IDE)

