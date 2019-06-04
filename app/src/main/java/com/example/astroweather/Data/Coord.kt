import com.google.gson.annotations.SerializedName




public data class Coord (

	@SerializedName("lon") val lon : Double,
	@SerializedName("lat") val lat : Double
)