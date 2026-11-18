package bridge.ui;

import applet.Main;
import bridge.movement.Speed;
import bridge.movement.NoClip;
import bridge.movement.NoStuck;
import bridge.movement.Teleport;
import bridge.movement.QuestFollower;
import bridge.player.Invisibility;
import bridge.visuals.ESP;
import bridge.visuals.BlockText;
import bridge.visuals.ShowStats;
import bridge.visuals.HideStatues;
import bridge.visuals.Zoom;
import bridge.visuals.CellTrail;
import bridge.visuals.SkinChanger;
import bridge.visuals.MapBuilder;
import bridge.player.Reach;
import bridge.player.AutoBattle;
import bridge.other.ObjectInteract;
import bridge.other.AutoCollect;
import bridge.other.AutoDungeon;
import bridge.other.AutoJuice;
import bridge.other.AutoOre;
import bridge.other.AutoSilk;
import bridge.other.MobGather;
import bridge.other.LuaScripts;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.Timer;

public final class ClickGui extends JComponent {
    private static final Font FONT = new Font("Calibri", Font.PLAIN, 15);
    private static final int BLUE = 0xff4269f5;
    private static final int LIGHT = 0xffd8d8d8;
    private static final String[] TAB_NAMES = {"Player", "Movement", "Visuals", "Other"};
    private final List<Tab> tabs = new ArrayList<>();
    private final BufferedImage[][] icons = new BufferedImage[5][2];
    private final Timer motion;
    private final List<Hit> hits = new ArrayList<>();
    private final List<Runnable> texts = new ArrayList<>();
    private final List<Runnable> circles = new ArrayList<>();
    private final Point origin = new Point(100, 100);
    private Point dragOrigin;
    private Point pressPoint;
    private int selectedTab;
    private float tabPosition;
    private Slider activeSlider;
    private ColorSetting activeColor;
    private int colorPart;
    private TextSetting activeText;
    private boolean textSelected;
    private Graphics2D drawing;
    private final Map<String, BufferedImage> roundedRects = new HashMap<>();
    private boolean contentClip;

    private abstract static class Setting {
        final String name;
        Setting(String name) { this.name = name; }
    }
    private static final class Slider extends Setting {
        float value, min, max, step;
        boolean integer;
        java.util.function.DoubleConsumer onChange = value -> {};
        void setValue(float next) {
            value=clamp(integer ? Math.round(next) : next,min,max);
            onChange.accept(value);
        }
        Slider(String name, float value, float min, float max, float step) {
            super(name); this.value=value; this.min=min; this.max=max; this.step=step;
        }
    }
    private static final class Checkbox extends Setting {
        boolean value;
        java.util.function.Consumer<Boolean> onChange=value->{};
        Checkbox(String name) { super(name); }
    }
    private static final class Combo extends Setting {
        final String[] items; int value; boolean open;
        java.util.function.IntConsumer onChange=value->{};
        Combo(String name, String... items) { super(name); this.items=items; }
    }
    private static final class Radio extends Setting {
        final String[] items; int value;
        Radio(String name, String... items) { super(name); this.items=items; }
    }
    private static final class Button extends Setting {
        boolean pressed;
        Runnable action=()->{};
        Button(String name) { super(name); }
    }
    private static final class TextSetting extends Setting {
        String value="";
        java.util.function.Supplier<String> get;
        java.util.function.Consumer<String> set=value->{};
        java.util.function.Predicate<String> valid=value->true;
        TextSetting(String name) { super(name); }
    }
    private static final class ColorSetting extends Setting {
        int primary=BLUE, secondary=0xff1a1a1a;
        float alpha=100, hueMain, hueSecondary;
        boolean open; int mode; String[] modes={};
        ColorSetting(String name) { super(name); }
    }
    public enum Category { PLAYER, MOVEMENT, VISUALS, OTHER }

