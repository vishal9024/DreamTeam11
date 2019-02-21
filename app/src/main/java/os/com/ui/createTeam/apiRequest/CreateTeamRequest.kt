package os.com.ui.createTeam.apiRequest

import android.os.Parcel
import android.os.Parcelable

class CreateTeamRequest() :Parcelable {

    var user_id:String=""
    var language:String=""
    var match_id:String=""
    var series_id:String=""
    var team_id:String=""
    var captain:String=""
    var vice_captain:String=""
    var substitute:String=""
    var player_id:ArrayList<String>?=null

    constructor(parcel: Parcel) : this() {
        user_id = parcel.readString()
        language = parcel.readString()
        match_id = parcel.readString()
        series_id = parcel.readString()
        team_id = parcel.readString()
        captain = parcel.readString()
        vice_captain = parcel.readString()
        substitute = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(user_id)
        parcel.writeString(language)
        parcel.writeString(match_id)
        parcel.writeString(series_id)
        parcel.writeString(team_id)
        parcel.writeString(captain)
        parcel.writeString(vice_captain)
        parcel.writeString(substitute)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CreateTeamRequest> {
        override fun createFromParcel(parcel: Parcel): CreateTeamRequest {
            return CreateTeamRequest(parcel)
        }

        override fun newArray(size: Int): Array<CreateTeamRequest?> {
            return arrayOfNulls(size)
        }
    }
}