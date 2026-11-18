package Moduls;

import Base.ImagesGlobal;
import Engine.AnimSost;
import java.io.DataInputStream;
import java.io.IOException;
import desktop.graphics.GameGraphics;

public final class AuraType {
   public String name;
   public String description;
   public int sprite;
   public int cost;
   private int offset;
   public AnimSost anim = new AnimSost(0);
   public boolean drawable = false;

   public AuraType(DataInputStream dataInputStream, boolean draw) {
      try {
         this.drawable = draw;
         this.name = dataInputStream.readUTF();
         this.description = dataInputStream.readUTF();
         this.sprite = dataInputStream.readInt();
         this.cost = dataInputStream.readInt();
         this.offset = dataInputStream.readInt();
      } catch (IOException exception) {
         exception.printStackTrace();
      }
   }

   public final void draw(GameGraphics gameGraphics2, int cx, int cy) {
      if (this.drawable) {
         ImagesGlobal.animServerSprites[this.sprite].draw(gameGraphics2, this.anim, cx - ImagesGlobal.animServerSprites[this.sprite].swHalf, cy - this.getTopOffset());
      }
   }

   public final int getTopOffset() {
      return !this.drawable ? 0 : ImagesGlobal.animServerSprites[this.sprite].sh - this.offset;
   }
}
