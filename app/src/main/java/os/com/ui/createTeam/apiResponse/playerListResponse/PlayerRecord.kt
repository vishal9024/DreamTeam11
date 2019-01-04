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
        parcel.writeString(id)
        parcel.writeString(player_id)
        parcel.writeString(player_name)
        parcel.writeString(image)
        parcel.writeString(age)
        parcel.writeString(born)
        parcel.writeString(playing_role)
        parcel.writeString(batting_style)
        parcel.writeString(bowling_style)
        parcel.writeString(country)
        parcel.writeString(batting_odiStrikeRate)
        parcel.writeString(batting_odiAverage)
        parcel.writeString(bowling_odiAverage)
        parcel.writeString(bowling_odiStrikeRate)
        parcel.writeString(batting_firstClassStrikeRate)
        parcel.writeString(batting_firstClassAverage)
        parcel.writeString(bowling_firstClassStrikeRate)
        parcel.writeString(bowling_firstClassAverage)
        parcel.writeString(batting_t20iStrikeRate)
        parcel.writeString(batting_t20iAverage)
        parcel.writeString(bowling_t20iStrikeRate)
        parcel.writeString(bowling_t20iAverage)
        parcel.writeString(batting_testStrikeRate)
        parcel.writeString(batting_testAverage)
        parcel.writeString(bowling_testStrikeRate)
        parcel.writeString(bowling_testAverage)
        parcel.writeString(batting_listAStrikeRate)
        parcel.writeString(batting_listAAverage)
        parcel.writeString(bowling_listAStrikeRate)
        parcel.writeString(bowling_listAAverage)
        parcel.writeString(batting_t20sStrikeRate)
        parcel.writeString(batting_t20sAverage)
        parcel.writeString(bowling_t20sStrikeRate)
        parcel.writeString(bowling_t20sAverage)
        parcel.writeString(teams)
        parcel.writeString(player_credit)
//        parcel.writeString(created)
//        parcel.writeString(modified)
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