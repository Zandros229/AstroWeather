import com.google.gson.annotations.SerializedName



public data class Wind (

    @SerializedName("speed") var speed : Double,
    @SerializedName("deg") val deg : Double
) {
	override fun toString(): String {
		return "Wind(speed=$speed, deg=$deg)"
	}
}