    public static class Feature {
        final String name, description;
        final List<Setting> settings = new ArrayList<>();
        public volatile boolean enabled;
        public final Category category;
        boolean open; float animation;
        Runnable onToggle = () -> {};
        void refreshSettings() {
            for(Setting setting:settings)if(setting instanceof TextSetting) {
                TextSetting text=(TextSetting)setting;
                if(text.get!=null)text.value=text.get.get();
            }
        }
        protected Feature(Category category,String name,String description) {
            this.category=category;this.name=name;this.description=description;
        }
        protected final void checkbox(String name,java.util.function.BooleanSupplier get,
                                      java.util.function.Consumer<Boolean> set) {
            Checkbox checkbox=new Checkbox(name);checkbox.value=get.getAsBoolean();
            checkbox.onChange=set;settings.add(checkbox);
        }
        protected final void dropdown(String name,String[] items,java.util.function.IntSupplier get,
                                      java.util.function.IntConsumer set) {
            Combo combo=new Combo(name,items);combo.value=get.getAsInt();
            combo.onChange=set;settings.add(combo);
        }
        protected final void textInput(String name,java.util.function.Supplier<String> get,
                                       java.util.function.Consumer<String> set,
                                       java.util.function.Predicate<String> valid) {
            TextSetting text=new TextSetting(name);
            text.get=get;text.set=set;text.valid=valid;settings.add(text);
        }
        protected final void button(String name,Runnable action) {
            Button button=new Button(name);button.action=action;settings.add(button);
        }
        protected final void onToggle(Runnable action) {
            onToggle = action;
        }
        protected final void action(Runnable action) {
            onToggle=()->{
                try { if(enabled)action.run(); }
                finally { enabled=false; }
            };
        }
        protected final void slider(String name,int min,int max,
                                    java.util.function.IntSupplier get,
                                    java.util.function.IntConsumer set) {
            Slider slider=new Slider(name,get.getAsInt(),min,max,1);
            slider.integer=true;
            slider.onChange=value->set.accept((int)value);
            settings.add(slider);
        }
    }
    private static final class Tab {
        final List<Feature> features = new ArrayList<>();
        float animation; int scroll, maxScroll;
    }
    private interface Drag { void move(double x, double y); }
    private static final class Hit {
        final double x,y,w,h; final int button; final Runnable click; final Drag drag;
        Hit(double x,double y,double w,double h,int button,Runnable click,Drag drag) {
            this.x=x;this.y=y;this.w=w;this.h=h;this.button=button;this.click=click;this.drag=drag;
        }
        boolean contains(double px,double py) {return px>=x && px<x+w && py>=y && py<y+h;}
    }
    private Drag activeDrag;

