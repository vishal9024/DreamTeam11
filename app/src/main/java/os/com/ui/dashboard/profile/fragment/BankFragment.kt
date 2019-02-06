package os.com.ui.dashboard.profile.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.fragment_bank.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import os.com.AppBase.BaseFragment
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.AppRequestCodes
import os.com.constant.Tags
import os.com.networkCall.ApiClient
import os.com.utils.AppDelegate
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File
import java.util.*

class BankFragment : BaseFragment(), View.OnClickListener{
    override fun onClick(p0: View?) {
        when(p0!!.id) {
            R.id.btnUploadBankImage -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(activity!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity!!, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA), 10)
                } else {
                    selectImage()
                }
            }
            R.id.btnSubmitVerificationForBank->{
                if(TextUtils.isEmpty(edtAccountNamber.text.toString().trim())){
                    Toast.makeText(context!!, getString(R.string.enter_account_number), Toast.LENGTH_LONG).show()
                }else if(TextUtils.isEmpty(edtRetypeAcoountNumber.text.toString().trim())){
                    Toast.makeText(context!!, getString(R.string.enter_retypeaccount_number), Toast.LENGTH_LONG).show()
                }else if(TextUtils.isEmpty(edtIFSCCode.text.toString().trim())){
                    Toast.makeText(context!!, getString(R.string.enter_ifsc_code), Toast.LENGTH_LONG).show()
                }else if(TextUtils.isEmpty(edtBankname.text.toString().trim())){
                    Toast.makeText(context!!, getString(R.string.enter_bank_name), Toast.LENGTH_LONG).show()
                }else if(TextUtils.isEmpty(edtBankbranch.text.toString().trim())){
                    Toast.makeText(context!!, getString(R.string.enter_bank_branch), Toast.LENGTH_LONG).show()
                }else{
                    verify_bank_details(edtAccountNamber.text.toString().trim(),edtIFSCCode.text.toString().trim(),edtBankname.text.toString().trim(),edtBankbranch.text.toString().trim())
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_bank, container, false)
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
        try{
            btnUploadBankImage.setOnClickListener(this)
            btnSubmitVerificationForBank.setOnClickListener(this)
            edtIFSCCode.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable) {
                    if (s.toString().length>=11) {
                       ifsc_call()
                    }else{
                        edtBankname.setText("")
                        edtBankbranch.setText("")
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence, start: Int,
                    count: Int, after: Int
                ) {

                }

                override fun onTextChanged(
                    s: CharSequence, start: Int,
                    before: Int, count: Int
                ) {

                }
            })
            withdraw_cash()

        }catch (e:Exception){
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
                    if (response.response!!.isStatus) {0
                        if(response.response.data.pen_verify==2){
                            cardviewBankBeforeVerify.visibility=View.GONE
                            cardViewAfterBankVerify.visibility=View.VISIBLE
                            cardVieAfterBankReviewVerify.visibility=View.GONE
                        }
                        if(response.response.data.bank_account_verify==1){
                            cardviewBankBeforeVerify.visibility=View.GONE
                            cardViewAfterBankVerify.visibility=View.GONE
                            cardVieAfterBankReviewVerify.visibility=View.VISIBLE
                        }
                        if(response.response.data.pen_verify==0||response.response.data.pen_verify==1){
                            cardviewBankBeforeVerify.visibility=View.VISIBLE
                            cardViewAfterBankVerify.visibility=View.GONE
                            cardVieAfterBankReviewVerify.visibility=View.GONE
                        }
                        if(response.response.data.bank_account_verify==2){
                            activity!!.finish()
                        }
                    } else {
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


    private fun ifsc_call() {
        try {
            GlobalScope.launch(Dispatchers.Main) {
                AppDelegate.showProgressDialog(activity!!)
                try {
                    val request = ApiClient.client
                        .getRetrofitService()
                        .ifsc_code("https://api.techm.co.in/api/v1/ifsc/"+edtIFSCCode.text.toString().trim())
                    val response = request.await()
                    AppDelegate.LogT("Response=>" + response)
                    AppDelegate.hideProgressDialog(activity!!)
                    if (response.status.equals("success")) {
                        edtBankname.setText(response.data.bank)
                        edtBankbranch.setText(response.data.branch)
                    } else {
                        AppDelegate.showToast(activity!!, response!!.message)
                        edtBankname.setText("")
                        edtBankbranch.setText("")
                    }
                } catch (exception: Exception) {
                    AppDelegate.hideProgressDialog(activity!!)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun verify_bank_details(account_number: String, ifsc_code: String, bank_name: String, bank_brach: String) {
        try {
            GlobalScope.launch(Dispatchers.Main) {
                AppDelegate.showProgressDialog(activity!!)
                try {
                    var map = HashMap<String, String>()
                    val dataObject = JSONObject()
                    dataObject.put("user_id", pref!!.userdata!!.user_id)
                    dataObject.put("language", FantasyApplication.getInstance().getLanguage())
                    dataObject.put("account_no", account_number)
                    dataObject.put("ifsc_code", ifsc_code)
                    dataObject.put("bank_name", bank_name)
                    dataObject.put("branch", bank_brach)
                    val requestBody = RequestBody.create(MediaType.parse("application/json"), dataObject.toString())
                    val request = ApiClient.client
                        .getRetrofitService()
                        .verify_bank_detail(requestBody, prepareFilePart("image", imageURI))
                    val response = request.await()
                    AppDelegate.LogT("Response=>" + response);
                    AppDelegate.hideProgressDialog(activity!!)
                    if (response.response!!.isStatus) {
                        AppDelegate.showToast(activity!!, response.response!!.message)
                        withdraw_cash()
                    } else {
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