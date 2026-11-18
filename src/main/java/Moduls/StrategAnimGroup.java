package Moduls;

import Base.Com;
import java.io.DataInputStream;

public final class StrategAnimGroup {
   public static byte ANIMATION_STATE_STAY = 0;
   public static byte ANIMATION_STATE_WALK = 1;
   public static byte ANIMATION_STATE_ATACK = 2;
   public static StrategAnimGroup[] strategAnimGroups;
   public StrategAnimSprite[] animations;
   public StrategProgramAnim useAnim = null;

   public static void loadAllFromStream(DataInputStream dataInputStream) {
      strategAnimGroups = new StrategAnimGroup[decompiledsupport.UncheckedDataInput.readShort(dataInputStream)];

      for(int i = 0; i < strategAnimGroups.length; ++i) {
         Com.PrBarTh.setText("Спрайт в игре " + (i + 1) + "/" + strategAnimGroups.length);
         StrategAnimGroup strategAnimGroup;
         StrategAnimGroup strategAnimGroup2;
         (strategAnimGroup2 = strategAnimGroup = new StrategAnimGroup()).animations = new StrategAnimSprite[3];
         short shortCode = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
         strategAnimGroup2.animations[ANIMATION_STATE_WALK] = shortCode < 0 ? null : StrategAnimSprite.strategAnimSprites[shortCode];
         shortCode = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
         strategAnimGroup2.animations[0] = shortCode < 0 ? null : StrategAnimSprite.strategAnimSprites[shortCode];
         shortCode = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
         strategAnimGroup2.animations[ANIMATION_STATE_ATACK] = shortCode < 0 ? null : StrategAnimSprite.strategAnimSprites[shortCode];
         shortCode = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
         strategAnimGroup2.useAnim = shortCode < 0 ? null : StrategProgramAnim.strategProgramAnims[shortCode];
         strategAnimGroups[i] = strategAnimGroup;
      }

   }
}