    public ClickGui() {
        setOpaque(false);
        setFocusable(true);
        loadIcons();
        for (Category category : Category.values()) tabs.add(new Tab());
        for (Feature feature : new Feature[]{
                Reach.INSTANCE, Invisibility.INSTANCE, AutoBattle.INSTANCE,
                Speed.INSTANCE, NoClip.INSTANCE, NoStuck.INSTANCE, Teleport.INSTANCE, QuestFollower.INSTANCE,
                ESP.INSTANCE, BlockText.INSTANCE, ShowStats.INSTANCE, HideStatues.INSTANCE, Zoom.INSTANCE,
                CellTrail.INSTANCE, SkinChanger.INSTANCE, MapBuilder.INSTANCE,
                ObjectInteract.INSTANCE, AutoCollect.INSTANCE, AutoDungeon.INSTANCE, AutoJuice.INSTANCE,
                AutoSilk.INSTANCE, AutoOre.INSTANCE, MobGather.INSTANCE, LuaScripts.INSTANCE
        }) {
            tabs.get(feature.category.ordinal()).features.add(feature);
        }
        motion=new Timer(16,event->animate());
        MouseAdapter input=new MouseAdapter() {
            public void mousePressed(MouseEvent event) {
                if(!isVisible())return;
                double x=localX(event.getX()),y=localY(event.getY());
                if(event.getButton()==MouseEvent.BUTTON1) {
                    activeText=null;
                    if(x>=0 && x<382 && y>=0 && y<30) {
                        pressPoint=event.getPoint();dragOrigin=new Point(origin);event.consume();return;
                    }
                }
                refreshLayout();
                if(y>=32 && y<297 || x<102) {
                    for(int i=hits.size()-1;i>=0;i--) {
                        Hit hit=hits.get(i);
                        if(hit.button==event.getButton() && hit.contains(x,y)) {
                            if(hit.click!=null)hit.click.run();
                            if(hit.drag!=null) {activeDrag=hit.drag;activeDrag.move(x,y);}
                            break;
                        }
                    }
                }
                motion.start();repaint();event.consume();
            }
            public void mouseDragged(MouseEvent event) {
                if(dragOrigin!=null)origin.setLocation(dragOrigin.x+event.getX()-pressPoint.x,
                        dragOrigin.y+event.getY()-pressPoint.y);
                if(activeDrag!=null)activeDrag.move(localX(event.getX()),localY(event.getY()));
                repaint();event.consume();
            }
            public void mouseReleased(MouseEvent event) {
                dragOrigin=null;activeDrag=null;activeSlider=null;activeColor=null;
                for(Tab tab:tabs)for(Feature f:tab.features)for(Setting s:f.settings)
                    if(s instanceof Button)((Button)s).pressed=false;
                event.consume();
            }
            public void mouseWheelMoved(MouseWheelEvent event) {
                Tab tab=tabs.get(selectedTab);
                tab.scroll=Math.max(0,Math.min(tab.maxScroll,tab.scroll+event.getWheelRotation()*12));
                repaint();event.consume();
            }
        };
        addMouseListener(input);addMouseMotionListener(input);addMouseWheelListener(input);
    }

    private void loadIcons() {
        String[] names={"combat","movement","visuals","other","configs"};
        for(int i=0;i<names.length;i++)try(InputStream stream=getClass().getResourceAsStream(names[i]+".png")) {
            if(stream==null)throw new IOException("Missing "+names[i]);
            BufferedImage source=ImageIO.read(stream);
            for(int variant=0;variant<2;variant++) {
                int shade=variant==0?188:22;
                BufferedImage tinted=new BufferedImage(22,22,BufferedImage.TYPE_INT_ARGB);
                for(int y=0;y<22;y++)for(int x=0;x<22;x++)
                    tinted.setRGB(x,y,(source.getRGB(x,y)&0xff000000)|shade<<16|shade<<8|shade);
                icons[i][variant]=tinted;
            }
        }catch(IOException exception){throw new IllegalStateException("Cannot read template icons",exception);}
    }

