package my.silly.filebrowser.valuefactory;


import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;
import my.silly.filebrowser.data.Item;

public class PathValueFactory implements Callback<CellDataFeatures<Item, String>, ObservableValue<String>> {

	@Override
	public ObservableValue<String> call(CellDataFeatures<Item, String> param) {
		Item item = param.getValue();
		return new ReadOnlyObjectWrapper<String>(item.getPath());
	}
}
