package in.basulabs.audiofocuscontroller.demoapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "content_type_entity")
public class Entity_ContentType {

	@ColumnInfo(name = "display_string")
	public String displayString;

	@PrimaryKey
	@ColumnInfo
	public int value;

	public Entity_ContentType(String displayString, int value) {
		this.displayString = displayString;
		this.value = value;
	}

}