    private void setMenuVisible(boolean value) {
        if(value) {
            for(Tab tab:tabs)for(Feature feature:tab.features)feature.refreshSettings();
            motion.start();
        }
        else {motion.stop();activeText=null;activeDrag=null;dragOrigin=null;}
        repaint();
    }
    private double localX(int x) {return (x-origin.x)/2.0;}
    private double localY(int y) {return (y-origin.y)/2.0;}
    private void animate() {
        boolean changed=false;
        float next=tabPosition+(selectedTab-tabPosition)*.15f;
        if(Math.abs(next-selectedTab)<.001f)next=selectedTab;
        changed|=next!=tabPosition;tabPosition=next;
        for(int i=0;i<tabs.size();i++) {
            Tab tab=tabs.get(i);float old=tab.animation;
            tab.animation=clamp(old+(i==selectedTab?.08f:-.08f),0,1);changed|=old!=tab.animation;
            for(Feature f:tab.features) {
                old=f.animation;f.animation=clamp(old+(f.enabled?.15f:-.08f),0,1);changed|=old!=f.animation;
            }
        }
        repaint();if(!changed)motion.stop();
    }
    private static float clamp(float value,float min,float max) {return Math.max(min,Math.min(max,value));}
    private void refreshLayout() {
        BufferedImage image=new BufferedImage(1,1,BufferedImage.TYPE_INT_RGB);
        Graphics2D g=image.createGraphics();g.translate(origin.x,origin.y);g.scale(2,2);
        render(g);g.dispose();
    }
    @Override protected void paintComponent(Graphics graphics) {
        Graphics2D g=(Graphics2D)graphics.create();
        g.translate(origin.x,origin.y);g.scale(2,2);render(g);g.dispose();
    }
    private void render(Graphics2D g) {
        drawing=g;hits.clear();texts.clear();circles.clear();contentClip=false;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        rect(0,0,382,300,0xee252725);
        rect(0,0,382,1,0xff222222);rect(0,299,382,300,0xff222222);
        rect(2,2,102,297,0xee252725);
        rect(2,2,102,32,0xff222222);rect(2,267,102,297,0xff222222);
        rect(105,32,380,297,0x90202020);
        rect(7,51,25,52,0xff454545);rect(80,51,97,52,0xff454545);
        rect(7,151,25,152,0xff454545);rect(80,151,97,152,0xff454545);
        circle(6,5,26,25,0xff191919);
        text(14,12,"V",0xffffffff);text(32,12,"Vuelo-Limit",0xff8db2ff);
        text(38,46,"CATEGORY",0xffaaaaaa);text(41,146,"CLIENT",0xffaaaaaa);
        round(8,61+Math.round(tabPosition*15),96,74+Math.round(tabPosition*15),BLUE,5);
        for(int i=0;i<tabs.size();i++) {
            final int index=i;
            icon(0+i,15,62+i*15,i==selectedTab);
            text(37,63+i*15,TAB_NAMES[i],mix(0xffbdbdbd,0xff161616,tabs.get(i).animation));
            hit(7,60+i*15,90,15,1,()->{selectedTab=index;activeText=null;motion.start();},null);
        }
        icon(4,15,162,false);text(37,163,"Configs",0xffbdbdbd);
        Tab tab=tabs.get(selectedTab);
        Graphics2D content=(Graphics2D)g.create();content.clipRect(105,32,275,265);
        drawing=content;contentClip=true;
        int cardY=35-tab.scroll;
        for(Feature feature:tab.features) {
            round(107,cardY,377,cardY+15,0xff1a1a1a,4);
            if(feature.open && !feature.settings.isEmpty())rect(107,cardY+11,377,cardY+15,0xff1a1a1a);
            text(110,cardY+3,feature.name,mix(0xffffffff,BLUE,feature.animation));
            int descWidth=measure(feature.description,7.5f);
            text(168+(182-descWidth)/2,cardY+3,feature.description,0xffaaaaaa);
            round(355,cardY+2,375,cardY+12,mix(0xff333333,BLUE,feature.animation),5);
            int knob=356+(int)(10*feature.animation);
            round(knob,cardY+3,knob+8,cardY+11,0xffffffff,4);
            hit(355,cardY+2,20,10,1,()->{feature.enabled=!feature.enabled;feature.onToggle.run();},null);
            hit(107,cardY,270,15,3,()->{
                feature.open=!feature.open;
                if(feature.open)feature.refreshSettings();
                activeText=null;
            },null);
            int y=cardY+15;
            if(feature.open)for(Setting setting:feature.settings)y=setting(setting,y);
            cardY=y+3;
        }
        tab.maxScroll=Math.max(0,cardY+tab.scroll-3-297);
        tab.scroll=Math.max(0,Math.min(tab.scroll,tab.maxScroll));
        drawing=g;contentClip=false;content.dispose();
        for(Runnable command:circles)command.run();
        for(Runnable command:texts)command.run();
        drawing=null;
    }

