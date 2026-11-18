package bridge.visuals;

import Base.Com;
import Engine.Camera;
import Moduls.Enemy;
import Moduls.EnemysMap;
import Moduls.Legionery;
import Moduls.Strategist;
import Moduls.TileMap;
import Moduls.Town;
import Moduls.otherheroes.HeroOnMap;
import Moduls.otherheroes.HeroesOnMapManager;
import bridge.ui.ClickGui;
import desktop.graphics.GameGraphics;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public final class ESP extends ClickGui.Feature {
    public static final ESP INSTANCE = new ESP();
    public volatile int target;
    public volatile boolean tracers;

    public interface DrawingSurface {
        Graphics entropy$createGraphics();
    }

    private ESP() {
        super(ClickGui.Category.VISUALS,"ESP","Highlight targets");
        dropdown("Targets",new String[]{"Objects","Enemies","Players"},()->target,value->target=value);
        checkbox("Tracers",()->tracers,value->tracers=value);
    }

    public void draw(GameGraphics surface) {
        Strategist player=Strategist.instance;
        TileMap map=Com.currentGameMap;
        Camera camera=Com.cameraWorld;
        if(!enabled)return;
        Graphics graphics=((DrawingSurface)(Object)surface).entropy$createGraphics();
        try {
            int cameraX=camera.getCx(),cameraY=camera.getCy();
            int[] padding=player.avatarGraphic.getBasePaddingsNoAura(player.watchDirection);
            int fromX=player.cx+(padding[1]-padding[0])/2-cameraX;
            int fromY=player.cy+(padding[3]-padding[2])/2-cameraY;
            boolean lines=tracers;
            switch(target) {
                case 0:
                    Town[] objects=map.towns;
                    if(objects==null)return;
                    for(Town object:objects)
                    {
                        if(object==null || object.state!=0 || object.type==0)continue;
                        if(object.SafeSpriteInd>=0 && (Com.Decors==null || object.SafeSpriteInd>=Com.Decors.length))continue;
                        int[] bounds=object.getPaddings(map);
                        box(graphics,object.dx-bounds[0]-cameraX,object.dy-bounds[2]-cameraY,
                                bounds[0]+bounds[1],bounds[2]+bounds[3],0x62a5ff,lines,fromX,fromY);
                    }
                    break;
                case 1:
                    EnemysMap enemies=Com.EnsMap;
                    Enemy[] snapshot=enemies.Enemys;
                    if(snapshot==null)return;
                    for(Enemy enemy:snapshot)
                    {
                        if(enemy==null || !enemy.Enable)continue;
                        int[] bounds=Legionery.getPaddings(enemy.BodyInd,enemy.HeadInd,enemy.direction);
                        box(graphics,enemy.cx-bounds[0]-cameraX,enemy.cy-bounds[2]-cameraY,
                                bounds[0]+bounds[1],bounds[2]+bounds[3],0xff647c,lines,fromX,fromY);
                    }
                    break;
                case 2:
                    HeroesOnMapManager heroes=HeroesOnMapManager.instance;
                    if(heroes.forMapInd!=map.serverIndex || heroes.isOldHeroesTilNextLoad())return;
                    for(Object entry:heroes.heroes.toArray())
                    {
                        if(!(entry instanceof HeroOnMap))continue;
                        HeroOnMap hero=(HeroOnMap)entry;
                        if(hero.id==player.id || hero.strategGraphic==null)continue;
                        int[] bounds=hero.strategGraphic.getBasePaddingsNoAura(hero.watchDirection);
                        box(graphics,hero.cx-bounds[0]-cameraX,hero.cy-bounds[2]-cameraY,
                                bounds[0]+bounds[1],bounds[2]+bounds[3],0x64e5ac,lines,fromX,fromY);
                    }
                    break;
            }
        } finally { graphics.dispose(); }
    }

    private static void box(Graphics graphics,int x,int y,int width,int height,int color,
                            boolean tracers,int fromX,int fromY) {
        width=Math.max(2,width);height=Math.max(2,height);
        Rectangle clip=graphics.getClipBounds();
        if(tracers) {
            graphics.setColor(new Color(color));
            graphics.drawLine(fromX,fromY,x+width/2,y+height/2);
        }
        if(clip!=null && !clip.intersects(x-1,y-1,width+3,height+3))return;
        graphics.setColor(new Color(0x141414));
        graphics.drawRect(x-1,y-1,width+2,height+2);
        graphics.drawRect(x+1,y+1,width-2,height-2);
        graphics.setColor(new Color(color));
        graphics.drawRect(x,y,width,height);
    }
}
