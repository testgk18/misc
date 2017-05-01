/*
 * Created on 18.11.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package einstein.unredo;

import einstein.Collage;

/**
 * @author zoppke
 *
 * TODO
 */
public class ActiveLayerUnredo implements Unredo {

    private int oldLayer;
    private int newLayer;
    private Collage collage;
    
    /**
     * @param oldLayer
     * @param newLayer
     */
    public ActiveLayerUnredo(int oldLayer, int newLayer, Collage collage) {
        super();
        this.oldLayer = oldLayer;
        this.newLayer = newLayer;
        this.collage = collage;
    }
    
    /* (non-Javadoc)
     * @see einstein.unredo.Unredo#undo()
     */
    public void undo() {
        collage.setActiveLayer(oldLayer);
    }

    /* (non-Javadoc)
     * @see einstein.unredo.Unredo#redo()
     */
    public void redo() {
        collage.setActiveLayer(newLayer);
    }

}