    private int setting(Setting setting,int y) {
        int bottom=y+15;
        if(!(setting instanceof Radio))rect(107,y,377,bottom,0x30000000);
        if(setting instanceof Slider) {
            Slider s=(Slider)setting;text(112,y+3,s.name,LIGHT);
            rect(276,y+1,372,y+13,0x30000000);border(276,y+1,372,y+13,0x904269f5);
            rect(284,y+2,285,y+12,0x904269f5);rect(364,y+2,365,y+12,0x904269f5);
            int dot=286+(int)(74*(s.value-s.min)/(s.max-s.min));
            rect(dot,y+2,dot+2,y+12,0x404269f5);
            text(279,y+3,"-",BLUE);text(366,y+3,"+",BLUE);
            text(316,y+3,s.integer ? Integer.toString(Math.round(s.value)) : String.format(Locale.ROOT,"%.2f",s.value),BLUE);
            hit(276,y+1,8,12,1,()->s.setValue(s.value-s.step),null);
            hit(364,y+1,8,12,1,()->s.setValue(s.value+s.step),null);
            hit(284,y+1,80,12,1,null,(x,py)->{activeSlider=s;s.setValue(s.min+(float)(x-284)/80*(s.max-s.min));});
        } else if(setting instanceof Checkbox) {
            Checkbox s=(Checkbox)setting;text(112,y+3,s.name,LIGHT);
            round(364,y+3,372,y+11,0xff333333,2);
            if(s.value)round(365,y+4,371,y+10,BLUE,1);
            hit(364,y+3,8,8,1,()->{s.value=!s.value;s.onChange.accept(s.value);},null);
        } else if(setting instanceof Combo) {
            Combo s=(Combo)setting;text(112,y+3,s.name,LIGHT);
            text(302,y+3,s.items[s.value],0xffffffff);text(364,y+3,s.open?"^":"v",BLUE);
            hit(107,y,270,15,1,()->s.open=!s.open,null);
            if(s.open)for(int i=0;i<s.items.length;i++) {
                final int index=i;int top=bottom;bottom+=13;
                rect(107,top,377,bottom,i==s.value?0xff252525:0xff1e1e1e);
                text(307,top+2,s.items[i],i==s.value?BLUE:0xffcccccc);
                hit(302,top,75,13,1,()->{s.value=index;s.onChange.accept(index);s.open=false;},null);
            }
        } else if(setting instanceof Radio) {
            Radio s=(Radio)setting;bottom=y;
            for(int i=0;i<s.items.length;i++) {
                int top=bottom;bottom+=13;final int index=i;
                rect(107,top,377,bottom,0x30000000);
                text(112,top+2,s.items[i],i==s.value?BLUE:0xffcccccc);
                circle(364,top+2,372,top+10,i==s.value?BLUE:0xff333333);
                hit(107,top,270,13,1,()->s.value=index,null);
            }
        } else if(setting instanceof Button) {
            Button s=(Button)setting;border(112,y+2,372,bottom-2,0x904269f5);
            int width=measure(s.name,15)/2;text(112+(260-width)/2,y+3,s.name,BLUE);
            hit(112,y,260,15,1,()->{s.pressed=true;s.action.run();},null);
        } else if(setting instanceof TextSetting) {
            TextSetting s=(TextSetting)setting;text(112,y+3,s.name,LIGHT);
            rect(272,y+1,372,bottom-2,s==activeText && textSelected?0xff25386b:0xff191919);
            rect(272,bottom-3,372,bottom-2,!s.valid.test(s.value)?0xffff6060:s==activeText?BLUE:0x60ffffff);
            centeredText(272,y+3,100,s.value.isEmpty()?"Enter text...":s.value,s==activeText?0xffffffff:0xff888888);
            hit(272,y+1,100,12,1,()->{activeText=s;textSelected=true;},null);
        } else if(setting instanceof ColorSetting) {
            ColorSetting s=(ColorSetting)setting;text(112,y+3,s.name,LIGHT);
            rect(227,y+2,241,bottom-2,s.primary);rect(243,y+2,257,bottom-2,s.secondary);
            String alpha="Alpha: "+Math.round(s.alpha)+"%";
            text(372-measure(alpha,15)/2,y+3,alpha,BLUE);
            hit(107,y,270,15,1,()->s.open=!s.open,null);
            if(s.open){colorPicker(s,bottom);bottom+=62;}
        }
        return bottom;
    }

