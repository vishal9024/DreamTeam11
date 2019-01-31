package os.com.ui.createTeam.apiResponse

import android.os.Parcel
import android.os.Parcelable

class SelectPlayer() : Parcelable {
    var extra_player = 3
    var wk_count = 1
    var wk_selected =0
    var bat_mincount = 3
    var bat_maxcount = 5
    var bat_selected = 0

    var ar_mincount = 1
    var ar_maxcount = 3
    var ar_selected = 0

    var bowl_mincount = 3
    var bowl_maxcount = 5
    var bowl_selected = 0

    var selectedPlayer = 0

    var localTeamplayerCount = 0
    var visitorTeamPlayerCount = 0

    var total_credit = 0.0
    var substitute = false
    var substitute_id = ""
    var substitute_type = 1


    constructor(parcel: Parcel) : this() {
        extra_player = parcel.readInt()
        wk_count = parcel.readInt()
        wk_selected = parcel.readInt()
        bat_mincount = parcel.readInt()
        bat_maxcount = parcel.readInt()
        bat_selected = parcel.readInt()
        ar_mincount = parcel.readInt()
        ar_maxcount = parcel.readInt()
        ar_selected = parcel.readInt()
        bowl_mincount = parcel.readInt()
        bowl_maxcount = parcel.readInt()
        bowl_selected = parcel.readInt()
        selectedPlayer = parcel.readInt()
        localTeamplayerCount = parcel.readInt()
        visitorTeamPlayerCount = parcel.readInt()
        total_credit = parcel.readDouble()
        substitute = parcel.readByte() != 0.toByte()
        substitute_id = parcel.readString()
        substitute_type = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(extra_player)
        parcel.writeInt(wk_count)
        parcel.writeInt(wk_selected)
        parcel.writeInt(bat_mincount)
        parcel.writeInt(bat_maxcount)
        parcel.writeInt(bat_selected)
        parcel.writeInt(ar_mincount)
        parcel.writeInt(ar_maxcount)
        parcel.writeInt(ar_selected)
        parcel.writeInt(bowl_mincount)
        parcel.writeInt(bowl_maxcount)
        parcel.writeInt(bowl_selected)
        parcel.writeInt(selectedPlayer)
        parcel.writeInt(localTeamplayerCount)
        parcel.writeInt(visitorTeamPlayerCount)
        parcel.writeDouble(total_credit)
        parcel.writeByte(if (substitute) 1 else 0)
        parcel.writeString(substitute_id)
        parcel.writeInt(substitute_type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SelectPlayer> {
        override fun createFromParcel(parcel: Parcel): SelectPlayer {
            return SelectPlayer(parcel)
        }

        override fun newArray(size: Int): Array<SelectPlayer?> {
            return arrayOfNulls(size)
        }
    }


}