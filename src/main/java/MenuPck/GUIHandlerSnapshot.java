package MenuPck;

public final class GUIHandlerSnapshot {
   public GUIHandlerEvents eventListener = null;
   public GUIBaseActivity bottomActivity = null;
   public boolean fastAnim = false;

   public GUIHandlerSnapshot(GUIHandlerEvents gUIHandlerEvents, GUIBaseActivity gUIBaseActivity, boolean fastAnim2) {
      this.eventListener = gUIHandlerEvents;
      this.bottomActivity = gUIBaseActivity;
      this.fastAnim = fastAnim2;
   }
}