    private void colorPicker(ColorSetting s,int top) {
        rect(107,top,377,top+62,0x9e1c1c1c);
        for(int part=0;part<2;part++) {
            final int selectedPart=part;int left=122+part*70;
            float hue=part==0?s.hueMain:s.hueSecondary;
            for(int y=0;y<40;y+=2)for(int x=0;x<65;x+=2)
                rect(left+x,top+2+y,left+x+2,top+4+y,hsv(hue,x/64f,1-y/39f));
            for(int x=0;x<65;x++)rect(left+x,top+45,left+x+1,top+49,hsv(x/64f,1,1));
            int marker=(int)(hue*64);rect(left+marker,top+44,left+marker+1,top+50,0xffffffff);
            hit(left,top+2,65,40,1,null,(x,y)->{
                activeColor=s;colorPart=selectedPart==0?1:3;
                int value=hsv(selectedPart==0?s.hueMain:s.hueSecondary,
                        clamp((float)(x-left)/64,0,1),1-clamp((float)(y-top-2)/39,0,1));
                if(selectedPart==0)s.primary=value;else s.secondary=value;
            });
            hit(left,top+45,65,4,1,null,(x,y)->{
                activeColor=s;colorPart=selectedPart==0?2:4;
                float next=clamp((float)(x-left)/64,0,1);
                int previous=selectedPart==0?s.primary:s.secondary;
                float[] hsv=Color.RGBtoHSB((previous>>16)&255,(previous>>8)&255,previous&255,null);
                if(selectedPart==0){s.hueMain=next;s.primary=hsv(next,hsv[1],hsv[2]);}
                else{s.hueSecondary=next;s.secondary=hsv(next,hsv[1],hsv[2]);}
            });
        }
        for(int x=0;x<135;x++) {int shade=255-x*255/134;rect(122+x,top+52,123+x,top+56,0xff000000|shade<<16|shade<<8|shade);}
        int marker=(int)((1-s.alpha/100)*134);rect(122+marker,top+51,123+marker,top+57,0xffffffff);
        hit(122,top+52,135,4,1,null,(x,y)->{activeColor=s;colorPart=5;s.alpha=(1-clamp((float)(x-122)/134,0,1))*100;});
        if(s.modes.length>0) {
            text(259,top+21,"Mode: "+s.modes[s.mode],BLUE);
            hit(259,top+21,118,10,1,()->s.mode=(s.mode+1)%s.modes.length,null);
        }
    }
    public boolean handleKey(KeyEvent event) {
        if(activeText==null)return false;
        if(event.getID()==KeyEvent.KEY_PRESSED) {
            int code=event.getKeyCode();String value=activeText.value;
            if(code==KeyEvent.VK_ESCAPE || code==KeyEvent.VK_ENTER)activeText=null;
            else if(code==KeyEvent.VK_A && event.isControlDown())textSelected=true;
            else if(code==KeyEvent.VK_BACK_SPACE && !value.isEmpty()) {
                int end=textSelected?0:value.offsetByCodePoints(value.length(),-1);
                textSelected=false;
                if(event.isControlDown())while(end>0 && value.charAt(end-1)!=' ')end=value.offsetByCodePoints(end,-1);
                activeText.value=value.substring(0,end);
            } else if(code==KeyEvent.VK_V && event.isControlDown()) {
                try {
                    String pasted=(String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                    activeText.value=(textSelected?"":activeText.value)+pasted;textSelected=false;
                }
                catch(Exception ignored) { }
            }
        } else if(event.getID()==KeyEvent.KEY_TYPED && !event.isControlDown() && event.getKeyChar()>=32
                && event.getKeyChar()!=127) {
            activeText.value=(textSelected?"":activeText.value)+event.getKeyChar();textSelected=false;
        }
        if(activeText!=null && (activeText.valid==null || activeText.valid.test(activeText.value)))
            activeText.set.accept(activeText.value);
        repaint();return true;
    }

    private void hit(double x,double y,double w,double h,int button,Runnable click,Drag drag) {
        if(contentClip && (y+h<=32 || y>=297))return;
        hits.add(new Hit(x,y,w,h,button,click,drag));
    }
    private static int hsv(float h,float s,float v) {
        float r=v,g=v,b=v;
        if(s!=0) {
            int i=(int)(h*6)%6;float f=h*6-(int)(h*6),p=v*(1-s),q=v*(1-s*f),t=v*(1-s*(1-f));
            switch(i){case 0:r=v;g=t;b=p;break;case 1:r=q;g=v;b=p;break;case 2:r=p;g=v;b=t;break;
                case 3:r=p;g=q;b=v;break;case 4:r=t;g=p;b=v;break;default:r=v;g=p;b=q;}
        }
        return 0xff000000|(int)(r*255)<<16|(int)(g*255)<<8|(int)(b*255);
    }
    private static int mix(int first,int second,float progress) {
        int result=0;for(int shift:new int[]{0,8,16,24})
            result|=((int)(((first>>>shift)&255)*(1-progress)+((second>>>shift)&255)*progress)&255)<<shift;
        return result;
    }
    private void rect(double l,double t,double r,double b,int color) {
        drawing.setColor(new Color(color,true));drawing.fill(new java.awt.geom.Rectangle2D.Double(l,t,r-l,b-t));
    }
    private void round(int l,int t,int r,int b,int color,int radius) {
        int width=(r-l)*2,height=(b-t)*2,rad=Math.min(radius*2,Math.min(width/2,height/2));
        String key=width+":"+height+":"+rad+":"+color;
        BufferedImage image=roundedRects.get(key);
        if(image==null) {
            image=new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
            for(int y=0;y<height;y++)for(int x=0;x<width;x++) {
                int samples=0;
                for(int sy=0;sy<3;sy++)for(int sx=0;sx<3;sx++) {
                    float px=x+(sx+.5f)/3,py=y+(sy+.5f)/3;
                    float dx=px<rad?px-rad:px>width-rad?px-(width-rad):0;
                    float dy=py<rad?py-rad:py>height-rad?py-(height-rad):0;
                    if(dx*dx+dy*dy<=rad*rad)samples++;
                }
                if(samples>0) {
                    int alpha=Math.max(1,(int)((color>>>24)*samples/9f+.5f));
                    image.setRGB(x,y,alpha<<24|color&0xffffff);
                }
            }
            if(roundedRects.size()>512)roundedRects.clear();
            roundedRects.put(key,image);
        }
        drawing.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        drawing.drawImage(image,l,t,r-l,b-t,null);
    }
    private void border(int l,int t,int r,int b,int color) {
        rect(l,t,r,t+1,color);rect(l,b-1,r,b,color);rect(l,t+1,l+1,b-1,color);rect(r-1,t+1,r,b-1,color);
    }
    private void circle(int l,int t,int r,int b,int color) {
        Graphics2D target=(Graphics2D)drawing.create();
        circles.add(()->{target.setColor(new Color(color,true));target.fillOval(l,t,r-l,b-t);target.dispose();});
    }
    private void icon(int index,int x,int y,boolean selected) {
        drawing.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        drawing.drawImage(icons[index][selected?1:0],x,y,11,11,null);
    }
    private int measure(String value,float size) {
        Font font=FONT.deriveFont(size);
        return (int)Math.round(font.getStringBounds(value,drawing.getFontRenderContext()).getWidth());
    }
    private void text(int x,int top,String value,int color) {queueText(x,top,0,value,color);}
    private void centeredText(int x,int top,int width,String value,int color) {queueText(x,top,width,value,color);}
    private void queueText(int x,int top,int width,String value,int color) {
        Graphics2D target=(Graphics2D)drawing.create();
        texts.add(()->{
            target.setFont(FONT.deriveFont(7.5f));
            target.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            target.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            if(width>0)target.clipRect(x,top,width,20);
            int left=x;
            if(width>0)left=x+(width-measureText(target,value))/2;
            target.setColor(new Color(color,true));
            float baseline=top+7.5f*1950/2048;
            target.drawString(value,left,baseline);target.dispose();
        });
    }
    private int measureText(Graphics2D g,String value) {
        return (int)Math.round(g.getFont().getStringBounds(value,g.getFontRenderContext()).getWidth());
    }
    private JFrame frame;
    private Component previousFocus;
    private boolean open;
    private boolean rightShiftDown;
    private boolean consumeEscapeRelease;

    private ClickGui(JFrame frame) {
        this();
        this.frame = frame;
        setVisible(false);
        setMenuVisible(false);
        Main.instance.setGlassPane(this);
        Main.instance.getRootPane().revalidate();
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this::dispatchKey);
        frame.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent event) {
            }

