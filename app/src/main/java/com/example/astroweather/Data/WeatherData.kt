import com.google.gson.annotations.SerializedName




public data class WeatherData (


	@SerializedName("coord") val coord : Coord,
	@SerializedName("weather") val weather : List<Weather>,
	@SerializedName("base") val base : String,
	@SerializedName("main") val main : Main,
	@SerializedName("visibility") val visibility : Int,
	@SerializedName("wind") val wind : Wind,
	@SerializedName("clouds") val clouds : Clouds,
	@SerializedName("dt") val dt : Int,
	@SerializedName("sys") val sys : Sys,
	@SerializedName("timezone") val timezone : Int,
	@SerializedName("id") val id : Int,
	@SerializedName("name") val name : String,
	@SerializedName("cod") val cod : Int

) {
	override fun toString(): String {
		return "WeatherData(coord=$coord, weather=$weather, base='$base', main=$main, visibility=$visibility, wind=$wind, clouds=$clouds, dt=$dt, sys=$sys, timezone=$timezone, id=$id, name='$name', cod=$cod)"
	}
}