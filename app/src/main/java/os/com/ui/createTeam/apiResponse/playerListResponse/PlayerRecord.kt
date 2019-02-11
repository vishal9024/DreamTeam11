package os.com.ui.createTeam.apiResponse.playerListResponse

import android.os.Parcel
import android.os.Parcelable

class PlayerRecord() :Parcelable {
    var id = ""
    var player_id = ""
    var player_name = ""
    var image = ""
    var age = ""
    var born = ""
    var playing_role = ""
    var batting_style = ""
    var bowling_style = ""
    var country = ""
    var batting_odiStrikeRate = ""
    var batting_odiAverage = ""
    var bowling_odiAverage = ""
    var bowling_odiStrikeRate = ""
    var batting_firstClassStrikeRate = ""
    var batting_firstClassAverage = ""
    var bowling_firstClassStrikeRate = ""
    var bowling_firstClassAverage = ""
    var batting_t20iStrikeRate = ""
    var batting_t20iAverage = ""
    var bowling_t20iStrikeRate = ""
    var bowling_t20iAverage = ""
    var batting_testStrikeRate = ""
    var batting_testAverage = ""
    var bowling_testStrikeRate = ""
    var bowling_testAverage = ""
    var batting_listAStrikeRate = ""
    var batting_listAAverage = ""
    var bowling_listAStrikeRate = ""
    var bowling_listAAverage = ""
    var batting_t20sStrikeRate = ""
    var batting_t20sAverage = ""
    var bowling_t20sStrikeRate = ""
    var bowling_t20sAverage = ""
    var teams = ""
    var player_credit = ""
//    var created = ""
//    var modified = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        player_id = parcel.readString()
        player_name = parcel.readString()
        image = parcel.readString()
        age = parcel.readString()
        born = parcel.readString()
        playing_role = parcel.readString()
        batting_style = parcel.readString()
        bowling_style = parcel.readString()
        country = parcel.readString()
        batting_odiStrikeRate = parcel.readString()
        batting_odiAverage = parcel.readString()
        bowling_odiAverage = parcel.readString()
        bowling_odiStrikeRate = parcel.readString()
        batting_firstClassStrikeRate = parcel.readString()
        batting_firstClassAverage = parcel.readString()
        bowling_firstClassStrikeRate = parcel.readString()
        bowling_firstClassAverage = parcel.readString()
        batting_t20iStrikeRate = parcel.readString()
        batting_t20iAverage = parcel.readString()
        bowling_t20iStrikeRate = parcel.readString()
        bowling_t20iAverage = parcel.readString()
        batting_testStrikeRate = parcel.readString()
        batting_testAverage = parcel.readString()
        bowling_testStrikeRate = parcel.readString()
        bowling_testAverage = parcel.readString()
        batting_listAStrikeRate = parcel.readString()
        batting_listAAverage = parcel.readString()
        bowling_listAStrikeRate = parcel.readString()
        bowling_listAAverage = parcel.readString()
        batting_t20sStrikeRate = parcel.readString()
        batting_t20sAverage = parcel.readString()
        bowling_t20sStrikeRate = parcel.readString()
        bowling_t20sAverage = parcel.readString()
        teams = parcel.readString()
        player_credit = parcel.readString()
//        created = parcel.readString()
//        modified = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(if (id == null) "" else id )
        parcel.writeString(if (player_id == null) "" else player_id )
        parcel.writeString(if (player_name == null) "" else player_name )
        parcel.writeString(if (image == null) "" else image )
        parcel.writeString(if (age == null) "" else age )
        parcel.writeString(if (born == null) "" else born )
        parcel.writeString(if (playing_role == null) "" else playing_role )
        parcel.writeString(if (batting_style == null) "" else batting_style )
        parcel.writeString(if (bowling_style == null) "" else bowling_style )
        parcel.writeString(if (country == null) "" else country )
        parcel.writeString(if (batting_odiStrikeRate == null) "" else batting_odiStrikeRate )
        parcel.writeString(if (batting_odiAverage == null) "" else batting_odiAverage )
        parcel.writeString(if (bowling_odiAverage == null) "" else bowling_odiAverage )
        parcel.writeString(if (bowling_odiStrikeRate == null) "" else bowling_odiStrikeRate )
        parcel.writeString(if (batting_firstClassStrikeRate == null) "" else batting_firstClassStrikeRate )
        parcel.writeString(if (batting_firstClassAverage == null) "" else batting_firstClassAverage )
        parcel.writeString(if (bowling_firstClassStrikeRate == null) "" else bowling_firstClassStrikeRate )
        parcel.writeString(if (bowling_firstClassAverage == null) "" else bowling_firstClassAverage )
        parcel.writeString(if (batting_t20iStrikeRate == null) "" else batting_t20iStrikeRate )
        parcel.writeString(if (batting_t20iAverage == null) "" else batting_t20iAverage )
        parcel.writeString(if (bowling_t20iStrikeRate == null) "" else bowling_t20iStrikeRate )
        parcel.writeString(if (bowling_t20iAverage == null) "" else bowling_t20iAverage )
        parcel.writeString(if (batting_testStrikeRate == null) "" else batting_testStrikeRate )
        parcel.writeString(if (batting_testAverage == null) "" else batting_testAverage )
        parcel.writeString(if (bowling_testStrikeRate == null) "" else bowling_testStrikeRate )
        parcel.writeString(if (bowling_testAverage == null) "" else bowling_testAverage )
        parcel.writeString(if (batting_listAStrikeRate == null) "" else batting_listAStrikeRate )
        parcel.writeString(if (batting_listAAverage == null) "" else batting_listAAverage )
        parcel.writeString(if (bowling_listAStrikeRate == null) "" else bowling_listAStrikeRate )
        parcel.writeString(if (bowling_listAAverage == null) "" else bowling_listAAverage )
        parcel.writeString(if (batting_t20sStrikeRate == null) "" else batting_t20sStrikeRate )
        parcel.writeString(if (batting_t20sAverage == null) "" else batting_t20sAverage )
        parcel.writeString(if (bowling_t20sStrikeRate == null) "" else bowling_t20sStrikeRate )
        parcel.writeString(if (bowling_t20sAverage == null) "" else bowling_t20sAverage )
        parcel.writeString(if (teams == null) "" else teams )
        parcel.writeString(if (player_credit == null) "" else player_credit )
//      parcel.writeString(created)
//      parcel.writeString(modified)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlayerRecord> {
        override fun createFromParcel(parcel: Parcel): PlayerRecord {
            return PlayerRecord(parcel)
        }

        override fun newArray(size: Int): Array<PlayerRecord?> {
            return arrayOfNulls(size)
        }
    }
}