            @Override
            public void windowLostFocus(WindowEvent event) {
                rightShiftDown = false;
            }
        });
    }

    public static void install() {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(ClickGui::install);
            return;
        }
        if (Main.instance == null) {
            throw new IllegalStateException("Game window has not initialized");
        }
        java.awt.Window window = SwingUtilities.getWindowAncestor(Main.instance);
        if (!(window instanceof JFrame)) {
            throw new IllegalStateException("Entropy needs the desktop game window");
        }
        JFrame frame = (JFrame) window;
        if (frame.getRootPane().getClientProperty("entropy.menu") == null) {
            frame.getRootPane().putClientProperty("entropy.menu", new ClickGui(frame));
        }
    }

    private boolean dispatchKey(KeyEvent event) {
        Component source = event.getComponent();
        boolean gameWindow = source != null
                && (source == frame || SwingUtilities.getWindowAncestor(source) == frame);
        if (!gameWindow) {
            return false;
        }
        if (event.getKeyCode() == KeyEvent.VK_SHIFT
                && event.getKeyLocation() == KeyEvent.KEY_LOCATION_RIGHT) {
            if (event.getID() == KeyEvent.KEY_PRESSED && !rightShiftDown) {
                rightShiftDown = true;
                setOpen(!open);
            } else if (event.getID() == KeyEvent.KEY_RELEASED) {
                rightShiftDown = false;
            }
            return true;
        }
        if (open && handleKey(event)) return true;
        if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (open && event.getID() == KeyEvent.KEY_PRESSED) {
                consumeEscapeRelease = true;
                setOpen(false);
                return true;
            }
            if (consumeEscapeRelease) {
                if (event.getID() == KeyEvent.KEY_RELEASED) {
                    consumeEscapeRelease = false;
                }
                return true;
            }
        }
        return isVisible();
    }

    private void setOpen(boolean value) {
        if (open == value) {
            return;
        }
        open = value;
        if (open) {
            previousFocus = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
            setVisible(true);
            Main.instance.getRootPane().revalidate();
            requestFocusInWindow();
        }
        setMenuVisible(open);
        if (!open) {
            setVisible(false);
            if (previousFocus != null && previousFocus.isShowing()) previousFocus.requestFocusInWindow();
            else if (Main.instance.getContentPane().getComponentCount() > 0)
                Main.instance.getContentPane().getComponent(0).requestFocusInWindow();
        }
    }

}
