package pivot;

import generic.LAY;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import status.LayerMode;

public class LayerMenu extends Menu {
	public LayerMenu(LAY rootLay, LAY lay) {		
		super.setGraphic(new Label(lay.getIndexLabel()));
		this.setOnShowing(e -> { if(rootLay !=lay )  lay.setMode(LayerMode.VIEW); lay.nnode.separateLayers(); });
		this.setOnHiding(e -> { if(rootLay !=lay ) lay.setMode(LayerMode.BASE); lay.nnode.overlapLayers(); });
	}

	public LayerMenu(LAY rootLay, LAY lay, Label viewLabel) {
		super.setGraphic(viewLabel);
		this.setOnShowing(e -> { if(rootLay !=lay )  lay.setMode(LayerMode.VIEW); lay.nnode.separateLayers(); });
		this.setOnHiding(e -> { if(rootLay !=lay ) lay.setMode(LayerMode.BASE); lay.nnode.overlapLayers(); });
	}
}
