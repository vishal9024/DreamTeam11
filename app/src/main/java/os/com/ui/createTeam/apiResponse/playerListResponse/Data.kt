package os.com.ui.createTeam.apiResponse.playerListResponse

import android.os.Parcel
import android.os.Parcelable

class Data() : Parcelable {
    var id: String = ""
    var series_id: String = ""
    var series_name: String = ""
    var team_id: String = ""
    var team_name: String = ""
    var player_id: String = ""
    var player_name: String = ""
    var player_role: String = ""
    var odi: String = ""
    var t20: String = ""
    var test: String = ""
    var player_record: PlayerRecord? = null
    var isSubstitute = false
    var isSelected = false
    var isCaptain = false
    var isViceCaptain = false

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        series_id = parcel.readString()
        series_name = parcel.readString()
        team_id = parcel.readString()
        team_name = parcel.readString()
        player_id = parcel.readString()
        player_name = parcel.readString()
        player_role = parcel.readString()
        odi = parcel.readString()
        t20 = parcel.readString()
        test = parcel.readString()
        player_record = parcel.readParcelable(PlayerRecord::class.java.classLoader)
        isSubstitute = parcel.readByte() != 0.toByte()
        isSelected = parcel.readByte() != 0.toByte()
        isCaptain = parcel.readByte() != 0.toByte()
        isViceCaptain = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(series_id)
        parcel.writeString(series_name)
        parcel.writeString(team_id)
        parcel.writeString(team_name)
        parcel.writeString(player_id)
        parcel.writeString(player_name)
        parcel.writeString(player_role)
        parcel.writeString(odi)
        parcel.writeString(t20)
        parcel.writeString(test)
        parcel.writeParcelable(player_record, flags)
        parcel.writeByte(if (isSubstitute) 1 else 0)
        parcel.writeByte(if (isSelected) 1 else 0)
        parcel.writeByte(if (isCaptain) 1 else 0)
        parcel.writeByte(if (isViceCaptain) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "Data(id='$id', series_id='$series_id', series_name='$series_name', team_id='$team_id', team_name='$team_name', player_id='$player_id', player_name='$player_name', player_role='$player_role', odi='$odi', t20='$t20', test='$test', player_record=$player_record, isSubstitute=$isSubstitute, isSelected=$isSelected, isCaptain=$isCaptain, isViceCaptain=$isViceCaptain)"
    }

    companion object CREATOR : Parcelable.Creator<Data> {
        override fun createFromParcel(parcel: Parcel): Data {
            return Data(parcel)
        }

        override fun newArray(size: Int): Array<Data?> {
            return arrayOfNulls(size)
        }
    }


}