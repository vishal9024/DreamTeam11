package os.com.ui.dashboard.profile.fragment

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.fragment_mobile_and_email.*
import kotlinx.android.synthetic.main.fragment_pan.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import os.com.AppBase.BaseActivity
import os.com.AppBase.BaseFragment
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.AppRequestCodes
import os.com.constant.Tags
import os.com.networkCall.ApiClient
import os.com.ui.dashboard.profile.adapter.CustomSpinnerAdapter
import os.com.utils.AppDelegate
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File
import java.util.*

class PanFragment : BaseFragment(), View.OnClickListener{

    private val dobCalendar = Calendar.getInstance()
    private var dob: String? = null

    internal var fromDate: DatePickerDialog.OnDateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // TODO Auto-generated method stub
            dobCalendar.set(Calendar.YEAR, year)
            dobCalendar.set(Calendar.MONTH, monthOfYear)
            dobCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            edtDateOfBirth.setText("" +dayOfMonth+ "-" + monthOfYear + 1 + "-" + year)
//            dob = state.toString() + "-" + Util.setZeroBeforeNine(monthOfYear + 1) + "-" +
//                    Util.setZeroBeforeNine(dayOfMonth)
        }

    override fun onClick(p0: View?) {
      when(p0!!.id){
          R.id.btn_uploadPanCard->{
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(activity!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity!!, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                  requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA), 10)
              } else {
                  selectImage()
              }
          }
          R.id.txtWhySubmitPANCard->{
              val str="Since " +  getString(R.string.app_name)+ " involves money related transactions. It is mandatory for us to verify your PAN card"
              Toast.makeText(context!!, str, Toast.LENGTH_LONG).show()
          }
          R.id.btnSubmitForVerifyPAN->{
              if(TextUtils.isEmpty(edtPanName.text.toString().trim())){
                  Toast.makeText(context!!, getString(R.string.enter_pan_name), Toast.LENGTH_LONG).show()
              }else if(TextUtils.isEmpty(edtPanNumber.text.toString().trim())){
                  Toast.makeText(context!!, getString(R.string.enter_pan_number), Toast.LENGTH_LONG).show()
              }else if(TextUtils.isEmpty(edtAdharCardNumber.text.toString().trim())){
                  Toast.makeText(context!!, getString(R.string.enter_aadhar_card_number), Toast.LENGTH_LONG).show()
              }else if(TextUtils.isEmpty(edtDateOfBirth.text.toString().trim())){
                  Toast.makeText(context!!, getString(R.string.select_dateofbirth), Toast.LENGTH_LONG).show()
              }else if(state.equals("Select State",true)){
                  Toast.makeText(context!!, getString(R.string.select_state_name), Toast.LENGTH_LONG).show()
              }else{
                  verify_pan_details(edtPanName.text.toString().trim(),edtPanNumber.text.toString().trim(),edtAdharCardNumber.text.toString().trim(),edtDateOfBirth.text.toString().trim())
              }
          }
          R.id.edtDateOfBirth->{
                  val fromDateDialog = DatePickerDialog(
                      activity,
                      fromDate,
                      dobCalendar.get(Calendar.YEAR),
                      dobCalendar.get(Calendar.MONTH),
                      dobCalendar.get(Calendar.DAY_OF_MONTH)
                  )
                  fromDateDialog.datePicker.maxDate = System.currentTimeMillis() - 1000
                  fromDateDialog.show()
          }
      }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_pan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            Handler().postDelayed(Runnable {
                initViews()
            }, 10)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initViews() {
        try {
            btn_uploadPanCard.setOnClickListener(this)
            btnSubmitForVerifyPAN.setOnClickListener(this)
            edtDateOfBirth.setOnClickListener(this)
            txtWhySubmitPANCard.setOnClickListener(this)
            initState()
            withdraw_cash()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun withdraw_cash() {
        try {
            GlobalScope.launch(Dispatchers.Main) {
                AppDelegate.showProgressDialog(activity!!)
                try {
                    var map = HashMap<String, String>()
                    map[Tags.user_id] = pref!!.userdata!!.user_id
                    map[Tags.language] = FantasyApplication.getInstance().getLanguage()
                    val request = ApiClient.client
                        .getRetrofitService()
                        .withdraw_cash(map)
                    val response = request.await()
                    AppDelegate.LogT("Response=>" + response)
                    AppDelegate.hideProgressDialog(activity!!)
                    if (response.response!!.isStatus) {
                        if(response.response.data.pen_verify==0){
                            cardVieAfterPanVerify.visibility = View.GONE
                            cardViewBeforePanVerify.visibility = View.VISIBLE
                        }else if(response.response.data.pen_verify==1){
                            cardVieAfterPanVerify.visibility = View.VISIBLE
                            cardViewBeforePanVerify.visibility = View.GONE
                        }else if(response.response.data.pen_verify==2){
                            llVerifiedSuccess.visibility=View.VISIBLE;
                        }
                        txtVerifiedMobileNumber.setText(response.response.data.mobile_no)
                    } else {
                        (activity as  BaseActivity).logoutIfDeactivate(response.response!!.message)
                        AppDelegate.showToast(activity!!, response.response!!.message)
                    }
                } catch (exception: Exception) {
                    AppDelegate.hideProgressDialog(activity!!)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private var stateList = ArrayList<String>()
    private var state = ""

    private fun initState() {
        try {
            val stateArray = resources.getStringArray(R.array.state)
            for (i in stateArray.indices) {
                stateList.add(stateArray[i])
            }
            val spinnerAdapter = CustomSpinnerAdapter(activity!!, stateList)
            spnStateName.setAdapter(spinnerAdapter)
            spnStateName.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    val layout = parent.getChildAt(0) as RelativeLayout
                    if (layout != null) {
                        val selectedText = layout.findViewById(R.id.txtItem) as TextView
                        selectedText?.setTextColor(Color.BLACK)
                        state = parent.getItemAtPosition(position).toString()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun verify_pan_details(pan_name: String,pan_number: String,aadhar_card: String,dob: String) {
        try {
            GlobalScope.launch(Dispatchers.Main) {
                AppDelegate.showProgressDialog(activity!!)
                try {
                  /*  { "user_id": "36","language": "en","pan_image": "","pan_name": "test","pan_number": "ASDE785","date_of_birth": "18-05-2004","state": "rajasthan","aadhar_card":"48781SGY4"}*/
                    var map = HashMap<String, String>()
                    val dataObject = JSONObject()

                    dataObject.put("user_id", pref!!.userdata!!.user_id)
                    dataObject.put("language", FantasyApplication.getInstance().getLanguage())
                    dataObject.put("pan_name", pan_name)
                    dataObject.put("pan_number", pan_number)
                    dataObject.put("date_of_birth", dob)
                    dataObject.put("state", state)
                    dataObject.put("aadhar_card", aadhar_card)
                    val requestBody = RequestBody.create(MediaType.parse("application/json"), dataObject.toString())
                    val request = ApiClient.client
                        .getRetrofitService()
                        .verify_pan_detail(requestBody, prepareFilePart("image", imageURI))
                    val response = request.await()
                    AppDelegate.LogT("Response=>" + response);
                    AppDelegate.hideProgressDialog(activity!!)
                    if (response.response!!.isStatus) {
                        if (response.response.data.pen_verify==1){
                            cardViewBeforePanVerify.visibility=View.GONE
                            cardVieAfterPanVerify.visibility=View.VISIBLE
                        }else if(response.response.data.pen_verify==2){
                            cardViewBeforePanVerify.visibility=View.GONE
                            cardVieAfterPanVerify.visibility=View.GONE
                        }

                        //AppDelegate.showToast(activity!!, response.response!!.message)
                        //finish()
                    } else {
                        (activity as BaseActivity).logoutIfDeactivate(response.response!!.message)
                        AppDelegate.showToast(activity!!, response.response!!.message)
                    }
                } catch (exception: Exception) {
                    AppDelegate.hideProgressDialog(activity!!)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun prepareFilePart(partName: String, fileUri: Uri?): MultipartBody.Part? {
        if (fileUri != null) {
            val file = File(fileUri.path)
            val requestFile = RequestBody.create(MediaType.parse("image/*"), file)
            return MultipartBody.Part.createFormData(partName, file.name, requestFile)
        }
        return null
    }

    private fun selectImage() {
        val items = arrayOf<CharSequence>(getString(R.string.take_photo), getString(R.string.from_gallery), getString(R.string.cancel))
        val builder = AlertDialog.Builder(activity!!)
        builder.setTitle(getString(R.string.add_photo))

        builder.setItems(items) { dialog, item ->
            when {
                items[item] == getString(R.string.take_photo) -> cameraIntent()
                items[item] == getString(R.string.from_gallery) -> galleryIntent()
                items[item] == getString(R.string.cancel) -> dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun cameraIntent() {
        EasyImage.openCameraForImage(this, AppRequestCodes.CAMERA_REQUEST_CODE)
    }

    private fun galleryIntent() {
        EasyImage.openGallery(this, AppRequestCodes.GALARY_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        //        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.size == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            selectImage()
        }
    }
    private var imageURI: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        EasyImage.handleActivityResult(requestCode, resultCode, data, activity, object : DefaultCallback() {

            override fun onImagesPicked(imageFiles: MutableList<File>, source: EasyImage.ImageSource?, type: Int) {
                val imagePath =AppDelegate.getCompressImagePath(Uri.fromFile(imageFiles[0]), activity!!)
                val file = File(imagePath)
                imageURI = Uri.fromFile(file)
//                imv_profile_images.setImageURI(imageURI)
            }
        })
    }

}