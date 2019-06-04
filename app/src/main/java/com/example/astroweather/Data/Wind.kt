import com.google.gson.annotations.SerializedName



public data class Wind (

	@SerializedName("speed") val speed : Double,
	@SerializedName("deg") val deg : Int
) {
	override fun toString(): String {
		return "Wind(speed=$speed, deg=$deg)"
	}
}