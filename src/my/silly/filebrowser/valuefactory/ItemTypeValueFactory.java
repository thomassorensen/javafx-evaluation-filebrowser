package my.silly.filebrowser.valuefactory;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;
import my.silly.filebrowser.data.Item;

public class ItemTypeValueFactory implements Callback<CellDataFeatures<Item, String>, ObservableValue<String>> {

	@Override
	public ObservableValue<String> call(CellDataFeatures<Item, String> param) {
		return new ReadOnlyStringWrapper(param.getValue().getItemType().name());
	}

}
