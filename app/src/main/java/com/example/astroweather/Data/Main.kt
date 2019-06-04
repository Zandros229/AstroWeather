import com.google.gson.annotations.SerializedName




public data class Main (

	@SerializedName("temp") val temp : Double,
	@SerializedName("pressure") val pressure : Int,
	@SerializedName("humidity") val humidity : Int,
	@SerializedName("temp_min") val temp_min : Double,
	@SerializedName("temp_max") val temp_max : Double
)