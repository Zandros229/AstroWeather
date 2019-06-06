import com.google.gson.annotations.SerializedName




data class ForeCastDate (

	@SerializedName("cod") val cod : Int,
	@SerializedName("message") val message : Double,
	@SerializedName("cnt") val cnt : Int,
	@SerializedName("list") val list : List<forecastItem>,
	@SerializedName("city") val city : City
)