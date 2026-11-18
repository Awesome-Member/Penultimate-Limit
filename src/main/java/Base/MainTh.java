
package Base;

import Base.Com;
import Base.GameCommons;
import Engine.Mystery;
import Engine.Threader;
import desktop.PlatformRuntime;
import Moduls.PFter;
import Moduls.EnemysMap;
import battlepck.client.BattleTh;
import java.util.Vector;
import desktop.graphics.GameGraphics;

public final class MainTh
extends Threader {
    private boolean[] wasPressesKeys = new boolean[34];
    private boolean wasPointerPressed = false;
    private final Object paintLock = new Object();
    private final Object actionsLock = new Object();
    private Vector actionsQueue = new Vector();
    public boolean stopFunctionate = false;
    public boolean stopPaint = false;
    public boolean isDrawProgress = false;
    private final Object pfterLock = new Object();
    private boolean isFinalPFter = false;
    private PFter curPFter1 = null;
    private PFter curPFter2 = null;
    private PFter prevPFter1 = null;
    private PFter prevPFter2 = null;
    private PFter prevKeyHandler = null;
    private static String text = "";
    private final Object changeSizeLock = new Object();
    private int changeSizeW = -1;
    private int changeSizeH = -1;
    private boolean skipActions = false;
    private boolean wasPaintError = false;
    private int fpsLastVal = 0;
    private int fpsAccumVal = 0;
    private long fpsLastTick = 0L;
    private boolean wasPaint = false;
    private long wasPaintStartTime = -1L;
    private boolean willInnerPaintFlag1 = false;
    private boolean willInnerPaintFlag2 = false;

    public MainTh() {
        this.setStopPaint();
    }

    public final void setStopPaint() {
        this.stopPaint = false;
        Com.showGameCursor();
    }


    @Override
    protected final void sizeChanged(int width, int height) {
        super.sizeChanged(width, height);
        Object resolutionLock = this.changeSizeLock;
        synchronized (resolutionLock) {
            this.changeSizeW = width;
            this.changeSizeH = height;
            return;
        }
    }


    public final PFter getPFter1() {
        Object controllersLock = this.pfterLock;
        synchronized (controllersLock) {
            return this.curPFter1;
        }
    }


    public final PFter getTopPFter() {
        Object controllersLock = this.pfterLock;
        synchronized (controllersLock) {
            if (this.curPFter2 != null) {
                return this.curPFter2;
            }
            return this.curPFter1;
        }
    }


    public final boolean isPFterController(PFter pFter1) {
        if (this.stopPaint || this.stopFunctionate) {
            return false;
        }
        Object controllersLock = this.pfterLock;
        synchronized (controllersLock) {
            if (pFter1 == this.curPFter2) {
                return true;
            }
            if (pFter1 == this.curPFter1 && this.curPFter2 == null) {
                return true;
            }
        }
        return false;
    }


    public final boolean setPFter1(PFter pFter1) {
        Object controllersLock = this.pfterLock;
        synchronized (controllersLock) {
            if (this.curPFter1 != null && !this.curPFter1.isReplaceable()) {
                return false;
            }
            if (pFter1 != this.curPFter1) {
                this.skipActions = true;
                if (this.curPFter1 != null && this.prevPFter1 == null) {
                    this.prevPFter1 = this.curPFter1;
                }
                this.curPFter1 = pFter1;
                if (this.curPFter2 == null) {
                    this.cleareWasPresses();
                }
                return true;
            }
        }
        return false;
    }


    public final boolean setPFter2(PFter pFter2) {
        Object controllersLock = this.pfterLock;
        synchronized (controllersLock) {
            if (this.curPFter2 != null && !this.curPFter2.isReplaceable()) {
                return false;
            }
            if (pFter2 != this.curPFter2) {
                this.skipActions = true;
                if (this.curPFter1 != null && this.prevPFter1 == null) {
                    this.prevPFter1 = this.curPFter1;
                }
                if (this.curPFter2 != null && this.prevPFter2 == null) {
                    this.prevPFter2 = this.curPFter2;
                }
                this.curPFter2 = pFter2;
                this.cleareWasPresses();
                return true;
            }
        }
        return false;
    }


    public final void stopAllActions() {
        Object queueLock = this.actionsLock;
        synchronized (queueLock) {
            this.actionsQueue.removeAllElements();
            return;
        }
    }


    public final void callReleasAllEvent() {
        Object controllersLock = this.pfterLock;
        synchronized (controllersLock) {
            if (this.curPFter1 != null) {
                this.curPFter1.releaseAllEvent();
            }
            if (this.curPFter2 != null) {
                this.curPFter2.releaseAllEvent();
            }
            return;
        }
    }


    private void functional() {
        updateFailure: {
            PFter pfter2;
            PFter pfter1;
            inputDispatch: {
                PFter keyHandler;
                Object actionsSnapshot;
                prepareInput: {
                    PFter prevpfter2;
                    actionsSnapshot = this.pfterLock;
                    synchronized (actionsSnapshot) {
                        pfter1 = this.curPFter1;
                        pfter2 = this.curPFter2;
                        keyHandler = this.prevPFter1;
                        prevpfter2 = this.prevPFter2;
                        this.prevPFter1 = null;
                        this.prevPFter2 = null;
                    }
                    Object queueLock = this.actionsLock;
                    synchronized (queueLock) {
                        actionsSnapshot = this.actionsQueue;
                        this.actionsQueue = new Vector();
                    }
                    try {
                        long time;
                        if (keyHandler != null) {
                            keyHandler.releaseAllEvent();
                        }
                        if (prevpfter2 != null) {
                            prevpfter2.releaseAllEvent();
                        }
                        if ((time = System.currentTimeMillis()) - this.fpsLastTick >= 1000L) {
                            this.fpsLastTick = time;
                            this.fpsLastVal = this.fpsAccumVal;
                            this.fpsAccumVal = 0;
                        }
                        ++this.fpsAccumVal;
                        if (!this.isDrawProgress) {
                            keyHandler = null;
                            if (pfter2 != null) {
                                keyHandler = pfter2;
                            } else if (pfter1 != null) {
                                keyHandler = pfter1;
                            }
                            if (this.prevKeyHandler != keyHandler) {
                                keyHandler.onAfterHardLoad();
                            }
                            this.prevKeyHandler = keyHandler;
                            if (keyHandler == null || this.stopPaint) break inputDispatch;
                            this.skipActions = false;
                            break prepareInput;
                        }
                        if (pfter1 != null) {
                            pfter1.functionate();
                        }
                        if (pfter2 != null) {
                            pfter2.functionate();
                        }
                        return;
                    }
                    catch (Exception exception) {
                        Com.SendMistace("Error in Functional all", true, false, exception);
                        Com.SendMistace("Error in Functional " + BattleTh.pressFireStep + " " + BattleTh.funcStep + exception.toString() + this.getCurrentSost() + " " + EnemysMap.closeEnsPhase, true, true, exception);
                        break updateFailure;
                    }
                }
                pendingActions: for (int i = 0; i < ((Vector)actionsSnapshot).size() && !this.skipActions; ++i) {
                    int[] action = (int[])((Vector)actionsSnapshot).elementAt(i);
                    switch (action[0]) {
                        case 3: {
                            keyHandler.pressPointer(action[1], action[2]);
                            continue pendingActions;
                        }
                        case 2: {
                            keyHandler.dragPointer(action[1], action[2]);
                            continue pendingActions;
                        }
                        case 5: {
                            keyHandler.movePointer(action[1], action[2]);
                            continue pendingActions;
                        }
                        case 1: {
                            keyHandler.releasePointer(action[1], action[2]);
                            continue pendingActions;
                        }
                        case 4: {
                            keyHandler.wheelRot(action[1]);
                            continue pendingActions;
                        }
                        case 6: {
                            keyHandler.pressStar();
                            continue pendingActions;
                        }
                        case 7: {
                            keyHandler.pressSharp();
                            continue pendingActions;
                        }
                        case 8: {
                            keyHandler.press1();
                            continue pendingActions;
                        }
                        case 9: {
                            keyHandler.press3();
                            continue pendingActions;
                        }
                        case 10: {
                            keyHandler.press7();
                            continue pendingActions;
                        }
                        case 11: {
                            keyHandler.press9();
                            continue pendingActions;
                        }
                        case 12: {
                            keyHandler.press0();
                            continue pendingActions;
                        }
                        case 13: {
                            keyHandler.pressLeftSoft();
                            continue pendingActions;
                        }
                        case 14: {
                            keyHandler.pressRightSoft();
                            continue pendingActions;
                        }
                        case 15: {
                            keyHandler.pressUp();
                            continue pendingActions;
                        }
                        case 16: {
                            keyHandler.pressDown();
                            continue pendingActions;
                        }
                        case 17: {
                            keyHandler.pressLeft();
                            continue pendingActions;
                        }
                        case 18: {
                            keyHandler.pressRight();
                            continue pendingActions;
                        }
                        case 19: {
                            keyHandler.pressFire();
                            continue pendingActions;
                        }
                        case 20: {
                            continue pendingActions;
                        }
                        case 21: {
                            continue pendingActions;
                        }
                        case 22: {
                            keyHandler.release1();
                            continue pendingActions;
                        }
                        case 23: {
                            keyHandler.release3();
                            continue pendingActions;
                        }
                        case 24: {
                            keyHandler.release7();
                            continue pendingActions;
                        }
                        case 25: {
                            keyHandler.release9();
                            continue pendingActions;
                        }
                        case 26: {
                            continue pendingActions;
                        }
                        case 27: {
                            keyHandler.releaseLeftSoft();
                            continue pendingActions;
                        }
                        case 28: {
                            keyHandler.releaseRightSoft();
                            continue pendingActions;
                        }
                        case 29: {
                            keyHandler.releaseUp();
                            continue pendingActions;
                        }
                        case 30: {
                            keyHandler.releaseDown();
                            continue pendingActions;
                        }
                        case 31: {
                            keyHandler.releaseLeft();
                            continue pendingActions;
                        }
                        case 32: {
                            keyHandler.releaseRight();
                            continue pendingActions;
                        }
                        case 33: {
                            keyHandler.releaseFire();
                            continue pendingActions;
                        }
                    }
                }
            }
            if (pfter1 != null) {
                pfter1.functionateUnderDrawProgress();
            }
            if (pfter2 != null) {
                pfter2.functionateUnderDrawProgress();
            }
            GameCommons.instance.functionalGlobal();
        }
    }


    private void paintInner(boolean willInnerPaintFlag12, boolean willInnerPaintFlag22, GameGraphics overGraphics) {
        PFter pfter2;
        PFter pfter1;
        if ((Com.isEverythingBad || PlatformRuntime.getInstance().getPlatformType() == 2) && overGraphics == null) {
            this.willInnerPaintFlag1 = willInnerPaintFlag12;
            this.willInnerPaintFlag2 = willInnerPaintFlag22;
            return;
        }
        PlatformRuntime.getInstance();
        if (this.stopPaint) {
            return;
        }
        if (!Com.isEverythingBad && PlatformRuntime.getInstance().getPlatformType() != 2 || overGraphics == null) {
            overGraphics = Mystery.GBuffer;
        }
        Object paintContext = this.pfterLock;
        synchronized (paintContext) {
            pfter1 = this.curPFter1;
            pfter2 = this.curPFter2;
        }
        try {
            paintContext = this.paintLock;
            synchronized (paintContext) {
                paintControllers: {
                    GameGraphics useGraphics = overGraphics;
                    useGraphics.translate(-useGraphics.getTranslateX(), -overGraphics.getTranslateY());
                    overGraphics.setClip(0, 0, Mystery.scrWidth, Mystery.scrHeight);
                    if (willInnerPaintFlag22) {
                        boolean drawProgress = this.isDrawProgress;
                        try {
                            if (pfter1 != null) {
                                pfter1.paint(overGraphics, drawProgress);
                            }
                            if (pfter2 != null) {
                                pfter2.paint(overGraphics, drawProgress);
                            }
                            GameCommons.instance.timerDraw(overGraphics);
                        }
                        catch (Exception exception) {
                            if (drawProgress) break paintControllers;
                            throw exception;
                        }
                    }
                }
                if (willInnerPaintFlag12) {
                    Com.PrBarTh.paint(overGraphics);
                }
            }
        }
        catch (Exception exception) {
            paintContext = exception;
            exception.printStackTrace();
            if (!this.wasPaintError) {
                Com.SendMistace("Error in MainTh paintAll2 all", true, false, (Exception)paintContext);
                Com.SendMistace("Error in MainTh paintAll2 is " + willInnerPaintFlag12 + " " + willInnerPaintFlag22 + " " + ((Throwable)paintContext).toString() + this.getCurrentSost(), true, false, (Exception)paintContext);
            }
            this.wasPaintError = true;
        }
    }

    public final void paintProgress() {
        Mystery.InitFPS();
        this.tryApplyNewResolution(true);
        this.paintInner(true, true, null);
    }


    @Override
    public final void paint(GameGraphics graphics) {
        Object renderLock = this.paintLock;
        synchronized (renderLock) {
            if (Com.isEverythingBad || PlatformRuntime.getInstance().getPlatformType() == 2) {
                MainTh mainThread = this;
                mainThread.paintInner(mainThread.willInnerPaintFlag1, this.willInnerPaintFlag2, graphics);
            } else {
                Mystery.drawBuffer(graphics);
            }
            if (this.stopPaint) {
                this.wasPaint = true;
                return;
            }
            if (!text.equals("")) {
                graphics.setColor(0);
                graphics.fillRect(0, 0, 200, 16);
                graphics.setColor(0xFFFFFF);
                graphics.drawString(text, 0, 0);
            }
            this.wasPaint = true;
            return;
        }
    }

    private String getCurrentSost() {
        String description = "";
        if (this.curPFter1 != null) {
            description = description + "curPFter1 = " + this.curPFter1.getClass().toString() + "; ";
        }
        if (this.curPFter2 != null) {
            description = description + "curPFter2 = " + this.curPFter2.getClass().toString() + "; ";
        }
        return description;
    }

    @Override
    public final void run() {
        PlatformRuntime.initializeThread();
        Mystery.InitFPS();
        try {
            while (true) {
                if (!this.isActive) {
                    return;
                }
                MainTh mainThread = this;
                mainThread.tryApplyNewResolution(false);
                Mystery.InitFPS();
                mainThread.functional();
                if (mainThread.stopFunctionate) continue;
                if (!mainThread.isDrawProgress) {
                    mainThread.paintInner(false, true, null);
                }
                if (!mainThread.stopPaint) {
                    mainThread.wasPaint = false;
                    mainThread.wasPaintStartTime = System.currentTimeMillis();
                    mainThread.repaint();
                    while (!(mainThread.wasPaint || mainThread.stopPaint || mainThread.stopFunctionate || System.currentTimeMillis() - mainThread.wasPaintStartTime >= 1000L)) {
                        Thread.yield();
                        try {
                            if (PlatformRuntime.getInstance().getPlatformType() != 1) continue;
                            Thread.sleep(5L);
                        }
                        catch (InterruptedException interruptedException) {}
                    }
                }
                try {
                    if (PlatformRuntime.getInstance().getPlatformType() == 1) {
                        Thread.sleep(5L);
                    }
                }
                catch (InterruptedException interruptedException) {}
                Thread.yield();
            }
        }
        catch (Exception exception) {
            Com.SendMistace("Error in MainTh is " + exception.toString(), true, true, exception);
            return;
        }
    }


    private void tryApplyNewResolution(boolean justPaint) {
        int appSW = 0;
        int appSH = 0;
        Object resolutionLock = this.changeSizeLock;
        synchronized (resolutionLock) {
            if (this.changeSizeW > 0 && this.changeSizeH > 0) {
                appSW = this.changeSizeW;
                appSH = this.changeSizeH;
                this.changeSizeW = -1;
                this.changeSizeH = -1;
            }
        }
        if (appSW > 0 && appSH > 0) {
            Com.ChangeResolution(appSW, appSH, justPaint);
            PlatformRuntime.getInstance();
        }
    }


    @Override
    public final void wheelRotate(int num) {
        GameCommons.instance.closeFloatTextByClick();
        Object queueLock = this.actionsLock;
        synchronized (queueLock) {
            this.actionsQueue.addElement(new int[]{4, num});
            return;
        }
    }


    @Override
    public final void enqueuePointerPressed(int x, int y) {
        Object queueLock = this.actionsLock;
        synchronized (queueLock) {
            this.actionsQueue.addElement(new int[]{5, x, y});
            return;
        }
    }


    @Override
    public final void pointerPressed(int x, int y) {
        GameCommons.instance.closeFloatTextByClick();
        Object queueLock = this.actionsLock;
        synchronized (queueLock) {
            this.wasPointerPressed = true;
            this.actionsQueue.addElement(new int[]{3, x, y});
            return;
        }
    }


    @Override
    public final void enqueuePointerDragged(int x, int y) {
        Object queueLock = this.actionsLock;
        synchronized (queueLock) {
            if (this.wasPointerPressed) {
                this.actionsQueue.addElement(new int[]{2, x, y});
            }
            return;
        }
    }


    @Override
    public final void pointerReleased(int x, int y) {
        Object queueLock = this.actionsLock;
        synchronized (queueLock) {
            if (this.wasPointerPressed) {
                this.actionsQueue.addElement(new int[]{1, x, y});
            }
            this.wasPointerPressed = false;
            return;
        }
    }

    @Override
    public final void keyPressed(int keyOrActionCode) {
        if ((keyOrActionCode = this.mapKeyToPressAction(keyOrActionCode)) >= 0) {
            this.addSimpleAction(keyOrActionCode);
            if (!this.wasPressesKeys[keyOrActionCode]) {
                GameCommons.instance.closeFloatTextByClick();
            }
            this.wasPressesKeys[keyOrActionCode] = true;
        }
    }

    private int mapKeyToPressAction(int keyCode) {
        int actionCode = -1;
        keyMapping : switch (MainTh.getGameAction(keyCode)) {
            case 2: {
                actionCode = 16;
                break;
            }
            case 3: {
                actionCode = 17;
                break;
            }
            case 4: {
                actionCode = 18;
                break;
            }
            case 1: {
                actionCode = 15;
                break;
            }
            case 5: {
                actionCode = 19;
                break;
            }
            default: {
                if (keyCode == -6 || keyCode == -21 || keyCode == -1) {
                    actionCode = 13;
                }
                if (keyCode == -7 || keyCode == -22 || keyCode == -4) {
                    actionCode = 14;
                }
                switch (keyCode) {
                    case 56: {
                        actionCode = 16;
                        break keyMapping;
                    }
                    case 52: {
                        actionCode = 17;
                        break keyMapping;
                    }
                    case 54: {
                        actionCode = 18;
                        break keyMapping;
                    }
                    case 50: {
                        actionCode = 15;
                        break keyMapping;
                    }
                    case 53: {
                        actionCode = 19;
                        break keyMapping;
                    }
                    case 42: {
                        actionCode = 6;
                        break keyMapping;
                    }
                    case 35: {
                        actionCode = 7;
                        break keyMapping;
                    }
                    case 49: {
                        actionCode = 8;
                        break keyMapping;
                    }
                    case 51: {
                        actionCode = 9;
                        break keyMapping;
                    }
                    case 55: {
                        actionCode = 10;
                        break keyMapping;
                    }
                    case 57: {
                        actionCode = 11;
                        break keyMapping;
                    }
                    case 48: {
                        actionCode = 12;
                        break keyMapping;
                    }
                }
            }
        }
        return actionCode;
    }

    @Override
    public final void keyReleased(int keyOrPressCode) {
        int keyCode = keyOrPressCode;
        MainTh mainThread = this;
        int actionCode = -1;
        keyMapping : switch (MainTh.getGameAction(keyCode)) {
            case 2: {
                actionCode = 30;
                break;
            }
            case 3: {
                actionCode = 31;
                break;
            }
            case 4: {
                actionCode = 32;
                break;
            }
            case 1: {
                actionCode = 29;
                break;
            }
            case 5: {
                actionCode = 33;
                break;
            }
            default: {
                if (keyCode == -6 || keyCode == -21 || keyCode == -1) {
                    actionCode = 27;
                }
                if (keyCode == -7 || keyCode == -22 || keyCode == -4) {
                    actionCode = 28;
                }
                switch (keyCode) {
                    case 56: {
                        actionCode = 30;
                        break keyMapping;
                    }
                    case 52: {
                        actionCode = 31;
                        break keyMapping;
                    }
                    case 54: {
                        actionCode = 32;
                        break keyMapping;
                    }
                    case 50: {
                        actionCode = 29;
                        break keyMapping;
                    }
                    case 53: {
                        actionCode = 33;
                        break keyMapping;
                    }
                    case 42: {
                        actionCode = 20;
                        break keyMapping;
                    }
                    case 35: {
                        actionCode = 21;
                        break keyMapping;
                    }
                    case 49: {
                        actionCode = 22;
                        break keyMapping;
                    }
                    case 51: {
                        actionCode = 23;
                        break keyMapping;
                    }
                    case 55: {
                        actionCode = 24;
                        break keyMapping;
                    }
                    case 57: {
                        actionCode = 25;
                        break keyMapping;
                    }
                    case 48: {
                        actionCode = 26;
                        break keyMapping;
                    }
                }
            }
        }
        if (actionCode >= 0) {
            if ((keyOrPressCode = this.mapKeyToPressAction(keyOrPressCode)) < 0 || this.wasPressesKeys[keyOrPressCode]) {
                this.addSimpleAction(actionCode);
            }
            if (keyOrPressCode >= 0) {
                this.wasPressesKeys[keyOrPressCode] = false;
            }
        }
    }


    private void addSimpleAction(int actionCode) {
        Object queueLock = this.actionsLock;
        synchronized (queueLock) {
            this.actionsQueue.addElement(new int[]{actionCode});
            return;
        }
    }


    public final void cleareWasPresses() {
        Object queueLock = this.actionsLock;
        synchronized (queueLock) {
            int keyIndex = 0;
            while (true) {
                if (keyIndex >= this.wasPressesKeys.length) {
                    this.wasPointerPressed = false;
                    return;
                }
                this.wasPressesKeys[keyIndex] = false;
                ++keyIndex;
            }
        }
    }

    public final void onOutFromCurrentDisplay() {
        Com.mainTh.stopPaint = true;
        this.callReleasAllEvent();
    }


    public final void onInToCurrentDisplay() {
        PFter pfter2;
        PFter pfter1;
        Com.mainTh.stopPaint = false;
        Object controllerContext = this.pfterLock;
        synchronized (controllerContext) {
            pfter1 = this.curPFter1;
            pfter2 = this.curPFter2;
        }
        controllerContext = null;
        if (pfter2 != null) {
            controllerContext = pfter2;
        } else if (pfter1 != null) {
            controllerContext = pfter1;
        }
        if (controllerContext != null) {
            ((PFter)controllerContext).onAfterHardLoad();
        }
    }
}

